package com.hq.tool.animation.spotlight;


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
