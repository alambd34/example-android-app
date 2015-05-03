package com.stefankendall.exampleApp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MyActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getPage());

        ImageView animatingImage = (ImageView) findViewById(R.id.animating_image);
        AnimationDrawable animation = (AnimationDrawable) animatingImage.getDrawable();
        animation.start();
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

        Intent i = new Intent(this, MyActivity.class);
        i.putExtra("page", getPage() == R.layout.page1 ? R.layout.page2 : R.layout.page3);
        startActivity(i);

        return true;
    }
}
