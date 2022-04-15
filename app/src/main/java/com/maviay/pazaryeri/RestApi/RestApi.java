package com.maviay.pazaryeri.RestApi;

import retrofit2.Call;

import com.maviay.pazaryeri.Models.Request.BaseRequest;
import com.maviay.pazaryeri.Models.Request.LoginRequest;
import com.maviay.pazaryeri.Models.Response.BaseResponse;
import com.maviay.pazaryeri.Models.Response.LoginResponse;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RestApi {

    @Headers("test:1")
    @POST("index.php")
    Call<BaseResponse<LoginResponse>> loginUser(@Body BaseRequest<LoginRequest> loginBaseModel);
}
