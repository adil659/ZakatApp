package com.ideas.innovative.zakatapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adil6 on 2018-11-07.
 */

public class  AddNewItemActivity extends AppCompatActivity {

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
                if (string.equals("Anything else")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddNewItemActivity.this);
                    final View layoutView = getLayoutInflater().inflate(R.layout.more_item_dialog_layout, null);
                    builder.setView(layoutView);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText editText = layoutView.findViewById(R.id.customZakatItem);
                            String string1 = editText.getText().toString().trim();
                            string1 = string1.substring(0, 1).toUpperCase() + string1.substring(1);
                            if(string1.length() == 0) {
                                Toast.makeText(getApplicationContext(), "Please fill out the required field", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent intent1 = new Intent();
                                intent1.putExtra("selected", string1);
                                setResult(0, intent1);
                                finish();
                            }
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else {
                    Intent intent = new Intent();
                    intent.putExtra("selected", string);
                    setResult(0, intent);
                    finish();
                }
            }
        });
    }


}
