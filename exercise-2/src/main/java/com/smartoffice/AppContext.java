package com.smartoffice;

import com.smartoffice.usage.UsageService;

public final class AppContext {
    private static final UsageService USAGE = new UsageService();

    public static UsageService usage() {
        return USAGE;
    }

    private AppContext() {
    }
}
