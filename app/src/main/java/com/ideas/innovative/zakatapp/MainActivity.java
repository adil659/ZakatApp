package com.ideas.innovative.zakatapp;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.util.Log;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.ideas.innovative.zakatapp.GoldValueAPI.GoldDataSet;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;


public class MainActivity extends AppCompatActivity {

    final static String BASE_URL = "https://www.quandl.com/api/v3/datasets/LBMA/";
    final static String GOLD = "GOLD";
    final static String SILVER = "SILVER";
    final static String START_DATE = "start_date=";
    final static String END_DATE = "end_date=";
    final static String API_KEY = "api_key=EhkfvazyLhnSSAVAM2qj";
    final static String AMPERSAND = "&";
    final static String QUESTION_MARK = "?";

    GoldDataSet mDataset;
    EditText goldEditText;

    AssetsFragment mAssetsFragment;
    LiabilitiesFragment mLiabilitiesFragment;
    CalculateFragment mCalculateFragment;

    ArrayMap<String, Boolean> arrayMapAsset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar); // Toolbar setup
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, //Nav drawer setup
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        goldEditText = findViewById(R.id.inputGold);

        mAssetsFragment  = new AssetsFragment();
        mLiabilitiesFragment  = new LiabilitiesFragment(); // creating instances of fragments
        mCalculateFragment = new CalculateFragment();

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager); // setting up adapter for fragments

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); // get todays date and make it in this format
        String date1 =  simpleDateFormat.format(date);

        String goldUrl = BASE_URL + GOLD + QUESTION_MARK + START_DATE + date1 + AMPERSAND + END_DATE + date1 + // gold API call
                AMPERSAND + API_KEY;

        String silverUrl = BASE_URL + SILVER + QUESTION_MARK + START_DATE + date1 + AMPERSAND + END_DATE + date1 + // silver API call
                AMPERSAND + API_KEY;

        makeApiCall(goldUrl); //fetch data how to return value from listener

    }

    private void setupViewPager(ViewPager viewPager) {    // YOU CAN ADD MORE PAGES FROM HERE
        TabsAdapter adapter = new TabsAdapter(getSupportFragmentManager());
        adapter.addFragment(mAssetsFragment, "Assets");
        adapter.addFragment(mLiabilitiesFragment, "Liabilities");
        adapter.addFragment(mCalculateFragment, "Calculate");
        viewPager.setAdapter(adapter);
    }

    public void clickCalculate(View view) {
        double goldPrice=4;
        double silverPrice=4;

        double totalAssets = mAssetsFragment.calculate(goldPrice, silverPrice); // get total assets
        double totalLiabilities = mLiabilitiesFragment.calculate(); // get total liabilities

        if (totalAssets == 0) {
           mCalculateFragment.calculateResult("No", "0");
        }
        else {
            double amount = totalAssets - totalLiabilities;
            mCalculateFragment.calculateResult("yes", String.valueOf(amount));
        }
    }

    private void makeApiCall (String url) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        final Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if( response.isSuccessful()) {
                    String string = response.body().string();

                    Document document = Jsoup.parse(string);
                    Elements elements = document.select("code");
                    for (Element element: elements) {
                        element.data();
                        String string1 = element.text();
                        mDataset = gson.fromJson(string1,GoldDataSet.class);

                        if (mDataset.dataset.data.isEmpty()) {
                            String end_date = mDataset.dataset.end_date;
                            String realUrl = BASE_URL + START_DATE + end_date + AMPERSAND + END_DATE +
                                    end_date + AMPERSAND + API_KEY;
                            makeApiCall(realUrl);
                        }
                        else {
                            //mAssetsFragment.setGoldValue(String.valueOf(mDataset.dataset.data.get(0).get(1)));
                             String.valueOf(mDataset.dataset.data.get(0).get(1));
                        }
                    }
                }
            }
        });

    }

}