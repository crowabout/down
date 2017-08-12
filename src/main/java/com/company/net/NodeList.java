package com.company.net;

/**
 * Created by pc on 2017/8/7.
 */
public final class NodeList<T> {


    private Node<T> pointer;
    private volatile Node curPosition;

    public NodeList() {
        pointer = new Node<T>();
    }

    public NodeList(Node<T> pointerPointer) {
        this.pointer = pointerPointer;
    }


    public Node headNode() {
        return pointer.head;
    }

    public Node tailNode() {
        return pointer.tail;
    }
    private void add(Node node) {
        Node temp = headNode();
        /**
         * 在添加之前没有节点
         */
        if (temp == null) {
            pointer.next = node;
            pointer.tail = node;
            pointer.head = node;
            return;
        }

        /**
         * 添加新节点时，节点列表中已经有节点
         */
        while (temp != null) {
            if (temp.next == null) {
                break;
            }
            temp = temp.next;
        }
        node.previous = temp;
        temp.next = node;
        pointer.tail = node;
    }


    /**
     * 增加一个节点
     * @param t 数据
     */
    public void add(T t){
        Node node =new Node(t);
        add(node);
    }

    /**
     * 移除指定的节点
     *
     * @param node
     */
    public void remove(Node node) {
        if (isEmpty()) {
            return;
        }
        Node find = seqSearch(node);
        if (find != null) {
            if (pointer.head == pointer.tail) {
                pointer.head=null;
                pointer.next=null;
                pointer.tail=null;
            } else {
                //找到的是第一个节点
                if (pointer.head == find) {
                    pointer.head = find.next;
                    pointer.next = find.next;
                } else if (pointer.tail == find) {
                    //最后一个
                    pointer.tail = find.previous;
                    find.previous.next = null;
                } else {
                    //中间的
                    find.previous.next = find.next;
                    find.next.previous = find.previous;
                }
            }
        }
    }


    /**
     * 返回下一个节点
     *
     * @return
     */
    public Node nextNode() {
        if (curPosition == null) {
            curPosition = headNode();
        }
        Node temp = curPosition;
        curPosition = curPosition.next();
        return temp;
    }

    public boolean isEmpty() {
        return pointer.next() == null;
    }


    /**
     * 顺序查找关键字
     *
     * @param keyword
     * @return NULL 没有找见  Node找见
     */
    private Node seqSearch(Node keyword) {
        Node temp = headNode();
        while (temp != null) {
            if (temp == keyword) {
                return temp;
            }
            if (temp == pointer.tail) {
                break;
            }
            temp = temp.next;
        }
        return null;
    }


}


