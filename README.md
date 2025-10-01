# Java Exercises 
 
This repository contains two separate Java projects: 
- **exercise-1**:
- Project 1 description
- Design Patterns Demo (Standalone)

This is a **standalone** Java console project that demonstrates **six software design patterns** in a smart-office context:

- **Behavioral**: Chain of Responsibility (booking validation), State (energy modes)
- **Creational**: Builder (BookingRequest), Abstract Factory (controller families)
- **Structural**: Decorator (retry + audit notifications), Adapter (legacy turnstile → Sensor)

> Plain Java (no Maven/Gradle). Tested on **JDK 25** (works on JDK 17+).

---

## Project layout
patterns/
└─ src/
└─ main/
└─ java/
└─ com/smartoffice/patterns/
├─ demo/PatternsDemoApp.java # main()
├─ behavioral/chain/... # Chain of Responsibility
├─ behavioral/state/... # State
├─ creational/builder/... # Builder
├─ creational/abstractfactory/... # Abstract Factory
├─ structural/decorator/... # Decorator
├─ structural/adapter/... # Adapter
└─ shared/... # lightweight domain helpers


---

## Build & Run (Windows PowerShell / VS Code)

cd patterns
# compile all sources to .\out
javac -encoding UTF-8 -d out $((Get-ChildItem -Recurse -Path src\main\java -Filter *.java).FullName)

# run the demo (main class)
java -cp out com.smartoffice.patterns.demo.PatternsDemoApp

# what the demo prints
chain of responsibility, state, builder, abstract factory, decorator, adapter


- **exercise-2**: Project 2 description

- # Exercise 2 — Smart Office Facility (Console App)

A console application to manage a smart office: **room configuration, booking/cancel, sensor-based occupancy (≥2 people), 5-minute no-show auto-release, AC/Lights automation with temperature scaling, authentication & roles, usage stats, and notifications** — with solid OOP, clean error handling, and pattern-driven design.

> Plain Java (no Maven/Gradle). Tested on **JDK 25** (works on JDK 17+).

---

## Project layout

smart-office-pro/
└─ src/
└─ main/
└─ java/
└─ com/smartoffice/
├─ App.java # main()
├─ core/, commands/, services/...
├─ observer/, rules/, sensors/, notify/ ...
└─ (optionally) patterns/ # if you included the 6 pattern demos here too


---

## Build & Run (Windows PowerShell / VS Code)

cd smart-office-pro
# compile to .\out
javac -encoding UTF-8 -d out $((Get-ChildItem -Recurse -Path src\main\java -Filter *.java).FullName)

# run main
java -cp out com.smartoffice.App

Default login: admin admin123

# sample input 
login admin admin123
config room count 3
create_user alice USER password1
logout
login alice password1
block room 1 23:00 60
add occupant 1 2
room status 1
rooms unoccupied

# features checklist

Configuration: set number of rooms; per-room max capacity

Booking: create & cancel bookings (conflict checks, ownership/admin rules)

Occupancy rule: room becomes OCCUPIED when ≥ 2 people are inside
(add occupant or sensor enter/exit)

No-show auto-release: booking is released if not occupied within 5 min of start time

Automation:

Lights & AC ON when occupied; OFF when unoccupied

AC temperature scales with headcount (cooler as room fills)

Auth & roles: Admin/User; change password; set contact info

Usage: summary and per-room usage stats; list unoccupied rooms

Notifications: console “Email/SMS” lines on auto-release

# negative/edge cases 

# already booked (overlap)
block room 1 23:00 60
# cancel not booked
cancel room 2
# insufficient occupancy info
add occupant 2 1
# non-existent room
add occupant 4 2
# invalid room number token
block room A 09:00 60
# invalid capacity format
config room max capacity 1-5

# commands reference(help)

Auth:
  login <username> <password>
  logout
  whoami
  change_password <username> <old> <new>
  set_contact <email|- > <phone|- >

Admin:
  create_user <username> <ADMIN|USER> <password>
  list_users
  config room count <N>
  config room max capacity <roomNo> <capacity>

User:
  block room <roomNo> <HH:mm> <durationMin>
  block room <roomNo> <yyyy-MM-dd> <HH:mm> <durationMin>
  cancel room <roomNo>
  add occupant <roomNo> <count>
  sensor enter <roomNo> <count>
  sensor exit <roomNo> <count>
  room status <roomNo>
  rooms unoccupied

Usage:
  usage summary
  usage room <roomNo>

Other:
  help
  exit

