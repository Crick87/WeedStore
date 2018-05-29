package com.python.cricket.weedstore.services;
import android.support.annotation.NonNull;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.scheduling.FrameworkJobSchedulerService;
import com.python.cricket.weedstore.DataApplication;

public class OfflineBasket extends FrameworkJobSchedulerService  {
    @NonNull
    @Override
    protected JobManager getJobManager() {
        return DataApplication.getInstance().getJobManager();
    }
}
