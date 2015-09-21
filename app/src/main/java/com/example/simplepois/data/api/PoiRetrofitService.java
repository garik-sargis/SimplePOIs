package com.example.simplepois.data.api;

import com.example.simplepois.data.model.PoiDetails;
import com.example.simplepois.data.model.PoiInfoList;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

public interface PoiRetrofitService {
    @GET("points")
    Call<PoiInfoList> poiInfoList();

    @GET("points/{id}")
    Call<PoiDetails> poiDetails(@Path("id") long id);
}
