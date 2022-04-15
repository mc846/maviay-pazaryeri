package com.maviay.pazaryeri.Models.Request;

public class BaseRequest <T>{
    String name;
    T param;

    public BaseRequest(String name, T param){
        this.name = name;
        this.param = param;
    }
}
