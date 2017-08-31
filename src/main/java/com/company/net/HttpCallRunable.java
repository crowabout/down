package com.company.net;
import com.company.TextUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Logger;
/**
 * Created by pc on 2017/8/11.
 */
public class HttpCallRunable implements Callable<String> {

    private Logger log = Logger.getLogger("httpCallRunnable");
    private String url;
    private final String defaultHost="http://www.baidu.com";
    private String html="";
    private CloseableHttpAsyncClient httpClient;
    public HttpCallRunable(String url) {
        this.url = url;
        httpClient= HttpAsyncClients.createDefault();
    }
    public String call() throws Exception {
        log.info(String.format("%s,%s",Thread.currentThread().getName(),url));
        httpClient.start();
       try{
           HttpGet requset =new HttpGet(url);
           Future<HttpResponse> future =httpClient
                   .execute(requset,null);
           HttpResponse response=future.get();
           HttpEntity entity =response.getEntity();
           if(response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
               html= TextUtils.convert2Str(entity.getContent(),
                       TextUtils.toCharSet(entity.getContentType().getValue()));
           }else{
               log.info("!!Failure!!"+response.getStatusLine().getStatusCode()+" "+requset.getURI().toString());
               html=response.getStatusLine().toString();
           }
       }finally {
           httpClient.close();
       }
        return html;
    }
}
