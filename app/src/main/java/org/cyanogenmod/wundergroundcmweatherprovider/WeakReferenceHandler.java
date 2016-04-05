package org.cyanogenmod.wundergroundcmweatherprovider;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public abstract class WeakReferenceHandler<T> extends Handler {
    private WeakReference<T> mReference;

    public WeakReferenceHandler(T reference) {
        mReference = new WeakReference<T>(reference);
    }

    @Override
    public void handleMessage(Message msg) {
        T reference = mReference.get();
        if (reference == null)
            return;
        handleMessage(reference, msg);
    }

    protected abstract void handleMessage(T reference, Message msg);
}
