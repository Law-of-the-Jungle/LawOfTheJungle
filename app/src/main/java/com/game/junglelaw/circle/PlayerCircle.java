package com.game.junglelaw.circle;

import android.graphics.PointF;
import android.util.Log;

import com.game.junglelaw.GameView;

/**
 * Created by apple on 10/15/15.
 * Take charge of lan control
 */
public class PlayerCircle extends MovableCircle {

    private static final String LOG_TAG = PlayerCircle.class.getSimpleName();

    private final float DEFAULT_SCREEN_RADIUS = 30;
    private final double SHIFT_THRESHOLD = 0.25;

    public float mPlayerOnScreenRadius;
    public float mZoomRate;

    public PlayerCircle(float x, float y, float radius, int color) {
        super(x, y, radius, color);
        mPlayerOnScreenRadius = DEFAULT_SCREEN_RADIUS;
        mZoomRate = calculateZoomRate();
    }

    public void setDirectTowardPoint(PointF targetPoint) {
        PointF p = new PointF(targetPoint.x - mCenter.x, targetPoint.y - mCenter.y);
        setmMovingDirection(p);
        Log.i(LOG_TAG, mCenter.toString() + " -> " + targetPoint.toString() + " ;" + p.toString());
        Log.v(LOG_TAG, mMovingDirection.toString());
    }

    /**
     * 根据用户当前位置，计算player circle的新direction
     */
    private float calculateZoomRate() {
        return mPlayerOnScreenRadius / getmRadius();
    }

    public void updateZoom(GameView view) {
        //switch the lan to a border
        //Log.d("updateZoom",Double.toString(mPlayerOnScreenRadius)+" |  "+Double.toString(Math.min(view.getMap_height(),view.getMap_width())*SHIFT_THRESHOLD));
        //if (mPlayerOnScreenRadius*2 > Math.min(view.getmScreenHeight(),view.getmScreenWidth())*SHIFT_THRESHOLD){
        if (mPlayerOnScreenRadius >= 80) {
            //mPlayerOnScreenRadius=DEFAULT_SCREEN_RADIUS;
            mPlayerOnScreenRadius *= 0.8;
            mZoomRate = calculateZoomRate();
        } else {
            mPlayerOnScreenRadius = getmRadius() * mZoomRate;
        }
    }
}