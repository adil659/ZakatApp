package com.ideas.innovative.zakatapp;


import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.ideas.innovative.zakatapp.GoldValueAPI.GoldDataSet;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
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

    AssetsFragment mAssetsFragment;
    LiabilitiesFragment mLiabilitiesFragment;
    CalculateFragment mCalculateFragment;

    String dateOfMetalPrices;
    Calendar calendar = Calendar.getInstance();
    Date date = calendar.getTime();
    SimpleDateFormat monthformat = new SimpleDateFormat("MMMM");
    SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); // get todays date and make it in this format

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

        NavigationView navigationView = findViewById(R.id.nav_view);
        setupNavViewListener(navigationView);

        mAssetsFragment  = new AssetsFragment();
        mLiabilitiesFragment  = new LiabilitiesFragment(); // creating instances of fragments
        mCalculateFragment = new CalculateFragment();

        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        fragmentChangeListener(viewPager);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager); // setting up adapter for fragments

        String date1 =  simpleDateFormat.format(date);

        String goldUrl = BASE_URL + GOLD + QUESTION_MARK + START_DATE + date1 + AMPERSAND + END_DATE + date1 + // gold API call
                AMPERSAND + API_KEY;

        String silverUrl = BASE_URL + SILVER + QUESTION_MARK + START_DATE + date1 + AMPERSAND + END_DATE + date1 + // silver API call
                AMPERSAND + API_KEY;

        Log.v("MetalApi", "date " + date1);
        Log.v("MetalApi", "gold[" + AssetsFragment.mGoldValue + "] silver[" + AssetsFragment.mSilverValue + "]");
        if(AssetsFragment.mGoldValue == 0 || !dateOfMetalPrices.equals(date1)) {
            dateOfMetalPrices = date1;
            makeApiCall(goldUrl); //fetch data how to return value from listener
        }
        if (AssetsFragment.mSilverValue == 0 || !dateOfMetalPrices.equals(date1)) {
            makeApiCall(silverUrl);
        }
    }

    public void setupNavViewListener(NavigationView view) {
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_faq:
                        Intent intent = new Intent(getApplicationContext(), FrequentlyAskedQuestions.class);
                        startActivity(intent);
                }
                return true;
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {    // YOU CAN ADD MORE PAGES FROM HERE
        TabsAdapter adapter = new TabsAdapter(getSupportFragmentManager());
        adapter.addFragment(mAssetsFragment, "Assets");
        adapter.addFragment(mLiabilitiesFragment, "Liabilities");
        adapter.addFragment(mCalculateFragment, "Calculate");
        viewPager.setAdapter(adapter);
    }

    public void clickCalculate(View view) {
        double nisaabValue = mAssetsFragment.calculateNisaab();
        double totalAssets = mAssetsFragment.calculateAssets(); // get total assets
        double totalLiabilities = mLiabilitiesFragment.calculate(); // get total liabilities

        double netWorth = totalAssets - totalLiabilities;
        Log.v("Calculation", "netWorth[" + netWorth + "]");

        if(netWorth > nisaabValue) {
            double zakatToPay = netWorth * 0.025;
            mCalculateFragment.calculateResult("yes", String.valueOf(zakatToPay), String.valueOf(nisaabValue), String.valueOf(netWorth));
            Log.v("Calculation", "can pay zakat");
        }
        else{
            mCalculateFragment.calculateResult("No", "0", String.valueOf(nisaabValue), String.valueOf(netWorth));
            Log.v("Calculation", "not eligible to pay zakat");
        }
    }

    public void fragmentChangeListener(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // hide the keyboard in order to avoid getTextBeforeCursor on inactive InputConnection
                InputMethodManager inputMethodManager = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                super.onPageSelected(position);
            }
        });
    }

    private void makeApiCall (final String url) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        final Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final Request request = new Request.Builder()
                .url(url)
                .build();
        Log.v("MetalApi", "starting api call");
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

                Log.v("MetalApi", "Failure[" + e.getMessage() + "]");
                Log.v("MetalApi", "Failure url[" + url + "]");
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
                            Log.v("MetalApi ", "trying again with new date[" + end_date + "]");
                            String realUrl = "";
                            if(url.contains("GOLD")) {
                                realUrl = BASE_URL + GOLD + QUESTION_MARK + START_DATE + end_date + AMPERSAND + END_DATE +
                                        end_date + AMPERSAND + API_KEY;
                            }
                            else if(url.contains("SILVER")) {
                                realUrl = BASE_URL + SILVER + QUESTION_MARK + START_DATE + end_date + AMPERSAND + END_DATE + end_date + // silver API call
                                        AMPERSAND + API_KEY;
                            }
                            makeApiCall(realUrl);
                            //Log.v("Are we here", "try again url " + realUrl);
                            //Log.v("Are we here ", ", did we make it here? ");
                        }
                        else {
                            if (url.contains("GOLD")) {
                                Log.v("MetalApi", "gold succeeded price[" + mDataset.dataset.data.get(0).get(1) + "]");
                                mAssetsFragment.setGoldValue(String.valueOf(mDataset.dataset.data.get(0).get(1)));
                                String end_date = mDataset.dataset.end_date;
                                Date newDate = null;
                                try {
                                    newDate = simpleDateFormat.parse(end_date);
                                    mAssetsFragment.paymentItemsAdapter.mGoldCurrentDay = dayFormat.format(newDate);
                                    mAssetsFragment.paymentItemsAdapter.mGoldCurrentMonth = monthformat.format(newDate);
                                    mAssetsFragment.paymentItemsAdapter.mGoldCurrentYear = yearFormat.format(newDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            else if (url.contains("SILVER")){
                                Log.v("MetalApi", "silver succeeded price[" + mDataset.dataset.data.get(0).get(1) + "]");
                                mAssetsFragment.setSilverValue(String.valueOf(mDataset.dataset.data.get(0).get(1)));
                                String end_date = mDataset.dataset.end_date;
                                try {
                                    Date newDate = simpleDateFormat.parse(end_date);
                                    mAssetsFragment.paymentItemsAdapter.mSilverCurrentDay = dayFormat.format(newDate);
                                    mAssetsFragment.paymentItemsAdapter.mSilverCurrentMonth = monthformat.format(newDate);
                                    mAssetsFragment.paymentItemsAdapter.mSilverCurrentYear = yearFormat.format(newDate);

                                    //mAssetsFragment.setupListeners();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        });

    }
}