package com.maviay.pazaryeri.RestApi;

import com.maviay.pazaryeri.Models.Request.BaseRequest;
import com.maviay.pazaryeri.Models.Response.BaseResponse;
import com.maviay.pazaryeri.Models.Response.LoginResponse;

import retrofit2.Call;

public class ManagerAll extends BaseManager{

    private static ManagerAll ourInstance = new ManagerAll();

    public static synchronized ManagerAll getInstance(){
        return ourInstance;
    }

    public Call<BaseResponse<LoginResponse>> logIn(String empcode, String pass) {
        Call<BaseResponse<LoginResponse>> x= getRestApi().loginUser
                (new BaseRequest<>("generateToken",
                        new com.maviay.pazaryeri.Models.Request.LoginRequest(pass, empcode)));

        return x;
    }
}