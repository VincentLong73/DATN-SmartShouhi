package com.dl.smartshouhi.api;

import static com.dl.smartshouhi.constaint.Constant.PREFIX_URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserDbApi {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-mm-dd HH:mm:ss")
            .create();

    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(600, TimeUnit.SECONDS)
            .readTimeout(600, TimeUnit.SECONDS)
            .writeTimeout(600, TimeUnit.SECONDS)
            .build();

    UserDbApi databaseApi = new Retrofit.Builder()
            .baseUrl(PREFIX_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(UserDbApi.class);

    @FormUrlEncoded
    @POST("user/update_password.php")
    Call<ResponseBody> updatePassword(@Field("email") String email,
                                      @Field("pass_word_old") String passWordOld,
                                      @Field("pass_word_new") String passWordNew );

    @FormUrlEncoded
    @POST("user/update_profile_user.php")
    Call<ResponseBody> updateProfile(@Field("email") String email,
                                      @Field("userName") String userName,
                                      @Field("fullName") String fullName,
                                      @Field("phone") String phone,
                                      @Field("dob") String dob);

    @FormUrlEncoded
    @POST("user/get_user_by_email.php")
    Call<ResponseBody> getUserByEmail(@Field("email") String email);

}
