package com.ideas.innovative.zakatapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by adil6 on 2018-11-04.
 */

public class CalculateFragment extends Fragment {

    TextView zakatEligible;
    TextView zakatAmount;

    TextView nisaabAmount;
    TextView netWorthAmount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calculate_fragment, container, false);
        zakatEligible = view.findViewById(R.id.zakatEligible);
        zakatAmount = view.findViewById(R.id.zakatAmount);
        nisaabAmount = view.findViewById(R.id.nisaabValue);
        netWorthAmount = view.findViewById(R.id.netWorthEditText);
        return view;
    }


    public void calculateResult(String eligible, String amount, String nisaab, String netWorth) {
        zakatEligible.setText(eligible);
        zakatAmount.setText("$" + amount);
        nisaabAmount.setText("$" + nisaab);
        netWorthAmount.setText("$" + netWorth);
    }


}
