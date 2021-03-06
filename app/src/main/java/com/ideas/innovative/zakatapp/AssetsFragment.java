package com.ideas.innovative.zakatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ideas.innovative.zakatapp.DataStructure.EditablePair;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by adil6 on 2018-10-27.
 */

public class AssetsFragment extends android.support.v4.app.Fragment {

    ListView listView;
    PaymentItemsAdapter paymentItemsAdapter;
    ArrayList<EditablePair<String, Boolean>> arrayMapAsset;
    Stack<String> currentAssets = new Stack<>();
    static double mGoldValue=0;
    static double mSilverValue=0;

    public static double GRAM_TO_OUNCE_DIVIDER = 28.34952;

    public static double OUNCES_OF_GOLD_FOR_NISAB = 3;
    public static double OUNCES_OF_SILVER_FOR_NISAB = 21;


// TODO make feature to remove payment items
// TODO put faq items in openable tabs
// TODO setup reminder system for when user wants to pay zakat every year
// TODO make setting to use gold or silver nisab, or use lowest or highest value
// TODO maybe make a homepage, options to calculate zakat, view previous calculations, see previous gold and silver prices on different days



    public AssetsFragment() {
        arrayMapAsset = new ArrayList<>();
        arrayMapAsset.add(new EditablePair<>("Cash", true));
        arrayMapAsset.add(new EditablePair<>("Gold(g)", true));
        arrayMapAsset.add(new EditablePair<>("Silver(g)", true));
        arrayMapAsset.add(new EditablePair<>("Shares", false));
        arrayMapAsset.add(new EditablePair<>("Business Assets", false));
        arrayMapAsset.add(new EditablePair<>("Investment\nProperties", false));
        arrayMapAsset.add(new EditablePair<>("Other", false));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.liability_fragment,container,false);
        listView = view.findViewById(R.id.listView);
        setupAssetsAdapter(listView);

        return view;
    }

    private void setupAssetsAdapter(ListView listView) {    // YOU CAN ADD MORE PAGES FROM HERE
        if (paymentItemsAdapter == null) {
            paymentItemsAdapter = new PaymentItemsAdapter(getContext(), R.layout.payment_item);

            for (int i = 0; i < arrayMapAsset.size(); i++) {
                String string = arrayMapAsset.get(i).getKey();
                if (arrayMapAsset.get(i).getValue()) {
                    currentAssets.add(string);
                    paymentItemsAdapter.addPayment(string, "");
                }
            }
        }

        listView.setAdapter(paymentItemsAdapter);
        setOnItemSelectedListener(listView);
    }

    public void setGoldValue(String goldValue) {
        mGoldValue = Double.valueOf(goldValue);
        paymentItemsAdapter.mGoldValue = Double.valueOf(goldValue);
    }

    public void setSilverValue(String silverValue) {
        mSilverValue = Double.valueOf(silverValue);
        paymentItemsAdapter.mSilverValue = Double.valueOf(silverValue);
    }

    public void setOnItemSelectedListener(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == paymentItemsAdapter.getArrayListSize()) {
                    Intent intent = new Intent(getContext(), AddNewItemActivity.class);
                    ArrayList<String> tmp = new ArrayList<>();
                    for (int i=0; i<arrayMapAsset.size(); i++) {
                        String string = arrayMapAsset.get(i).getKey();
                        if (!arrayMapAsset.get(i).getValue()) {
                            tmp.add(string);
                        }
                    }
                    intent.putExtra("items", tmp);
                    startActivityForResult(intent, 1);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1 && resultCode ==0 ) {
            if (data != null) {
                String sel = data.getStringExtra("selected");
                //arrayMapAsset.add(new EditablePair<String, Boolean>(sel, true));
                for (int i=0; i <arrayMapAsset.size(); i++) {
                    if (arrayMapAsset.get(i).getKey().equals(sel)) {
                        arrayMapAsset.get(i).setValue(true);
                    }
                }
                currentAssets.add(sel);
                paymentItemsAdapter.addAnswers("");
                updateAdapter();
            }
        }
    }

    public void updateAdapter() {
        ArrayList<String> tmp = new ArrayList<>();
        for (int i=0; i<currentAssets.size(); i++) {
                tmp.add(currentAssets.get(i));
        }

        paymentItemsAdapter.updateAdapter(tmp);
        listView.invalidate();
    }

    public double calculateNisaab() {
        double nisaabValue = mGoldValue * OUNCES_OF_GOLD_FOR_NISAB;
        Log.v("Calculation", "nisaab calc, gold[" + mGoldValue + "/oz * 3] nisaab value[" + nisaabValue + "]");
        return nisaabValue;
    }

    public double calculateAssets() {
        double totalAssets = totalAssets();
        return totalAssets;
    }

    public double totalAssets() {
        double total =0;
        for (int i=0; i<paymentItemsAdapter.getArrayListSize(); i++) {
            if(!paymentItemsAdapter.answerBoxes.get(i).isEmpty()) {
                String string = paymentItemsAdapter.arrayList.get(i);
                String value  = paymentItemsAdapter.answerBoxes.get(i);
                if (!value.isEmpty()) {
                    double actualValue = Double.valueOf(value);
                    if (string.equals("Gold(g)")) {
                        double goldCalc = (actualValue / GRAM_TO_OUNCE_DIVIDER) * mGoldValue;
                        Log.v("Calculation", "gold(g|oz)[" + actualValue + "|" +
                                (actualValue/GRAM_TO_OUNCE_DIVIDER) + "] goldvalue[ " + mGoldValue + "][adding asset " + string + "[" + goldCalc + "]");
                        total += goldCalc;

                    } else if (string.equals("Silver(g)")) {
                        double silverCalc = (actualValue / GRAM_TO_OUNCE_DIVIDER) * mSilverValue;
                        Log.v("Calculation", "silver(g|oz)[" + actualValue + "|" +
                                (actualValue/GRAM_TO_OUNCE_DIVIDER) + "] silverValue[ " + mSilverValue + "][adding asset " + string + "[" + silverCalc + "]");
                        total += silverCalc;

                    } else {
                        total += actualValue;
                        Log.v("Calculation", "adding asset " + string + "[" + actualValue + "]");
                    }
                }
            }
        }
        Log.v("Calculation", "Total Asset[" + total + "]");
        return total;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        // outState.putStringArrayList("answers", paymentItemsAdapter.answerBoxes);
        super.onSaveInstanceState(outState);
    }
}


































/*
    public void setupListeners() {
        if(paymentItemsAdapter != null) {
            paymentItemsAdapter.mGoldImageView.setOnClickListener(new View.OnClickListener() {
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
        }

        if (paymentItemsAdapter != null ) {
            paymentItemsAdapter.mSilverImageView.setOnClickListener(new View.OnClickListener() {
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

    }
*/
