package com.company;

import com.company.net.HttpCallRunable;

import javax.persistence.EntityManager;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Created by pc on 2017/7/8.
 */
public class ProducerOrConsumer implements Runnable {

    private Logger log = Logger.getLogger("DownTask");

    private final int SIZE_OF_THREED_POOL = 100;
    private final int SIZE_OF_QUEUE = 1000;
    /**
     * 退出的超时
     */
    private final long EXIT_WAIT_TIME_OUT = 1 * 60 * 1000;
    /**
     * 等待超时
     */
    private final long WAIT_TIME_OUT = 500;

    private final Queue<GraphNode> nodeQueue;
    private final ExecutorService executor;
    private HtmlpageParser parser;
    private EntityManager manager;
    private int waitTime = 0;
    private List<Future<String>> futureList;
    private volatile boolean isStopping = false;


    public ProducerOrConsumer(EntityManager manager, ExecutorService executor, Queue<GraphNode> queue) {
        this.nodeQueue = queue;
        this.manager = manager;
        parser = new HtmlpageParser(nodeQueue);
        this.executor = executor;
        futureList = new ArrayList<Future<String>>();
    }


    private void execute() {
        log.config("ProducerOrConsumer beginning work.");
        try {
            while (!isStopping) {
                GraphNode node = null;
                if (!nodeQueue.isEmpty()) {
                    node = nodeQueue.remove();
                } else {
                    populateQueue();
                }

                //Queue is empty;
                if (node == null) {
                    log.warning(" now queue is empty! we are prepare to populate it. hold on..");
                    if (nodeQueue.isEmpty()) {
                        if (waitTime > EXIT_WAIT_TIME_OUT) {
                            log.warning("========================");
                            log.warning("# it's will be exit... #");
                            log.warning("========================");
                            break;
                        }
                        wait(WAIT_TIME_OUT);
                        waitTime += WAIT_TIME_OUT;
                    }
                    continue;
                }

                if (node.isLeafNode()) {
                    save(node);
                } else {
                    URL absUrl = node.relUrl2abs();
                    log.warning(String.format(">>> node:%d absurl:%s index:%s",node.hashCode(),absUrl,node.getCurPageIndex()));
                    HttpCallRunable runable = new HttpCallRunable(absUrl.toString());
                    Future<String> future = executor.submit(runable);
                    futureList.add(future);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            isStopping = true;
            executor.shutdownNow();
        }
    }


    private void populateQueue() throws ExecutionException, InterruptedException {

        Iterator it = futureList.iterator();
        while (it.hasNext()) {
            Future<String> html = (Future<String>) it.next();
            parser.exactTrTagFromHtml(html.get());
        }
    }

    /**
     * persistence date
     *
     * @param node
     */
    private void save(GraphNode node) {
        manager.getTransaction().begin();
        log.warning(node.toString());
        manager.persist(node);
        manager.getTransaction().commit();
    }


    /**
     * persistence data
     *
     * @param nodeL
     */
    private void save(List<GraphNode> nodeL) {
        manager.getTransaction().begin();
        int size = nodeL.size();
        if (size == 0) {
            return;
        }
        for (int i = 0; i < size; i++) {
            manager.persist(nodeL.get(i));
        }
        manager.getTransaction().commit();
    }


    public void run() {
        execute();
    }

    public void stop() {
        isStopping = true;
    }

}
