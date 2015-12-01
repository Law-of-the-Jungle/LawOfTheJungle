package com.game.junglelaw;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by apple on 12/1/15.
 */
public class ShowRankCursorAdapter extends SimpleCursorAdapter {

    public ShowRankCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flag) {
        super(context, layout, c, from, to, flag);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int tmp = cursor.getPosition() + 1;
        LinearLayout ll = (LinearLayout) view;
        TextView tv = (TextView) ll.findViewById(R.id.score_entry_rank);
        tv.setText(tmp + "");
        super.bindView(view, context, cursor);
    }
}
