package com.game.junglelaw;

import com.game.junglelaw.circle.*;

import android.graphics.Color;
import android.graphics.PointF;

/**
 * Created by apple on 10/15/15.
 */
public class Utility {

    private static final String LOG_TAG = Utility.class.getSimpleName();

    public static final float ABSORB_THREASHOLD_RATE = (float) 1.1;

    public static int generateRandomInt(int lo, int hi) {
        return lo + (int) (Math.random() * (hi - lo + 1));
    }

    public static float generateRandomFloat(float lo, float hi) {
        return lo + (float) (Math.random() * (hi - lo));
    }

    public static int generateRandomNonWhite_Transparent_BlackColor() {
        int color = Color.WHITE;
        while (color == Color.WHITE || color == Color.TRANSPARENT || color == Color.BLACK) {
            color = Color.argb(255, generateRandomInt(0, 255), generateRandomInt(0, 255), generateRandomInt(0, 255));
        }

        return color;
    }

    /**
     * Can if largeCircle is large enough to absorb smallCircle.
     *
     * Pre-assumption: largeCircle's mRadius is larger than or equal to smallCircle's
     */
    public static boolean isAbsorbableLarger(AbstractCircle largeCircle, AbstractCircle smallCircle) {
        return largeCircle.getmRadius() >= ABSORB_THREASHOLD_RATE * smallCircle.getmRadius();
    }

    /**
     * Can largeCircle absorb smallCircle.
     *
     * Pre-assumption: largeCircle's mRadius is larger than or equal to smallCircle's
     */
    public static boolean canAbsorb(AbstractCircle largeCircle, AbstractCircle smallCircle) {
        if (!isAbsorbableLarger(largeCircle, smallCircle)) {
            return false;
        }

        return pointsDistance(largeCircle.getmCenter(), smallCircle.getmCenter()) <= largeCircle.getmRadius() - smallCircle.getmRadius();
    }

    public static float pointsDistance(PointF p1, PointF p2) {
        return (float) Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
}