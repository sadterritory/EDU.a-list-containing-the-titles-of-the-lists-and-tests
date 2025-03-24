package org.list;

import org.list.collections.MyList;
import org.list.types.*;

import java.io.IOException;
import java.util.Scanner;

public class Menu {

    public static void printMenu() {
        System.out.println("Choose option:");
        System.out.println("[1] Add value");
        System.out.println("[2] Add value by index");
        System.out.println("[3] Add children value by indexes");
        System.out.println("[4] Get value by index");
        System.out.println("[5] Remove parent value by index");
        System.out.println("[6] Remove child value by indexes");
        System.out.println("[7] Get size of list");
        System.out.println("[8] Get childs count");
        System.out.println("[9] Sort parent list");
        System.out.println("[10] Sort child list");
        System.out.println("[11] Print list");
        System.out.println("[12] Serialize list to Binary File");
        System.out.println("[13] Deserialize list from Binary File");
        System.out.println("[0] Exit");
    }

    public static void menu() {
        MyList list = new MyList();
        System.out.println("Choose data type: 1 - Integer, 2 - Float, 3 - String, 4 - Fraction");
        Scanner scanner = new Scanner(System.in);
        int dataType = scanner.nextInt();
        if (dataType == 1) {
            for (int i = 0; i < 10; i++) {
                UserType integer = MyInteger.createStatic();
                list.add(integer);
            }
        } else if (dataType == 2) {
            for (int i = 0; i < 10; i++) {
                list.add(MyFloat.createStatic());
            }
        } else if (dataType == 3) {
            for (int i = 0; i < 10; i++) {
                list.add(MyString.createStatic());
            }
        } else {
            for (int i = 0; i < 10; i++) {
                list.add(MyType.createStatic());
            }
        }
        list.printList();
        MyInteger myInteger = new MyInteger();
        MyFloat myFloat = new MyFloat();
        MyString myString = new MyString();
        MyType myType = new MyType();
        Integer index1 = null;
        Integer index2 = null;
        if (scanner.hasNextLine()) scanner.nextLine();
        while (true) {
            printMenu();
            int option = scanner.nextInt();
            switch (option) {
                case 1:
                    if (scanner.hasNextLine()) scanner.nextLine();
                    if (dataType == 1) {
                        System.out.println("Enter integer value");
                        String value = scanner.nextLine();
                        myInteger = (MyInteger) myInteger.parseValue(value);
                        list.add(myInteger);
                    } else if (dataType == 2) {
                        System.out.println("Enter real");
                        String value = scanner.nextLine();
                        myFloat = (MyFloat) myFloat.parseValue(value);
                        list.add(myFloat);
                    } else if (dataType == 3) {
                        System.out.println("Enter string");
                        String value = scanner.nextLine();
                        myString = (MyString) myString.parseValue(value);
                        list.add(myString);
                    } else {
                        System.out.println("Enter fraction");
                        String value = scanner.nextLine();
                        myType = (MyType) myType.parseValue(value);
                        list.add(myType);
                    }
                    break;
                case 2:
                    if (list.getSize() == 0) {
                        System.out.println("List is empty, add something first");
                    } else {
                        System.out.println("Enter index from 0 to " + (list.getSize()));
                    }
                    index1 = scanner.nextInt();
                    scanner.nextLine(); // Очистка буфера

                    if (index1 < 0 || index1 >= list.getSize() + 1) {
                        System.out.println("Invalid index. Please enter a valid index.");
                        return;
                    }

                    String value;
                    if (dataType == 1) {
                        System.out.println("Enter integer value");
                        value = scanner.nextLine();
                        try {
                            myInteger = (MyInteger) myInteger.parseValue(value);
                            list.add(myInteger, index1);
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    } else if (dataType == 2) {
                        System.out.println("Enter real");
                        value = scanner.nextLine();
                        try {
                            myFloat = (MyFloat) myFloat.parseValue(value);
                            list.add(myFloat, index1);
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    } else if (dataType == 3) {
                        System.out.println("Enter string");
                        value = scanner.nextLine();
                        try {
                            myString = (MyString) myString.parseValue(value);
                            list.add(myString);
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    } else {
                        System.out.println("Enter fraction");
                        value = scanner.nextLine();
                        try {
                            myType = (MyType) myType.parseValue(value);
                            list.add(myType);
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    break;
                case 3:

                    if (list.getSize() == 0) {
                        System.out.println("List is empty, add something first");
                    } else {
                        System.out.println("Enter index from 0 to " + (list.getSize() - 1));
                    }
                    index1 = scanner.nextInt();
                    scanner.nextLine(); // Очистка буфера
                    System.out.println("Enter index from 0 to " + (list.getChildSize(index1)));
                    index2 = scanner.nextInt();
                    scanner.nextLine();
                    if ((index1 < 0 || index1 >= list.getSize()) && (index2 < 0 || index2 >= list.getChildSize(index1) + 1)) {
                        System.out.println("Invalid indexes. Please enter a valid indexes.");
                        return;
                    }

                    if (dataType == 1) {
                        System.out.println("Enter integer value");
                        value = scanner.nextLine();
                        try {
                            myInteger = (MyInteger) myInteger.parseValue(value);
                            list.addChild(myInteger, index1, index2);
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    } else if (dataType == 2) {
                        System.out.println("Enter real");
                        value = scanner.nextLine();
                        try {
                            myFloat = (MyFloat) myFloat.parseValue(value);
                            list.addChild(myFloat, index1, index2);
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    } else if (dataType == 3) {
                        System.out.println("Enter string");
                        value = scanner.nextLine();
                        try {
                            myString = (MyString) myString.parseValue(value);
                            list.addChild(myString, index1, index2);
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    } else {
                        System.out.println("Enter fraction");
                        value = scanner.nextLine();
                        try {
                            myType = (MyType) myType.parseValue(value);
                            list.addChild(myType, index1, index2);
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    break;
                case 4:
                    if (list.getSize() == 0)
                        System.out.println("List is empty, there is no element to get");
                    else {
                        System.out.println("Enter index from 0 to " + (list.getSize() - 1));
                        index1 = scanner.nextInt();
                        if (scanner.hasNextLine()) scanner.nextLine();
                        try {
                            System.out.println("Value of " + index1 + " element = " + list.getNode(index1).getData());
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    break;
                case 5:
                    if (list.getSize() == 0)
                        System.out.println("List is empty, there is no element to remove");
                    else {
                        System.out.println("Enter index from 0 to " + (list.getSize() - 1));
                        index1 = scanner.nextInt();
                        if (scanner.hasNextLine()) scanner.nextLine();
                        try {
                            System.out.println("Removed element:" + list.remove(index1));
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    break;
                case 6:
                    if (list.getSize() == 0)
                        System.out.println("List is empty, there is no element to remove");
                    else {
                        System.out.println("Enter index from 0 to " + (list.getSize() - 1));
                        index1 = scanner.nextInt();
                        scanner.nextLine(); // Очистка буфера
                        if (list.getChildSize(index1) == 0) {
                            System.out.println("No children to remove.");
                            break;
                        }
                        System.out.println("Enter index from 0 to " + (list.getChildSize(index1) - 1));
                        index2 = scanner.nextInt();
                        if (scanner.hasNextLine()) scanner.nextLine();
                        try {
                            System.out.println("Removed element: " + list.removeChild(index1, index2));
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                    break;
                case 7:
                    System.out.println("Size of parent list: " + list.getSize());
                    System.out.println("Total child elements: " + list.getTotalChildElements());
                    System.out.println("Total elements: " + (list.getSize() + list.getTotalChildElements()));
                    break;
                case 8:
                    if (list.getSize() == 0)
                        System.out.println("List is empty, there is no element to print");
                    else {
                        System.out.println("Enter index from 0 to " + (list.getSize() - 1));
                        index1 = scanner.nextInt();
                        scanner.nextLine(); // Очистка буфера
                        if (index1 < 0 || index1 >= list.getSize()) {
                            throw new IllegalArgumentException("Invalid index " + index1);
                        } else {
                            System.out.println("Size of childs list: " + list.getNode(index1).getChilds());
                        }
                    }
                    break;
                case 9:
                    if (list.getSize() == 0)
                        System.out.println("List is empty, add something first");
                    else {
                        list.printList();
                        if (dataType == 1) {
                            list.quickSort(0, list.getSize() - 1, myInteger.getTypeComparator());
                        } else if (dataType == 2) {
                            list.quickSort(0, list.getSize() - 1, myFloat.getTypeComparator());
                        } else if (dataType == 3){
                            list.quickSort(0, list.getSize() - 1, myString.getTypeComparator());
                        } else {
                            list.quickSort(0, list.getSize() - 1, myType.getTypeComparator());
                        }
                        list.printList();
                        break;
                    }
                case 10:
                    if (list.getTotalChildElements() == 0)
                        System.out.println("List is empty, add something first");
                    else {
                        list.printList();
                        if (dataType == 1) {
                            for (int i = 0; i < list.getSize(); i++) {
                                System.out.println(i);
                                list.quickSortChild(i, 0, list.getChildSize(i) - 1, myInteger.getTypeComparator());
                            }
                        } else if (dataType == 2) {
                            for (int i = 0; i < list.getSize(); i++) {
                                list.quickSortChild(i, 0, list.getChildSize(i) - 1, myFloat.getTypeComparator());
                            }
                        } else if (dataType == 3) {
                            for (int i = 0; i < list.getSize(); i++) {
                                list.quickSortChild(i, 0, list.getChildSize(i) - 1, myString.getTypeComparator());
                            }
                        } else {
                            for (int i = 0; i < list.getSize(); i++) {
                                list.quickSortChild(i, 0, list.getChildSize(i) - 1, myType.getTypeComparator());
                            }
                        }
                        list.printList();
                    }
                    break;
                case 11:
                    list.printList();
                    break;
                case 12:
                    try {
                        if (list.getSize() == 0)
                            System.out.println("List is empty, add something first");
                        else {
                            MyList.serializeToBinary(list);
                        }
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                case 13:
                    try {
                        list = MyList.deserializeFromBinary();
                    } catch (ClassNotFoundException | IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                    break;
                case 0:
                    return;
                default:
                    break;
            }
        }
    }
}
