package com.game.junglelaw;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/***
 * This the main activity for welcome and settings
 */
public class MainActivity extends Activity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int GAME_ACTIVITY_RESULT_CODE = 1;

    private MediaPlayer mMainBackgroundMusic;
    private boolean mIsMute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mMainBackgroundMusic = MediaPlayer.create(MainActivity.this, R.raw.main);
        mMainBackgroundMusic.setLooping(true);

        final Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, GameActivity.class), GAME_ACTIVITY_RESULT_CODE);

                if (!mIsMute) {
                    mMainBackgroundMusic.pause();
                }
            }
        });

        final Button highestScoreButton = (Button) findViewById(R.id.highest_scores_button);
        highestScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HighestScoresActivity.class));

                if (!mIsMute) {
                    mMainBackgroundMusic.pause();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        mIsMute = prefs.getBoolean(getString(R.string.pref_mute_key), false);

        if (!mIsMute && !mMainBackgroundMusic.isPlaying()) {
            mMainBackgroundMusic.start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GAME_ACTIVITY_RESULT_CODE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Your Score: " + data.getExtras().getInt(Utility.INTENT_EXTRA_SCORE_KEY) +
                    ";\n Game Difficulty: " + data.getExtras().getString(Utility.INTENT_EXTRA_GAME_DIFFICULTY_KEY), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!mIsMute && mMainBackgroundMusic.isPlaying()) {
            mMainBackgroundMusic.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mIsMute) {
            mMainBackgroundMusic.release();
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

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}