package com.ideas.innovative.zakatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by adil6 on 2018-11-03.
 */

public class PaymentItemsAdapter extends ArrayAdapter {
    Context mContext;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> answerBoxes = new ArrayList<>();
    public PaymentItemsAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.payment_item, parent, false);

        if(position == arrayList.size()) {
            View rowView1 = inflater.inflate(R.layout.add_more_item, parent, false);
            TextView addMore = rowView1.findViewById(R.id.itemMore);
            addMore.setText("More...");
            return rowView1;
        }

        TextView textView = rowView.findViewById(R.id.itemName);
        final EditTextListView editTextListView = rowView.findViewById(R.id.answerBox);
        editTextListView.setPosition(position);

        editTextListView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.v("AdapterLog", "answerBox focus " + hasFocus);
                if (!hasFocus) {
                    int pos = ((EditTextListView)v).getPosition();
                     String text = ((EditTextListView)v).getText().toString();
                     answerBoxes.add(pos,text);
                }

            }
        });
        textView.setText(arrayList.get(position));
        editTextListView.setText(answerBoxes.get(position));
        //answerBoxes.add(position, answer);
        //Log.v("AdapterLog", "answerBox " + editText.getText());
        //editText.setText(editText.getText());

        return rowView;
    }

    @Override
    public int getCount() {
        return arrayList.size() +1;
    }

    public void addPayment (String string){
        arrayList.add(string);
        answerBoxes.add("");
    }

    public void addAnswers(String string) {
        answerBoxes.add(string);
    }

    public int getArrayListSize() {
        return arrayList.size();
    }

    public void updateAdapter(ArrayList<String> strings) {
        arrayList = strings;
        notifyDataSetChanged();
    }
}