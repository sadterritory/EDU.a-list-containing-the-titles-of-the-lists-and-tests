package org.list.collections;

import org.list.types.UserType;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class MyList implements Serializable {
    private Node head;

    private Node child;

    private int size = 0;

    public int N = 0;

    private int totalChildElements = 0;

    public MyList() {
        this.head = null;
        this.child = null;
        this.size = 0;
    }

    public void add(UserType ob) {
        Node cur = this.head;
        if (this.head == null) {
            this.head = new Node(null, ob); // Создайте новый узел с данными
            size++;
            return;
        }
        while (cur.getNext() != null) {
            cur = cur.getNext();
        }
        Node node = new Node(null, ob);
        cur.setNext(node); // Связать предыдущий узел с новым
        size++;

    }

    public void add(UserType ob, int index) {
        if (index < 0 || index > size) {
            throw new IllegalArgumentException("Invalid index " + index);
        }

        if (index == size) {
            add(ob);
            return;
        }
        Node cur = head;
        Node prev = head;
        for (int i = 0; i < index; i++) {
            prev = cur;
            cur = cur.getNext();
        }
        Node node = new Node(null, ob);
        if (prev == cur) {
            head = node;
            node.setNext(cur);
            size++;
            return;
        }
        size++;
        prev.setNext(node);
        node.setNext(cur);
    }

    public void addChild(UserType ob, int index) {
        Node parentNode = getNode(index);
        if (parentNode != null) {
            Node newChildNode = new Node(null, ob);

            if (parentNode.getChild() == null) {
                parentNode.setChild(newChildNode);
            } else {
                Node currentChild = parentNode.getChild();
                while (currentChild.getNext() != null) {
                    currentChild = currentChild.getNext();
                }
                currentChild.setNext(newChildNode);
            }
            parentNode.addChilds();
        }
        this.totalChildElements++;
    }

    public void addChild(UserType ob, int index1, int index2) {
        if (index1 < 0 || index1 > size + 1) {
            throw new IllegalArgumentException("Invalid parent index " + index1);
        }
        if (index2 < 0 || index2 > getChildSize(index1)) {
            throw new IllegalArgumentException("Invalid child index " + index2);
        }

        Node parentNode = getNode(index1);
        Node newChildNode = new Node(null, ob);

        // Обрабатываем случай, когда добавляем в начало или в конец
        if (index2 == 0) {
            // Добавление в начало
            newChildNode.setNext(parentNode.getChild());
            parentNode.setChild(newChildNode);

        } else {
            // Добавление в середину или в конец

            Node curChild = parentNode.getChild();
            for (int i = 0; i < index2; i++) {
                curChild = curChild.getNext();
            }
            newChildNode.setNext(curChild);
            if (curChild != null) {
                Node prevChild = parentNode.getChild();
                for (int i = 0; i < index2 - 1; i++) {
                    prevChild = prevChild.getNext();
                }
                prevChild.setNext(newChildNode);
            } else {
                parentNode.setChild(newChildNode);
            }
        }
        parentNode.addChilds();
        this.totalChildElements++;
    }


    public Node getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("Invalid index " + index);
        }
        Node cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur.getNext();
        }
        return cur;
    }

    public Node getChildNode(int index1, int index2) {
        if (index2 < 0 || index2 >= getChildSize(index1)) {
            throw new IllegalArgumentException("Invalid index " + index2);
        }
        Node cur = getNode(index1).getChild();
        for (int i = 0; i < index2; i++) {
            cur = cur.getNext();
        }
        return cur;
    }

    public Object remove(int index) {
        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("Invalid index " + index);
        }

        Node cur = head;
        Node prev = null;

        for (int i = 0; i < index; i++) {
            prev = cur;
            cur = cur.getNext();
        }

        // Сохраняем ссылку на дочерние элементы
        Node curChild = cur.getChild();

        // Рекурсивно удаляем всех потомков
        removeChildren(curChild);

        Object ob = cur.getData();

        if (prev == null) { // Удаление первого элемента
            head = head.getNext();
        } else {
            prev.setNext(cur.getNext());
        }
        this.size--;
        return ob;
    }

    private void removeChildren(Node child) {
        while (child != null) {
            Node nextChild = child.getNext();
            removeChildren(child.getChild()); // Рекурсивно удаляем потомков
            child.setNext(null); // Убираем ссылку на следующий элемент
            child = nextChild; // Переходим к следующему дочернему элементу
        }
    }


    public Object removeChild(int index1, int index2) {
        if (index1 < 0 || index1 >= size) {
            throw new IllegalArgumentException("Invalid index " + index1);
        } else if (index2 < 0 || index2 >= getChildSize(index1)) {
            throw new IllegalArgumentException("Invalid index " + index2);
        }

        Node cur = getNode(index1);
        Node curChild = cur.getChild();
        Node prevChild = null;
        for (int i = 0; i < index2; i++) {
            prevChild = curChild;
            curChild = curChild.getNext();
        }

        Object ob = curChild.getData();
        if (prevChild == null) {
            cur.setChild(curChild.getNext());
        } else {
            prevChild.setNext(curChild.getNext());
        }
        cur.setChilds(cur.getChilds()-1);

        this.totalChildElements--;
        return ob;
    }

    public int getChildSize(int index1) {
        Node parent = getNode(index1);
        return parent.getChilds();
    }

    public int getSize() {
        return size;
    }

    public int getTotalChildElements() {
        return totalChildElements;
    }

    void swap(Node a, Node aPrev, Node b, Node bPrev) {
        if (a.getNext() == b) {
            a.setNext(b.getNext());
            b.setNext(a);
            if (aPrev != null)
                aPrev.setNext(b);
            else head = b;
        } else {
            Node tmp = a.getNext();
            a.setNext(b.getNext());
            b.setNext(tmp);
            if (aPrev != null)
                aPrev.setNext(b);
            else
                head = b; //for childs change to parent.setChild
            if (bPrev != null)
                bPrev.setNext(a);
        }
    }

    int partition(int start, int end, Comparator comparator) {
        Node pivot = getNode(end);
        Node prev, pPrev, pivotPrev;
        if (end - 1 < 0)
            pivotPrev = null;
        else
            pivotPrev = getNode(end - 1);
        if (start - 1 < 0)
            pPrev = null;
        else
            pPrev = getNode(start - 1);
        Node cur = getNode(start);
        int pIndex = start;
        Node p = getNode(pIndex);
        if (start - 1 < 0)
            prev = null;
        else
            prev = getNode(start - 1);
        while (cur != pivot) {
            if (comparator.compare(cur.getData(), pivot.getData()) <= 0) {
                swap(p, pPrev, cur, prev);
                if (cur == pivotPrev)
                    pivotPrev = p;
                Node tmp = p;
                pPrev = cur;
                p = cur.getNext();
                pIndex++;
                cur = tmp.getNext();
                prev = tmp;
            } else {
                prev = cur;
                cur = cur.getNext();
            }
        }
        swap(p, pPrev, pivot, pivotPrev);
        return pIndex;
    }

    public void quickSort(int start, int end, Comparator comparator) {
        if (start >= end)
            return;
        int pivot = partition(start, end, comparator);
        quickSort(start, pivot - 1, comparator);
        quickSort(pivot + 1, end, comparator);
    }

    public void quickSortChild(int index, int start, int end, Comparator comparator) {
        if (start >= end)
            return;
        int pivot = partitionChilds(index, start, end, comparator);
        quickSortChild(index, start, pivot - 1, comparator);
        quickSortChild(index, pivot + 1, end, comparator);
    }

    int partitionChilds(int index, int start, int end, Comparator comparator) {

        Node pivot = getChildNode(index, end);
        System.out.println(pivot.getData()); //correct -------------------------------- delete this string
        Node prev, pPrev, pivotPrev;
        if (end - 1 < 0)
            pivotPrev = null;
        else
            pivotPrev = getChildNode(index, end - 1);
        if (start - 1 < 0)
            pPrev = null;
        else
            pPrev = getChildNode(index, start - 1);
        Node cur = getChildNode(index, start);
        int pIndex = start;
        Node p = getChildNode(index, pIndex);
        if (start - 1 < 0)
            prev = null;
        else
            prev = getChildNode(index, start - 1);
        while (cur != pivot) {
            if (comparator.compare(cur.getData(), pivot.getData()) <= 0) {
                swapChild(index, p, pPrev, cur, prev);
                if (cur == pivotPrev)
                    pivotPrev = p;
                Node tmp = p;
                pPrev = cur;
                p = cur.getNext();
                pIndex++;
                cur = tmp.getNext();
                prev = tmp;
            } else {
                prev = cur;
                cur = cur.getNext();
            }
        }
        swapChild(index, p, pPrev, pivot, pivotPrev);
        System.out.println(pIndex);
        return pIndex;
    }

    void swapChild(int index, Node a, Node aPrev, Node b, Node bPrev) {
        if (a.getNext() == b) {
            a.setNext(b.getNext());
            b.setNext(a);
            if (aPrev != null)
                aPrev.setNext(b);
            else getNode(index).setChild(b);
        } else {
            Node tmp = a.getNext();
            a.setNext(b.getNext());
            b.setNext(tmp);
            if (aPrev != null)
                aPrev.setNext(b);
            else
                getNode(index).setChild(b); //for childs change to parent.setChild
            if (bPrev != null)
                bPrev.setNext(a);
        }
    }

    @Override
    public String toString() {
        Node cur = head;
        StringBuilder stringBuilder = new StringBuilder();
        if (size == 0) {
            stringBuilder.append("List is empty");
        } else {
            List<String> childsStringList = new ArrayList<>();
            while (cur != null) {
                Node curChild = cur.getChild();
                stringBuilder.append("Node: " + cur.getData());
                stringBuilder.append("(");
                childsStringList.clear();
                for (int i = 0; i < cur.getChilds(); i++) {
                    childsStringList.add(curChild.getData().toString());
                    curChild = curChild.getNext();
                }
                stringBuilder.append(String.join("->", childsStringList));
                stringBuilder.append(")\n");
                cur = cur.getNext();
            }
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public void printList() {
        System.out.println(this);
    }

    public void forEach(CallBack callBackInt) {
        Node cur = head;
        while (cur != null) {
            callBackInt.toDo(cur.getData());
            cur = cur.getNext();
        }
    }

    public static void serializeToBinary(MyList list) throws IOException {
        FileOutputStream fos = new FileOutputStream("temp.out");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(list);
        oos.flush();
        oos.close();
    }

    public static MyList deserializeFromBinary() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("temp.out");
        ObjectInputStream oin = new ObjectInputStream(fis);
        return (MyList) oin.readObject();
    }

}
