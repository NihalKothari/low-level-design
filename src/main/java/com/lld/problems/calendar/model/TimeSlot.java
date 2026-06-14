package com.lld.problems.calendar.model;

import java.time.Instant;

public class TimeSlot {

    private final Instant start;
    private final Instant end;

    public TimeSlot(Instant start, Instant end) {
        this.start = start;
        this.end = end;
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

    public boolean overlaps(TimeSlot other) {
        return start.isBefore(other.end) && other.start.isBefore(end);
    }
}
