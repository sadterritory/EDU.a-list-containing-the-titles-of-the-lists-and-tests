package org.list.types;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.Stream;

public class MyType implements UserType {
    public MyType() {
        integerPart = 1;
        numerator = 1;
        denominator = 2;
    }

    public MyType(final int integerPart, final int numerator, final int denominator) {
        if (isMyTypeValid(numerator, denominator)) {
            this.numerator = numerator;
            this.denominator = denominator;
            this.integerPart = integerPart;
        } else {
            this.integerPart = 1;
            this.numerator = 1;
            this.denominator = 2;
        }

    }

    public static boolean isMyTypeValid(int numerator, int denominator) {
        if (numerator >= denominator)
            return false;
        return denominator != 0;
    }

    @Override
    public String typeName() {
        return String.valueOf(this.getClass());
    }


    public static UserType createStatic() {
        Random r = new Random();
        int randIntPart = r.nextInt(100) + 1;
        int randNum = r.nextInt(100) + 1;
        int ranDen = r.nextInt(100) + 1;
        while (!isMyTypeValid(randNum, ranDen)) {
            randNum = r.nextInt(100) + 1;
            ranDen = r.nextInt(100) + 1;
        }
        MyType myType = new MyType(randIntPart, randNum, ranDen);
        return myType;
    }

    @Override
    public Object create() {
        return createStatic();
    }

    //@Override
    public static UserType cloneStatic(UserType obj) {
        MyType myType = new MyType();
        myType.setDenominator(((MyType) obj).getDenominator());
        myType.setNumerator(((MyType) obj).getNumerator());
        myType.setIntegerPart(((MyType) obj).getIntegerPart());
        return myType;
    }

    @Override
    public Object clone(Object obj) {
        return cloneStatic((UserType)obj);
    }


    @Override
    public UserType parseValue(String ss) {
        String[] parts = ss.split(" ");
        if (parts.length == 1) parts = new String[] {"0", ss};
        String[] frParts = parts[1].split("/");
        MyType myType = new MyType(Integer.parseInt(parts[0]), Integer.parseInt(frParts[0]), Integer.parseInt(frParts[1]));
        return myType;
    }

    @Override
    public Comparator<Object> getTypeComparator() {
        return new Comparator<>() {
            @Override
            public int compare(Object o1, Object o2) {
                double myType1 = ((double) ((MyType) o1).numerator) / ((MyType) o1).denominator;
                double myType2 = ((double) ((MyType) o2).numerator) / ((MyType) o2).denominator;
                if (((MyType) o1).integerPart != ((MyType) o2).integerPart) {
                    return ((MyType) o1).integerPart - ((MyType) o2).integerPart;
                } else
                    return Double.compare(myType1, myType2);
            }
        };
    }


    public int getNumerator() {
        return numerator;
    }

    public void setNumerator(int numerator) {
        if (isMyTypeValid(numerator, denominator)) {
            this.numerator = numerator;
        } else
            this.numerator = 1;
    }

    public void setDenominator(int denominator) {
        if (isMyTypeValid(numerator, denominator)) {
            this.denominator = denominator;
        } else
            this.denominator = 2;

    }

    public void setIntegerPart(int integerPart) {
        this.integerPart = integerPart;
    }

    public int getDenominator() {
        return denominator;
    }

    public int getIntegerPart() {
        return integerPart;
    }

    @Override
    public String toString() {
        return "Fraction{" +
                integerPart + " " +
                numerator + "/" +
                denominator + "}";
    }

    private int numerator;
    private int denominator;
    private int integerPart;
}
