package com.ideas.innovative.zakatapp;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;

import android.widget.EditText;

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

    GoldDataSet mDataset;
    EditText goldEditText;

    AssetsFragment mAssetsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        goldEditText = findViewById(R.id.inputGold);
        mAssetsFragment  = new AssetsFragment();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date1 =  simpleDateFormat.format(date);
        String url = "https://www.quandl.com/api/v3/datasets/LBMA/GOLD?start_date=" + date1 + "&end_date=" + date1 + "&api_key=EhkfvazyLhnSSAVAM2qj";
        makeApiCall(url);

    }



    private void setupViewPager(ViewPager viewPager) {    // YOU CAN ADD MORE PAGES FROM HERE
        TabsAdapter adapter = new TabsAdapter(getSupportFragmentManager());
        adapter.addFragment(mAssetsFragment, "Assets");
        adapter.addFragment(new AssetsFragment(), "Liabilities");

        viewPager.setAdapter(adapter);
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
                            String realUrl = "https://www.quandl.com/api/v3/datasets/LBMA/GOLD?start_date=" + end_date + "&end_date=" + end_date + "&api_key=EhkfvazyLhnSSAVAM2qj";
                            makeApiCall(realUrl);
                        }
                        else {
                            mAssetsFragment.setGoldValue(String.valueOf(mDataset.dataset.data.get(0).get(1)));
                            Log.v("dsfs", "SDF");
                        }
                    }
                }
            }
        });
    }
}
