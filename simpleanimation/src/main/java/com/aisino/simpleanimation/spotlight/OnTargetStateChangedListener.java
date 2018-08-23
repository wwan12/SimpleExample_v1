package com.aisino.simpleanimation.spotlight;


public interface OnTargetStateChangedListener<T extends Target> {
    /**
     * Called when Target is started
     */
     void onStarted(T target);

    /**
     * Called when Target is started
     */
    void onEnded(T target);
}
