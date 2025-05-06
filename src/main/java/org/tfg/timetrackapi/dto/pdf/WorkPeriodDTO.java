package org.tfg.timetrackapi.dto.pdf;

public record WorkPeriodDTO(
        String entry,
        String exit,
        String inIsMod,  // Usamos Boolean para permitir null
        String outIsMod,  // Usamos Boolean para permitir null

        long durationMs,
        boolean isComplete
) {}
