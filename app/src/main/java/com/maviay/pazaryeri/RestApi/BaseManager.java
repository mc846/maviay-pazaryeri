package com.maviay.pazaryeri.RestApi;

public class BaseManager {

    protected RestApi getRestApi(){
        RestApiClient restApiClient = new RestApiClient(BaseUrl.URL);
        return restApiClient.getRestApi();
    }
}
