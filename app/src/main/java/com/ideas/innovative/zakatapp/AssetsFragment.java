package com.ideas.innovative.zakatapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ideas.innovative.zakatapp.GoldValueAPI.GoldDataSet;

/**
 * Created by adil6 on 2018-10-27.
 */

public class AssetsFragment extends android.support.v4.app.Fragment {

    EditText mGoldEditText;
    TextView mTextView;
    public AssetsFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.asset_fragment,container,false);
        mGoldEditText = view.findViewById(R.id.inputGold);
        mTextView = view.findViewById(R.id.gold);
        return view;

    }


    public void setGoldValue(String goldValue) {
        mTextView.append("\n($" + goldValue + "/oz)");
    }
}
