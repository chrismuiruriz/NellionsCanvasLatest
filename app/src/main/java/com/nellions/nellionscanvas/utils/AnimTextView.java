package com.nellions.nellionscanvas.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by Chris Muiruri on 12/31/2015.
 */

public class AnimTextView extends AppCompatTextView {
    public AnimTextView(Context context) {
        super(context);
    }

    public AnimTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AnimTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

    private int score;

    public void setScore(final int score) {
        this.score = score;
        setText(String.valueOf(score));
    }

    public int getScore() {
        return this.score;
    }
}
