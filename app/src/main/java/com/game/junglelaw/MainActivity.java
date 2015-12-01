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

import com.game.junglelaw.data.JungleLawDbAdapter;

import java.util.Date;

/***
 * This the main activity for welcome and setting etc
 */
public class MainActivity extends Activity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private boolean isMute;
    private MediaPlayer bkgMusic;

    private JungleLawDbAdapter mJungleLawDbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        bkgMusic = MediaPlayer.create(MainActivity.this, R.raw.main);
        bkgMusic.setLooping(true);

        mJungleLawDbAdapter = new JungleLawDbAdapter(MainActivity.this);

        final Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, GameActivity.class), 1);

                if (!isMute) {
                    bkgMusic.pause();
                }
            }
        });

        final Button highestScoreButton = (Button) findViewById(R.id.highest_scores_button);
        highestScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HighestScoresActivity.class));

                if (!isMute) {
                    bkgMusic.pause();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        isMute = prefs.getBoolean(getString(R.string.pref_mute_key), false);

        if (!isMute) {
            bkgMusic.start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            int scores = (int) data.getExtras().getFloat("score");
            mJungleLawDbAdapter.insert(scores, new Date().toString());
            Toast.makeText(this, "Your Score: " + scores, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!isMute) {
            bkgMusic.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!isMute) {
            bkgMusic.release();
        }
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