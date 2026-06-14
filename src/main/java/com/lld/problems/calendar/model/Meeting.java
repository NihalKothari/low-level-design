package com.lld.problems.calendar.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Meeting {

    private final String meetingId;
    private final String seriesId;
    private final String title;
    private final String organizerId;
    private final List<String> attendees;
    private final Instant start;
    private final Instant end;
    private final Recurrence recurrence;
    private boolean cancelled;

    public Meeting(
            String title,
            String organizerId,
            List<String> attendees,
            Instant start,
            Instant end,
            Recurrence recurrence
    ) {
        this(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                title,
                organizerId,
                attendees,
                start,
                end,
                recurrence,
                false
        );
    }

    public Meeting(
            String meetingId,
            String seriesId,
            String title,
            String organizerId,
            List<String> attendees,
            Instant start,
            Instant end,
            Recurrence recurrence,
            boolean cancelled
    ) {
        this.meetingId = meetingId;
        this.seriesId = seriesId;
        this.title = title;
        this.organizerId = organizerId;
        this.attendees = new ArrayList<>(attendees);
        this.start = start;
        this.end = end;
        this.recurrence = recurrence;
        this.cancelled = cancelled;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public String getTitle() {
        return title;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public List<String> getAttendees() {
        return Collections.unmodifiableList(attendees);
    }

    public Instant getStart() {
        return start;
    }

    public Instant getEnd() {
        return end;
    }

    public Recurrence getRecurrence() {
        return recurrence;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }

    public List<String> allParticipants() {
        List<String> participants = new ArrayList<>(attendees);
        if (!participants.contains(organizerId)) {
            participants.add(organizerId);
        }
        return participants;
    }
}
