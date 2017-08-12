package com.company.test;

import org.hibernate.annotations.SourceType;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by pc on 2017/8/2.
 */
public class FutureTest {
    public static void main(String[] args) {

        Scanner in =new Scanner(System.in);
        System.out.println("Enter base directory (e.g /usr/local");
        String directory =in.nextLine();
        System.out.println("Enter keyword (e.g volatile)");

        String keyword =in.nextLine();


        MatchCounter counter =new MatchCounter(new File(directory),keyword);
        FutureTask<Integer> task =new FutureTask<Integer>(counter);
        Thread t =new Thread(task);
        t.start();


        try {
            System.out.println(task.get()+" matching files");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }



}
