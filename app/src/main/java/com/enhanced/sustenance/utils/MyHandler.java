package com.enhanced.sustenance.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class MyHandler {
    private static android.os.Handler workerThread;
    private static Handler uiHandler;

    public MyHandler() {
        HandlerThread handlerThread = new HandlerThread("handlerThread");
        handlerThread.start();
        workerThread = new android.os.Handler(handlerThread.getLooper());
        uiHandler = new Handler(Looper.getMainLooper());
    }

    public android.os.Handler getWorkerThread() {
        return workerThread;
    }

    public Handler getUiHandler() {
        return uiHandler;
    }
}
