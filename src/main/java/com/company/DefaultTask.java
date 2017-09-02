package com.company;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
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

    private void startWorking(){
        initQueue(url);
        for(int i=0;i<THREADS_AMOUNT;i++){
            ProducerOrConsumer poc =new ProducerOrConsumer(saver,queue);
            pool.execute(poc);
        }
    }



    public static void main(String[] args) {
        DataSaver saver =new DataSaver();
        DefaultTask dfTask =new DefaultTask(Iidc.BASE,saver);
        dfTask.startWorking();
    }
}



