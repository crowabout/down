package com.company;

import com.company.net.HttpDowerTask;
import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.logging.Logger;
/**
 * Created by pc on 2017/7/8.
 */
public class ProducerOrConsumer implements Runnable {

    private Logger log = Logger.getLogger("DownTask");

    private final Queue<GraphNode> nodeQueue;
    private HtmlpageParser parser;
    private  DataSaver saver;
    private volatile boolean isStopping = false;


    public ProducerOrConsumer(DataSaver saver, Queue<GraphNode> queue) {
        this.nodeQueue = queue;
        this.saver =saver;
        parser = new HtmlpageParser(nodeQueue);
    }


    private void execute() {
        log.info("["+Thread.currentThread().getName()+"]");
        try {
            while (!isStopping) {
                GraphNode node = null;
                synchronized (nodeQueue) {
                    log.warning(String.format("[queue.size(%d)",nodeQueue.size()));
                    if (!nodeQueue.isEmpty()) {
                        node = nodeQueue.remove();
                    }else {
                        return;
                    }
                }

                if (node.isLeafNode()) {
                    save(node);
                } else {
                    dealWithNetUrl(node);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            isStopping = true;
        }
    }


    /**
     * persistence date
     *
     * @param node
     */
    private void save(GraphNode node) {
        saver.save(node);
    }


    /**
     * persistence data
     *
     * @param nodeL
     */
    private void save(List<GraphNode> nodeL) {
        int size = nodeL.size();
        if (size == 0) {
            return;
        }
        for (int i = 0; i < size; i++) {
            saver.save(nodeL.get(i));
        }
    }

    /**
     * 解析url
     *
     * @param node
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws IOException
     */
    private void dealWithNetUrl(GraphNode node) throws InterruptedException, ExecutionException, IOException {
        HttpDowerTask http = new HttpDowerTask(node);
        String html = http.url2Html();
        parser.setCurNode(node);
        parser.exactTrTagFromHtml(html);
    }

    public void run() {
        execute();
    }

    public void stop() {
        isStopping = true;
    }

}
