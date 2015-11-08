package com.vk.fesswood.billdivider.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.animation.PathInterpolator;

import com.vk.fesswood.billdivider.R;
import com.vk.fesswood.billdivider.ui.dialog.AddSumDialog;

import org.jetbrains.annotations.NotNull;


public class GUIUtils {

    public static final int SCALE_FACTOR = 30;
    private static final java.lang.String TAG = GUIUtils.class.getSimpleName();

    public static void configureWindowEnterExitExplodeTransition(Window w) {

        Fade ex = new Fade();
        ex.setInterpolator(new PathInterpolator(0.4f, 0, 1, 1));
        ex.setDuration(5000);
        w.setExitTransition(ex);
        w.setEnterTransition(ex);
    }

    public static void configureFab(View fabButton) {

        int fabSize = fabButton.getContext().getResources()
                .getDimensionPixelSize(R.dimen.fab_size);
        Outline fabOutLine = new Outline();
        fabOutLine.setOval(0, 0, fabSize, fabSize);
    }

    public static int getStatusBarHeight(Context c) {

        int result = 0;
        int resourceId = c.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = c.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static void hideRevealEffect(final View v, int centerX, int centerY, int initialRadius) {

        v.setVisibility(View.VISIBLE);

        // create the animation (the final radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(
                v, centerX, centerY, initialRadius, 0);

        anim.setDuration(350);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setVisibility(View.INVISIBLE);
            }
        });

        anim.start();
    }

    public static void showRevealEffect(final View v, int centerX, int centerY, Animator.AnimatorListener lis) {

        v.setVisibility(View.VISIBLE);

        int height = v.getHeight();

        Animator anim = ViewAnimationUtils.createCircularReveal(
                v, centerX, centerY, 0, height);

        anim.setDuration(350);

        anim.addListener(lis);
        anim.start();
    }

    private static void revealShow(View rootView, boolean reveal, final Dialog dialog) {
        final View view = rootView.findViewById(R.id.circularReveal);
        int w = view.getWidth();
        int h = view.getHeight();
        float maxRadius = (float) Math.sqrt(w * w / 4 + h * h / 4);

        if (reveal) {
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view,
                    w / 2, h / 2, 0, maxRadius);

            view.setVisibility(View.VISIBLE);
            revealAnimator.start();

        } else {

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, w / 2, h / 2, maxRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);

                }
            });

            anim.start();
        }

    }

    public static void hideViewByScale(View view, @Nullable Animator.AnimatorListener listener) {
        ViewPropertyAnimator propertyAnimator = view.animate().setStartDelay(SCALE_FACTOR)
                .scaleX(0).scaleY(0);
        propertyAnimator.setListener(listener);
        propertyAnimator.start();
    }

    public static void showViewByScale(View view, @Nullable Animator.AnimatorListener listener) {
        ViewPropertyAnimator propertyAnimator = view.animate().setStartDelay(SCALE_FACTOR)
                .scaleX(1).scaleY(1);
        propertyAnimator.setListener(listener);
        propertyAnimator.start();
    }


    public static void changeFub(@NotNull final FloatingActionButton fabAddMore, final Drawable drawable) {
        Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fabAddMore.setImageDrawable(drawable);
                showViewByScale(fabAddMore, null);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        };
        hideViewByScale(fabAddMore, animationListener);
    }

    public static void showDialog(final View root, AppCompatActivity activity) {
        final AddSumDialog dialogAdd = new AddSumDialog(activity);
        dialogAdd.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                revealShow(root, true, dialogAdd);
            }
        });
        dialogAdd.show();
    }


}
