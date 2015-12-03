package com.game.junglelaw;

import com.game.junglelaw.circle.AiCircle;
import com.game.junglelaw.circle.PlayerCircle;
import com.game.junglelaw.circle.StaticCircle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 10/15/15.
 */
public class CircleFactory {

    private static final String LOG_TAG = CircleFactory.class.getSimpleName();

    private final String mGameDifficulty;
    private final float mMapWidth;
    private final float mMapHeight;

    public CircleFactory(String gameDifficulty, float mapWidth, float mapHeight) {
        mGameDifficulty = gameDifficulty;
        mMapWidth = mapWidth;
        mMapHeight = mapHeight;
    }

    public PlayerCircle createRandomPlayerCircle() {
        return new PlayerCircle(Utility.generateRandomFloat(0, mMapWidth), Utility.generateRandomFloat(0, mMapHeight), mMapWidth, mMapHeight);
    }

    public StaticCircle createStaticCircle(float x, float y, int color) {
        return new StaticCircle(x, y, color);
    }

    public AiCircle createAiCircle(float x, float y, float radius, int color) {
        return new AiCircle(x, y, radius, color, mGameDifficulty, mMapWidth, mMapHeight);
    }

    public List<StaticCircle> createRandomStaticCircles(int num) {
        List<StaticCircle> staticCircles = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            staticCircles.add(createStaticCircle(Utility.generateRandomFloat(0, mMapWidth), Utility.generateRandomFloat(0, mMapHeight),
                    Utility.generateRandomNonWhiteTransparentBlackColor()));
        }

        return staticCircles;
    }

    public List<AiCircle> createRandomAiCircles(int num, PlayerCircle playerCircle) {
        List<AiCircle> aiCircles = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            aiCircles.add(createAiCircle(Utility.generateRandomFloat(0, mMapWidth), Utility.generateRandomFloat(0, mMapHeight),
                    playerCircle.getmRadius() * AiCircle.AI_CIRCLE_DEFAULT_RADIUS_RATE,
                    Utility.generateRandomNonWhiteTransparentBlackColor()));
        }

        return aiCircles;
    }
}