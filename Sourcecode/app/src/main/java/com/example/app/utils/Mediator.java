package com.example.app.utils;

import java.util.function.Consumer;

public class Mediator {
    private static Mediator instance;

    private Consumer<String> consumer;

    public static Mediator getInstance() {
        if(instance == null) {
            instance = new Mediator();
        }
        return instance;
    }

    private Mediator() {
    }

    public void register(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    public void fireEvent(String string) {
        if(consumer != null) {
            consumer.accept(string);
        }
    }
}
