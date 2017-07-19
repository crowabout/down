package com.company.net;

import java.net.HttpURLConnection;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pc on 2017/7/18.
 */
public final class Request {

    private final HttpMethod method;
    private final String url;
    private final Map<String,String> header;

    private Request(Builder builder){
        method =builder.method;
        header=builder.header;
        url =builder.url;
    }

    public static class Builder{
        private  HttpMethod method;
        private String url;
        private Map<String,String> header;

        public Builder(){
            method=HttpMethod.GET;
            header=new ConcurrentHashMap<String, String>();
        }

        public Builder addHeader(String name,String value){
            header.put(name,value);
            return this;
        }

        public Builder setCookie(String value){
            header.put("Cookie",value);
            return this;

        }
        public Builder url(String url){
            this.url=url;
            return this;
        }

        public Builder method(HttpMethod method){
            this.method=method;
            return this;
        }

        Request build(){
            return new Request(this);
        }
    }

    public Map<String,String> headers(){
        return header;
    }

    public String url(){
        return url;
    }


    public HttpMethod method(){
        return method;
    }

    public ByteBuffer toStream(){

        return null;


    }


    @Override
    public String toString() {
        StringBuilder sb =new StringBuilder();
        sb.append(String.format("%s\t%s\r\n",method.name(),url));
        Iterator<Map.Entry<String,String>> is =header.entrySet().iterator();

        while(is.hasNext()){
            Map.Entry<String,String> item =is.next();
            sb.append(String.format("%s:%s\n",item.getKey(),item.getValue()));
        }
        return sb.toString();
    }
}
