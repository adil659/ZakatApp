package com.ideas.innovative.zakatapp;

import android.content.Intent;
import android.os.Bundle;
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

    EditText mGoldEditText;
    TextView mTextView;
    ListView listView;
    PaymentItemsAdapter paymentItemsAdapter;
    //ArrayMap<String, Boolean> arrayMapAsset;
    ArrayList<EditablePair<String, Boolean>> arrayMapAsset;


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
        mGoldEditText = view.findViewById(R.id.inputGold);
        mTextView = view.findViewById(R.id.gold);
        listView = view.findViewById(R.id.listView);
        setupLiabilitiesAdapter(listView);
        return view;
    }

    private void setupLiabilitiesAdapter(ListView listView) {    // YOU CAN ADD MORE PAGES FROM HERE
        paymentItemsAdapter = new PaymentItemsAdapter(getContext(), R.layout.payment_item);
        for (int i=0; i<arrayMapAsset.size(); i++) {
            String string = arrayMapAsset.get(i).getKey();
            if (arrayMapAsset.get(i).getValue()) {
                paymentItemsAdapter.addPayment(string);
            }
        }

        listView.setAdapter(paymentItemsAdapter);
        setOnItemSelectedListener(listView);
    }

    public void setGoldValue(String goldValue) {
        mTextView.append("\n($" + goldValue + "/oz)");
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

    public double calculate(double goldPrice, double silverPrice) {

        int certainOuncesOfGold = 3;
        double nisaabValue = goldPrice * certainOuncesOfGold;
        double totalAssets = totalAssets();

        if (totalAssets < nisaabValue) {
            return 0;
        }

        return totalAssets;
    }

    public double totalAssets() {
        for (int i=0; i<paymentItemsAdapter.getArrayListSize(); i++) {

        }
        return 0;
    }
}
