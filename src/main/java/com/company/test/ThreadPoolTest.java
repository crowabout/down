package com.company.test;

import org.hibernate.annotations.SourceType;

import javax.xml.bind.SchemaOutputResolver;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * Created by pc on 2017/8/3.
 */
public class ThreadPoolTest {
    public static void main(String[] args) {

        Scanner in =new Scanner(System.in);
        System.out.println("Enter base directory (e.g:/usr/lib):");
        String directory =in.nextLine();

        System.out.println("Enter keyword (e.g. volatile):");
        String keyword =in.nextLine();

        ExecutorService pool = Executors.newCachedThreadPool();

        MatchCounter2 counter =new MatchCounter2(new File(directory),keyword,pool);
        Future<Integer> result =pool.submit(counter);

        try {
            System.out.println(result.get()+" matching file");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        pool.shutdown();


        int size =((ThreadPoolExecutor)pool).getLargestPoolSize();
        System.out.println("largest pool size:"+size);





    }
}
