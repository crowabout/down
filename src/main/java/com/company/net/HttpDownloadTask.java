package com.company.net;
import com.company.DownerConfigure;
import com.company.GraphNode;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;


/**
 * Created by pc on 2017/8/11.
 */
public class HttpDownloadTask implements Runnable {

    private Logger log = Logger.getLogger("httpDownlaodTask");
    private CloseableHttpAsyncClient httpClient;
    private GraphNode node;
    private DownerConfigure configure;
    private CountDownLatch counter;

    public HttpDownloadTask(GraphNode node,DownerConfigure configure,CountDownLatch counter){
        this(node,configure);
        this.counter =counter;
    }
    public HttpDownloadTask(GraphNode node,DownerConfigure configure){
        this(node);
        this.configure=configure;
    }

    public HttpDownloadTask(GraphNode node) {
        this.node = node;
        httpClient = HttpAsyncClients.createDefault();
    }
    public void run() {

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

            if(counter!=null){
                counter.countDown();
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


//    public static void main(String[] args) {
//
//
//        CountDownLatch counter =new CountDownLatch(5);
//
//        DownerConfigure configure =new DownerConfigure.Builder()
//                .setDownloadDir("E:\\").build();
//
//        System.out.println(String.format("%s,%s,%s,%s",configure.downloadFielType(),
//                configure.fileDownloadDir(),
//                configure.sizeOfThread(),
//                configure.sizeOfThreadPool()));
//
//        ExecutorService pool = Executors.newFixedThreadPool(10);
//        GraphNode node =new GraphNode("基于Android的高效短信查询软件的实现.pdf","linuxconf/download.php?file=Li9saW51eGZpbGVzLzIwMTHE6tfKwc8vQW5kcm9pZMjrw8W9zLPMLyVCQiVGOSVEMyVEQUFuZHJvaWQlQjUlQzQlQjglREYlRDAlQTclQjYlQ0MlRDAlQzUlQjIlRTklRDElQUYlQzglRUQlQkMlRkUlQjUlQzQlQ0ElQjUlQ0YlRDYucGRm");
//        GraphNode node1 =new GraphNode("制作Fedora 15的USB启动盘.pdf","linuxconf/download.php?file=Li9saW51eGZpbGVzL3B1Yi9GZWRvcmEvJUQ2JUM2JUQ3JUY3RmVkb3JhJTIwMTUlQjUlQzRVU0IlQzYlRjQlQjYlQUYlQzUlQ0MucGRm");
//        GraphNode node2 =new GraphNode("在Red Hat Enterprise Linux 5.3安装RRDTool.pdf","linuxconf/download.php?file=Li9saW51eGZpbGVzL3B1Yi9SZWRIYXQvJUQ0JURBUmVkJTIwSGF0JTIwRW50ZXJwcmlzZSUyMExpbnV4JTIwNS4zJUIwJUIyJUQ3JUIwUlJEVG9vbC5wZGY=");
//        GraphNode node3 =new GraphNode("2004-2013软考网络工程师历年试题答案及详解.pdf","linuxconf/download.php?file=Li9saW51eGZpbGVzL3B1Yi/I7b+8LzIwMDQtMjAxMyVDOCVFRCVCRiVCQyVDRCVGOCVDMiVFNyVCOSVBNCVCMyVDQyVDQSVBNiVDMCVGQSVDNCVFQSVDQSVENCVDQyVFMiVCNCVGMCVCMCVCOCVCQyVCMCVDRiVFQSVCRCVFMi5wZGY=");
//        GraphNode node4 =new GraphNode("2004下半年－2009上半年试题及答案-(完整高清版).pdf","linuxconf/download.php?file=Li9saW51eGZpbGVzL3B1Yi/I7b+8LzIwMDQlQ0YlQzIlQjAlRUIlQzQlRUElQTMlQUQyMDA5JUM5JUNGJUIwJUVCJUM0JUVBJUNBJUQ0JUNDJUUyJUJDJUIwJUI0JUYwJUIwJUI4LSUyOCVDRCVFQSVENSVGQiVCOCVERiVDNyVFNSVCMCVFNiUyOS5wZGY=");
//
//        List<GraphNode> nodes=new ArrayList<GraphNode>();
//        nodes.add(node1);
//        nodes.add(node);
//        nodes.add(node2);
//        nodes.add(node3);
//        nodes.add(node4);
//        for (GraphNode item:
//                nodes) {
//           pool.submit(new HttpDownloadTask(item,configure,counter));
//        }
//
//        try {
//            counter.await();
//            pool.shutdown();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//
//    }


}
