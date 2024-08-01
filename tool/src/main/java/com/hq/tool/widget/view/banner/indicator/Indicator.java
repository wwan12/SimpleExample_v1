package com.hq.tool.widget.view.banner.indicator;

import android.view.View;

import androidx.annotation.NonNull;

import com.hq.tool.widget.view.banner.config.IndicatorConfig;
import com.hq.tool.widget.view.banner.listener.OnPageChangeListener;

public interface Indicator extends OnPageChangeListener {
    @NonNull
    View getIndicatorView();

    IndicatorConfig getIndicatorConfig();

    void onPageChanged(int count, int currentPosition);

}
