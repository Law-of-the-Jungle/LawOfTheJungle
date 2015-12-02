package com.game.junglelaw;

import android.graphics.Color;

import com.game.junglelaw.circle.AiCircle;
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

    public PlayerCircle createPlayerCircle(float mapWidth, float mapHeight) {
        return new PlayerCircle(mapWidth, mapHeight, DEFAULT_PLAYER_CIRCLE_RADIUS, Color.BLACK);
    }

    public StaticCircle createStaticCircle(float mapWidth, float mapHeight, float radius, int color) {
        return new StaticCircle(mapWidth, mapHeight, radius, color);
    }

    public AiCircle createAiCircle(float mapWidth, float mapHeight, float radius, int color) {
        return new AiCircle(mapWidth, mapHeight, radius, color, mGameDifficulty);
    }

    public List<StaticCircle> createStaticCircles(int mapWidth, int mapHeight, int num) {
        List<StaticCircle> rst = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            StaticCircle tmp = createStaticCircle(Utility.generateRandomFloat(0, mapWidth), Utility.generateRandomFloat(0, mapHeight),
                    DEFAULT_STATIC_CIRCLE_RADIUS, Utility.generateRandomNonWhite_Transparent_BlackColor());
            rst.add(tmp);
        }

        return rst;
    }

    public List<AiCircle> createAiCircles(int mapWidth, int mapHeight, int num, PlayerCircle playerCircle) {
        List<AiCircle> rst = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            rst.add(createAiCircle(Utility.generateRandomFloat(0, mapWidth), Utility.generateRandomFloat(0, mapHeight),
                    playerCircle.getmRadius() * Utility.ABSORB_THREASHOLD_RATE,
                    Utility.generateRandomNonWhite_Transparent_BlackColor()));
        }

        return rst;
    }
}