package com.company.test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by pc on 2017/7/29.
 */
public class WorkerThread extends Thread{

    private ByteBuffer buffer=ByteBuffer.allocate(1024);

    private ThreadPool pool;

    private SelectionKey key;


    public WorkerThread(ThreadPool pool) {
        this.pool = pool;
    }

    @Override
    public synchronized void run() {

        System.out.println(this.getName()+" is ready");

        while(true){

            try {
                wait();

            } catch (InterruptedException e) {
                e.printStackTrace();
                interrupted();
            }

            if(key==null){
                continue;
            }

            System.out.println(getName()+" has been awakened");


            try {
                drainChannel(key);
            } catch (IOException e) {

                System.out.println("Cauch "+e+" closing channel");

                try {
                    key.channel().close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                key.selector().wakeup();
            }

            key=null;

            pool.returnWorker(this);
        }

    }

    synchronized void serviceChannel(SelectionKey key){
        this.key=key;
        key.interestOps(key.interestOps()&(~SelectionKey.OP_READ));
        notify(); //Awaken the thread
    }



    void drainChannel(SelectionKey key) throws IOException {

        SocketChannel channel = (SocketChannel) key.channel();

        int count;

        buffer.clear();

        while((count=channel.read(buffer))>0){

            buffer.flip();

            while(buffer.hasRemaining()){
                channel.write(buffer);
            }

            buffer.clear();
        }

        if(count<0){
            channel.close();
            return;
        }
    }



}

