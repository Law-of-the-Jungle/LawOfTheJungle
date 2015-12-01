package com.game.junglelaw;

import com.game.junglelaw.circle.*;

import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;

import java.util.Random;

/**
 * Created by apple on 10/15/15.
 */
public class Utility {

    private static String TAG = "Utility";

    public static float getRelativeRadius(float playerRadius, float circleRadius, float player_on_screen_radius) {
        Log.d(TAG, Float.toString(player_on_screen_radius * (circleRadius / playerRadius)));
        return player_on_screen_radius * circleRadius / playerRadius;
    }

    public static int generateRandomInt(int lo, int hi) {
        return lo + (int) (Math.random() * (hi - lo + 1));
    }

    public static float generateRandomFloat(float lo, float hi) {
        return lo + (float) (Math.random() * (hi - lo));
    }

    /**
     * If circle1's radius is larger than or equal to circle2's
     *
     * @param circle1
     * @param circle2
     * @return
     */
    public static boolean isLarger(AbstractCircle circle1, AbstractCircle circle2) {
        return circle1.getRadius() >= circle2.getRadius();
    }

    /**
     * Can circleLarge absorb circle2.
     * <p>
     * Pre-assumption: circleLarge's radius is larger than or equal to circle2's
     */
    public static boolean canAbsorb(AbstractCircle circleLarge, AbstractCircle circleSmall) {
        if (circleLarge.getRadius() < circleSmall.getRadius())
            return false;
        return circleCenterDistance(circleLarge, circleSmall) + circleSmall.getRadius() <= 1.1 * circleLarge.getRadius();
    }

    public static double circleCenterDistance(AbstractCircle circle1, AbstractCircle circle2) {
        return Math.sqrt(Math.pow(circle1.getCenter().x - circle2.getCenter().x, 2) + Math.pow(circle1.getCenter().y - circle2.getCenter().y, 2));
    }
}