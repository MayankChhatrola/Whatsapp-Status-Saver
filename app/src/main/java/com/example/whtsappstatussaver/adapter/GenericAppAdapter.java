package com.example.whtsappstatussaver.adapter;


public class GenericAppAdapter<T> {
    protected T value;

    public GenericAppAdapter(T value) {
        this.value = value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}