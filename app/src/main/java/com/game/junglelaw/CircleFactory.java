package com.game.junglelaw;

import android.graphics.Color;

import com.game.junglelaw.circle.AiCircle;
import com.game.junglelaw.circle.MovableCircle;
import com.game.junglelaw.circle.PlayerCircle;
import com.game.junglelaw.circle.StaticCircle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 10/15/15.
 * Provide circle in the map
 */
public class CircleFactory {

    private static final String LOG_TAG = CircleFactory.class.getSimpleName();

    public static final float DEFAULT_STATIC_CIRCLE_RADIUS = 20;
    public static final float DEFAULT_PLAYER_CIRCLE_RADIUS = 40;

    private String mGameDifficulty;

    public CircleFactory(String gameDifficulty) {
        this.mGameDifficulty = gameDifficulty;
    }

    public PlayerCircle createPlayerCircle(float maxWidth, float maxHeight) {
        return new PlayerCircle(maxWidth, maxHeight, DEFAULT_PLAYER_CIRCLE_RADIUS, Color.BLACK);
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
                    DEFAULT_STATIC_CIRCLE_RADIUS, Utility.generateRandomNonWhite_Transparent_BlackColor());
            res.add(tmp);
        }

        return res;
    }

    public List<MovableCircle> createAiCircles(int x, int y, int num, MovableCircle playerCircle) {
        List<MovableCircle> res = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            res.add(createAiCircle(Utility.generateRandomFloat(0, x), Utility.generateRandomFloat(0, y),
                    (float) (playerCircle.getmRadius() * Utility.ABSORB_THREASHOLD_RATE),
                    Utility.generateRandomNonWhite_Transparent_BlackColor()));
        }

        return res;
    }
}