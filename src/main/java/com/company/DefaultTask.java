package com.company;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Created by pc on 2017/8/31.
 */
public class DefaultTask {

    private Logger log = Logger.getLogger("DownTask");

    private static final int SIZE_OF_POOL=100;
    private HtmlpageParser parser;
    private ConcurrentLinkedQueue<GraphNode> queue;
    private ExecutorService pool;
    private EntityManager manager;
    private String url;
    public DefaultTask(String baseUrl, EntityManager manager) {
        this.manager=manager;
        queue=new ConcurrentLinkedQueue<GraphNode>();
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
        ProducerOrConsumer poc =new ProducerOrConsumer(manager,pool,queue);
        pool.execute(poc);
    }


    public static void main(String[] args) {

        Map<String, String> configurationOverrides = new HashMap<String, String>();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ConsolePU", configurationOverrides);
        EntityManager entityManager = emf.createEntityManager();
        DefaultTask dfTask =new DefaultTask(Iidc.BASE,entityManager);
        dfTask.startWorking();

    }
}



