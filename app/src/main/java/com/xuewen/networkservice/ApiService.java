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

    @GET("api/users/{user_id}")
    Call<UUidResult> requestUUid(@Path("user_id") int uid);

    @GET("api/users/{user_id}/introduction")
    Call<UUidIResult> requestUUidI(@Path("user_id") int uid);

    @GET("api/users/{user_id}/follows")
    Call<UUidFResult> requestUUidF(@Path("user_id") int uid);

    @GET("api/users/{user_id}/recommendations")
    Call<UUidRResult> requestUUidR(@Path("user_id") int uid);

//    Call<QRResult> repoContributors(
//            @Path("owner") String owner,
//            @Path("repo") String repo);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(GlobalUtil.getInstance().baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}