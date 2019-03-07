package com.ideas.innovative.zakatapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adil6 on 2018-11-07.
 */

public class AddNewItemActivity extends AppCompatActivity {

    ArrayList<String> arrayList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_item_activity);

        ListView listView = findViewById(R.id.newItemListView);

        Bundle bundle = getIntent().getExtras();
        arrayList = bundle.getStringArrayList("items");
        NewPaymentItemAdapter adapter = new NewPaymentItemAdapter(getApplicationContext(), R.layout.payment_item, arrayList);

        listView.setAdapter(adapter);
        listViewListener(listView);
    }


    public void listViewListener(ListView listView) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String string = arrayList.get(position);
                Intent intent = new Intent();
                intent.putExtra("selected", string);
                setResult(0, intent);
                finish();
            }
        });
    }


}
