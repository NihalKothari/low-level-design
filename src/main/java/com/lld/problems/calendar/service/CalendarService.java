package com.lld.problems.calendar.service;

import com.lld.common.ErrorCode;
import com.lld.common.Result;
import com.lld.problems.calendar.model.Meeting;
import com.lld.problems.calendar.model.Recurrence;
import com.lld.problems.calendar.model.TimeSlot;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CalendarService {

    private final Map<String, Meeting> meetings = new ConcurrentHashMap<>();

    public Result<Meeting> scheduleMeeting(Meeting meeting) {
        if (!meeting.getEnd().isAfter(meeting.getStart())) {
            return Result.failure(ErrorCode.INVALID_INPUT);
        }

        List<Meeting> conflicts = findConflicts(meeting);
        if (!conflicts.isEmpty()) {
            return Result.failure(ErrorCode.CONFLICT);
        }

        meetings.put(meeting.getMeetingId(), meeting);
        return Result.success(meeting);
    }

    public Result<Meeting> cancelMeeting(String meetingId) {
        Meeting meeting = meetings.get(meetingId);
        if (meeting == null) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        meeting.cancel();
        return Result.success(meeting);
    }

    public Result<Integer> cancelSeries(String seriesId) {
        int cancelled = 0;
        for (Meeting meeting : meetings.values()) {
            if (meeting.getSeriesId().equals(seriesId) && !meeting.isCancelled()) {
                meeting.cancel();
                cancelled++;
            }
        }
        if (cancelled == 0) {
            return Result.failure(ErrorCode.NOT_FOUND);
        }
        return Result.success(cancelled);
    }

    public List<Meeting> getMeetingsForUser(String userId, Instant from, Instant to) {
        List<Meeting> result = new ArrayList<>();
        for (Meeting meeting : meetings.values()) {
            if (meeting.isCancelled()) {
                continue;
            }
            if (!meeting.allParticipants().contains(userId)) {
                continue;
            }
            List<TimeSlot> slots = meeting.getRecurrence().expand(
                    meeting.getStart(),
                    meeting.getEnd(),
                    from,
                    to
            );
            if (!slots.isEmpty()) {
                result.add(meeting);
            }
        }
        return result;
    }

    public List<Meeting> findConflicts(Meeting candidate) {
        Instant windowStart = candidate.getStart();
        Instant windowEnd = candidate.getRecurrence().getSeriesEnd() != null
                ? candidate.getRecurrence().getSeriesEnd()
                : candidate.getEnd().plusSeconds(365L * 24 * 3600);

        List<TimeSlot> candidateSlots = candidate.getRecurrence().expand(
                candidate.getStart(),
                candidate.getEnd(),
                windowStart,
                windowEnd
        );

        Map<String, Meeting> conflictMap = new LinkedHashMap<>();
        for (Meeting existing : meetings.values()) {
            if (existing.isCancelled()) {
                continue;
            }
            if (existing.getMeetingId().equals(candidate.getMeetingId())) {
                continue;
            }

            List<TimeSlot> existingSlots = existing.getRecurrence().expand(
                    existing.getStart(),
                    existing.getEnd(),
                    windowStart,
                    windowEnd
            );

            boolean sharesParticipant = existing.allParticipants().stream()
                    .anyMatch(candidate.allParticipants()::contains);
            if (!sharesParticipant) {
                continue;
            }

            if (slotsOverlap(candidateSlots, existingSlots)) {
                conflictMap.put(existing.getMeetingId(), existing);
            }
        }
        return new ArrayList<>(conflictMap.values());
    }

    public Optional<Meeting> getMeeting(String meetingId) {
        return Optional.ofNullable(meetings.get(meetingId));
    }

    private boolean slotsOverlap(List<TimeSlot> a, List<TimeSlot> b) {
        for (TimeSlot slotA : a) {
            for (TimeSlot slotB : b) {
                if (slotA.overlaps(slotB)) {
                    return true;
                }
            }
        }
        return false;
    }
}
