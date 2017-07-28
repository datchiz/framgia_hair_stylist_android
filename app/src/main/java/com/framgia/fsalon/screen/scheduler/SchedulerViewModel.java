package com.framgia.fsalon.screen.scheduler;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.IntDef;

import com.framgia.fsalon.BR;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import static com.framgia.fsalon.screen.scheduler.SchedulerViewModel.TabFilter.TAB_CALENDAR;
import static com.framgia.fsalon.screen.scheduler.SchedulerViewModel.TabFilter.TAB_TODAY;
import static com.framgia.fsalon.screen.scheduler.SchedulerViewModel.TabFilter.TAB_TOMORROW;
import static com.framgia.fsalon.screen.scheduler.SchedulerViewModel.TabFilter.TAB_YESTERDAY;

/**
 * Exposes the data to be used in the Scheduler screen.
 */
public class SchedulerViewModel extends BaseObservable implements SchedulerContract.ViewModel {
    private SchedulerContract.Presenter mPresenter;
    private int mTabFilter;

    public SchedulerViewModel() {
        setTabFilter(TAB_TODAY);
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
    }

    @Override
    public void setPresenter(SchedulerContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Bindable
    public int getTabFilter() {
        return mTabFilter;
    }

    public void setTabFilter(@TabFilter int tabFilter) {
        mTabFilter = tabFilter;
        notifyPropertyChanged(BR.tabFilter);
    }

    @Override
    public void onItemFilterClick(@TabFilter int tab) {
        setTabFilter(tab);
    }

    @IntDef({TAB_TODAY, TAB_YESTERDAY, TAB_TOMORROW, TAB_CALENDAR})
    @Target(ElementType.PARAMETER)
    public @interface TabFilter {
        int TAB_TODAY = 0;
        int TAB_YESTERDAY = 1;
        int TAB_TOMORROW = 2;
        int TAB_CALENDAR = 3;
    }
}
