package org.tfg.timetrackapi.controller;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.tfg.timetrackapi.dto.pdf.DailyWorkReportDTO;
import org.tfg.timetrackapi.dto.pdf.WorkPeriodDTO;
import org.tfg.timetrackapi.entity.User;
import org.tfg.timetrackapi.services.ReportService;
import org.tfg.timetrackapi.services.TimeStampService;
import org.tfg.timetrackapi.services.UserService;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final TimeStampService timeStampService;
    private final UserService userService;

    private final ReportService reportService;
    public ReportController(TimeStampService timeStampService, UserService userService, ReportService reportServiceImpl) {
        this.timeStampService = timeStampService;
        this.userService = userService;
        this.reportService = reportServiceImpl;
    }
// http://localhost:8080/api/report/employee/1/report/pdf?start=2025-04-01&end=2025-04-30
    @GetMapping("/employee/{employeeId}/report/pdf")
    public void downloadPdfReport(
            @PathVariable Long employeeId,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull LocalDate end,
            HttpServletResponse response) throws IOException {

        // Verificar si el empleado existe
        if (!userService.existsById(employeeId)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Empleado no encontrado");
            return;
        }

        // Validar fechas
        if (start.isAfter(end)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "La fecha de inicio debe ser anterior a la fecha final");
            return;
        }

        // Obtener el reporte
        List<DailyWorkReportDTO> report = reportService.getEmployeeMonthlyReport(employeeId, start, end);

        if (report.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NO_CONTENT, "No hay registros para el período especificado");
            return;
        }

        // Configurar respuesta
        response.setContentType("application/pdf");
        String filename = String.format("informe_jornadas_%s_%s_a_%s.pdf",
                employeeId, start, end);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        // Generar PDF
        try (OutputStream os = response.getOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, os);
            document.open();

            // Estilos
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 10);
            Font warningFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.RED);

            // Título
            document.add(new Paragraph("Informe de Jornadas Laborales", titleFont));
            document.add(new Paragraph("Empleado: " + employeeId, subtitleFont));
            document.add(new Paragraph("Periodo: " + start + " a " + end, subtitleFont));
            document.add(Chunk.NEWLINE);

            // Contenido
            for (DailyWorkReportDTO day : report) {
                // Encabezado del día
                Paragraph dayHeader = new Paragraph();
                dayHeader.add(new Chunk("Fecha: ", subtitleFont));
                dayHeader.add(new Chunk(day.day(), normalFont));
                document.add(dayHeader);

                // Total trabajado
                document.add(new Paragraph("Total: " + day.totalWorked(), normalFont));

                // Advertencias
                if (day.warning() != null) {
                    document.add(new Paragraph(day.warning(), warningFont));
                }

                document.add(Chunk.NEWLINE);

                // Tabla de registros
                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{2, 2, 3});
                table.setSpacingBefore(10f);

                // Encabezados de tabla
                Stream.of("Entrada", "Salida", "Duración")
                        .forEach(header -> {
                            PdfPCell cell = new PdfPCell();
                            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            cell.setPhrase(new Phrase(header, subtitleFont));
                            table.addCell(cell);
                        });

                // Datos
                for (WorkPeriodDTO period : day.periods()) {
                    table.addCell(new Phrase(period.entry(), normalFont));
                    table.addCell(new Phrase(period.exit() != null ? period.exit() : "--", normalFont));
                    table.addCell(new Phrase(formatDuration(period.durationMs()), normalFont));
                }

                document.add(table);
                document.add(Chunk.NEWLINE);
            }

            document.close();
        } catch (DocumentException e) {
            throw new IOException("Error al generar el PDF", e);
        }
    }

    private String formatDuration(long ms) {
        long hours = ms / 3600000;
        long minutes = (ms % 3600000) / 60000;
        return String.format("%dh %02dm", hours, minutes);
    }

    @GetMapping("/employee/{employeeId}/report/pdf/monthly")
    public void downloadMonthlyPdfReport(
            @PathVariable Long employeeId,
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            HttpServletResponse response) throws IOException {

        // Validación de mes
        if (month < 1 || month > 12) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mes inválido");
            return;
        }

        if (!userService.existsById(employeeId)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Empleado no encontrado");
            return;
        }

        List<DailyWorkReportDTO> report = reportService.getEmployeeMonthlyReport(employeeId, year, month);

        if (report.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NO_CONTENT, "No hay registros para el mes especificado");
            return;
        }



        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        response.setContentType("application/pdf");
        String filename = String.format("informe_jornadas_%s_%s_%02d.pdf", employeeId, year, month);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        User user = new User();
        user = userService.getById(employeeId);

        try (OutputStream os = response.getOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, os);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
            Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 10);
            Font warningFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.RED);

            document.add(new Paragraph("Informe de Jornadas Laborales", titleFont));
            document.add(new Paragraph("Empleado: " + user.getName() + " " + user.getLastName() + " " + user.getSecondLastName() + " ", subtitleFont));
            document.add(new Paragraph("DNI: " + user.getDni() , subtitleFont));
            document.add(new Paragraph("Mes: " + month + "  Año: " + year, subtitleFont));
            document.add(Chunk.NEWLINE);


            for (DailyWorkReportDTO day : report) {
                Paragraph dayHeader = new Paragraph();
                dayHeader.add(new Chunk("Fecha: ", subtitleFont));
                dayHeader.add(new Chunk(day.day(), normalFont));
                document.add(dayHeader);

                document.add(new Paragraph("Total: " + day.totalWorked(), normalFont));

                if (day.warning() != null) {
                    document.add(new Paragraph(day.warning().substring(2), warningFont));
                }

                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                table.setWidths(new float[]{2, 2, 3});
                table.setSpacingBefore(10f);

                Stream.of("Entrada", "Salida", "Duración")
                        .forEach(header -> {
                            PdfPCell cell = new PdfPCell();
                            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            cell.setPhrase(new Phrase(header, subtitleFont));
                            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            table.addCell(cell);
                        });

                for (WorkPeriodDTO period : day.periods()) {
                    table.addCell(new Phrase(period.entry(), normalFont));
                    table.addCell(new Phrase(period.exit() != null ? period.exit() : "--", normalFont));
                    table.addCell(new Phrase(formatDuration(period.durationMs()), normalFont));
                }

                document.add(table);
                document.add(Chunk.NEWLINE);
            }

            document.close();
        } catch (DocumentException e) {
            throw new IOException("Error al generar el PDF", e);
        }
    }





}
