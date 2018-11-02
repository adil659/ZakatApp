package com.ideas.innovative.zakatapp.GoldValueAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by adil6 on 2018-10-28.
 */

public interface GoldPriceService {
    @GET("api/v3/datasets/LBMA/GOLD")
    Call<GoldDataSet> getGoldPrice(@Query("start_date") String startDate, @Query("end_date") String endDate, @Query("api_key") String api_key);
}

  /*
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.quandl.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        GoldPriceService goldPriceService = retrofit.create(GoldPriceService.class);

       Call<GoldDataSet> call = goldPriceService.getGoldPrice("2018-10-26", "2018-10-26", "EhkfvazyLhnSSAVAM2qj");

       call.enqueue(new Callback<GoldDataSet>() {
           @Override
           public void onResponse(Call<GoldDataSet> call, Response<GoldDataSet> response) {
               if (response.isSuccessful()) {
                   Log.v("mainActivity ", " hi ");
               }
           }

           @Override
           public void onFailure(Call<GoldDataSet> call, Throwable t) {
               Log.v("mainActivity ", " fail " + t.getMessage());
               t.getStackTrace();

           }
       });
*/