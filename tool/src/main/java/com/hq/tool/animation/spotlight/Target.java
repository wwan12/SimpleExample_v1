package com.hq.tool.animation.spotlight;

import android.graphics.PointF;
import android.view.View;

import com.hq.tool.animation.spotlight.OnTargetStateChangedListener;

/**
 * Target
 *
 **/
interface Target {

    /**
     * gets the point of this Target
     *
     * @return the point of this Target
     */
    PointF getPoint();

    /**
     * gets the radius of this Target
     *
     * @return the radius of this Target
     */
    float getRadius();

    /**
     * gets the view of this Target
     *
     * @return the view of this Target
     */
    View getView();

    /**
     * gets the listener of this Target
     *
     * @return the listener of this Target
     */
    OnTargetStateChangedListener getListener();

    /**
     * default target
     */
    Target DEFAULT = new Target() {
        @Override
        public PointF getPoint() {
            return new PointF(0, 0);
        }

        @Override
        public float getRadius() {
            return 100f;
        }

        @Override
        public View getView() {
            return null;
        }

        @Override
        public OnTargetStateChangedListener getListener() {
            return null;
        }
    };
}