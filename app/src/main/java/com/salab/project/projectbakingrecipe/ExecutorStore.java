package com.salab.project.projectbakingrecipe;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Hold Executors allowing easily switching between threads
 */
public class ExecutorStore {

    private static ExecutorStore sInstance;
    private static final Object LOCK = new Object();
    Executor diskIO;
    Executor networkIO;
    Executor mainThread;

    public ExecutorStore(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public static ExecutorStore getInstance(){
        // For Singleton instantiation
        if (sInstance == null){
            synchronized (LOCK){
                sInstance = new ExecutorStore(
                        Executors.newSingleThreadExecutor(),
                        Executors.newSingleThreadExecutor(),
                        new MainThreadExecutor());
            }
        }
        return sInstance;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

}
