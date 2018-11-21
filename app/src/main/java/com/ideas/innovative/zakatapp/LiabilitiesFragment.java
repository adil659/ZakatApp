package com.ideas.innovative.zakatapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.util.Log;
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
    android.support.v4.util.ArrayMap<String, Boolean> arrayMap;
    ListView listView;

    public LiabilitiesFragment() {
        arrayMap = new android.support.v4.util.ArrayMap<>();
        arrayMap.put("Rent", true);
        arrayMap.put("Personal Living Expenses", true);
        arrayMap.put("Utilities", true);
        arrayMap.put("School Debt", false);
        arrayMap.put("Loan", false);
        arrayMap.put("Bonds", false);
        arrayMap.put("Business Liabilities", false);

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
            String string = arrayMap.keyAt(i);
            if (arrayMap.get(string)) {
                paymentItemsAdapter.addPayment(string);
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
                    Log.v ("Hello", "1");
                    Intent intent = new Intent(getContext(), AddNewItemActivity.class);
                    ArrayList<String> tmp = new ArrayList<>();
                    for (int i=0; i<arrayMap.size(); i++) {
                        String string = arrayMap.keyAt(i);
                        if (!arrayMap.get(string)) {
                            tmp.add(string);
                        }
                    }
                    intent.putExtra("items", tmp);
                    startActivityForResult(intent, 0);

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

    public void updateAdapter() {
        ArrayList<String> tmp = new ArrayList<>();
        for (int i=0; i<arrayMap.size(); i++) {
            String string = arrayMap.keyAt(i);
            if (arrayMap.get(string)) {
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
                arrayMap.put(sel, true);
                updateAdapter();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
       // outState.putStringArrayList("answers", paymentItemsAdapter.answerBoxes);
        super.onSaveInstanceState(outState);
    }

    public void calculate() {


    }
}
/*
<!---
<TextView
        android:id="@+id/rentTextView"
        android:layout_width="187dp"
        android:layout_height="50dp"
        android:layout_marginBottom="8dp"
        android:layout_marginEnd="144dp"
        android:layout_marginStart="8dp"
        android:layout_marginTop="8dp"
        android:fontFamily="monospace"
        android:gravity="center"
        android:text="Rent"
        android:textSize="18sp"
        android:textStyle="bold"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.073"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.139" />

    <EditText
        android:id="@+id/rentEditText"
        android:layout_width="133dp"
        android:layout_height="50dp"
        android:background="@drawable/rounded_button1"
        android:ems="10"
        android:gravity="center"
        android:inputType="textPersonName"
        android:text="Name"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.905"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.15" />

    <TextView
        android:id="@+id/personalTextView"
        android:layout_width="187dp"
        android:layout_height="50dp"
        android:layout_marginBottom="8dp"
        android:layout_marginEnd="144dp"
        android:layout_marginStart="8dp"
        android:layout_marginTop="8dp"
        android:fontFamily="monospace"
        android:gravity="center"
        android:text="Personal"
        android:textSize="18sp"
        android:textStyle="bold"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.088"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.343" />

    <EditText
        android:id="@+id/personalEditText"
        android:layout_width="133dp"
        android:layout_height="50dp"
        android:background="@drawable/rounded_button1"
        android:ems="10"
        android:gravity="center"
        android:inputType="textPersonName"
        android:text="Name"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.904"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.349" />

-!>
 */

 /*  paymentItemsAdapter.addPayment("Rent");
        paymentItemsAdapter.addPayment("Personal");
        paymentItemsAdapter.addPayment("Checking Account");
        paymentItemsAdapter.addPayment("Savings Accounts");
        */
//  paymentItemsAdapter.addPayment("More...");