package com.company;
import java.net.URL;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Created by pc on 2017/7/8.
 */
public class DownerTask {

    private final int SIZE_OF_THREED_POOL=100;
    private final int SIZE_OF_QUEUE=1000;
    private final Queue<GraphNode> nodeQueue;
    private final ExecutorService executor;
    private final CompletionService<GraphNode> service;
    private HtmlpageParser parser;

    public DownerTask(URL baseUrl) {
        this.nodeQueue = new ConcurrentLinkedQueue<GraphNode>();
        parser =new HtmlpageParser(baseUrl,nodeQueue);
        executor =Executors.newFixedThreadPool(SIZE_OF_THREED_POOL);
        service =new ExecutorCompletionService<GraphNode>(executor);
    }

    /**
     *
     */
    private void initQueue(){
        try {
            //there should be more than 10 elements in the Queue
            // when after exactTrTagFromUrl() finish
            long count =parser.exactTrTagFromUrl();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void down() {



    }
}
