package com.lld.problems.calendar.demo;

import com.lld.problems.calendar.model.Meeting;
import com.lld.problems.calendar.model.Recurrence;
import com.lld.problems.calendar.model.RecurrenceType;
import com.lld.problems.calendar.service.CalendarService;
import java.time.Instant;
import java.util.List;

public class CalendarDemo {

    public static void main(String[] args) {
        CalendarService calendar = new CalendarService();
        Instant start = Instant.parse("2026-06-10T10:00:00Z");
        Instant end = Instant.parse("2026-06-10T11:00:00Z");

        Meeting standup = new Meeting(
                "Daily Standup",
                "alice",
                List.of("bob", "carol"),
                start,
                end,
                new Recurrence(RecurrenceType.DAILY, start.plusSeconds(7 * 24 * 3600))
        );

        calendar.scheduleMeeting(standup);
        System.out.println("Scheduled: " + standup.getTitle());

        Meeting conflict = new Meeting(
                "1:1",
                "bob",
                List.of("alice"),
                start.plusSeconds(1800),
                end.plusSeconds(1800),
                new Recurrence(RecurrenceType.NONE)
        );

        System.out.println("Conflicts detected: " + calendar.findConflicts(conflict).size());
        System.out.println("Alice meetings this week: " + calendar.getMeetingsForUser(
                "alice",
                start,
                start.plusSeconds(7 * 24 * 3600)
        ).size());
    }
}
