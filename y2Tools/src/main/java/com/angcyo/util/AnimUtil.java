package com.angcyo.util;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class AnimUtil {

    public AnimUtil() {
        // TODO Auto-generated constructor stub
    }

    // 平移视图动画,从什么位置到什么位置
    public static TranslateAnimation newTranAnim(int startX, int toX,
                                                 int startY, int toY) {
        TranslateAnimation anim = new TranslateAnimation(startX, toX, startY,
                toY);
        anim.setDuration(Constants.ANIM_DURATION);
        anim.setFillAfter(true);

        return anim;
    }

    public static TranslateAnimation newTranAnim(int startX, int toX,
                                                 int startY, int toY, long time, boolean fillAfter) {
        TranslateAnimation anim = new TranslateAnimation(startX, toX, startY,
                toY);
        anim.setDuration(time);
        anim.setFillAfter(fillAfter);
        return anim;
    }

    public static void moveView(View v, int startX, int toX, int startY, int toY) {
        TranslateAnimation anim = new TranslateAnimation(startX, toX, startY,
                toY);
        anim.setDuration(Constants.ANIM_DURATION);
        anim.setFillAfter(true);
        v.startAnimation(anim);
    }

    public static void moveView(View v, int startX, int toX, int startY,
                                int toY, long time, boolean fillAfter) {
        TranslateAnimation anim = new TranslateAnimation(startX, toX, startY,
                toY);
        anim.setDuration(time);
        anim.setFillAfter(fillAfter);

        v.startAnimation(anim);
    }

    public static void moveView(View v, int startX, int toX, int startY,
                                int toY, long time, boolean fillAfter, AnimationListener listener) {
        TranslateAnimation anim = new TranslateAnimation(startX, toX, startY,
                toY);
        anim.setDuration(time);
        anim.setFillAfter(fillAfter);

        anim.setAnimationListener(listener);
        v.startAnimation(anim);
    }

    // 透明视图动画，从多少透明度到多少透明度
    public static AlphaAnimation newAlphaAnim(float fromAlpha, float toAlpha) {
        AlphaAnimation anim = new AlphaAnimation(fromAlpha, toAlpha);
        anim.setDuration(Constants.ANIM_DURATION);
        anim.setFillAfter(true);

        return anim;
    }

    public static AlphaAnimation newAlphaAnim(float fromAlpha, float toAlpha,
                                              long time, boolean fillAfter) {
        AlphaAnimation anim = new AlphaAnimation(fromAlpha, toAlpha);
        anim.setDuration(time);
        anim.setFillAfter(fillAfter);

        return anim;
    }

    public static void alphaView(View v, float fromAlpha, float toAlpha) {
        AlphaAnimation anim = new AlphaAnimation(fromAlpha, toAlpha);
        anim.setDuration(Constants.ANIM_DURATION);
        anim.setFillAfter(true);
        v.startAnimation(anim);
    }

    public static void alphaView(View v, float fromAlpha, float toAlpha,
                                 long time, boolean fillAfter, AnimationListener listener) {
        AlphaAnimation anim = new AlphaAnimation(fromAlpha, toAlpha);
        anim.setDuration(time);
        anim.setFillAfter(fillAfter);

        anim.setAnimationListener(listener);
        v.startAnimation(anim);
    }

    // 旋转视图动画，从多少角度到多少角度，并指定圆点。

    public static RotateAnimation newRotateAniim(float fromDegrees,
                                                 float toDegrees, float pivotX, float pivotY) {
        RotateAnimation anim = new RotateAnimation(fromDegrees, toDegrees,
                pivotX, pivotY);
        anim.setDuration(Constants.ANIM_DURATION);
        anim.setFillAfter(true);
        return anim;
    }

    public static RotateAnimation newRotateAniim(float fromDegrees,
                                                 float toDegrees, float pivotX, float pivotY, long time,
                                                 boolean fillAfter) {
        RotateAnimation anim = new RotateAnimation(fromDegrees, toDegrees,
                pivotX, pivotY);
        anim.setDuration(time);
        anim.setFillAfter(fillAfter);
        return anim;
    }

    public static void rotateView(View v, float fromDegrees, float toDegrees,
                                  float pivotX, float pivotY) {
        RotateAnimation anim = new RotateAnimation(fromDegrees, toDegrees,
                pivotX, pivotY);
        anim.setDuration(Constants.ANIM_DURATION);
        anim.setFillAfter(true);
        v.startAnimation(anim);
    }

    public static void rotateView(View v, float fromDegrees, float toDegrees,
                                  float pivotX, float pivotY, long time, boolean fillAfter,
                                  AnimationListener listener) {
        RotateAnimation anim = new RotateAnimation(fromDegrees, toDegrees,
                pivotX, pivotY);
        anim.setDuration(time);
        anim.setFillAfter(fillAfter);
        anim.setAnimationListener(listener);
        v.startAnimation(anim);
    }

    // 放大缩小动画，从多少比例到多少比例
    public static ScaleAnimation newScaleAnim(float fromX, float toX,
                                              float fromY, float toY) {
        ScaleAnimation anim = new ScaleAnimation(fromX, toX, fromY, toY);
        anim.setDuration(Constants.ANIM_DURATION);
        anim.setFillAfter(true);
        return anim;
    }

    public static ScaleAnimation newScaleAnim(float fromX, float toX,
                                              float fromY, float toY, long time, boolean fillAfter) {
        ScaleAnimation anim = new ScaleAnimation(fromX, toX, fromY, toY);
        anim.setDuration(time);
        anim.setFillAfter(fillAfter);
        return anim;
    }

    public static ScaleAnimation newScaleAnim(float fromX, float toX,
                                              float fromY, float toY, float pivotX, float pivotY, long time,
                                              boolean fillAfter) {
        ScaleAnimation anim = new ScaleAnimation(fromX, toX, fromY, toY,
                pivotX, pivotY);
        anim.setDuration(time);
        anim.setFillAfter(fillAfter);
        return anim;
    }

    public static void scaleView(View v, float fromX, float toX, float fromY,
                                 float toY) {
        ScaleAnimation anim = new ScaleAnimation(fromX, toX, fromY, toY);
        anim.setDuration(Constants.ANIM_DURATION);
        anim.setFillAfter(true);
        v.startAnimation(anim);
    }

    public static void scaleView(View v, float fromX, float toX, float fromY,
                                 float toY, long time, boolean fillAfter, AnimationListener listener) {
        ScaleAnimation anim = new ScaleAnimation(fromX, toX, fromY, toY);
        anim.setDuration(time);
        anim.setFillAfter(fillAfter);
        anim.setAnimationListener(listener);
        v.startAnimation(anim);
    }
}
