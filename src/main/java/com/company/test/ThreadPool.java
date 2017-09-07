package com.company.test;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pc on 2017/7/29.
 */
public class ThreadPool {

    List idle =new LinkedList();

    ThreadPool(int poolSize){
        for(int i=0;i<poolSize;i++){
            WorkerThread thread =new WorkerThread(this);
            thread.setName("Worker"+ (i+1));
            thread.start();
            idle.add(thread);
        }
    }



    WorkerThread getWorker(){
        WorkerThread worker =null;
        synchronized (idle){
            if(idle.size()>0){
                worker=(WorkerThread)idle.remove(0);
            }
        }
        return (worker);
    }

    void returnWorker(WorkerThread worker){
        synchronized (idle){
            idle.add(worker);
        }
    }

}
