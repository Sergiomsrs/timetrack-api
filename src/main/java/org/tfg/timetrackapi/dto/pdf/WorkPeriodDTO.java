package org.tfg.timetrackapi.dto.pdf;

public record WorkPeriodDTO(
        String entry,
        String exit,
        long durationMs,
        boolean isComplete
) {}
