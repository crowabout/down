package com.company;
import com.company.net.HttpDownloadTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

/**
 * Created by pc on 2017/8/31.
 */
public class DefaultTask {

    private Logger log = Logger.getLogger("DownTask");

    private static final int SIZE_OF_POOL=200;
    private static final int SIZE_OF_QUEUE=4000;

    private HtmlpageParser parser;
    private Queue<GraphNode> queue;
    private ExecutorService pool;
    private String url;
    private DataSaver saver;

    private final int THREADS_AMOUNT=200;


    public DefaultTask(){}
    public DefaultTask(String baseUrl,DataSaver saver) {
        this.saver=saver;
        queue=new LinkedBlockingQueue<GraphNode>(SIZE_OF_QUEUE);
        parser=new HtmlpageParser(queue);
        pool = Executors.newFixedThreadPool(SIZE_OF_POOL);
        this.url=baseUrl;
    }

    private void initQueue(String urlStr) {
        try {
            URL url =new URL(urlStr);
            //there should be more than 10 elements in the Queue
            // when after exactTrTagFromUrl() finish
            long count = parser.exactTrTagFromUrl(url);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     *  下载数据
     */
    private void startDownLoadData(){
        initQueue(url);
        for(int i=0;i<THREADS_AMOUNT;i++){
            ProducerOrConsumer poc =new ProducerOrConsumer(saver,queue);
            pool.execute(poc);
        }
    }


    /**
     * 下载文件
     * @param configure
     * @throws InterruptedException
     */
    private void startDownloadFile(DownerConfigure configure) throws InterruptedException {

        String type =configure.downloadFielType();
        List<GraphNode> nodes =saver.queryAllGraphNodeByFileTypeAndKey("",type);
        queue.addAll(nodes);

        CountDownLatch latch =new CountDownLatch(queue.size());
        while (!queue.isEmpty()){
            System.out.println(String.format("queue.size(%d)",queue.size()));
            GraphNode node=queue.remove();
            HttpDownloadTask task =new HttpDownloadTask(node,configure,latch);
            pool.submit(task);
        }
        latch.wait();
        if(!pool.isShutdown()){
           pool.shutdown();
        }
    }

    private void showHelp(){
        System.out.println("\tdefaultTask (FILE|DATA) [option] \t");
        System.out.println("\t[option]: DATA\t\t only download data");
        System.out.println("\t\t-h                   \t\thelp manual.");
        System.out.println("\t\t--thredPool-size=SIZE\t\tsize of thread pool.");
        System.out.println("\t\t--thredNum-size=SIZE\t\tsize of thread.");
        System.out.println("\t\t--queue-size=SIZE\t\tsize of queue.");
        System.out.println("\t[option]: FILE\t\t file download");
        System.out.println("\t\t--download-dir=DIR\t\tthe dir which store download files.");
        System.out.println("\t\t--download-type=TYPE\t\tthe type of will download.");
        System.out.println("\t\t                    \t\ttype := (pdf|txt|doc|chm).");
    }


    public static void main(String[] args) {



        DataSaver saver =new DataSaver();
        DefaultTask dfTask =new DefaultTask(Iidc.BASE,saver);

        if(args.length==0 || args[0].trim().equalsIgnoreCase("-h") ||
                args[0].trim().equalsIgnoreCase("--help")){
                dfTask.showHelp();
                System.exit(0);
        }

        int downLoadDirIndex=findKeyFromArgs("--download-dir",args);
        int downLoadTypeIndex =findKeyFromArgs("--download-type",args);
        String dirPath =findOptionValueFromArgs(args[downLoadDirIndex]);
        String fileType=findOptionValueFromArgs(args[downLoadTypeIndex]);


        DownerConfigure configure =new DownerConfigure.Builder()
                .setDownloadDir(dirPath)
                .downloadFileType(fileType)
                .build();

        if(args[0].equalsIgnoreCase("FILE")){
            try {
                dfTask.startDownloadFile(configure);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else if(args[0].equalsIgnoreCase("DATA")){
            dfTask.startDownLoadData();
        }
//        System.exit(0);

    }


    private static int findKeyFromArgs(String key,String args[]){
        int index=0;
        for(int i=0;i<args.length;i++){
            if (args[i].contains(key)) {
                index=i;
            }
        }
        return index;
    }

    private static String findOptionValueFromArgs(String args){
        return args.substring(args.indexOf("=")+1);
    }

}



