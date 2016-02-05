package com.khch.blurdialogfragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Created by kuanghaochuan on 2016/2/5.
 */
public class BlurDialogFragment extends DialogFragment {

    private static final String TAG = "BlurDialogFragment";
    private static final int HEIGHT = 500; // DialogFragment的高度，需要这个参数来截取需要处理的图片
    private static final float DIM = 0.5f; // DialogFragment周边背景的颜色深度，0f~1f，深度随数值递增
    private static final int RADIUS = 5;   // 高斯模糊控制模糊程度的参数，模糊效果随数值递增
    private Drawable mBackgroundDrawable;
    private View mRootView;

    public void showFragment(final FragmentActivity activity, final String tag) {
        final Bitmap backgroundBitmap = createBackgroundBitmap(activity);

        // create fast blur bitmap and show dialog
        new AsyncTask<Void, Void, Drawable>() {
            @Override
            protected Drawable doInBackground(Void... params) {
                Bitmap localBitmap = ImageBlurUtilsRS.fastblur(activity, backgroundBitmap, RADIUS);
                return new BitmapDrawable(activity.getResources(), localBitmap);
            }

            @Override
            protected void onPostExecute(Drawable drawable) {
                mBackgroundDrawable = drawable;
                show(activity.getSupportFragmentManager(), tag);

                if (backgroundBitmap != null) {
                    backgroundBitmap.recycle();
                }
            }
        }.execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 去掉标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        mRootView = inflater.inflate(R.layout.dialogfragment_selections, container);
        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        int width = getActivity().getResources().getDisplayMetrics().widthPixels;

        // 设置DialogFragment的位置，款高度以及背景图片，以及周边背景
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(width, HEIGHT);
        window.setBackgroundDrawable(mBackgroundDrawable);
        window.setDimAmount(DIM);
    }

    private Bitmap createBackgroundBitmap(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        Bitmap bmp = view.getDrawingCache();

        int width = bmp.getWidth();
        int height = bmp.getHeight();

        Bitmap bgBitmap = Bitmap.createBitmap(bmp, 0, height / 2 - HEIGHT / 2, width, HEIGHT);
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);
        return bgBitmap;
    }
}
