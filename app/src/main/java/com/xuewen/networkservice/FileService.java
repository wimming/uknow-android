package com.xuewen.networkservice;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by ym on 16-11-26.
 */

public interface FileService {
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    @Multipart
    @POST("upload")
    Call<ResponseBody> uploadFile(@Part("description") RequestBody description, @Part MultipartBody.Part file);

    public static final Retrofit retrofit = new Retrofit.Builder().
            baseUrl("http://pic51.nipic.com/").
            build();
}
