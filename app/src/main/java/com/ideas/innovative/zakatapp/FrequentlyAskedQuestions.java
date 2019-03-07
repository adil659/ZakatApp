package com.ideas.innovative.zakatapp;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;


public class FrequentlyAskedQuestions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frequently_asked_questions);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }
}
