package com.game.junglelaw.data;

import android.provider.BaseColumns;

/**
 * Created by apple on 11/28/15.
 */
public class JungleLawContract {

    private static final String LOG_TAG = JungleLawContract.class.getSimpleName();

    public static final class PlayerScores implements BaseColumns {

        public static final String TABLE_NAME = "player_scores";

        public static final String COLUMN_SCORE = "score";
    }
}
