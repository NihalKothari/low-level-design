package com.lld.problems.calendar;

import com.lld.common.ErrorCode;
import com.lld.problems.calendar.model.Meeting;
import com.lld.problems.calendar.model.Recurrence;
import com.lld.problems.calendar.model.RecurrenceType;
import com.lld.problems.calendar.service.CalendarService;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalendarTest {

    private CalendarService calendar;
    private Instant baseStart;
    private Instant baseEnd;

    @BeforeEach
    void setUp() {
        calendar = new CalendarService();
        baseStart = Instant.parse("2026-06-10T09:00:00Z");
        baseEnd = Instant.parse("2026-06-10T10:00:00Z");
    }

    @Test
    void scheduleNonConflictingMeeting() {
        Meeting meeting = new Meeting(
                "Planning",
                "alice",
                List.of("bob"),
                baseStart,
                baseEnd,
                new Recurrence(RecurrenceType.NONE)
        );
        assertTrue(calendar.scheduleMeeting(meeting).isSuccess());
    }

    @Test
    void detectOverlappingConflict() {
        Meeting first = new Meeting(
                "Sprint Review",
                "alice",
                List.of("bob"),
                baseStart,
                baseEnd,
                new Recurrence(RecurrenceType.NONE)
        );
        calendar.scheduleMeeting(first);

        Meeting overlapping = new Meeting(
                "Design Sync",
                "bob",
                List.of("alice"),
                baseStart.plusSeconds(1800),
                baseEnd.plusSeconds(1800),
                new Recurrence(RecurrenceType.NONE)
        );

        assertFalse(calendar.findConflicts(overlapping).isEmpty());
        assertEquals(ErrorCode.CONFLICT, calendar.scheduleMeeting(overlapping).getError().orElseThrow());
    }

    @Test
    void recurringDailyMeetingsAppearInRangeQuery() {
        Meeting daily = new Meeting(
                "Standup",
                "alice",
                List.of("bob"),
                baseStart,
                baseEnd,
                new Recurrence(RecurrenceType.DAILY, baseStart.plusSeconds(3 * 24 * 3600))
        );
        calendar.scheduleMeeting(daily);

        List<Meeting> aliceMeetings = calendar.getMeetingsForUser(
                "alice",
                baseStart,
                baseStart.plusSeconds(3 * 24 * 3600)
        );
        assertEquals(1, aliceMeetings.size());
    }

    @Test
    void cancelMeeting() {
        Meeting meeting = new Meeting(
                "Retro",
                "alice",
                List.of("bob"),
                baseStart,
                baseEnd,
                new Recurrence(RecurrenceType.NONE)
        );
        calendar.scheduleMeeting(meeting);
        calendar.cancelMeeting(meeting.getMeetingId());

        assertTrue(calendar.getMeeting(meeting.getMeetingId()).orElseThrow().isCancelled());
        assertTrue(calendar.findConflicts(meeting).isEmpty());
    }

    @Test
    void rejectInvalidInterval() {
        Meeting invalid = new Meeting(
                "Bad",
                "alice",
                List.of("bob"),
                baseEnd,
                baseStart,
                new Recurrence(RecurrenceType.NONE)
        );
        assertEquals(ErrorCode.INVALID_INPUT, calendar.scheduleMeeting(invalid).getError().orElseThrow());
    }
}
