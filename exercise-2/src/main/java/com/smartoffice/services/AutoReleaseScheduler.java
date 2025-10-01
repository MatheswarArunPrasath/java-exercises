package com.smartoffice.services;

import com.smartoffice.core.Office;
import com.smartoffice.core.Room;
import com.smartoffice.notify.NotificationService;
import com.smartoffice.security.AuthService;
import com.smartoffice.security.User;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class AutoReleaseScheduler {
    private static final Logger LOG = Logger.getLogger(AutoReleaseScheduler.class.getName());

    private final Office office;
    private final TimeProvider time;
    private final NotificationService notifier;
    private final AuthService auth;

    private final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "auto-release");
        t.setDaemon(true);
        return t;
    });
    private ScheduledFuture<?> handle;
    private final Duration threshold = Duration.ofMinutes(5);

    public AutoReleaseScheduler(Office office, TimeProvider time,
            NotificationService notifier, AuthService auth) {
        this.office = office;
        this.time = time;
        this.notifier = notifier;
        this.auth = auth;
    }

    public void start() {
        if (handle != null && !handle.isCancelled())
            return;
        handle = exec.scheduleAtFixedRate(this::tick, 20, 20, TimeUnit.SECONDS);
    }

    public void stop() {
        if (handle != null)
            handle.cancel(true);
        exec.shutdownNow();
    }

    private void tick() {
        try {
            for (Room r : office.allRooms()) {
                r.finishIfOver();
                var opt = r.getCurrentBooking();
                if (opt.isPresent()) {
                    var b = opt.get();
                    String owner = b.getOwner();
                    if (r.autoReleaseIfNoShow(threshold)) {
                        User u = auth.getUser(owner);
                        String email = u != null ? u.getEmail() : null;
                        String phone = u != null ? u.getPhone() : null;
                        notifier.notifyAutoRelease(owner, r.getRoomNo(), time.now().toString(), email, phone);
                        LOG.info(() -> "Auto-released booking for Room " + r.getRoomNo() + " and notified " + owner);
                    }
                }
            }
        } catch (Exception e) {
            LOG.warning("AutoRelease tick failed: " + e.getMessage());
        }
    }
}
