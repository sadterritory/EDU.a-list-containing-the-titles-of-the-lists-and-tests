package org.list.types;

import java.util.Comparator;
import java.util.Random;

public class MyFloat implements UserType {

    private float value;

    public MyFloat() {}

    public MyFloat(float value) {
        this.value = value;
    }

    public MyFloat(double value) {
        this.value = (float)value;
    }

    public float getValue() {
        return this.value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public Comparator<Object> getTypeComparator() {
        return new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                return Float.compare(((MyFloat) o1).value, ((MyFloat) o2).value);
            }
        };
    }

    public static UserType cloneStatic(UserType obj){
        MyFloat myFloat = new MyFloat();
        myFloat.value = ((MyFloat)obj).getValue();
        return myFloat;
    }

    @Override
    public Object clone(Object obj) {
        return cloneStatic((UserType)obj);
    }


    @Override
    public String typeName() {
        return String.valueOf(this.getClass());
    }

    @Override
    public UserType parseValue(String ss) {
        return new MyFloat(Float.parseFloat(ss));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public static UserType createStatic() {
        return new MyFloat(new Random().nextFloat(10));
    }

    @Override
    public Object create() {
        return createStatic();
    }
}
