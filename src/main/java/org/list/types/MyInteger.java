package org.list.types;

import java.util.Comparator;
import java.util.Random;

public class MyInteger implements UserType {

    private Integer value;

    @Override
    public String typeName() {
        return String.valueOf(this.getClass());
    }

    public MyInteger() {
    }

    public MyInteger(Integer value) {
        this.value = value;
    }

    @Override
    public UserType parseValue(String ss) {
        return new MyInteger(Integer.parseInt(ss));
    }

    @Override
    public Comparator<Object> getTypeComparator() {
        return new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((MyInteger) o1).value - ((MyInteger) o2).value;
            }
        };
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static UserType createStatic() {
        Random r = new Random();
        int randValue = r.nextInt(100) + 1;
        return new MyInteger(randValue);
    }

    public static UserType cloneStatic(UserType obj) {
        MyInteger myInteger = new MyInteger();
        myInteger.value = ((MyInteger) obj).value;
        return myInteger;
    }

    @Override
    public Object clone(Object obj) {
        return cloneStatic((UserType)obj);
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    @Override
    public Object create() {
        return createStatic();
    }
}
