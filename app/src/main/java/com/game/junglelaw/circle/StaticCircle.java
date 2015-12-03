package com.game.junglelaw.circle;

/**
 * Created by apple on 10/15/15.
 */
public class StaticCircle extends AbstractCircle {

    private static final String LOG_TAG = StaticCircle.class.getSimpleName();

    public static final float STATIC_CIRCLE_DEFAULT_RADIUS = 20;

    public StaticCircle(float x, float y, int color) {
        super(x, y, STATIC_CIRCLE_DEFAULT_RADIUS, color);
    }
}