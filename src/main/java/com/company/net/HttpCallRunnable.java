package com.company.net;

import com.company.util.ByteUtil;

import java.io.*;
import java.net.*;
import java.util.concurrent.Callable;

/**
 * Created by pc on 2017/8/11.
 */
public class HttpCallRunnable implements Callable<String> {

    private String url;
    private final String defaultHost="www.baidu.com";
    private String rqstContent="";

    public HttpCallRunnable(String url) {
        this.url = url;
    }

    public String call() throws Exception {

        System.out.println("httpCall...begin");

        if (url == null || url.equals(""))
            url=defaultHost;
        InputStream in = null;
        try {
            Socket sock = new Socket();
            Inet4Address i4ReAdd = (Inet4Address)Inet4Address.getByName(url);
            InetSocketAddress isocka = new InetSocketAddress(i4ReAdd, 80);
            sock.connect(isocka);
            if (sock.isConnected()) {
                System.out.println("connected ...");
                System.out.println(Thread.currentThread().getName());
                byte[] buffer;
                StringBuilder query = new StringBuilder("GET / HTTP/1.1").append("\r\n");
                query.append("host:").append(url).append("\r\n\r\n");
                buffer = query.toString().getBytes();

                OutputStream out = sock.getOutputStream();
                out.write(buffer);
                out.flush();

                in = sock.getInputStream();

                //###################
                Writer writer= new BufferedWriter(new OutputStreamWriter(System.out, "UTF-8"));
                int c;
                long start =System.currentTimeMillis();
                while((c=in.read())!=-1){
                    writer.write(c);
                    writer.flush();
                }

                System.out.println(String.format("\n\n spendTime:%1$.2f",(System.currentTimeMillis()-start)/1000.0f));

                in.close();
                //####################

                return rqstContent;
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rqstContent;
    }
}
