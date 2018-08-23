package com.aisino.tool.ani;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aisino.tool.R;

public class LoadingDialog extends Dialog {
    private TextView tv;
    public String text="请稍等";
    /**
     * style很关键
     */
    public LoadingDialog(Context context) {
        super(context, R.style.WeixinLoadingDialog);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wxdialog_loading);
        tv =  findViewById(R.id.wxloading_text);
        tv.setText(text);
        LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.LinearLayout);
        linearLayout.getBackground().setAlpha(210);
    }
}
