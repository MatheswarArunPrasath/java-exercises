package com.smartoffice.observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HeadcountSubject {
    private final List<HeadcountObserver> observers = new CopyOnWriteArrayList<>();
    private volatile int headcount = 0;

    public void addObserver(HeadcountObserver o) {
        observers.add(o);
    }

    public void removeObserver(HeadcountObserver o) {
        observers.remove(o);
    }

    public int getHeadcount() {
        return headcount;
    }

    public void setHeadcount(int newCount) {
        if (newCount < 0)
            newCount = 0;
        if (this.headcount != newCount) {
            this.headcount = newCount;
            for (HeadcountObserver o : observers)
                o.onHeadcountChange(newCount);
        }
    }
}
