package org.list.collections;

import org.list.types.UserType;

import java.io.Serializable;

public class Node implements Serializable {

    private Node next;

    private Node child;

    private Integer childs = 0;

    private UserType data;

    public Object getData() {
        return this.data;
    }

    public void setChilds(Integer childs){
        this.childs = childs;
    }

    public void addChilds(){
        this.childs++;
    }

    public Integer getChilds(){
        return this.childs;
    }

    public Node getNext() {
        return next;
    }

    public Node getChild() {
        return child;
    }

    public void setChild(Node child) {
        this.child = child;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setData(UserType data) {
        this.data = data;
    }

    public Node(Node next, UserType data) {
        this.next = next;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Node{" +
                "next=" + next +
                ", child=" + child +
                ", childs=" + childs +
                ", data=" + data +
                '}';
    }
}
