package com.game.junglelaw.xx;

import android.graphics.Canvas;
import android.graphics.PointF;

/**
 * Created by apple on 10/15/15.
 *
 * Declares the common properties of all circles.
 */
public abstract class AbstractCircle extends PointF {

    private int color;
    private float radius;

    protected AbstractCircle(float x, float y, float radius, int color) {
        super(x, y);
        this.radius = radius;
        this.color = color;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void draw(Canvas canvas) {
    } // TODO

}