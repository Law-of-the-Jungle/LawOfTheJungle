package com.game.junglelaw;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.game.junglelaw.data.JungleLawDbAdapter;
import com.game.junglelaw.data.JungleLawContract.PlayerScores;

/**
 * Created by apple on 11/28/15.
 */
public class HighestScoresActivity extends Activity {

    private static final String LOG_TAG = HighestScoresActivity.class.getSimpleName();

    private JungleLawDbAdapter mJungleLawDbAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highest_scores);

        mJungleLawDbAdapter = new JungleLawDbAdapter(this);
        listView = (ListView) findViewById(R.id.highest_scores_list);
        showHighestScoresList();
    }

    private void showHighestScoresList() {
        String[] from = {PlayerScores.COLUMN_SCORE};
        int[] to = {R.id.score_entry_score};

        try {
            listView.setAdapter(new SimpleCursorAdapter(this, R.layout.score_entry,
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

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}