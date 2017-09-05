package com.company;

import javafx.util.Builder;

import javax.swing.plaf.ButtonUI;

/**
 * Created by swan on 9/5/17.
 */
public class DownerConfigure {


    private final String fileDownloadDir;
    private final int sizeOfThreadPool;
    private final String logLevel;
    private final String downLoadFileType;
    private final int sizeOfThread;
    public DownerConfigure(Builder buidler){
        fileDownloadDir=buidler.fileDownloadDir;
        sizeOfThreadPool=buidler.sizeOfThreadPool;
        sizeOfThread=buidler.sizeOfThread;
        logLevel=buidler.logLevel;
        downLoadFileType=buidler.downLoadFileType;
    }
    public int sizeOfThread(){
        return sizeOfThread;
    }
    public String fileDownloadDir(){
        return fileDownloadDir;
    }

    public String logLevel(){
        return logLevel;
    }

    public String downloadFielType(){
        return downLoadFileType;
    }

    public int sizeOfThreadPool(){
        return sizeOfThreadPool;
    }


    public static class Builder{
        private String fileDownloadDir;
        private int sizeOfThreadPool;
        private String logLevel;
        private String downLoadFileType;
        private int sizeOfThread;
        public Builder(){
           fileDownloadDir="/home/swan/Download/Down/";
           sizeOfThreadPool=100;
           logLevel="info";
           downLoadFileType="pdf";
           sizeOfThread=100;
        }
        public Builder setDownloadDir(String downloadPath){
            this.fileDownloadDir=downloadPath;
            return this;
        }

        public Builder sizeOfThreadPool(int size){
            this.sizeOfThreadPool=size;
            return this;
        }

        public Builder logLevel(String level){
            this.logLevel=level;
            return this;
        }
        public Builder downloadFileType(String type){
            this.downLoadFileType=type;
            return this;
        }

        public Builder sizeOfThread(int sizeOfThread){
            this.sizeOfThread=sizeOfThread;
            return this;
        }

        public DownerConfigure build(){
            return new DownerConfigure(this);
        }
    }

}
