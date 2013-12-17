package com.jc.app;

/**
 * Created by amclaughlin on 12/15/13.
 */
public class Task {
    String name, complete, length, id;
    Integer time, when;

    public Task(String name, String complete, String length, Integer time, Integer when) {
        this.name = name;
        this.complete = complete;
        this.length = length;
        this.time = time;
        this.when = when;
    }

    public void setId(String value) {
        this.id = value;
    }

    @Override
    public String toString() {
        return name;
    }
}
