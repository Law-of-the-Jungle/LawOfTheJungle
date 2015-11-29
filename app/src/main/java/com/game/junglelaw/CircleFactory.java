package com.game.junglelaw;

import android.util.Log;

import com.game.junglelaw.circle.MovableCircle;
import com.game.junglelaw.circle.StaticCircle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by apple on 10/15/15.
 * Provide circle in the map
 */
public class CircleFactory {
    private float SCircleDefaultRadius, MCircleDefaultRadius;
    private String TAG = "Circle Factory";

    public CircleFactory() {
        SCircleDefaultRadius = 20;
        MCircleDefaultRadius = 40;
    }

    public List<StaticCircle> BatchWorkForScircle(int x, int y, int num) {
        List<StaticCircle> res = new ArrayList<StaticCircle>();
        Random rx, ry, rcolor;
        rx = new Random();
        ry = new Random();
        rcolor = new Random(255);
        for (int i = 0; i < num; i++) {
            StaticCircle tmp = createStaticCircle(rx.nextFloat() * x, ry.nextFloat() * y, SCircleDefaultRadius, rcolor.nextInt());
            res.add(tmp);
            //Log.d(TAG,tmp.toString());
        }
        return res;
    }

    public List<MovableCircle> BatchWorkForPCircle(int x, int y, int num) {
        List<MovableCircle> res = new ArrayList<MovableCircle>();
        Random rx, ry, rcolor;
        rx = new Random(x);
        ry = new Random(y);
        rcolor = new Random(255);
        for (int i = 0; i < num; i++) {
            res.add(createMovableCircle(rx.nextFloat() * x, ry.nextFloat() * y, MCircleDefaultRadius, rcolor.nextInt()));
        }
        return res;
    }

    public MovableCircle createMovableCircle(float x, float y, float radius, int color) {
        return new MovableCircle(x, y, radius, color);
    }

    public StaticCircle createStaticCircle(float x, float y, float radius, int color) {
        return new StaticCircle(x, y, radius, color);
    }
}