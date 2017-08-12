package com.company.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by pc on 2017/7/28.
 */
public class SelectSocket {

    public static int PORT_NUMBER=1234;

    public static void main(String[] args) {
        new SelectSocket().go(args);

    }


    public void go(String[] argv){

        int port =PORT_NUMBER;
        if(argv.length>0){
            port=Integer.parseInt(argv[0]);
        }

        System.out.println("Listening on port "+port);

        try {
            ServerSocketChannel serverChannel =ServerSocketChannel.open();
            ServerSocket serverSocket =serverChannel.socket();

            Selector selector =Selector.open();
            serverSocket.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            while(true){
                int n =selector.select();
                System.out.println("n="+n);
                if(n==0){
                    continue;
                }

                Set<SelectionKey> selectedSet =selector.selectedKeys();
                Iterator it =selectedSet.iterator();
                System.out.println("iterator.size():"+selectedSet.size());
                while(it.hasNext()){
                    SelectionKey key = (SelectionKey) it.next();
                    System.out.println("key:"+key.hashCode());
                    System.out.println("it.hasNext()....");
                    if(key.isAcceptable()){
                        System.out.println("key.isAcceptable....");
                        ServerSocketChannel server =
                                (ServerSocketChannel) key.channel();
                        SocketChannel channel =server.accept();
                        registerChannel(selector,channel,SelectionKey.OP_READ);
                        sayHello(channel);
                    }
                    //Is there data to read on this channel?
                    if(key.isReadable()){
                        System.out.println("key.isReadable....");
                        readDataFromSocket(key);
                    }
                    //Remove key from selected set;it's been handled
                    it.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected  void registerChannel(Selector selector, SelectableChannel channel, int ops) throws IOException {

        if(channel ==null){
            return;
        }
        channel.configureBlocking(false);
        channel.register(selector,ops);



    }


    private ByteBuffer buffer = ByteBuffer.allocateDirect(1024);


    protected void readDataFromSocket(SelectionKey key) throws IOException {

        SocketChannel socketChannel = (SocketChannel) key.channel();
        System.out.println("socketChanel:"+socketChannel.hashCode());
        int count;
        buffer.clear();
        while((count=socketChannel.read(buffer))>0){
            buffer.flip();

            while(buffer.hasRemaining()){
                socketChannel.write(buffer);
            }
            buffer.clear();
        }

        if(count<0){
            System.out.println("socketChannel is closed");
            /**
             * it will close the connections between servers and client when you invoke close();
             * not execute
             */
            socketChannel.close();
        }


    }


    private void sayHello(SocketChannel channel) throws IOException {

        buffer.clear();
        buffer.put("Hi there!\r\n".getBytes());
        buffer.flip();
        channel.write(buffer);

    }

}
