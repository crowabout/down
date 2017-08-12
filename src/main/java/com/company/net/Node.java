package com.company.net;

import java.util.UUID;

/**
 * Created by pc on 2017/8/7.
 */
public final class  Node<T> {


    /**
     * 下一个
     */
    protected  Node next;
    /**
     * 上一个
     */
    protected Node previous;
    /**
     * id标识
     */
    private UUID uuid;
    /**
     * 数据区域
     */
    private T data;

    /**
     * 头部索引
     */
    protected Node head;
    /**
     * 尾部索引
     */
    protected Node tail;


    public Node() {
        this.uuid =UUID.randomUUID();
    }

    public Node(T data) {
        this();
        this.data = data;
    }

    public T value(){
        return data;
    }

   protected Node next(){
        return next;
    }

//    public Node previous(){
//        return previous;
//    }

    public void data(T t){
        this.data=t;
    }



//    public  void add(Node node){
//       this.next=node;
//       node.previous=this;
//    }





}
