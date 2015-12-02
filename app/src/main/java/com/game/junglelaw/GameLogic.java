package com.game.junglelaw;

import android.util.Log;

/**
 * GameLogic controls the game logic.
 */
public class GameLogic implements Runnable {

    private static final String LOG_TAG = GameLogic.class.getSimpleName();

    private GameView mGameView;
    private boolean mIsGamePlaying; // If the game is still playing
    private boolean mIsPause; // If the game is paused

    public GameLogic(GameView gameView) {
        mGameView = gameView;
        mIsGamePlaying = false;
        mIsPause = false;
    }

    public void setmIsPause(boolean isPause) {
        mIsPause = isPause;
    }

    public void setmIsGamePlaying(boolean isGamePlaying) {
        mIsGamePlaying = isGamePlaying;
    }

    @Override
    public void run() {
        Log.d(LOG_TAG, "Start game loop...");

        while (mIsGamePlaying) {
            if (mIsPause) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
                continue;
            }

            executeGameLogic();
            mGameView.drawGamePlayView();

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        }

        Log.d(LOG_TAG, "Left game loop...");

        mGameView.drawGameOverView();
    }

    private void executeGameLogic() {
        mGameView.getmCircleManager().getmPlayerCircle().updateZoomRate();
        mGameView.getmCircleManager().moveMovableCirclesToDirection();
        mGameView.getmCircleManager().movableCirclesAbsorb();
        mGameView.getmCircleManager().controlCirclesPopulation();

        // If player circle get absorbed
        if (mGameView.getmCircleManager().getmPlayerCircle().getmIsAbsorbed()) {
            mIsGamePlaying = false;
        }
    }
}