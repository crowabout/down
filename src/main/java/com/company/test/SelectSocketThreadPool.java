package com.company.test;

import java.nio.channels.SelectionKey;

/**
 *
 * Created by pc on 2017/7/29.
 *
 * Servicing Channel with a thread pool
 */
public class SelectSocketThreadPool extends SelectSocket{


    private static final int MAX_THREADS=5;

    private ThreadPool pool =new ThreadPool(MAX_THREADS);

    public static void main(String[] args) {
        new SelectSocketThreadPool().go(args);
    }




    @Override
    protected void readDataFromSocket(SelectionKey key){

        WorkerThread worker =pool.getWorker();

        if(worker ==null){
            return;
        }
        worker.serviceChannel(key);
    }





}
