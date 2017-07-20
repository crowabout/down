package com.company.net;

import com.sun.corba.se.impl.encoding.ByteBufferWithInfo;
import com.sun.org.apache.xml.internal.utils.ThreadControllerWrapper;
import com.sun.scenario.effect.impl.sw.java.JSWBlend_GREENPeer;
import org.hibernate.result.Output;

import javax.print.DocFlavor;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Arrays;

/**
 * Created by pc on 2017/7/18.
 */
public class TEst {

    public static void main(String[] args) {
//        System.out.println(HttpMethod.POST.name().equals("POST"));
//        System.out.println(HttpMethod.POST.ordinal());


//        Request request =new Request.Builder()
//                .url("http://www.baid.com")
//                .addHeader("Content-Type","application/json")
//                .addHeader("Content-Length","2154")
//                .addHeader("HOST","http://www.baidu.coM")
//                .setCookie("jession=Ae43eFF26303913eda")
//                .build();
//
//
//        System.out.println(request.toString());




//        socketTest();

        try {
            socketChannelTEst();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private static void socketTest(){

        String host ="www.sina.com.cn";
        try {

            Socket sock = new Socket();
            Inet4Address i4ReAdd = (Inet4Address) Inet4Address.getByName(host);
            InetSocketAddress isocka =new InetSocketAddress(i4ReAdd,80);
            sock.connect(isocka);
            if(sock.isConnected()){
                System.out.println("connected ...");

                byte[] buffer;
                StringBuilder query =new StringBuilder("GET / HTTP/1.1").append("\r\n");
                query.append("host:").append(host).append("\r\n\r\n");

                buffer=query.toString().getBytes();


                OutputStream out =sock.getOutputStream();
                out.write(buffer);
                out.flush();


                InputStream in =sock.getInputStream();
                Writer write =new OutputStreamWriter(System.out,"UTF-8");
                byte[] cc =new byte[1024];
                while(in.read(cc)!=-1){
                    write.write(new String(cc,"UTF-8"));
                }
                write.flush();
                write.close();
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void URLConnection(){

        try {
            URL url  =new URL("http://www.sina.com");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(1*60*100);
            conn.connect();

            InputStream in =conn.getInputStream();
            byte[] buffer =new byte[2*1024];

            Writer writer =new BufferedWriter(new OutputStreamWriter(System.out,Charset.forName("UTF-8")));

            while(in.read(buffer)!=-1){
                writer.write(new String(buffer,"UTF-8"));
            }
            writer.flush();
            writer.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void socketChannelTEst() throws UnknownHostException {


        String host="www.baidu.com";
        int port=80;
        InetSocketAddress i4Addr =new InetSocketAddress(host,port);

        try {
//            SocketChannel sc =
//                    SocketChannel.open(new InetSocketAddress(host,port));
            SocketChannel sc =
                    SocketChannel.open();
            sc.connect(i4Addr);
            sc.configureBlocking(false);

            while(!sc.finishConnect()){
                System.out.println("connection is progressing...");
                Thread.currentThread().sleep(200);
            }

            System.out.println("conneccted......");


            String greeting="GET / HTTP/1.1\r\nhost:"+host+"\r\n\r\n";
            int size =1024;
            ByteBuffer buffer =ByteBuffer.allocate(size);
            buffer.put(greeting.getBytes());
            buffer.flip();
            sc.write(buffer);
            buffer.clear();

            OutputStreamWriter ow =new OutputStreamWriter(System.out,"UTF-8");
            Writer writer =new BufferedWriter(ow);


            int c=0;
            while((c=sc.read(buffer))==0){
                System.out.println("c:"+c);
                Thread.currentThread().sleep(200);
            }

            do{
                System.out.println("c:"+c);
                writer.write(new String(buffer.array(),ow.getEncoding()));
                buffer.clear();
                c=sc.read(buffer);
            }while (c!=0);


            writer.flush();
            writer.close();

            sc.close();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    static byte[] drainBytes(ByteBuffer buffer){


        byte[] b =new byte[buffer.capacity()];
        int i=0;
        while(buffer.hasRemaining()){
            b[i]=buffer.get();
            i++;
        }
        return b;
    }

}


