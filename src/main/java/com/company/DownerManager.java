package com.company;
import java.net.URL;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by pc on 2017/7/8.
 */
public class DownerManager {

    private final BlockingQueue<GraphNode> queue;
    private HtmlpageParser parser;

    public DownerManager(URL baseUrl) {
        this.queue = new LinkedBlockingQueue<GraphNode>();
        parser =new HtmlpageParser(baseUrl,queue);
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
