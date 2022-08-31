package com.bignerdranch.android.sunset;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SunsetFragment extends Fragment {
    private static final String TAG = "SunsetFragment";

    private View mSceneView;
    private View mSunView;
    private View mSkyView;
    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;
    private boolean isDay = true;
    private int mSkyBright;
    private int mSkyBright2;
    private boolean hasBeenRun = false;

    private AnimatorSet mAnimatorSet;

    public static SunsetFragment newInstance() {
        return new SunsetFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sunset, container, false);

        mSceneView = v;
        mSunView = v.findViewById(R.id.sun);
        mSkyView = v.findViewById(R.id.sky);

        Resources resources = getResources();

        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);
        mSkyBright = resources.getColor(R.color.bright_sun);
        mSkyBright2 = resources.getColor(R.color.bright_sun2);

        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDay){
                    if (hasBeenRun){
                        if (!mAnimatorSet.isRunning()){
                            startAnimation();
                            isDay = false;
                        }
                    } else {
                        startAnimation();
                        isDay = false;
                        hasBeenRun = true;
                    }
                } else {
                    if (!mAnimatorSet.isRunning()){
                        reverseAnimation();
                        isDay = true;
                    }
                }
            }
        });

        return v;
    }

    private void startAnimation(){
        float sunYStart = mSunView.getTop();
        float sunYEnd = mSkyView.getHeight();
        Log.i(TAG, "startSun: " + sunYStart);
        Log.i(TAG, "endSun: " + sunYEnd);


        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "y", sunYStart, sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mBlueSkyColor, mSunsetSkyColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mNightSkyColor)
                        .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet
                .play(heightAnimator)
                .with(sunsetSkyAnimator)
                .before(nightSkyAnimator);
        mAnimatorSet.start();



        heightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.i(TAG, "onAnimationUpdate: " + animation.getAnimatedValue());
            }
        });

    }

    private void reverseAnimation(){

        float sunYStart = mSkyView.getHeight();
        float sunYEnd = mSunView.getTop();


        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "y", sunYStart, sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor",mSunsetSkyColor, mBlueSkyColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor",mNightSkyColor , mSunsetSkyColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet
                .play(heightAnimator)
                .with(sunsetSkyAnimator)
                .after(nightSkyAnimator);
        mAnimatorSet.start();
    }
}
