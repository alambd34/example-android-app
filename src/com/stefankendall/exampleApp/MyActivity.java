package com.stefankendall.exampleApp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

public class MyActivity extends Activity {
    private Random random;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.random = new Random();

        setContentView(getPage());

        ImageView animatingImage = (ImageView) findViewById(R.id.animating_image);
        if (animatingImage != null) {
            AnimationDrawable animation = (AnimationDrawable) animatingImage.getDrawable();
            animation.start();
        }

        if (getPage() == R.layout.page3) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 15; i++) {
                        launchFirework();
                    }
                }
            }, 100);
        }
    }

    private void launchFirework() {
        final View firework = new View(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(getPixels(5), getPixels(5));
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.ABOVE, R.id.trophy);
        params.bottomMargin = getPixels(-40);
        firework.setLayoutParams(params);
        firework.setBackgroundColor(Color.WHITE);
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.trophy_view);
        layout.addView(firework);

        TranslateAnimation animation = getRandomDirectionMovement();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                final Animation.AnimationListener listener = this;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TranslateAnimation randomDirectionMovement = getRandomDirectionMovement();
                        randomDirectionMovement.setAnimationListener(listener);
                        firework.startAnimation(randomDirectionMovement);
                    }
                }, MyActivity.this.random.nextInt(100));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        firework.startAnimation(animation);
    }

    private TranslateAnimation getRandomDirectionMovement() {
        int maxHeight = -125;
        int maxWidth = 200;
        int xPositiveMultiplier = this.random.nextInt(2) == 0 ? -1 : 1;
        float yMultiplier = (float) ((40 + this.random.nextInt(60)) / 100.0);
        float xMultiplier = (float) (this.random.nextInt(50) / 100.0) * xPositiveMultiplier;
        TranslateAnimation animation = new TranslateAnimation(0, maxWidth * xMultiplier, 0, maxHeight * yMultiplier);
        animation.setDuration(250);
        animation.setFillAfter(true);
        animation.setInterpolator(new LinearInterpolator());
        return animation;
    }

    private int getPixels(int dipValue) {
        Resources r = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue,
                r.getDisplayMetrics());
    }

    private int getPage() {
        Bundle extras = getIntent().getExtras();
        int page = R.layout.page1;
        if (extras != null) {
            page = extras.getInt("page", R.layout.page1);
        }
        return page;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getPage() == R.layout.page3) {
            return false;
        }

        Intent i = new Intent(this, MyActivity.class);
        i.putExtra("page", getPage() == R.layout.page1 ? R.layout.page2 : R.layout.page3);
        startActivity(i);

        return true;
    }
}
