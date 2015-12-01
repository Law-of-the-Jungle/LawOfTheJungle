package com.game.junglelaw;

import com.game.junglelaw.circle.AiCircle;
import com.game.junglelaw.circle.MovableCircle;
import com.game.junglelaw.circle.PlayerCircle;
import com.game.junglelaw.circle.StaticCircle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by apple on 10/15/15.
 * Provide circle in the map
 */
public class CircleFactory {

    private static final String LOG_TAG = CircleFactory.class.getSimpleName();
    private static final float STATIC_CIRCLE_RADIUS = 20;

    private String mGameDifficulty;

    public CircleFactory(String gameDifficulty) {
        this.mGameDifficulty = gameDifficulty;
    }

    public StaticCircle createStaticCircle(float x, float y, float radius, int color) {
        return new StaticCircle(x, y, radius, color);
    }

    public AiCircle createAiCircle(float x, float y, float radius, int color) {
        return new AiCircle(x, y, radius, color, mGameDifficulty);
    }

    public List<StaticCircle> createStaticCircles(int x, int y, int num) {
        List<StaticCircle> res = new ArrayList<>();
        Random rx, ry, rcolor;
        rx = new Random();
        ry = new Random();
        rcolor = new Random(255);
        for (int i = 0; i < num; i++) {
            StaticCircle tmp = createStaticCircle(rx.nextFloat() * x, ry.nextFloat() * y, STATIC_CIRCLE_RADIUS, rcolor.nextInt());
            res.add(tmp);
        }

        return res;
    }

    public List<MovableCircle> createAiCircles(int x, int y, int num, MovableCircle playerCircle) {
        List<MovableCircle> res = new ArrayList<>();
        Random rx, ry, rcolor;
        rx = new Random();
        ry = new Random();
        rcolor = new Random();
        for (int i = 0; i < num; i++) {
            res.add(createAiCircle(rx.nextFloat() * x, ry.nextFloat() * y, (float) (playerCircle.getmRadius() * 1.1), rcolor.nextInt()));
        }

        return res;
    }
}