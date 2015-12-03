package com.game.junglelaw;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import com.game.junglelaw.data.JungleLawDbAdapter;

import java.util.Date;

public class GameActivity extends Activity {

    private static final String LOG_TAG = GameActivity.class.getSimpleName();

    private GameView mGameView;
    private MediaPlayer mGameBackgroundMusic;
    private JungleLawDbAdapter mJungleLawDbAdapter;
    private String mGameDifficulty;
    private boolean mIsMute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTheme(R.style.TransparentActionBarTheme);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(GameActivity.this);
        mGameDifficulty = prefs.getString(getString(R.string.pref_game_diffculty_key), getString(R.string.pref_game_diffculty_easy));
        mIsMute = prefs.getBoolean(getString(R.string.pref_mute_key), false);
        mGameView = new GameView(this, mGameDifficulty);
        setContentView(mGameView);

        mJungleLawDbAdapter = new JungleLawDbAdapter(GameActivity.this);

        mGameBackgroundMusic = MediaPlayer.create(GameActivity.this, R.raw.fighting);
        mGameBackgroundMusic.setLooping(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!mIsMute && !mGameBackgroundMusic.isPlaying()) {
            mGameBackgroundMusic.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!mIsMute) {
            mGameBackgroundMusic.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGameBackgroundMusic.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGameView = new GameView(GameActivity.this, mGameDifficulty);
        setContentView(mGameView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.save_score_and_end_game) {
            mJungleLawDbAdapter.insert(mGameView.getScore(), mGameDifficulty, new Date().toString());
            Intent sharedIntent = new Intent();
            sharedIntent.putExtra(Utility.INTENT_EXTRA_SCORE_KEY, mGameView.getScore());
            sharedIntent.putExtra(Utility.INTENT_EXTRA_GAME_DIFFICULTY_KEY, mGameDifficulty);
            setResult(RESULT_OK, sharedIntent);
            finish();
            return true;
        } else if (id == R.id.pause_game) {
            mGameView.pauseGame();
        } else if (id == R.id.resume_game) {
            mGameView.resumeGame();
        }

        return super.onOptionsItemSelected(item);
    }
}
