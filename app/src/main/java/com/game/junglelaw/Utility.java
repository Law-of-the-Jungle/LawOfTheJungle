package com.game.junglelaw;

import android.graphics.Color;
import android.graphics.PointF;

import com.game.junglelaw.circle.AbstractCircle;
import com.game.junglelaw.circle.MovableCircle;

/**
 * Created by apple on 10/15/15.
 */
public class Utility {

    private static final String LOG_TAG = Utility.class.getSimpleName();

    public static final String INTENT_EXTRA_SCORE = "com.game.junglelaw.intent.extra.SCORE";
    public static final String INTENT_EXTRA_GAME_DIFFICULTY = "com.game.junglelaw.intent.GAME_DIFFICULTY";

    public static int generateRandomInt(int lo, int hi) {
        return lo + (int) (Math.random() * (hi - lo + 1));
    }

    public static float generateRandomFloat(float lo, float hi) {
        return lo + (float) (Math.random() * (hi - lo));
    }

    public static int generateRandomNonWhiteTransparentBlackColor() {
        int color = Color.WHITE;
        while (color == Color.WHITE || color == Color.TRANSPARENT || color == Color.BLACK) {
            color = Color.argb(255, generateRandomInt(0, 255), generateRandomInt(0, 255), generateRandomInt(0, 255));
        }

        return color;
    }

    /**
     * If largeCircle is large enough to movableCirclesAbsorbCircles smallCircle.
     *
     * Pre-assumption: largeCircle's mRadius is larger than or equal to smallCircle's
     */
    public static boolean isAbsorbableLarger(AbstractCircle largeCircle, AbstractCircle smallCircle) {
        return largeCircle.getmRadius() >= MovableCircle.ABSORB_THREASHOLD_RATE * smallCircle.getmRadius();
    }

    /**
     * Can largeCircle movableCirclesAbsorbCircles smallCircle.
     *
     * Pre-assumption: largeCircle's mRadius is larger than or equal to smallCircle's
     */
    public static boolean canAbsorb(AbstractCircle largeCircle, AbstractCircle smallCircle) {
        if (!isAbsorbableLarger(largeCircle, smallCircle)) {
            return false;
        }

        return pointsDistance(largeCircle.getmCenter(), smallCircle.getmCenter()) <=
                1.1 * largeCircle.getmRadius() - smallCircle.getmRadius();
    }

    public static float pointsDistance(PointF p1, PointF p2) {
        return (float) Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
}