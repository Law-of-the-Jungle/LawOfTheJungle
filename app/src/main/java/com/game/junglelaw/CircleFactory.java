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

    private String difficulty;
    private float SCircleDefaultRadius;
    private String TAG = "Circle Factory";

    public CircleFactory(String difficulty) {
        SCircleDefaultRadius = 20;
        this.difficulty = difficulty;
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

    public List<MovableCircle> BatchWorkForPCircle(int x, int y, int num, MovableCircle playerCircle) {
        List<MovableCircle> res = new ArrayList<>();
        Random rx, ry, rcolor;
        rx = new Random();
        ry = new Random();
        rcolor = new Random();
        for (int i = 0; i < num; i++) {
            res.add(createMovableCircle(rx.nextFloat() * x, ry.nextFloat() * y, (float) (playerCircle.getRadius() * 1.1), rcolor.nextInt()));
        }
        return res;
    }

    public MovableCircle createMovableCircle(float x, float y, float radius, int color) {
        return new MovableCircle(x, y, radius, color, difficulty);
    }

    public StaticCircle createStaticCircle(float x, float y, float radius, int color) {
        return new StaticCircle(x, y, radius, color);
    }
}