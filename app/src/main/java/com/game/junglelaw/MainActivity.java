package com.game.junglelaw;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.game.junglelaw.data.JungleLawDbAdapter;

/***
 * This the main activity for welcome and setting etc
 */
public class MainActivity extends Activity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private MediaPlayer bgmMusic;

    private JungleLawDbAdapter mJungleLawDbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        bgmMusic  = MediaPlayer.create(MainActivity.this, R.raw.main);

        mJungleLawDbAdapter = new JungleLawDbAdapter(MainActivity.this);

        final Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, GameActivity.class), 1);
                bgmMusic.stop();
            }
        });

        final Button highestScoreButton = (Button) findViewById(R.id.highest_scores_button);
        highestScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HighestScoresActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        bgmMusic.setLooping(true);
        bgmMusic.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO deal with data return from game activity
        if(requestCode == 1 && resultCode==RESULT_OK){
            int scores = (int) data.getExtras().getFloat("score");
            mJungleLawDbAdapter.insert(scores);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_setting) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}