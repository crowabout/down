package com.company.net;

import com.company.HtmlpageParser;
import com.company.test.SelectSocket;
import com.company.util.ByteUtil;
import com.sun.corba.se.impl.encoding.ByteBufferWithInfo;
import com.sun.org.apache.xml.internal.utils.ThreadControllerWrapper;
import com.sun.scenario.effect.impl.sw.java.JSWBlend_GREENPeer;
import jdk.internal.util.xml.impl.Input;
import org.hibernate.annotations.SourceType;
import org.hibernate.result.Output;
import org.hibernate.sql.Select;

import javax.persistence.criteria.Selection;
import javax.print.DocFlavor;
import java.io.*;
import java.net.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.*;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

/**
 * Created by pc on 2017/7/18.
 */
public class TEst {


    static final int size = 1024;

    public TEst() throws UnsupportedEncodingException {
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
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

            execPool();

//        NodeTest();


//        socketTest();

    }


    /**
     * connection with socket
     */
    private static void socketTest() {

        String host = "www.sina.com.cn";
        try {

            Socket sock = new Socket();
            Inet4Address i4ReAdd = (Inet4Address) Inet4Address.getByName(host);
            InetSocketAddress isocka = new InetSocketAddress(i4ReAdd, 80);
            sock.connect(isocka);
            if (sock.isConnected()) {
                System.out.println("connected ...");

                byte[] buffer;
                StringBuilder query = new StringBuilder("GET / HTTP/1.1").append("\r\n");
                query.append("host:").append(host).append("\r\n\r\n");

                buffer = query.toString().getBytes();


                OutputStream out = sock.getOutputStream();
                out.write(buffer);
                out.flush();


                InputStream in = sock.getInputStream();
                Writer write = new OutputStreamWriter(System.out, "UTF-8");
                byte[] cc = new byte[1024];
                long start =System.currentTimeMillis();
                while (in.read(cc) != -1) {
                    write.write(new String(cc, "UTF-8"));
                    write.flush();
                }
                long end =System.currentTimeMillis();
                System.out.println(String.format("\n\nspendTime:%1$.2f",(end-start)/1000.0f));
                write.close();
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * connection with urlConnection
     */
    private void URLConnection() {

        try {
            URL url = new URL("http://www.sina.com");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(1 * 60 * 100);
            conn.connect();

            InputStream in = conn.getInputStream();
            byte[] buffer = new byte[2 * 1024];

            Writer writer = new BufferedWriter(new OutputStreamWriter(System.out, Charset.forName("UTF-8")));

            while (in.read(buffer) != -1) {
                writer.write(new String(buffer, "UTF-8"));
            }
            writer.flush();
            writer.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * connection with Channel
     *
     * @throws UnknownHostException
     */
    public void socketChannelTEst() throws UnknownHostException {


        final String host = "www.baidu.com";
        final int port = 80;
        try {
            InetSocketAddress addr = new InetSocketAddress(host, port);

            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(false);
            Selector selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);
            channel.connect(addr);


            do {
                sleep(100);
            } while (!channel.finishConnect());

            System.out.println("connected....");

            //   Socket soc =channel.socket();

            if (channel.isConnected()) {
                System.out.println("say Hello to Server!!!");
                writeToChanel(channel);
            }


            while (true) {
                int n = selector.select();
                if (n == 0) {
                    continue;
                }

                Iterator it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = (SelectionKey) it.next();
                    if (key.isReadable()) {
                        System.out.println("=======isReadable=======");
                        readFromChannel(channel);
                    }
//                    if(key.isWritable()){
//                        System.out.println("=======isWritable=======");
//                    }
                    it.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * write data to channel
     *
     * @param chanel
     */
    private static void writeToChanel(SocketChannel chanel) throws IOException {

        Inet4Address addr = (Inet4Address) chanel.socket().getInetAddress();
        System.out.println("send data to :" + addr.getHostAddress());
        String greeting = "GET / HTTP/1.1\r\nhost:" + addr.getHostAddress() + "\r\n\r\n";
        ByteBuffer buffer = ByteBuffer.allocate(size);
        buffer.put(greeting.getBytes());
        try {
//            while (buffer.hasRemaining()){
            chanel.write(buffer);
//            }
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * read data from channel
     *
     * @param chanel
     * @throws IOException
     */
    private static void readFromChannel(SocketChannel chanel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(size);
        long c;
        while ((c = chanel.read(buffer)) != -1) {
            buffer.flip();
            System.out.print(ByteUtil.string(buffer));
            buffer.clear();
        }
    }


    /**
     * @param channel
     */
    private static void readDataFromServer(SocketChannel channel) throws IOException {


        ByteBuffer buffer = ByteBuffer.allocate(size);

//        SocketChannel channel = (SocketChannel) key.channel();
        buffer.clear();
        long c = -1;
        while ((c = channel.read(buffer)) != -1) {
            buffer.flip();
            System.out.print(ByteUtil.string(buffer));
            buffer.clear();
        }
    }


    private static void execPool() {

        String[] urls = {"www.linuxidc.com"
//                "www.baidu.com"
        };
//                "www.sohu.com", "www.qq.com", "linux.linuxidc.com", "www.linuxidc.com"};

        final NodeList<FutureTask<String>> results = new NodeList<FutureTask<String>>();
        List<HttpCallRunnable> request = new ArrayList<HttpCallRunnable>();
        for (int i = 0; i < urls.length; i++) {
            request.add(new HttpCallRunnable(urls[i]));
        }
        ExecutorService pool = Executors.newFixedThreadPool(100);
        for (HttpCallRunnable item : request) {
            FutureTask<String> result = (FutureTask<String>) pool.submit(item);
            results.add(result);
        }

        new Thread(new Runnable() {
            public void run() {
                while(!results.isEmpty()){
                    String s=null;
                    try
                    {
                        Node node =results.nextNode();
                        FutureTask<String> item = (FutureTask<String>) node.value();

//                s=item.get(400,TimeUnit.MILLISECONDS);
                        s =item.get();
                        if(s!=null){
//                            System.out.println(s);
                            results.remove(node);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

        pool.shutdown();
        int size = ((ThreadPoolExecutor) pool).getLargestPoolSize();
        System.out.println("\n==============\n"+size+"\n============\n");

    }


    public static void NodeTest() {

        NodeList list = new NodeList<Integer>();
        Integer i1 =new Integer(1);
        Integer i2 =new Integer(2);
        Integer i3 =new Integer(3);

        list.add(i1);
        list.add(i2);
        list.add(i3);

        System.out.println((Integer) list.nextNode().value());
        System.out.println((Integer) list.nextNode().value());
        System.out.println((Integer) list.nextNode().value());

        list.remove(list.nextNode());

        if (!list.isEmpty()) {
            System.out.println("list.isEmpty():" + list.isEmpty());
            System.out.println("list.head:" + list.headNode().value());
            System.out.println("list.tail:" + list.tailNode().value());
        }

        list.add(new Integer(4));


        list.remove(list.nextNode());
        if (!list.isEmpty()) {
            System.out.println("list.isEmpty():" + list.isEmpty());
            System.out.println("list.head:" + list.headNode().value());
            System.out.println("list.tail:" + list.tailNode().value());

        }
        list.remove(list.nextNode());
        System.out.println("list.isEmpty():" + list.isEmpty());
        if (!list.isEmpty()) {
            System.out.println("list.head:" + list.headNode().value());
            System.out.println("list.tail:" + list.tailNode().value());
        }
    }


}



