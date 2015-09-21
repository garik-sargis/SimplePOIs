package com.example.simplepois.data.model;

public class WithId<T> {
    public final long id;
    public final T data;

    public WithId(long id, T data) {
        this.id = id;
        this.data = data;
    }

    @Override
    public String toString() {
        return "WithId{" +
                "id=" + id +
                ", data=" + data +
                '}';
    }
}
