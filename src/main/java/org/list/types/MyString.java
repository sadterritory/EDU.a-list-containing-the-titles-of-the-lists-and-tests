package org.list.types;

import java.util.Comparator;
import java.util.Random;

public class MyString implements UserType {

    private String value;

    @Override
    public String typeName() {
        return String.valueOf(this.getClass());
    }

    public MyString() {
    }

    public MyString(String value) {
        this.value = value;
    }

    @Override
    public UserType parseValue(String ss) {
        return new MyString(ss);
    }

    @Override
    public Comparator<Object> getTypeComparator() {
        return new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((MyString) o1).value.compareTo(((MyString) o2).value);
            }
        };
    }

    public static UserType createStatic() {
        Random r = new Random();
        String randVal = r.ints(65, 91)
                .limit(5)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        MyString myString = new MyString(randVal);
        return myString;
    }

    @Override
    public Object create() {
        return createStatic();
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static UserType cloneStatic(UserType obj) {
        MyString myString = new MyString();
        myString.value = ((MyString) obj).value;
        return myString;
    }

    @Override
    public Object clone(Object obj) {
        return cloneStatic((UserType)obj);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }

}
