package com.company.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by pc on 2017/8/3.
 */
public class MatchCounter2 implements Callable<Integer> {


    private File directory;
    private String keyword;
    private ExecutorService pool;
    private int count;

    public MatchCounter2(File directory, String keyword,ExecutorService pool) {
        this.directory = directory;
        this.keyword = keyword;
        this.pool=pool;
    }


    public Integer call() throws Exception{

        count=0;
        File[] files =directory.listFiles();
        List<Future<Integer>> results =new ArrayList<Future<Integer>>();
        for(File file:files){
            if(file.isDirectory()){
                MatchCounter2 counter =new MatchCounter2(file,keyword,pool);
                Future<Integer> result =pool.submit(counter);
                results.add(result);
            }else{
                if(search(file))
                    count++;
            }
        }
        for(Future<Integer> result:results){
            try {
                count+=result.get();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return count;
    }


    public boolean search(File file){
        try
        {
            Scanner in = new Scanner(file,"UTF-8");
            boolean found=false;
            while(!found&&in.hasNextLine()){
                String line =in.nextLine();
                if (line.contains(keyword)) {
                    found=true;
                }
                return found;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
