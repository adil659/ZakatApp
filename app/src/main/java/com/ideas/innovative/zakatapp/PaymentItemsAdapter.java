package com.ideas.innovative.zakatapp;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ideas.innovative.zakatapp.DataStructure.EditTextListView;

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

    ArrayList<PaymentItem> items = new ArrayList<>();

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

        boolean isGoldOrSilverBox = false;
        switch(arrayList.get(position)) {
            case "Gold(g)":
                isGoldOrSilverBox = true;
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
                isGoldOrSilverBox = true;
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
        if (!isGoldOrSilverBox) {
            editTextListView.setCompoundDrawablesWithIntrinsicBounds(new TextDrawable("$", editTextListView.getHeight()), null, null, null);
        }
        editTextListView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    int pos = ((EditTextListView)v).getPosition();
                    String text = ((EditTextListView)v).getText().toString();

                    answerBoxes.set(pos,text);
                }

            }
        });
        textView.setText(arrayList.get(position));
        editTextListView.setText(answerBoxes.get(position));

        return rowView;
    }

    @Override
    public int getCount() {
        return arrayList.size() +1;
    }

    public void addPayment (String string, String answer){
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