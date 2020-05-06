package com.aisino.tool.system.camera2;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aisino.tool.R;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CameraFragment extends Fragment {

    private ImageView sufShow;
    private Camera2View mCameraView;
    private Camera2Proxy mCameraProxy;

    // 当前获取的帧数
    private int currentIndex = 0;
    // 处理的间隔帧
    private static final int PROCESS_INTERVAL = 60;

    private UpBitmap upBitmap;
    private ExecutorService imageProcessExecutor;

    private final TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            try {
                mCameraProxy.setPreviewSurface(texture);
                mCameraProxy.openCamera();
                // 根据相机预览设置View大小，避免显示变形
//                Size previewSize = mCameraProxy.getPreviewSize();
//                mCameraView.setAspectRatio(previewSize.getWidth(), previewSize.getHeight());
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "摄像头被占用，将关闭该页面", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }

    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera, null);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
//        mCloseIv = rootView.findViewById(R.id.toolbar_close_iv);
        mCameraView = rootView.findViewById(R.id.camera_view);
        sufShow = rootView.findViewById(R.id.imageView);
        mCameraProxy = mCameraView.getCameraProxy();
        mCameraProxy.setImageAvailableListener(mOnImageAvailableListener);
        imageProcessExecutor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCameraView.isAvailable()) {
            mCameraProxy.openCamera();
        } else {
            mCameraView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    @Override
    public void onPause() {
        mCameraProxy.releaseCamera();
        super.onPause();
    }


    private byte[] mYuvBytes;
    private boolean lock;
    private ImageReader.OnImageAvailableListener mOnImageAvailableListener
            = new ImageReader.OnImageAvailableListener() {

        @Override
        public void onImageAvailable(ImageReader reader) {
            //  if (!lock) {
            Image image = reader.acquireNextImage();
            if (currentIndex++ % PROCESS_INTERVAL == 0 && !lock) {
                lock = true;
                if (image == null) {
                    return;
                }
                int width = mCameraProxy.getPreviewSize().getWidth();
                int height = mCameraProxy.getPreviewSize().getHeight();
                if (mYuvBytes == null) {
                    // YUV420 大小总是 width * height * 3 / 2
                    mYuvBytes = new byte[width * height * 3 / 2];
                }
                //       Log.e("aaaaa",image.getFormat()+"");
                // YUV_420_888
                Image.Plane[] planes = image.getPlanes();

                // Y通道，对应planes[0]
                // Y size = width * height
                // yBuffer.remaining() = width * height;
                // pixelStride = 1
                ByteBuffer yBuffer = planes[0].getBuffer();
                int yLen = width * height;
                yBuffer.get(mYuvBytes, 0, yLen);
                // U通道，对应planes[1]
                // U size = width * height / 4;
                // uBuffer.remaining() = width * height / 2;
                // pixelStride = 2
                ByteBuffer uBuffer = planes[1].getBuffer();
                int pixelStride = planes[1].getPixelStride(); // pixelStride = 2
                for (int i = 0; i < uBuffer.remaining(); i += pixelStride) {
                    mYuvBytes[yLen++] = uBuffer.get(i);
                }
                // V通道，对应planes[2]
                // V size = width * height / 4;
                // vBuffer.remaining() = width * height / 2;
                // pixelStride = 2
                ByteBuffer vBuffer = planes[2].getBuffer();
                pixelStride = planes[2].getPixelStride(); // pixelStride = 2
                try {
                    for (int i = 0; i < vBuffer.remaining(); i += pixelStride) {
                        mYuvBytes[yLen++] = vBuffer.get(i);
                    }
                    // 回传数据是YUV420
                    imageProcessExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            // ImageUtil.yuv420ToYuv420sp(yBuffer.array(), uBuffer.array(), vBuffer.array(), mYuvBytes, planes[0].getRowStride(), height);
//                        YuvImage yuvImage = new YuvImage(mYuvBytes, ImageFormat.NV21, mCameraProxy.getPreviewSize().getWidth(), mCameraProxy.getPreviewSize().getHeight(), null);
//                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                        yuvImage.compressToJpeg(new Rect(0, 0, mCameraProxy.getPreviewSize().getWidth(), mCameraProxy.getPreviewSize().getHeight()), 100, byteArrayOutputStream);
//                        byte[] jpgBytes = byteArrayOutputStream.toByteArray();
//                        BitmapFactory.Options options = new BitmapFactory.Options();
//                        options.inSampleSize = 4;
//                        byte[] jpgBytes=new byte[mCameraProxy.getPreviewSize().getWidth()* mCameraProxy.getPreviewSize().getHeight()*3];
//                        ImageUtil.decodeYUV420SP(jpgBytes,mYuvBytes,mCameraProxy.getPreviewSize().getWidth(),mCameraProxy.getPreviewSize().getHeight());
//                        Bitmap originalBitmap = BitmapFactory.decodeByteArray(jpgBytes, 0, jpgBytes.length);
                            //   originalBitmap = convertToBlackWhite(originalBitmap);
//                        if (BuildConfig.DEBUG){
//                            final Bitmap finalOriginalBitmap = originalBitmap;
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    sufShow.setImageBitmap(finalOriginalBitmap);
//                                }
//                            });
//                        }

                            Log.e("bitmap=====>>", "preView照片宽：" + mCameraProxy.getPreviewSize().getWidth()
                                    + "   preView照片高：" + mCameraProxy.getPreviewSize().getHeight());
                            Bitmap originalBitmap = ColorConvertUtil.yuv420pToBitmap(mYuvBytes, mCameraProxy.getPreviewSize().getWidth(), mCameraProxy.getPreviewSize().getHeight());
                            originalBitmap = rotaingImageView(Camera2Proxy.CAMERA_DIRECT == 1 ? 270 : 90, originalBitmap);
//                            Bitmap xczpBitmap= BitmapUtil.zoomImg(xczpBitmap)
                            Log.e("bitmap=====>>", "照片宽：" + originalBitmap.getWidth()
                                    + "   照片高：" + originalBitmap.getHeight());
//                            final Bitmap finalOriginalBitmap = originalBitmap;
//                            getActivity().runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    sufShow.setImageBitmap(finalOriginalBitmap);
//                                }
//                            });
                            if (upBitmap != null && originalBitmap != null) {
                                upBitmap.UpBitmap(originalBitmap);
                            }

                            lock = false;
                            // 一定不能忘记close

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (image != null) {
                image.close();
            }
        }
    };

    public void setUpBitmap(UpBitmap upBitmap) {
        this.upBitmap = upBitmap;
    }

    public void switchCamera() {
        mCameraProxy.switchCamera();
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public interface UpBitmap {
        void UpBitmap(Bitmap bitmap);
    }

    /**
     * 将彩色图转换为纯黑白二色
     *
     * @return 返回转换好的位图
     */
    public static Bitmap convertToBlackWhite(Bitmap bmp) {
        int width = bmp.getWidth(); // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                //分离三原色
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                //转化成灰度像素
                grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        //新建图片
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        //设置图片数据
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);

        //    Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newBmp, 380, 460);
        return newBmp;
    }

    /* 旋转图片
     *
     * @param angle  被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    public Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

}
