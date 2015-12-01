package com.game.junglelaw;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.game.junglelaw.data.JungleLawDbAdapter;
import com.game.junglelaw.data.JungleLawContract.PlayerScores;

/**
 * Created by apple on 11/28/15.
 */
public class HighestScoresActivity extends Activity {

    private static final String LOG_TAG = HighestScoresActivity.class.getSimpleName();

    private JungleLawDbAdapter mJungleLawDbAdapter;
    private ListView mHighestScoresListView;
    private MediaPlayer mHighestScoresBackgroundMusic;
    private boolean mIsMute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highest_scores);

        mHighestScoresBackgroundMusic = MediaPlayer.create(HighestScoresActivity.this, R.raw.score_board);
        mHighestScoresBackgroundMusic.setLooping(true);

        mJungleLawDbAdapter = new JungleLawDbAdapter(this);
        mHighestScoresListView = (ListView) findViewById(R.id.highest_scores_list);
        showHighestScoresList();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(HighestScoresActivity.this);
        mIsMute = prefs.getBoolean(getString(R.string.pref_mute_key), false);

        if(!mIsMute) {
            mHighestScoresBackgroundMusic.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!mIsMute) {
            mHighestScoresBackgroundMusic.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mIsMute) {
            mHighestScoresBackgroundMusic.release();
        }
    }

    private void showHighestScoresList() {
        String[] from = {PlayerScores.COLUMN_SCORE, PlayerScores.COLUMN_SCORE_CREATE_TIME};
        int[] to = {R.id.score_entry_score, R.id.score_entry_create_time};

        try {
            mHighestScoresListView.setAdapter(new ShowRankCursorAdapter(this, R.layout.score_entry,
                    mJungleLawDbAdapter.query(), from, to, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_setting) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}