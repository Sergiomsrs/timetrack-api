package org.tfg.timetrackapi.dto.pdf;

import java.util.List;

public record EmployeeReportDTO(
        Long employeeId,
        List<DailyWorkReportDTO> days
) {}
