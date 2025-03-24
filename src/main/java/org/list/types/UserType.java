package org.list.types;

import java.io.Serializable;
import java.util.Comparator;

public interface UserType extends Serializable {
    String typeName();
    Object create();
    Object clone(Object obj);
    Object parseValue(String ss);
    Comparator<Object> getTypeComparator();
}