package org.tfg.timetrackapi.dto.pdf;

import java.util.List;

public record DailyWorkReportDTO(
        String day,
        List<WorkPeriodDTO> periods,
        String totalWorked,
        int recordsCount,
        String warning
) {}
