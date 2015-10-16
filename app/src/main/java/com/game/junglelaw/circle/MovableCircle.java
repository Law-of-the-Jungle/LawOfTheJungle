package com.game.junglelaw.circle;

import android.graphics.PointF;

/**
 * Created by apple on 10/15/15.
 */
public class MovableCircle extends AbstractCircle {

    private PointF direction; // direction vector size should be 1 (i.e. has a unit distance)

    protected MovableCircle(float x, float y, float radius, int color) {
        super(x, y, radius, color);
    }

    public void setDirection(float newX, float newY) {
        direction.set(newX, newY);
    }

    public PointF getDirection() {
        return direction;
    }

    /** Moves forward to the direction by one unit distance */
    public void moveToDirection() {
        x = direction.x;
        y = direction.y;
    }

    /**
     * Absorbs the targetCircle into current player circle.
     *
     * Pre-assumption: current player circle's radius is larger than targetCircle's.
     */
    public void absorb(AbstractCircle targetCircle) {
        // TODO stay tuned
    }
}