package org.tfg.timetrackapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tfg.timetrackapi.dto.pdf.DailyWorkReportDTO;
import org.tfg.timetrackapi.dto.pdf.WorkPeriodDTO;
import org.tfg.timetrackapi.entity.TimeStamp;
import org.tfg.timetrackapi.repository.TimeStampRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private TimeStampRepository timestampRepository;

    public List<DailyWorkReportDTO> getEmployeeMonthlyReport(Long employeeId, LocalDate startDate, LocalDate endDate) {
        List<TimeStamp> records = timestampRepository.findByEmployeeIdAndTimestampBetweenOrderByTimestampAsc(
                employeeId,
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay()
        );

        Map<LocalDate, List<TimeStamp>> groupedByDay = records.stream()
                .collect(Collectors.groupingBy(
                        record -> record.getTimestamp().toLocalDate(),
                        TreeMap::new,
                        Collectors.toList()
                ));

        List<DailyWorkReportDTO> report = new ArrayList<>();

        for (Map.Entry<LocalDate, List<TimeStamp>> entry : groupedByDay.entrySet()) {
            LocalDate date = entry.getKey();
            List<TimeStamp> dayRecords = entry.getValue();

            List<WorkPeriodDTO> periods = new ArrayList<>();
            long totalWorkedMs = 0;
            String warning = null;

            for (int i = 0; i < dayRecords.size(); i += 2) {
                TimeStamp inRecord = dayRecords.get(i);
                TimeStamp outRecord = (i + 1 < dayRecords.size()) ? dayRecords.get(i + 1) : null;

                LocalDateTime in = inRecord.getTimestamp();
                LocalDateTime out = outRecord != null ? outRecord.getTimestamp() : null;

                long durationMs = out != null ? Duration.between(in, out).toMillis() : 0;
                if (out == null) warning = "⚠ Falta registro de salida";

                // Preparamos los valores MOD/NORMAL
                String inModStatus = "true".equals(inRecord.isMod()) ? "true" : "false";
                String outModStatus = outRecord != null
                        ? ("true".equals(outRecord.isMod()) ? "true" : "false")
                        : "NO REGISTRADO";

                periods.add(new WorkPeriodDTO(
                        in.format(DateTimeFormatter.ofPattern("HH:mm")),
                        out != null ? out.format(DateTimeFormatter.ofPattern("HH:mm")) : null,
                        inModStatus,
                        out != null ? outModStatus : null,
                        durationMs,
                        out != null
                ));

                totalWorkedMs += durationMs;
            }

            report.add(new DailyWorkReportDTO(
                    date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    periods,
                    formatDuration(totalWorkedMs),
                    dayRecords.size(),
                    warning
            ));
        }

        return report;
    }

    private String formatDuration(long ms) {
        long hours = ms / 3600000;
        long minutes = (ms % 3600000) / 60000;
        return String.format("%dh %02dm", hours, minutes);
    }

    public List<DailyWorkReportDTO> getEmployeeMonthlyReport(Long employeeId, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth()); // último día del mes

        return getEmployeeMonthlyReport(employeeId, start, end);
    }
}
