package com.company.net;

/**
 * Created by pc on 2017/7/18.
 */
public enum HttpMethod {

    GET("GET"),
    POST("POST"),
    OPTION("OPTION");
    private String method;
    private HttpMethod(String method){
        this.method =method;
    }

}
