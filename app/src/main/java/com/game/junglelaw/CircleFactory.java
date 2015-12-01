package com.game.junglelaw;

import com.game.junglelaw.circle.AiCircle;
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

    private static final String LOG_TAG = CircleFactory.class.getSimpleName();
    private static final float STATIC_CIRCLE_RADIUS = 20;

    private String mGameDifficulty;

    public CircleFactory(String gameDifficulty) {
        this.mGameDifficulty = gameDifficulty;
    }

    public StaticCircle createStaticCircle(float maxWidth, float maxHeight, float radius, int color) {
        return new StaticCircle(maxWidth, maxHeight, radius, color);
    }

    public AiCircle createAiCircle(float maxWidth, float maxHeight, float radius, int color) {
        return new AiCircle(maxWidth, maxHeight, radius, color, mGameDifficulty);
    }

    public List<StaticCircle> createStaticCircles(int x, int y, int num) {
        List<StaticCircle> res = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            StaticCircle tmp = createStaticCircle(Utility.generateRandomFloat(0, x), Utility.generateRandomFloat(0, y),
                    STATIC_CIRCLE_RADIUS, Utility.generateRandomNonWhiteNonTransparentColor());
            res.add(tmp);
        }

        return res;
    }

    public List<MovableCircle> createAiCircles(int x, int y, int num, MovableCircle playerCircle) {
        List<MovableCircle> res = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            res.add(createAiCircle(Utility.generateRandomFloat(0, x), Utility.generateRandomFloat(0, y),
                    (float) (playerCircle.getmRadius() * Utility.ABSORB_THREASHOLD_RATE),
                    Utility.generateRandomNonWhiteNonTransparentColor()));
        }

        return res;
    }
}