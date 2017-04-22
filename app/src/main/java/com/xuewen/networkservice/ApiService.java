package com.xuewen.networkservice;

import android.support.annotation.Nullable;

import com.xuewen.utility.CurrentUser;
import com.xuewen.utility.Global;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
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

    // for update
    @GET("api/update")
    Call<UpdateResult> getUpdate(@Query("currentVersion") String currentVersion);

    // for authentication
    @POST("api/wxlogin")
    @FormUrlEncoded
    Call<WXLoginResult> requestWXLogin(@Field("appid") String appid,
                                       @Field("code") String code);
    @POST("api/tklogin")
    @FormUrlEncoded
    Call<TKLoginResult> requestTKLogin(@Field("user_id") int user_id,
                                       @Field("token") String token);
    @POST("api/logout")
    Call<LogoutResult> requestLogout();

    // for questions
    @GET("api/questions/recommend")
    Call<QRResult> requestQR();
    @GET("api/questions/{question_id}")
    Call<QQidResult> requestQQid(@Path("question_id") int qid,
                                 @Nullable @Query("user_id") int uid);
    @POST("api/questions/{question_id}/listenings")
    Call<QQidLResult> requestQQidL(@Path("question_id") int qid);
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
                                   @Part("recognizeResult") RequestBody recognizeResult,
                                   @Nullable @Part MultipartBody.Part audio);

    // for users
    @PATCH("api/users/{user_id}/perfect")
    @Multipart
    Call<UUidPResult> requestUUidP(@Path("user_id") int uid,
                                   @Part("school") RequestBody school,
                                   @Part("major") RequestBody major,
                                   @Part("grade") RequestBody grade);
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

    static final String CER_STRING = "-----BEGIN CERTIFICATE-----\n" +
            "MIICgzCCAewCCQD3Zd/dyXNaATANBgkqhkiG9w0BAQUFADCBhTELMAkGA1UEBhMC\n" +
            "Q04xEjAQBgNVBAgTCUd1YW5nZG9uZzESMBAGA1UEBxMJR3Vhbmd6aG91MQ4wDAYD\n" +
            "VQQKEwV1a25vdzENMAsGA1UECxMEa25vdzEOMAwGA1UEAxMFdWtub3cxHzAdBgkq\n" +
            "hkiG9w0BCQEWEDgyNDkyODIwN0BxcS5jb20wHhcNMTcwNDEyMTExMjA3WhcNMTgw\n" +
            "NDEyMTExMjA3WjCBhTELMAkGA1UEBhMCQ04xEjAQBgNVBAgTCUd1YW5nZG9uZzES\n" +
            "MBAGA1UEBxMJR3Vhbmd6aG91MQ4wDAYDVQQKEwV1a25vdzENMAsGA1UECxMEa25v\n" +
            "dzEOMAwGA1UEAxMFdWtub3cxHzAdBgkqhkiG9w0BCQEWEDgyNDkyODIwN0BxcS5j\n" +
            "b20wgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAMegpECDXyH4XVsfU/rbfmdc\n" +
            "qHrsyFHkI7bK3J5p1FaBq/EuueFHaR0EcgXfTp1O1YtUTg3MOGX/VlTSpWeOFQIZ\n" +
            "RAeMLI7y9mjhtMCCHFIx48NHd20hH3vSkVL/Y9fdI5RL3VaZ/eqRlywCbUgIJ5Ec\n" +
            "ShH5DyMMnTawUu4lTR+rAgMBAAEwDQYJKoZIhvcNAQEFBQADgYEAhGzdZkIY872+\n" +
            "AWAF77Td+N/wyDlbFeE9SD6Ry5l7dY2MyqMumu6CGUL2BMNabiZWgEu35wDHvN10\n" +
            "H6RhOjgmrXDDPpJRGmtJLt+GyhDZ8MMGpiaj+5KUZRnlfgTmMCYd79XPzUkTugDe\n" +
            "WwXLZqnkS7zXWce/rEg4LIATtfG0KxU=\n" +
            "-----END CERTIFICATE-----";
    static final Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request oldRequest = chain.request();
            // 新的请求
            Request newRequest = oldRequest
                    .newBuilder()
                    .header("Cookie", CurrentUser.cookie)
                    .build();

            return chain.proceed(newRequest);
        }
    };
    static final HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(
            new InputStream[] { new Buffer().writeUtf8(CER_STRING).inputStream() },
            null, null);
    static final OkHttpClient client = new OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
            .build();
    static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Global.getInstance().baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();

}