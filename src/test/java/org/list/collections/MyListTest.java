package org.list.collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.list.types.MyInteger;
import org.list.types.MyType;
import org.list.types.UserType;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class MyListTest {

    @ParameterizedTest
    @ValueSource(ints = { 5 })
    public void testAddToEnd(int numOfValues) {
        final MyList myList = new MyList();
        final ArrayList<UserType> expectedList = new ArrayList<>();

        for (int i=0; i<numOfValues; ++i) {
            final var testValue = MyType.createStatic();
            expectedList.add(testValue);
            myList.add(testValue);
        }

        for (int i=0; i<numOfValues; ++i) {
            assertEquals(expectedList.get(i), myList.getNode(i).getData());
        }
    }

    @Test
    public void testAddByIndex() {
        final MyList myList = new MyList();
        var testValue1 = MyInteger.createStatic();
        var testValue2 = MyInteger.createStatic();
        var testValue3 = MyInteger.createStatic();
        var testValue4 = MyInteger.createStatic();

        myList.add(testValue1, 0); // testValue1 - 0
        myList.add(testValue2, 0); // testValue2 - 0, testValue1 - 1,
        myList.add(testValue3, 1); // testValue2 - 0, testValue3 - 1, testValue1 - 2
        myList.add(testValue4, 2); // testValue2 - 0, testValue3 - 1, testValue4 - 2, testValue1 - 3

        assertEquals(testValue1, myList.getNode(3).getData());
        assertEquals(testValue2, myList.getNode(0).getData());
        assertEquals(testValue3, myList.getNode(1).getData());
        assertEquals(testValue4, myList.getNode(2).getData());
    }

    @Test
    public void testAddByInvalidIndex() {
        final MyList myList = new MyList();
        var testValue1 = MyInteger.createStatic();

        assertThrows(IllegalArgumentException.class, ()-> myList.add(testValue1, 3));
        assertThrows(IllegalArgumentException.class, ()-> myList.add(testValue1, 2));
        assertThrows(IllegalArgumentException.class, ()-> myList.add(testValue1, 1));
    }

    @Test
    public void testAddChildToEnd() {
        final MyList myList = new MyList();
        final var parentValue1 = MyInteger.createStatic();
        final var parentValue2 = MyInteger.createStatic();
        final var testValue1 = MyInteger.createStatic();
        final var testValue2 = MyInteger.createStatic();
        final var testValue3 = MyInteger.createStatic();
        final var testValue4 = MyInteger.createStatic();

        myList.add(parentValue1);
        myList.add(parentValue2);
        myList.addChild(testValue1, 0);
        myList.addChild(testValue2, 0);
        myList.addChild(testValue3, 1);
        myList.addChild(testValue4, 1);

        assertEquals(testValue1, myList.getChildNode(0,0).getData());
        assertEquals(testValue2, myList.getChildNode(0,1).getData());
        assertEquals(testValue3, myList.getChildNode(1,0).getData());
        assertEquals(testValue4, myList.getChildNode(1,1).getData());
    }

    @Test
    public void testAddChildToEndByInvalidIndex() {
        final MyList myList = new MyList();
        var testValue1 = MyInteger.createStatic();

        assertThrows(IllegalArgumentException.class, ()-> myList.addChild(testValue1, 3));
        assertThrows(IllegalArgumentException.class, ()-> myList.addChild(testValue1, 2));
        assertThrows(IllegalArgumentException.class, ()-> myList.addChild(testValue1, 1));
    }

    @Test
    public void testAddChildByIndex() {
        final MyList myList = new MyList();
        final var parentValue1 = MyInteger.createStatic();
        final var parentValue2 = MyInteger.createStatic();
        final var testValue1 = MyInteger.createStatic();
        final var testValue2 = MyInteger.createStatic();
        final var testValue3 = MyInteger.createStatic();
        final var testValue4 = MyInteger.createStatic();
        final var testValue5 = MyInteger.createStatic();
        final var testValue6 = MyInteger.createStatic();

        myList.add(parentValue1);
        myList.add(parentValue2);
        myList.addChild(testValue1, 0, 0);
        myList.addChild(testValue2, 0, 0);
        myList.addChild(testValue3, 0, 1);
        myList.addChild(testValue4, 1, 0);
        myList.addChild(testValue5, 1, 0);
        myList.addChild(testValue6, 1, 1);

        assertEquals(testValue1, myList.getChildNode(0,2).getData());
        assertEquals(testValue2, myList.getChildNode(0,0).getData());
        assertEquals(testValue3, myList.getChildNode(0,1).getData());
        assertEquals(testValue4, myList.getChildNode(1,2).getData());
        assertEquals(testValue5, myList.getChildNode(1,0).getData());
        assertEquals(testValue6, myList.getChildNode(1,1).getData());
    }

    @Test
    public void testAddChildByInvalidIndex() {
        final MyList myList = new MyList();
        var testValue1 = MyInteger.createStatic();
        final var parentValue1 = MyInteger.createStatic();

        myList.add(parentValue1);
        assertThrows(IllegalArgumentException.class, ()-> myList.addChild(testValue1, 0, 3));
        assertThrows(IllegalArgumentException.class, ()-> myList.addChild(testValue1, 0, 2));
        assertThrows(IllegalArgumentException.class, ()-> myList.addChild(testValue1, 0, 1));
    }


    @ParameterizedTest
    @ValueSource(ints = { 30 })
    public void testRemoveParent(int numOfValues) {
        final MyList myList = new MyList();
        final List<UserType> expectedList = new LinkedList<>();

        for (int i=0; i<numOfValues; ++i) {
            final var testValue = MyType.createStatic();
            expectedList.add(testValue);
            myList.add(testValue);
        }

        final List<Integer> indexesToRemove = IntStream
                .range(0, numOfValues)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(indexesToRemove);

        for (int i=0; i < numOfValues; ++i) {
            final int expectedSize = numOfValues-(i+1);
            int indexToRemove = indexesToRemove.get(i);
            while (indexToRemove > myList.getSize()-1) indexToRemove -= 1;

            assertEquals(expectedList.remove(indexToRemove),
                         myList.remove(indexToRemove));
            assertEquals(expectedSize, myList.getSize());
        }
    }

    @Test
    public void testRemoveByInvalidIndex() {
        final MyList myList = new MyList();
        var testValue1 = MyInteger.createStatic();

        myList.add(testValue1);

        assertThrows(IllegalArgumentException.class, ()-> myList.remove(3));
        assertThrows(IllegalArgumentException.class, ()-> myList.remove(2));
        assertThrows(IllegalArgumentException.class, ()-> myList.remove(1));
    }



    @ParameterizedTest
    @ValueSource(ints = { 5 })
    public void testRemoveChild(int numOfValues) {
        final MyList myList = new MyList();
        final List<List<UserType>> expectedList = new LinkedList<>();

        for (int i=0; i<numOfValues; ++i) {
            final var testValue = MyType.createStatic();
            myList.add(testValue);
            List<UserType> expectedChildList = new LinkedList<>();
            for (int j=0; j<numOfValues; ++j) {
                final var testChild = MyType.createStatic();
                myList.addChild(testChild, i);
                expectedChildList.add(testChild);
            }
            expectedList.add(expectedChildList);
        }

        final List<Integer> indexesToRemove = IntStream
                .range(0, numOfValues)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(indexesToRemove);
        int totalChildElems = numOfValues*numOfValues;

        for (int i=0; i < numOfValues; ++i) {
            final List<UserType> expectedChildList = expectedList.get(i);
            for (int j=0; j < numOfValues; ++j) {
                final int expectedSize = numOfValues-(j+1);
                int indexToRemove = indexesToRemove.get(j);
                while (indexToRemove > myList.getNode(i).getChilds()-1) indexToRemove -= 1;

                var expObj = expectedChildList.remove(indexToRemove);
                var testObj = myList.removeChild(i, indexToRemove);
                totalChildElems--;

                assertEquals(expObj, testObj);
                assertEquals(expectedSize, myList.getChildSize(i));
                assertEquals(totalChildElems, myList.getTotalChildElements());
            }

        }
    }

    @Test
    public void testRemoveChildByInvalidIndex() {
        final MyList myList = new MyList();
        var testValue1 = MyInteger.createStatic();
        var childValue = MyInteger.createStatic();

        myList.add(testValue1);
        myList.addChild(childValue, 0);

        assertThrows(IllegalArgumentException.class, ()-> myList.removeChild(1, 1));
        assertThrows(IllegalArgumentException.class, ()-> myList.removeChild(0, 3));
        assertThrows(IllegalArgumentException.class, ()-> myList.removeChild(0, 2));
        assertThrows(IllegalArgumentException.class, ()-> myList.removeChild(0, 1));
    }

    @ParameterizedTest
    @ValueSource(ints = { 12 })
    public void testSort(int numOfValues) {
        final MyList myList = new MyList();
        final List<UserType> expectedList = IntStream
                .range(0, numOfValues)
                .mapToObj(num-> MyInteger.createStatic())
                .peek(myList::add)
                .collect(Collectors.toCollection(ArrayList::new));

        myList.quickSort(0, myList.getSize()-1, expectedList.getFirst().getTypeComparator());
        expectedList.sort(expectedList.getFirst().getTypeComparator());

        for (int i=0; i < numOfValues; ++i) {
            assertEquals(((MyInteger)expectedList.get(i)).getValue(),
                         ((MyInteger)myList.getNode(i).getData()).getValue());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = { 12 })
    public void testSortOrderedAscending(int numOfValues) {
        final MyList myList = new MyList();
        final List<UserType> expectedList = IntStream
                .range(0, numOfValues)
                .mapToObj(MyInteger::new)
                .peek(myList::add)
                .collect(Collectors.toCollection(ArrayList::new));

        myList.quickSort(0, myList.getSize()-1, expectedList.getFirst().getTypeComparator());
        expectedList.sort(expectedList.getFirst().getTypeComparator());

        for (int i=0; i < numOfValues; ++i) {
            assertEquals(((MyInteger)expectedList.get(i)).getValue(),
                         ((MyInteger)myList.getNode(i).getData()).getValue());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = { 3 })
    public void testSortOrderedDescending(int numOfValues) {
        final MyList myList = new MyList();
        final List<UserType> expectedList = IntStream
                .range(0, numOfValues)
                .mapToObj(num -> new MyInteger(numOfValues-num-1))
                .peek(myList::add)
                .collect(Collectors.toCollection(ArrayList::new));

        myList.quickSort(0, myList.getSize()-1, expectedList.getFirst().getTypeComparator());
        expectedList.sort(expectedList.getFirst().getTypeComparator());

        for (int i=0; i < numOfValues; ++i) {
            assertEquals(((MyInteger)expectedList.get(i)).getValue(),
                         ((MyInteger)myList.getNode(i).getData()).getValue());
        }
    }

    @ParameterizedTest
    @ValueSource(ints = { 500 })
    public void testSortTime(int numOfValues) {
        final MyList myList = new MyList();
        IntStream.range(0, numOfValues)
                .mapToObj(num -> MyInteger.createStatic())
                .forEach(myList::add);

        long startTime = System.currentTimeMillis();
        myList.quickSort(0, myList.getSize()-1, MyInteger.createStatic().getTypeComparator());
        long endTime = System.currentTimeMillis();
        long timeELapsed = endTime - startTime;
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        long usedMemory = memoryBean.getHeapMemoryUsage().getUsed();
        System.out.println("N Memory Time");
        System.out.printf("%d %d %d%n", numOfValues, usedMemory, timeELapsed);

        assertEquals(numOfValues, myList.getSize());
    }


}