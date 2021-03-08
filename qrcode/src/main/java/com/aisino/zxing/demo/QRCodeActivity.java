package com.aisino.zxing.demo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.aisino.qrcode.R;
import com.aisino.zxing.CaptureActivity;
import com.aisino.zxing.DecodeConfig;
import com.aisino.zxing.DecodeFormatManager;
import com.aisino.zxing.analyze.MultiFormatAnalyzer;
import com.aisino.zxing.util.StatusBarUtils;
import com.google.zxing.Result;


/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class QRCodeActivity extends CaptureActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.toolbar);
        StatusBarUtils.immersiveStatusBar(this,toolbar,0.2f);
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("扫描二维码");
    }


    @Override
    public int getLayoutId() {
        return R.layout.qr_code_activity;
    }

    @Override
    public void initCameraScan() {
        super.initCameraScan();

        //初始化解码配置
        DecodeConfig decodeConfig = new DecodeConfig();
        decodeConfig.setHints(DecodeFormatManager.QR_CODE_HINTS)//如果只有识别二维码的需求，这样设置效率会更高
            .setFullAreaScan(false)//设置是否全区域识别，默认false
            .setAreaRectRatio(0.8f)//设置识别区域比例，默认0.8，设置的比例最终会在预览区域裁剪基于此比例的一个矩形进行扫码识别
            .setAreaRectVerticalOffset(0)//设置识别区域垂直方向偏移量，默认为0，为0表示居中，可以为负数
            .setAreaRectHorizontalOffset(0);//设置识别区域水平方向偏移量，默认为0，为0表示居中，可以为负数

        //在启动预览之前，设置分析器，只识别二维码
        getCameraScan()
                .setVibrate(true)//设置是否震动，默认为false
                .setNeedAutoZoom(true)//二维码太小时可自动缩放，默认为false
                .setAnalyzer(new MultiFormatAnalyzer(decodeConfig));//设置分析器,如果内置实现的一些分析器不满足您的需求，你也可以自定义去实现
    }

    @Override
    public boolean onScanResultCallback(Result result) {
        return super.onScanResultCallback(result);
    }
}
