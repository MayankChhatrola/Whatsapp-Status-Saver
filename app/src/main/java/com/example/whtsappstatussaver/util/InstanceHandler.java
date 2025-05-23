package com.example.whtsappstatussaver.util;



public class InstanceHandler<T> {
    protected T value;
    Class<T> type;

    public InstanceHandler(T value) {
        this.value = value;
    }

    public void setValue (T value) {
        this.value = value;
    }

    public T getValue () {
        return value;
    }

    public String test(Class<T> type, Object obj) {
        type= type;
        if ( type.isInstance(obj) ) {
            return type.getName();
        }
        return type.getName();
    }
}