package org.tfg.timetrackapi.services;

import org.tfg.timetrackapi.dto.pdf.DailyWorkReportDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    public List<DailyWorkReportDTO> getEmployeeMonthlyReport(Long employeeId, LocalDate startDate, LocalDate endDate);
    public List<DailyWorkReportDTO> getEmployeeMonthlyReport(Long employeeId, int year, int month);


}
