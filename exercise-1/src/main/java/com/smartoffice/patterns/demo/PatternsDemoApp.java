package com.smartoffice.patterns.demo;

import com.smartoffice.patterns.behavioral.chain.*;
import com.smartoffice.patterns.behavioral.state.EnergyContext;
import com.smartoffice.patterns.creational.abstractfactory.ControllerFactory;
import com.smartoffice.patterns.creational.abstractfactory.EcoFactory;
import com.smartoffice.patterns.creational.abstractfactory.PerformanceFactory;
import com.smartoffice.patterns.creational.builder.BookingRequest;
import com.smartoffice.patterns.creational.builder.BookingRequestBuilder;
import com.smartoffice.patterns.shared.*;
import com.smartoffice.patterns.structural.adapter.LegacyTurnstile;
import com.smartoffice.patterns.structural.adapter.TurnstileAdapter;
import com.smartoffice.patterns.structural.decorator.AuditNotificationDecorator;
import com.smartoffice.patterns.structural.decorator.FlakyNotificationService;
import com.smartoffice.patterns.structural.decorator.RetryNotificationDecorator;

import java.time.LocalDate;
import java.time.LocalTime;

public class PatternsDemoApp {
    public static void main(String[] args) {
        System.out.println("=== Patterns Demo (Standalone) ===");

        // Bootstrap a tiny standalone office
        TimeProvider time = new SystemTimeProvider();
        OfficeLite office = new OfficeLite(time);
        office.configureRooms(2);
        RoomLite room1 = office.getRoomOrThrow(1);
        RoomLite room2 = office.getRoomOrThrow(2);

        // ===== 1) BEHAVIORAL — Chain of Responsibility (booking validation) =====
        System.out.println("\n[Behavioral: Chain of Responsibility]");
        BookingInput input = new BookingInput(1, LocalDate.now(), LocalTime.now().plusMinutes(5), 30, "alice");
        ValidationHandler chain = new RoomExistsHandler(office)
                .linkWith(new DurationPositiveHandler())
                .linkWith(new NotPastHandler(time))
                .linkWith(new NoOverlapHandler(office));
        try {
            chain.validate(input);
            BookingManagerLite manager = new BookingManagerLite();
            System.out.println("Booking result: "
                    + manager.book(room1, input.date(), input.start(), input.durationMinutes(), input.owner()));
        } catch (IllegalArgumentException ex) {
            System.out.println("Validation failed: " + ex.getMessage());
        }
        // Attempt an overlap to see validation fail
        try {
            BookingInput bad = new BookingInput(1, LocalDate.now(), input.start().plusMinutes(10), 30, "bob");
            chain.validate(bad);
            BookingManagerLite manager = new BookingManagerLite();
            System.out.println(manager.book(room1, bad.date(), bad.start(), bad.durationMinutes(), bad.owner()));
        } catch (IllegalArgumentException ex) {
            System.out.println("Expected overlap caught: " + ex.getMessage());
        }

        // ===== 2) BEHAVIORAL — State (Energy modes per room) =====
        System.out.println("\n[Behavioral: State]");
        EnergyContext energy = new EnergyContext(room2);
        energy.peopleEnter(1); // still <2, Unoccupied
        energy.peopleEnter(1); // >=2 -> Occupied
        energy.idleTimeout(); // Occupied -> Standby (idle)
        energy.peopleExit(2); // -> Unoccupied

        // ===== 3) CREATIONAL — Builder (BookingRequest) =====
        System.out.println("\n[Creational: Builder]");
        BookingRequest req = BookingRequestBuilder.create()
                .roomNo(2)
                .date(LocalDate.now())
                .start(LocalTime.now().plusMinutes(15))
                .durationMinutes(45)
                .owner("alice")
                .purpose("Design review")
                .attendees(6)
                .build();
        System.out.println("Built request: " + req);
        BookingManagerLite m2 = new BookingManagerLite();
        System.out.println(m2.book(room2, req.date(), req.start(), req.durationMinutes(), req.owner()));

        // ===== 4) CREATIONAL — Abstract Factory (vendor-specific controllers) =====
        System.out.println("\n[Creational: Abstract Factory]");
        ControllerFactory eco = new EcoFactory(); // energy saver (warmer)
        ControllerFactory perf = new PerformanceFactory(); // performance (cooler)
        ACControllerLite ecoAC = eco.createAC(room1);
        LightControllerLite ecoLight = eco.createLight(room1);
        ACControllerLite perfAC = perf.createAC(room2);
        LightControllerLite perfLight = perf.createLight(room2);
        room2.setHeadcount(2);
        perfAC.on();
        perfAC.adjust();
        ecoLight.on();
        System.out.println("Created ECO and PERFORMANCE controllers and toggled them.");

        // ===== 5) STRUCTURAL — Decorator (Retry + Audit notifications) =====
        System.out.println("\n[Structural: Decorator]");
        NotificationService flaky = new FlakyNotificationService(1); // fail first attempt
        NotificationService resilient = new AuditNotificationDecorator(new RetryNotificationDecorator(flaky, 3, 100));
        resilient.notifyAutoRelease("alice", 2, "2025-09-27T22:30:00", "alice@example.com", "9999912345");

        // ===== 6) STRUCTURAL — Adapter (legacy turnstile -> Sensor) =====
        System.out.println("\n[Structural: Adapter]");
        LegacyTurnstile legacy = new LegacyTurnstile();
        Sensor adapted = new TurnstileAdapter(legacy);
        adapted.registerDelta(+3); // 3 enter
        adapted.registerDelta(-1); // 1 exit
        System.out.println("Adapted sensor count: " + adapted.currentCount()); // -> 2

        System.out.println("\n=== Demo complete ===");
    }
}
