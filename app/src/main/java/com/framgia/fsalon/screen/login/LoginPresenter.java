package com.framgia.fsalon.screen.login;

import android.text.TextUtils;
import android.util.Log;
import com.framgia.fsalon.data.model.UserRespone;
import com.framgia.fsalon.data.source.UserRepository;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.framgia.fsalon.utils.Constant.Permission.PERMISSION_ADMIN;

/**
 * Listens to user actions from the UI ({@link LoginActivity}), retrieves the data and updates
 * the UI as required.
 */
public class LoginPresenter implements LoginContract.Presenter {

    private static final String TAG = LoginPresenter.class.getName();
    private final LoginContract.ViewModel mViewModel;
    private UserRepository mRepository;
    private CompositeSubscription mCompositeSubscriptions = new CompositeSubscription();

    public LoginPresenter(LoginContract.ViewModel viewModel, UserRepository repository) {
        mViewModel = viewModel;
        mRepository = repository;
        getCurrentUser();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mCompositeSubscriptions.clear();
    }

    @Override
    public void login(String account, String passWord) {
        if (!validateDataInput(account, passWord)) {
            return;
        }
        Subscription subscription = mRepository.login(account, passWord)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe(new Action0() {
                @Override
                public void call() {
                    mViewModel.showProgressbar();
                }
            })
            .subscribe(new Action1<UserRespone>() {
                @Override
                public void call(UserRespone userRespone) {
                    loginWithPermission(userRespone.getUser().getPermission());
                    mRepository.saveCurrentUser(userRespone).subscribe();
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    mViewModel.hideProgressbar();
                    mViewModel.onLoginErrror(throwable.getMessage());
                }
            }, new Action0() {
                @Override
                public void call() {
                    mViewModel.hideProgressbar();
                }
            });
        mCompositeSubscriptions.add(subscription);
    }

    @Override
    public void getCurrentUser() {
        Subscription subscription = mRepository.getCurrentUser()
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
            .subscribe(new Action1<UserRespone>() {
                @Override
                public void call(UserRespone userRespone) {
                    if (userRespone != null) {
                        loginWithPermission(userRespone.getUser().getPermission());
                    }
                }
            }, new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Log.d(TAG, "call: " + throwable.getMessage());
                }
            });
        mCompositeSubscriptions.add(subscription);
    }

    public boolean validateDataInput(String account, String password) {
        boolean isValid = true;
        if (TextUtils.isEmpty(account)) {
            isValid = false;
            mViewModel.onInputAccountError();
        }
        if (TextUtils.isEmpty(password)) {
            isValid = false;
            mViewModel.onInputPassWordError();
        }
        return isValid;
    }

    private void loginWithPermission(int permission) {
        if (permission == PERMISSION_ADMIN) {
            mViewModel.onAdminLoginSuccess();
        } else {
            mViewModel.onCustomerLoginSuccess();
        }
    }
}
