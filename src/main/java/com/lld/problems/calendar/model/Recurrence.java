package com.lld.problems.calendar.model;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Recurrence {

    private final RecurrenceType type;
    private final Instant seriesEnd;

    public Recurrence(RecurrenceType type) {
        this(type, null);
    }

    public Recurrence(RecurrenceType type, Instant seriesEnd) {
        this.type = type;
        this.seriesEnd = seriesEnd;
    }

    public RecurrenceType getType() {
        return type;
    }

    public Instant getSeriesEnd() {
        return seriesEnd;
    }

    /**
     * Expands a meeting into concrete time slots within the query window.
     */
    public List<TimeSlot> expand(Instant meetingStart, Instant meetingEnd, Instant windowStart, Instant windowEnd) {
        List<TimeSlot> slots = new ArrayList<>();
        Duration duration = Duration.between(meetingStart, meetingEnd);

        Instant cursor = meetingStart;
        while (!cursor.isAfter(windowEnd)) {
            Instant slotEnd = cursor.plus(duration);
            if (!slotEnd.isBefore(windowStart)) {
                if (seriesEnd != null && cursor.isAfter(seriesEnd)) {
                    break;
                }
                slots.add(new TimeSlot(cursor, slotEnd));
            }

            cursor = switch (type) {
                case NONE -> windowEnd.plusSeconds(1);
                case DAILY -> cursor.plus(Duration.ofDays(1));
                case WEEKLY -> cursor.plus(Duration.ofDays(7));
            };

            if (type == RecurrenceType.NONE) {
                break;
            }
        }
        return slots;
    }
}
