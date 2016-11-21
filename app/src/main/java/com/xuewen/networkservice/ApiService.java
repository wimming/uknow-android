package com.xuewen.networkservice;

import com.xuewen.utility.GlobalUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ym on 16-11-4.
 */

public interface ApiService {
    @GET("api/questions/recommend")
    Call<QRResult> requestQR();

    @GET("api/questions/{question_id}")
    Call<QQidResult> requestQQid(@Path("question_id") int qid);
//    Call<QRResult> repoContributors(
//            @Path("owner") String owner,
//            @Path("repo") String repo);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(GlobalUtil.getInstance().baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}