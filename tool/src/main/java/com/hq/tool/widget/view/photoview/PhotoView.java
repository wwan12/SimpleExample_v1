/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.hq.tool.widget.view.photoview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.widget.ImageView;

/**
 * A zoomable {@link ImageView}. See {@link PhotoViewAttacher} for most of the details on how the zooming
 * is accomplished
 *  mPhotoView.setOnMatrixChangeListener(new MatrixChangeListener());
 *         mPhotoView.setOnPhotoTapListener(new PhotoTapListener());
 *         mPhotoView.setOnSingleFlingListener(new SingleFlingListener());
 *
 *          @Override
 *     public boolean onOptionsItemSelected(MenuItem item) {
 *         switch (item.getItemId()) {
 *             case R.id.menu_zoom_toggle:
 *                 mPhotoView.setZoomable(!mPhotoView.isZoomEnabled());
 *                 return true;
 *
 *             case R.id.menu_scale_fit_center:
 *                 mPhotoView.setScaleType(ScaleType.FIT_CENTER);
 *                 return true;
 *
 *             case R.id.menu_scale_fit_start:
 *                 mPhotoView.setScaleType(ScaleType.FIT_START);
 *                 return true;
 *
 *             case R.id.menu_scale_fit_end:
 *                 mPhotoView.setScaleType(ScaleType.FIT_END);
 *                 return true;
 *
 *             case R.id.menu_scale_fit_xy:
 *                 mPhotoView.setScaleType(ScaleType.FIT_XY);
 *                 return true;
 *
 *             case R.id.menu_scale_scale_center:
 *                 mPhotoView.setScaleType(ScaleType.CENTER);
 *                 return true;
 *
 *             case R.id.menu_scale_scale_center_crop:
 *                 mPhotoView.setScaleType(ScaleType.CENTER_CROP);
 *                 return true;
 *
 *             case R.id.menu_scale_scale_center_inside:
 *                 mPhotoView.setScaleType(ScaleType.CENTER_INSIDE);
 *                 return true;
 *
 *             case R.id.menu_scale_random_animate:
 *             case R.id.menu_scale_random:
 *                 Random r = new Random();
 *
 *                 float minScale = mPhotoView.getMinimumScale();
 *                 float maxScale = mPhotoView.getMaximumScale();
 *                 float randomScale = minScale + (r.nextFloat() * (maxScale - minScale));
 *                 mPhotoView.setScale(randomScale, item.getItemId() == R.id.menu_scale_random_animate);
 *
 *                 showToast(String.format(SCALE_TOAST_STRING, randomScale));
 *
 *                 return true;
 *             case R.id.menu_matrix_restore:
 *                 if (mCurrentDisplayMatrix == null)
 *                     showToast("You need to capture display matrix first");
 *                 else
 *                     mPhotoView.setDisplayMatrix(mCurrentDisplayMatrix);
 *                 return true;
 *             case R.id.menu_matrix_capture:
 *                 mCurrentDisplayMatrix = new Matrix();
 *                 mPhotoView.getDisplayMatrix(mCurrentDisplayMatrix);
 *                 return true;
 *         }
 *
 *         return super.onOptionsItemSelected(item);
 *     }
 *
 *     private class PhotoTapListener implements OnPhotoTapListener {
 *
 *         @Override
 *         public void onPhotoTap(ImageView view, float x, float y) {
 *             float xPercentage = x * 100f;
 *             float yPercentage = y * 100f;
 *
 *             showToast(String.format(PHOTO_TAP_TOAST_STRING, xPercentage, yPercentage, view == null ? 0 : view.getId()));
 *         }
 *     }
 *     private class MatrixChangeListener implements OnMatrixChangedListener {
 *
 *         @Override
 *         public void onMatrixChanged(RectF rect) {
 *             mCurrMatrixTv.setText(rect.toString());
 *         }
 *     }
 *
 *     private class SingleFlingListener implements OnSingleFlingListener {
 *
 *         @Override
 *         public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
 *             Log.d("PhotoView", String.format(FLING_LOG_STRING, velocityX, velocityY));
 *             return true;
 *         }
 *     }
 */
public class PhotoView extends androidx.appcompat.widget.AppCompatImageView {

    private PhotoViewAttacher attacher;

    public PhotoView(Context context) {
        this(context, null);
    }

    public PhotoView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public PhotoView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        init();
    }


    private void init() {
        attacher = new PhotoViewAttacher(this);
        //We always pose as a Matrix scale type, though we can change to another scale type
        //via the attacher
        super.setScaleType(ScaleType.MATRIX);
    }

    /**
     * Get the current {@link PhotoViewAttacher} for this view. Be wary of holding on to references
     * to this attacher, as it has a reference to this view, which, if a reference is held in the
     * wrong place, can cause memory leaks.
     *
     * @return the attacher.
     */
    public PhotoViewAttacher getAttacher() {
        return attacher;
    }

    @Override
    public ScaleType getScaleType() {
        return attacher.getScaleType();
    }

    @Override
    public Matrix getImageMatrix() {
        return attacher.getImageMatrix();
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        attacher.setOnLongClickListener(l);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        attacher.setOnClickListener(l);
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (attacher != null) {
            attacher.setScaleType(scaleType);
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        // setImageBitmap calls through to this method
        if (attacher != null) {
            attacher.update();
        }
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        if (attacher != null) {
            attacher.update();
        }
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        if (attacher != null) {
            attacher.update();
        }
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        boolean changed = super.setFrame(l, t, r, b);
        if (changed) {
            attacher.update();
        }
        return changed;
    }

    public void setRotationTo(float rotationDegree) {
        attacher.setRotationTo(rotationDegree);
    }

    public void setRotationBy(float rotationDegree) {
        attacher.setRotationBy(rotationDegree);
    }

    public boolean isZoomEnabled() {
        return attacher.isZoomEnabled();
    }

    public RectF getDisplayRect() {
        return attacher.getDisplayRect();
    }

    public void getDisplayMatrix(Matrix matrix) {
        attacher.getDisplayMatrix(matrix);
    }

    public boolean setDisplayMatrix(Matrix finalRectangle) {
        return attacher.setDisplayMatrix(finalRectangle);
    }

    public float getMinimumScale() {
        return attacher.getMinimumScale();
    }

    public float getMediumScale() {
        return attacher.getMediumScale();
    }

    public float getMaximumScale() {
        return attacher.getMaximumScale();
    }

    public float getScale() {
        return attacher.getScale();
    }

    public void setAllowParentInterceptOnEdge(boolean allow) {
        attacher.setAllowParentInterceptOnEdge(allow);
    }

    public void setMinimumScale(float minimumScale) {
        attacher.setMinimumScale(minimumScale);
    }

    public void setMediumScale(float mediumScale) {
        attacher.setMediumScale(mediumScale);
    }

    public void setMaximumScale(float maximumScale) {
        attacher.setMaximumScale(maximumScale);
    }

    public void setScaleLevels(float minimumScale, float mediumScale, float maximumScale) {
        attacher.setScaleLevels(minimumScale, mediumScale, maximumScale);
    }

    public void setOnMatrixChangeListener(OnMatrixChangedListener listener) {
        attacher.setOnMatrixChangeListener(listener);
    }

    public void setOnPhotoTapListener(OnPhotoTapListener listener) {
        attacher.setOnPhotoTapListener(listener);
    }

    public void setOnOutsidePhotoTapListener(OnOutsidePhotoTapListener listener) {
        attacher.setOnOutsidePhotoTapListener(listener);
    }

    public void setScale(float scale) {
        attacher.setScale(scale);
    }

    public void setScale(float scale, boolean animate) {
        attacher.setScale(scale, animate);
    }

    public void setScale(float scale, float focalX, float focalY, boolean animate) {
        attacher.setScale(scale, focalX, focalY, animate);
    }

    public void setZoomable(boolean zoomable) {
        attacher.setZoomable(zoomable);
    }

    public void setZoomTransitionDuration(int milliseconds) {
        attacher.setZoomTransitionDuration(milliseconds);
    }

    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener newOnDoubleTapListener) {
        attacher.setOnDoubleTapListener(newOnDoubleTapListener);
    }

    public void setOnScaleChangeListener(OnScaleChangedListener onScaleChangedListener) {
        attacher.setOnScaleChangeListener(onScaleChangedListener);
    }

    public void setOnSingleFlingListener(OnSingleFlingListener onSingleFlingListener) {
        attacher.setOnSingleFlingListener(onSingleFlingListener);
    }
}
