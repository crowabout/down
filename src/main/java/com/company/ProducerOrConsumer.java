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
    //    private final ExecutorService executor;
    private HtmlpageParser parser;
    private  DataSaver saver;
//    private List<Future<String>> futureList;
    private volatile boolean isStopping = false;


    public ProducerOrConsumer(DataSaver saver, Queue<GraphNode> queue) {
        this.nodeQueue = queue;
        this.saver =saver;
        parser = new HtmlpageParser(nodeQueue);
//        this.executor = executor;
//        futureList = new ArrayList<Future<String>>();
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
//                   else{
//                        wait(WAIT_TIME_OUT);
//                    }
                }
                //Queue is empty;
//                if (node == null) {
//                    log.warning(" now queue is empty! we are prepare to populate it. hold on..");
//                    if (nodeQueue.isEmpty()) {
//                        if (waitTime > EXIT_WAIT_TIME_OUT) {
//                            log.warning("========================");
//                            log.warning("# it's will be exit... #");
//                            log.warning("========================");
//                            break;
//                        }
//                        wait(WAIT_TIME_OUT);
//                        waitTime += WAIT_TIME_OUT;
//                    }
//                    continue;
//                }

                if (node.isLeafNode()) {
                    save(node);
                } else {
//                    URL absUrl = node.relUrl2abs();
//                    log.warning(String.format(">>> node:%d absurl:%s index:%s",node.hashCode(),absUrl,node.getCurPageIndex()));
//                    HttpCallRunable runable = new HttpCallRunable(absUrl.toString());
//                    Future<String> future = executor.submit(runable);
//                    String html =future.get();
//                    parser.exactTrTagFromHtml(html);
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
//            executor.shutdownNow();
        }
    }

//    private void populateQueue() throws ExecutionException, InterruptedException {
//        Iterator it = futureList.iterator();
//        while (it.hasNext()) {
//            Future<String> html = (Future<String>) it.next();
//            parser.exactTrTagFromHtml(html.get());
//        }
//    }

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
