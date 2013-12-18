package com.jc.app;

/**
 * Created by amclaughlin on 12/15/13.
 */
public class Task {
    String name, complete, length, id;
    Integer time, when, best, allTime, total;

    public Task(String name, String complete, String length, Integer time, Integer when, Integer best, Integer elapsed, Integer total) {
        this.name = name;
        this.complete = complete;
        this.length = length;
        this.time = time;
        this.when = when;
        this.best = best;
        this.allTime = elapsed;
        this.total = total;
    }

    public void setId(String value) {
        this.id = value;
    }

    @Override
    public String toString() {
        return name;
    }
}
