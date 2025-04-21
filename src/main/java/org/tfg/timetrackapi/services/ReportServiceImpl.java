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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private TimeStampRepository timestampRepository;

    public List<DailyWorkReportDTO> getEmployeeMonthlyReport(Long employeeId, LocalDate startDate, LocalDate endDate) {
        List<TimeStamp> records = timestampRepository.findByEmployeeIdAndTimestampBetweenOrderByTimestampAsc(
                employeeId,
                startDate.atStartOfDay(),
                endDate.plusDays(1).atStartOfDay() // hasta el final del día
        );

        Map<LocalDate, List<LocalDateTime>> groupedByDay = records.stream()
                .map(TimeStamp::getTimestamp)
                .collect(Collectors.groupingBy(LocalDateTime::toLocalDate));

        List<DailyWorkReportDTO> report = new ArrayList<>();

        for (Map.Entry<LocalDate, List<LocalDateTime>> entry : groupedByDay.entrySet()) {
            LocalDate date = entry.getKey();
            List<LocalDateTime> times = entry.getValue().stream()
                    .sorted()
                    .collect(Collectors.toList());

            List<WorkPeriodDTO> periods = new ArrayList<>();
            long totalWorkedMs = 0;
            String warning = null;

            for (int i = 0; i < times.size(); i += 2) {
                LocalDateTime in = times.get(i);
                LocalDateTime out = (i + 1 < times.size()) ? times.get(i + 1) : null;

                long durationMs = (out != null) ? Duration.between(in, out).toMillis() : 0;
                if (out == null) warning = "⚠ Falta registro de salida";

                WorkPeriodDTO period = new WorkPeriodDTO(
                        in.format(DateTimeFormatter.ofPattern("HH:mm")),
                        (out != null) ? out.format(DateTimeFormatter.ofPattern("HH:mm")) : null,
                        durationMs,
                        (out != null)
                );

                periods.add(period);
                totalWorkedMs += durationMs;
            }

            DailyWorkReportDTO dayReport = new DailyWorkReportDTO(
                    date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    periods,
                    formatDuration(totalWorkedMs),
                    times.size(),
                    warning
            );

            report.add(dayReport);
        }

        return report;
    }

    private String formatDuration(long ms) {
        long hours = ms / 3600000;
        long minutes = (ms % 3600000) / 60000;
        return String.format("%dh %02dm", hours, minutes);
    }
}
