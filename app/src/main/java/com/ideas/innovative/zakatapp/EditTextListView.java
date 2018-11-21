package com.ideas.innovative.zakatapp;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by adil6 on 2018-11-18.
 */

public class EditTextListView extends android.support.v7.widget.AppCompatEditText {

    int position;

    public EditTextListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
