package com.ideas.innovative.zakatapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by adil6 on 2018-11-02.
 */

public class LiabilitiesFragment extends android.support.v4.app.Fragment {

    PaymentItemsAdapter paymentItemsAdapter;
    ListView listView;
    ArrayList<EditablePair<String, Boolean>> arrayMap;

    public LiabilitiesFragment() {
        arrayMap = new ArrayList<>();
        arrayMap.add(new EditablePair<>("Rent", true));
        arrayMap.add(new EditablePair<>("Personal Living Expenses", true));
        arrayMap.add(new EditablePair<>("Utilities", true));
        arrayMap.add(new EditablePair<>("School Debt", false));
        arrayMap.add(new EditablePair<>("Loan", false));
        arrayMap.add(new EditablePair<>("Bonds", false));
        arrayMap.add(new EditablePair<>("Business Liabilities", false));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.liability_fragment,container,false);
        ArrayList<String> arrayList = null;

        listView = view.findViewById(R.id.listView);
        setupLiabilitiesAdapter(listView, arrayList);

        return view;

    }

    private void setupLiabilitiesAdapter(ListView listView, ArrayList<String> answers) {    // YOU CAN ADD MORE PAGES FROM HERE
        paymentItemsAdapter = new PaymentItemsAdapter(getContext(), R.layout.payment_item);
        for (int i=0; i<arrayMap.size(); i++) {
            String string = arrayMap.get(i).getKey();
            if (arrayMap.get(i).value) {
                paymentItemsAdapter.addPayment(string, "");
            }
            if (answers != null) {
               // paymentItemsAdapter.addAnswers(answers.get(i));
            }
        }

        listView.setAdapter(paymentItemsAdapter);
        setOnItemSelectedListener(listView);
    }

    public void setOnItemSelectedListener(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == paymentItemsAdapter.getArrayListSize()) {
                    Intent intent = new Intent(getContext(), AddNewItemActivity.class);
                    ArrayList<String> tmp = new ArrayList<>();
                    for (int i=0; i<arrayMap.size(); i++) {
                        String string = arrayMap.get(i).getKey();
                        if (!arrayMap.get(i).getValue()) {
                            tmp.add(string);
                        }
                    }
                    intent.putExtra("items", tmp);
                    startActivityForResult(intent, 0);

                    setUpAddNewItem();

                }
                else {
                }
            }
        });
    }

    public void setUpAddNewItem() {

    }

    public void updateAdapter() {
        ArrayList<String> tmp = new ArrayList<>();
        for (int i=0; i<arrayMap.size(); i++) {
            String string = arrayMap.get(i).getKey();
            if (arrayMap.get(i).getValue()) {
                tmp.add(string);
            }
        }
        paymentItemsAdapter.updateAdapter(tmp);
        listView.invalidate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==0 && resultCode ==0 ) {
            if (data != null) {
                String sel = data.getStringExtra("selected");
                Log.v("Liability", "updateREsult " + sel);
                //arrayMap.add(new EditablePair<>(sel, true));
                for (int i=0; i <arrayMap.size(); i++) {
                    if (arrayMap.get(i).getKey().equals(sel)) {
                        arrayMap.get(i).setValue(true);
                    }
                }
                paymentItemsAdapter.addAnswers("");
                updateAdapter();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
       // outState.putStringArrayList("answers", paymentItemsAdapter.answerBoxes);
        super.onSaveInstanceState(outState);
    }

    public double calculate() {
        double totalLiabilities = calculateLiabilities();
        return totalLiabilities;
    }

    public double calculateLiabilities() {
        int total =0;
        for (int i=0; i<paymentItemsAdapter.getArrayListSize(); i++) {
            if(arrayMap.get(i).getValue()) {
                String string = arrayMap.get(i).getKey();
                int pos = paymentItemsAdapter.arrayList.indexOf(string);
                String value = paymentItemsAdapter.answerBoxes.get(pos);
                Log.v("Rainbow", "Liability item" + string + " position " + pos) ;

                if(!value.isEmpty()) {
                    int actualValue = Integer.valueOf(value);
                    total += actualValue;
                    //Log.v("Rainbow", "Liability " + string + " " + actualValue);
                    Log.v("Calculation", "adding liability " + string + "[" + actualValue + "]");

                }
            }
        }
        Log.v("Calculation", "Total Liability[" + total + "]");
        return total;
    }
}
