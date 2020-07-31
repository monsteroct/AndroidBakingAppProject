package com.salab.project.projectbakingrecipe.utils;

import android.util.Log;

import com.salab.project.projectbakingrecipe.models.Recipe;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Fetch Recipes from network and parse them into suitable models
 */
public class NetWorkUtil {

    private static final String TAG = NetWorkUtil.class.getSimpleName();
    public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    private static Retrofit retrofitInstance;

    private static Retrofit getRetrofitInstance(){
        if (retrofitInstance == null){
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Log.d(TAG, "New Retrofit instance is built");
        }
        return retrofitInstance;
    }

    public static BakingDataService createBakingDateService(){
        return getRetrofitInstance().create(BakingDataService.class);
    }

    //Retrofit webservice interface to define connections
    public interface BakingDataService {
        //topher/2017/May/59121517_baking/baking.json
        @GET("topher/2017/May/59121517_baking/baking.json")
        Call<List<Recipe>> getRecipes();
    }

}


