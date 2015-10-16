package com.game.junglelaw.circlecc;

import android.graphics.PointF;

/**
 * Created by apple on 10/15/15.
 */
public class PlayerCircle extends MovableCircle {

    public PlayerCircle(float x, float y, float radius, int color) {
        super(x, y, radius, color);
    }

    /** 根据用户点击位置，计算player circle的新direction */
    public void setNewDirection(PointF userClickPoint) {
        float newX = userClickPoint.x - x;
        float newY = userClickPoint.y - y;
        float len = (float) Math.sqrt(Math.pow(newX, 2) + Math.pow(newY, 2)); // for 归一化处理

        setDirection(newX / len, newY / len);
    }
}