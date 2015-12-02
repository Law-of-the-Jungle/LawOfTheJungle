package com.game.junglelaw;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by apple on 12/1/15.
 *
 * Add rank to each score entry
 */
public class ShowRankCursorAdapter extends SimpleCursorAdapter {

    public ShowRankCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flag) {
        super(context, layout, c, from, to, flag);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int rank = cursor.getPosition() + 1; // Rank use 1-based index
        LinearLayout highestScoreLinearLayout = (LinearLayout) view;
        TextView scoreEntryTextView = (TextView) highestScoreLinearLayout.findViewById(R.id.score_entry_rank);
        scoreEntryTextView.setText(rank + "");
        super.bindView(view, context, cursor);
    }
}