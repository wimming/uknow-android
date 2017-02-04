package com.xuewen.networkservice;

import android.support.annotation.Nullable;

import com.xuewen.bean.EmptyBean;
import com.xuewen.utility.GlobalUtil;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ym on 16-11-4.
 */

public interface ApiService {
    @POST("api/wxlogin")
    @FormUrlEncoded
    Call<WXLoginResult> requestWXLogin(@Field("appid") String appid,
                                       @Field("code") String code);

    @GET("api/questions/recommend")
    Call<QRResult> requestQR();

    @GET("api/questions/{question_id}")
    Call<QQidResult> requestQQid(@Path("question_id") int qid,
                                 @Nullable @Query("user_id") int uid);

    @POST("api/questions/{question_id}/listenings")
    Call<QQidLResult> requestQQidL(@Path("question_id") int qid);
//
//    @POST("api/questions/{question_id}/comments")
//    @FormUrlEncoded
//    Call<QQidCResult> requestQQidC(@Path("question_id") int qid,
//                                   @Field("praise") int praise);

    @POST("api/questions/{question_id}/comments")
    @FormUrlEncoded
    Call<QQidCResult> requestQQidC(@Path("question_id") int qid,
                                   @Field("praise") int praise,
                                   @Nullable @Field("user_id") int uid);

    @GET("api/questions/find")
    Call<QFResult> requestQF(@Query("query_string") String query_string);

    @POST("api/questions")
    @FormUrlEncoded
    Call<QResult> requestQ(@Field("asker_id") int asker_id,
                           @Field("description") String description,
                           @Field("answerer_id") int answerer_id);

    @PATCH("api/questions/{question_id}/answer")
    @Multipart
    Call<QQidAResult> requestQQidA(@Path("question_id") int qid,
                                   @Part("answerer_id") RequestBody answerer_id,
                                   @Part("audioSeconds") RequestBody audioSeconds,
                                   @Nullable @Part MultipartBody.Part audio);


    @GET("api/users/{user_id}")
    Call<UUidResult> requestUUid(@Path("user_id") int uid);
    @PATCH("api/users/{user_id}")
    @Multipart
    Call<UUidResult> requestUUid(@Path("user_id") int uid,
                                 @Part("username") RequestBody username,
                                 @Part("status") RequestBody status,
                                 @Part("description") RequestBody description,
                                 @Part("school") RequestBody school,
                                 @Part("major") RequestBody major,
                                 @Part("grade") RequestBody grade,
                                 @Nullable @Part MultipartBody.Part file);

    @GET("api/users/{user_id}/introduction")
    Call<UUidIResult> requestUUidI(@Path("user_id") int uid,
                                   @Nullable @Query("id") int request_uid);

    @GET("api/users/{user_id}/followsAndRecommendations")
    Call<UUidFARResult> requestUUidFAR(@Path("user_id") int uid);

    @GET("api/users/{user_id}/follows")
    Call<UUidFResult> requestUUidF(@Path("user_id") int uid);
    @POST("api/users/{user_id}/follows")
    @FormUrlEncoded
    Call<UUidFResult> requestUUidF(@Path("user_id") int uid,
                                   @Field("followed_uid") int followed_uid);

    @DELETE("api/users/{user_id}/follows")  //安卓delete不支持body传递数据
    Call<UUidFResult> deleteUUidF(@Path("user_id") int uid,
                                  @Nullable @Query("followed_uid") int followed_uid);

    @GET("api/users/{user_id}/recommendations")
    Call<UUidRResult> requestUUidR(@Path("user_id") int uid);

    @GET("api/users/find")
    Call<UFResult> requestUF(@Query("query_string") String query_string);

//    Call<QRResult> repoContributors(
//            @Path("owner") String owner,
//            @Path("repo") String repo);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(GlobalUtil.getInstance().baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}