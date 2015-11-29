package com.game.junglelaw.circle;

import android.graphics.PointF;

/**
 * Created by apple on 10/15/15.
 */
public class MovableCircle extends AbstractCircle {

    private static final String LOG_TAG = MovableCircle.class.getSimpleName();

    private PointF direction; // direction vector size should be 1 (i.e. has a unit distance)
    private float speed;

    protected MovableCircle(float x, float y, float radius, int color) {
        super(x, y, radius, color);
        direction = new PointF(0, 0);
        speed = 10;
    }

    public void setSpeed(float new_speed) {
        speed = new_speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setDirection(float newX, float newY) {
        direction.set(newX, newY);
    }

    public PointF getDirection() {
        return direction;
    }

    /**
     * Moves forward to the direction by one unit distance
     */
    public void moveToDirection(int width, int height) {
        float newX = center.x + speed * direction.x;
        float newY = center.y + speed * direction.y;

        if ((newX > 0 && newX < width) && (newY > 0 && newY < height)) {
            setCenter(newX, newY);
        }
    }

    public void setNewDirection(PointF userClickPoint, PointF center) {
        float newX = userClickPoint.x - center.x;
        float newY = userClickPoint.y - center.y;
        float len = (float) Math.sqrt(Math.pow(newX, 2) + Math.pow(newY, 2)); // for 归一化处理
        setDirection(newX / len, newY / len);
    }

    public void randomMove() {

    }

    /**
     * Absorbs the targetCircle into current player circle.
     * <p/>
     * Pre-assumption: current player circle's radius is larger than targetCircle's.
     */
    public void absorb(AbstractCircle targetCircle) {
        // TODO stay tuned
    }
}