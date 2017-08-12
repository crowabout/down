package com.company.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by pc on 2017/8/2.
 */
public class MatchCounter implements Callable<Integer>{

    private File directory;
    private String keyword;

    /**
     * Constructs a MatchCounter.
     * @param directory the directory in which to start the search
     * @param keyword the keyword to look for
     */
    public MatchCounter(File directory, String keyword) {
        this.directory = directory;
        this.keyword = keyword;
    }



    public Integer call() throws Exception {

        int count=0;
        File[] files =directory.listFiles();
        List<Future<Integer>> results =new ArrayList<Future<Integer>>();


        for(File file:files){
            if(file.isDirectory()){
                MatchCounter counter =new MatchCounter(file,keyword);
                FutureTask<Integer> task =new FutureTask<Integer>(counter);
                results.add(task);
                Thread t =new Thread(task);
                System.out.println(t.getName()+" start running");
                t.start();
            }else{
                if(search(file))
                    count++;
            }
        }
        for(Future<Integer> result:results){
            count+=result.get();
        }
        return count;
    }

    /**
     * Search a file for a given keyword
     * @param file the file to search
     * @return  ture if the keyword is contained in the file
     */
    public boolean search(File file){

        try {
            Scanner in =new Scanner(file,"UTF-8");
            boolean found =false;

            while(!found&&in.hasNextLine()){
                String line =in.nextLine();
                if(line.contains(keyword))
                    found=true;
            }
            return  found;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


}
