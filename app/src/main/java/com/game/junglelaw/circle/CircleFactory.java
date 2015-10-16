package com.game.junglelaw.circle;

/**
 * Created by apple on 10/15/15.
 */
public class CircleFactory {

    public CircleFactory () {
    }

    public MovableCircle createPlayerCircle(float x, float y, float radius, int color) {
        return new MovableCircle(x, y, radius, color);
    }

    public PlayerCircle createStaticCircle(float x, float y, float radius, int color) {
        return new PlayerCircle(x, y ,radius, color);
    }
}