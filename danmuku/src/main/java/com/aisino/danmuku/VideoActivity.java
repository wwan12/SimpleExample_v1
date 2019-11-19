package com.aisino.danmuku;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.URL;


/**
 * 文件描述：
 * 作者：Administrator
 * 创建时间：2018/8/29/029
 * 更改时间：2018/8/29/029
 * 版本号：1
 */
public class VideoActivity extends AppCompatActivity {
    public static final int HTTP_TYPE = 0;
    public static final int LOCAL_TYPE = 1;
    public static final String PATH = "0";
    public static final String TYPE = "1";
    public static final String TITLE = "2";
    public static final String IMAGE_LOCAL = "3";
    public static final String IMAGE_HTTP = "4";
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        JZVideoPlayerStandard jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoplayer);

        if (getIntent().getIntExtra(TYPE, HTTP_TYPE) == HTTP_TYPE) {

        }
        if (getIntent().getStringExtra(IMAGE_HTTP)==null){
            jzVideoPlayerStandard.thumbImageView.setImageResource(getIntent().getIntExtra(IMAGE_LOCAL, 0));
        }else {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(new URL(getIntent().getStringExtra(IMAGE_HTTP)).openStream());
                jzVideoPlayerStandard.thumbImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        jzVideoPlayerStandard.setUp(getIntent().getStringExtra(PATH), JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, getIntent().getStringExtra(TITLE));
    }

//    2.Add JZVideoPlayer in your layout:
//
//    <cn.jzvd.JZVideoPlayerStandard
//    android:id="@+id/videoplayer"
//    android:layout_width="match_parent"
//    android:layout_height="200dp"/>
//    3.Set the video uri, video thumb url and video title:
//

    //    4.In Activity:
//
    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }
//    5.In AndroidManifest.xml:
//
//    <activity
//    android:name=".MainActivity"
//    android:configChanges="orientation|screenSize|keyboardHidden"
//    android:screenOrientation="portrait" />
//    <!-- or android:screenOrientation="landscape"-->
}
