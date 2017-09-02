package com.company.net;
import com.company.GraphNode;
import com.company.TextUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * Created by pc on 2017/9/2.
 */
public class HttpDowerTask {

    private Logger log = Logger.getLogger("HttpDowerTask");
    private  GraphNode node;
    private CloseableHttpAsyncClient httpClient;
    public HttpDowerTask(GraphNode node) {
        this.node =node;
        this.httpClient = HttpAsyncClients.createDefault();
    }

    public String url2Html() throws ExecutionException, InterruptedException, IOException {
        String html=null;
        httpClient.start();
        try{
            String url =node.relUrl2abs().toString();
            log.info(String.format("now_url > %s",url));
            HttpGet requset =new HttpGet(url);
            Future<HttpResponse> future =httpClient
                    .execute(requset,null);
            HttpResponse response=future.get();
            HttpEntity entity =response.getEntity();
            if(response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                html= TextUtils.convert2Str(entity.getContent(),
                        TextUtils.toCharSet(entity.getContentType().getValue()));
            }else{
                //FIXME  fix log
//                log.info("!!Failure!!"+response.getStatusLine().getStatusCode()+" "+requset.getURI().toString());
                html=response.getStatusLine().toString();
            }
        }finally {
            httpClient.close();
        }
        return html;

    }



}
