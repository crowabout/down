package com.company.net;

import com.company.DownerConfigure;
import com.company.GraphNode;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * Created by pc on 2017/8/11.
 */
public class HttpDownloadTaskSyn {
    private static final int  READ_TIME_OUT =1*60*1000;
    private Logger log = Logger.getLogger("httpDownlaodTask");
    private CloseableHttpAsyncClient httpClient;
    private GraphNode node;
    private DownerConfigure configure;
    private CountDownLatch counter;

    public HttpDownloadTaskSyn(GraphNode node, DownerConfigure configure, CountDownLatch counter){
        this(node,configure);
        this.counter =counter;
    }
    public HttpDownloadTaskSyn(GraphNode node, DownerConfigure configure){
        this(node);
        this.configure=configure;
    }

    public HttpDownloadTaskSyn(GraphNode node) {
        this.node = node;
        httpClient = HttpAsyncClients.createDefault();
    }
    public void execute() {

        System.out.println(String.format("%s downloading..%s",Thread.currentThread().getName(),node.getNodeName()));
        httpClient.start();
        try {
            HttpGet requset = new HttpGet(node.relUrl2abs().toString());
            Future<HttpResponse> future = httpClient
                    .execute(requset, null);
            HttpResponse response = null;
            response = future.get();
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                saveFile(entity.getContent());
            } else {
                log.info("!!Failure!!" + response.getStatusLine().getStatusCode() + " " + requset.getURI().toString());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    private void saveFile(InputStream in) throws IOException {

        String strName=node.getNodeName();
        File fileWithPath=null;
        if(configure!=null){
             fileWithPath=new File(configure.fileDownloadDir());
        }

        File fileName=new File(fileWithPath,strName);
        if(!fileWithPath.exists())
            if(!fileWithPath.mkdirs())
                System.err.println("mkdirs errors!!");

        if(!fileName.createNewFile()){
            System.out.println("create file error");
        }

        OutputStream out =new BufferedOutputStream(new FileOutputStream(fileName));

        byte[] buffer =new byte[1024];
        long count=0;
        while((count=in.read(buffer))!=-1){
            out.write(buffer);
        }
        out.flush();
        out.close();
        System.out.println(String.format("file %s download complete!",fileName.getAbsoluteFile().toString()));
    }
}
