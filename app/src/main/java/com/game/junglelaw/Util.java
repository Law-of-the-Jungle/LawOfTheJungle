package com.game.junglelaw;

import com.game.junglelaw.circle.*;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;

import java.util.Random;

/**
 * Created by apple on 10/15/15.
 */
public class Util {

    private static String TAG="Util";
    public static float getRelativeRadius(float playerRadius,float circleRadius,float player_on_screen_radius){
        Log.d(TAG,Float.toString(player_on_screen_radius*(circleRadius/playerRadius)));
        return player_on_screen_radius*circleRadius/playerRadius;
    }
    public static PointF generateRandomPoint(int xBound, int yBound, Random rand) {
        return new PointF(rand.nextFloat() * xBound, rand.nextFloat() * yBound);
    }

    /** Returns a random radius in range of [lo, hi] */
    public static double generateRandomRadius(double lo, double hi, Random rand) {
        return lo + rand.nextInt((int) (hi - lo));
    }

    public static int generateRandomColor(Random rand) {
        return Color.argb(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
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
     *
     * Pre-assumption: circleLarge's radius is larger than or equal to circle2's
     */
    public static boolean canAbsorb(AbstractCircle circleLarge, AbstractCircle circleSmall) {
        if (circleLarge.getRadius()<circleSmall.getRadius())
            return false;
        return circleCenterDistance(circleLarge, circleSmall) + circleSmall.getRadius() <= 1.1 * circleLarge.getRadius();
    }

    public static double circleCenterDistance(AbstractCircle circle1, AbstractCircle circle2) {
        return Math.sqrt(Math.pow(circle1.x - circle2.x, 2) + Math.pow(circle1.y - circle2.y, 2));
    }
}