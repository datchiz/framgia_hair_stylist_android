package com.framgia.fsalon.screen.scheduler;

/**
 * Listens to user actions from the UI ({@link SchedulerFragment}), retrieves the data and updates
 * the UI as required.
 */
final class SchedulerPresenter implements SchedulerContract.Presenter {
    private static final String TAG = SchedulerPresenter.class.getName();
    private final SchedulerContract.ViewModel mViewModel;

    public SchedulerPresenter(SchedulerContract.ViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

}
