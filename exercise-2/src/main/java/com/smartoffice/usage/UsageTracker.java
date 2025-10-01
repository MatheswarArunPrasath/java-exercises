package com.smartoffice.usage;

import com.smartoffice.observer.HeadcountObserver;
import com.smartoffice.observer.OccupancyObserver;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public class UsageTracker implements HeadcountObserver, OccupancyObserver {
    private final UsageService usageService;
    private final int roomNo;
    private final Supplier<LocalDateTime> nowSupplier;

    public UsageTracker(UsageService service, int roomNo, Supplier<LocalDateTime> nowSupplier) {
        this.usageService = service;
        this.roomNo = roomNo;
        this.nowSupplier = nowSupplier;
    }

    @Override
    public void onHeadcountChange(int headcount) {
        usageService.forRoom(roomNo).onHeadcount(headcount);
    }

    @Override
    public void onOccupancyChange(boolean occupied) {
        usageService.forRoom(roomNo).onOccupiedChange(occupied, nowSupplier.get());
    }

    public void onBooked() {
        usageService.forRoom(roomNo).onBooked();
    }
}
