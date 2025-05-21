package org.tfg.timetrackapi.dto.pdf;

public record WorkPeriodDTO(
        String entry,
        String exit,
        String inIsMod,
        String outIsMod,

        long durationMs,
        boolean isComplete
) {}
