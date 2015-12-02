package com.game.junglelaw;

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

    private final String mGameDifficulty;

    public CircleFactory(String gameDifficulty) {
        this.mGameDifficulty = gameDifficulty;
    }

    public PlayerCircle createRandomPlayerCircle(float mapWidth, float mapHeight) {
        return new PlayerCircle(Utility.generateRandomFloat(0, mapWidth), Utility.generateRandomFloat(0, mapHeight));
    }

    public StaticCircle createStaticCircle(float x, float y, int color) {
        return new StaticCircle(x, y, StaticCircle.STATIC_CIRCLE_DEFAULT_RADIUS, color);
    }

    public AiCircle createAiCircle(float x, float y, float radius, int color) {
        return new AiCircle(x, y, radius, color, mGameDifficulty);
    }

    public List<StaticCircle> createRandomStaticCircles(int mapWidth, int mapHeight, int num) {
        List<StaticCircle> staticCircles = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            staticCircles.add(createStaticCircle(Utility.generateRandomFloat(0, mapWidth), Utility.generateRandomFloat(0, mapHeight),
                    Utility.generateRandomNonWhiteTransparentBlackColor()));
        }

        return staticCircles;
    }

    public List<AiCircle> createRandomAiCircles(int mapWidth, int mapHeight, int num, PlayerCircle playerCircle) {
        List<AiCircle> aiCircles = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            aiCircles.add(createAiCircle(Utility.generateRandomFloat(0, mapWidth), Utility.generateRandomFloat(0, mapHeight),
                    playerCircle.getmRadius() * AiCircle.AI_CIRCLE_DEFAULT_RADIUS_RATE,
                    Utility.generateRandomNonWhiteTransparentBlackColor()));
        }

        return aiCircles;
    }
}