package com.ideas.innovative.zakatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by adil6 on 2018-11-07.
 */

public class NewPaymentItemAdapter extends ArrayAdapter {

    ArrayList<String> arrayList = new ArrayList<String>();
    Context context;

    public NewPaymentItemAdapter(@NonNull Context context, int resource, ArrayList<String> strings) {
        super(context, resource);
        this.context = context;
       arrayList = strings;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.add_more_item, parent, false);

        TextView textView = rowView.findViewById(R.id.itemMore);
        textView.setText(arrayList.get(position));
        return rowView;
    }

    public void addItem (String string) {
        arrayList.add(string);
    }
}
