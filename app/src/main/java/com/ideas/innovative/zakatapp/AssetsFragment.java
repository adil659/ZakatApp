package com.ideas.innovative.zakatapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ideas.innovative.zakatapp.GoldValueAPI.GoldDataSet;

import java.util.ArrayList;

/**
 * Created by adil6 on 2018-10-27.
 */

public class AssetsFragment extends android.support.v4.app.Fragment {

    ListView listView;
    PaymentItemsAdapter paymentItemsAdapter;
    ArrayList<EditablePair<String, Boolean>> arrayMapAsset;

    double mGoldValue=0;
    double mSilverValue=0;

    String mGoldCurrentMonth;
    String mGoldCurrentDay;
    String mGoldCurrentYear;

    String mSilverCurrentMonth;
    String mSilverCurrentDay;
    String mSilverCurrentYear;


    public AssetsFragment() {
        arrayMapAsset = new ArrayList<>();
        arrayMapAsset.add(new EditablePair<String, Boolean>("Cash", true));
        arrayMapAsset.add(new EditablePair<String, Boolean>("Gold(g)", true));
        arrayMapAsset.add(new EditablePair<String, Boolean>("Silver(g)", true));
        arrayMapAsset.add(new EditablePair<String, Boolean>("Shares", false));
        arrayMapAsset.add(new EditablePair<String, Boolean>("Business Assets", false));
        arrayMapAsset.add(new EditablePair<String, Boolean>("Investment Properties", false));
        arrayMapAsset.add(new EditablePair<String, Boolean>("Anything else", false));
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
        setupLiabilitiesAdapter(listView);

        return view;
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
    private void setupLiabilitiesAdapter(ListView listView) {    // YOU CAN ADD MORE PAGES FROM HERE
        paymentItemsAdapter = new PaymentItemsAdapter(getContext(), R.layout.payment_item);
        for (int i=0; i<arrayMapAsset.size(); i++) {
            String string = arrayMapAsset.get(i).getKey();
            if (arrayMapAsset.get(i).getValue()) {
                paymentItemsAdapter.addPayment(string, "");
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
                    Log.v ("Hello", "1");
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

                    setUpAddNewItem();

                }
                else {
                    Log.v ("Hello", "2");
                }
            }
        });
    }

    public void setUpAddNewItem() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1 && resultCode ==0 ) {
            if (data != null) {
                String sel = data.getStringExtra("selected");
                Log.v("Liability", "updateREsult " + sel);
                //arrayMapAsset.add(new EditablePair<String, Boolean>(sel, true));
                for (int i=0; i <arrayMapAsset.size(); i++) {
                    if (arrayMapAsset.get(i).getKey().equals(sel)) {
                        arrayMapAsset.get(i).setValue(true);
                    }
                }
                paymentItemsAdapter.addAnswers("");
                updateAdapter();
            }
        }
    }

    public void updateAdapter() {
        ArrayList<String> tmp = new ArrayList<>();
        for (int i=0; i<arrayMapAsset.size(); i++) {
            String string = arrayMapAsset.get(i).getKey();
            if (arrayMapAsset.get(i).getValue()) {
                tmp.add(string);
            }
        }
        paymentItemsAdapter.updateAdapter(tmp);
        listView.invalidate();
    }

    public double calculate() {

        double certainOuncesOfGold = 3.0857662;
        double nisaabValue = mGoldValue * certainOuncesOfGold;
        Log.v("Calculation", "nisaab value[" + nisaabValue + "]");
        double totalAssets = totalAssets();

        if (totalAssets < nisaabValue) {
            Log.v("Calculation", "not eligible to pay zakat");
            return 0;
        }
        Log.v("Calculation", "can pay zakat");
        return totalAssets;
    }

    public double totalAssets() {
        int total =0;
        for (int i=0; i<paymentItemsAdapter.getArrayListSize(); i++) {
            if(arrayMapAsset.get(i).getValue()) {
                String string = arrayMapAsset.get(i).getKey();
                int pos = paymentItemsAdapter.arrayList.indexOf(string);
                String value  = paymentItemsAdapter.answerBoxes.get(pos);
                if (!value.isEmpty()) {
                    int actualValue = Integer.valueOf(value);
                    if (string.equals("Gold(g)")) {
                        //Log.v("Rainbow", "we got gold(g)" + actualValue + " mGoldValue " + mGoldValue);
                        double goldCalc = (actualValue / 28.35) * mGoldValue;
                        Log.v("Calculation", "adding asset " + string + "[" + goldCalc + "]");
                        total += goldCalc;

                    } else if (string.equals("Silver(g)")) {
                        double silverCalc = (actualValue / 28.35) * mSilverValue;
                        Log.v("Calculation", "adding asset " + string + "[" + silverCalc + "]");

                        total += silverCalc;

                        //Log.v("Rainbow", "we got silver(g)" + actualValue);

                    } else {
                        total += actualValue;
                        Log.v("Calculation", "adding asset " + string + "[" + actualValue + "]");

                        //Log.v("Rainbow", "Asset " + string + " " + actualValue);


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
