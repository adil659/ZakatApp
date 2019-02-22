package com.ideas.innovative.zakatapp;

import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
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
    ImageView mGoldImageView = null;
    ImageView mSilverImageView = null;
    ArrayList<EditablePair<String, String>> items = new ArrayList<>();

    double mGoldValue=0;
    double mSilverValue=0;

    String mGoldCurrentMonth;
    String mGoldCurrentDay;
    String mGoldCurrentYear;

    String mSilverCurrentMonth;
    String mSilverCurrentDay;
    String mSilverCurrentYear;
    public PaymentItemsAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mContext = context;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.payment_item, parent, false);

        if(position == arrayList.size()) {
            View rowView1 = inflater.inflate(R.layout.add_more_item, parent, false);
            TextView addMore = rowView1.findViewById(R.id.itemMore);
            addMore.setText("More...");
            return rowView1;
        }
        TextView textView = rowView.findViewById(R.id.itemName);

        rowView.findViewById(R.id.info_image).setVisibility(View.INVISIBLE);


        switch(arrayList.get(position)) {
            case "Gold(g)":

                mGoldImageView = rowView.findViewById(R.id.info_image);
                mGoldImageView.setVisibility(View.VISIBLE);
                mGoldImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("The price of gold as of " + mGoldCurrentDay + " " +
                                mGoldCurrentMonth + ", " + mGoldCurrentYear + "\n is " + mGoldValue + "/g")
                                .setTitle("Gold");

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }
                });
                break;
            case "Silver(g)":
                mSilverImageView = rowView.findViewById(R.id.info_image);
                mSilverImageView.setVisibility(View.VISIBLE);
                mSilverImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        builder.setMessage("The price of silver as of " + mSilverCurrentDay + " " +
                                mSilverCurrentMonth + ", " + mSilverCurrentYear + "\n is " + mSilverValue + "/g")
                                .setTitle("Silver");

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }
                });

        }

        final EditTextListView editTextListView = rowView.findViewById(R.id.answerBox);
        editTextListView.setPosition(position);

        editTextListView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.v("AdapterLog", "answerBox focus " + hasFocus);
                if (!hasFocus) {
                    int pos = ((EditTextListView)v).getPosition();
                    String text = ((EditTextListView)v).getText().toString();
                    Log.v("AdapterLog", "answerBox " + pos + " string " + text);

                    answerBoxes.set(pos,text);
                    //items.get(pos).setValue(text);
                    //answerBoxes.set()
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

    public void addPayment (String string, String answer){
        arrayList.add(string);
        //items.add(new EditablePair<String, String>(string, answer));
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