package org.tfg.timetrackapi.services;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.tfg.timetrackapi.dto.Last3Dto;
import org.tfg.timetrackapi.dto.TimeStampDTO;
import org.tfg.timetrackapi.dto.TimeStampDataDTO;
import org.tfg.timetrackapi.entity.TimeStamp;
import org.tfg.timetrackapi.entity.User;
import org.tfg.timetrackapi.repository.TimeStampRepository;
import org.tfg.timetrackapi.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeStampServiceImpl implements TimeStampService{

    private final TimeStampRepository timeStampRepository;
    private final UserRepository userRepository;

    private final EmailService emailService;

    public TimeStampServiceImpl(TimeStampRepository timeStampRepository, UserRepository userRepository, EmailService emailService) {
        this.timeStampRepository = timeStampRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public TimeStamp addTimeStamp(User user) {
        TimeStamp timeStamp = new TimeStamp();
        timeStamp.setTimestamp(LocalDateTime.now());
        timeStamp.setEmployee(user);
        return timeStampRepository.save(timeStamp);
    }

    @Override
    public void addTimeStampWithData(Long employeeId, LocalDateTime newTimestamp, String isMod) {
        User user = userRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + employeeId));

        TimeStamp newTimeStamp = new TimeStamp();
        newTimeStamp.setEmployee(user);
        newTimeStamp.setTimestamp(newTimestamp);
        newTimeStamp.setMod(isMod);

        timeStampRepository.save(newTimeStamp);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy 'a las' HH:mm");
        DateTimeFormatter hFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String destinatario = user.getEmail();
        String asunto = "Nuevo Registro Añadido";
        String cuerpo = "Hola, un nuevo registro ha sido añadido para el dia " + newTimeStamp.getTimestamp().format(formatter) + " por tu administrador." ;

        try {
            emailService.enviarEmail(destinatario, asunto, cuerpo);
        } catch (Exception e) {
            System.err.println("Error al enviar el correo electrónico: " + e.getMessage());
        }
    }

    @Override
    public void editTimeStampWithData(Long timeStId, LocalDateTime newTimestamp) {
        TimeStamp timeStamp = timeStampRepository.findById(timeStId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No se encontró el fichaje con ID: " + timeStId
                ));

        LocalDateTime last = timeStamp.getTimestamp();
        String destinatario = timeStamp.getEmployee().getEmail();


        timeStamp.setTimestamp(newTimestamp);
        timeStamp.setMod("true");
        timeStampRepository.save(timeStamp);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy 'a las' HH:mm");
        DateTimeFormatter hFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String asunto = "Registro actualizado";
        String cuerpo = "Hola, tu registro del día "+ last.format(formatter)  + " ha sido modificado a las " +  newTimestamp.format(hFormatter) + " por tu administrador." ;

        try {
            emailService.enviarEmail(destinatario, asunto, cuerpo);
        } catch (Exception e) {
            System.err.println("Error al enviar el correo electrónico: " + e.getMessage());
        }
    }


    public List<TimeStampDTO> getTimeStampsByEmployeeId(Long employeeId) {
        List<TimeStamp> timeStamps = timeStampRepository.findByEmployeeId(employeeId);
        return timeStamps.stream()
                .map(timeStamp -> new TimeStampDTO(
                        timeStamp.getId(),
                        timeStamp.getTimestamp(),
                        timeStamp.getEmployee().getId(),
                        timeStamp.isMod()
                ))
                .collect(Collectors.toList());
    }

    public List<TimeStampDTO> getTimeStampsByEmployeeIdAndMonth(Long employeeId, int year, int month) {
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.withDayOfMonth(start.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);

        List<TimeStamp> timeStamps = timeStampRepository.findByEmployeeIdAndTimestampBetween(employeeId, start, end);

        return timeStamps.stream()
                .map(timeStamp -> new TimeStampDTO(
                        timeStamp.getId(),
                        timeStamp.getTimestamp(),
                        timeStamp.getEmployee().getId(),
                        timeStamp.isMod()
                ))
                .sorted(Comparator.comparing(TimeStampDTO::getTimestamp))  // Orden ascendente
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRecord(Long id) {
        TimeStamp timeStamp = timeStampRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No se encontró el fichaje con ID: " + id
                ));

        LocalDateTime last = timeStamp.getTimestamp();
        String destinatario = timeStamp.getEmployee().getEmail();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy 'a las' HH:mm");
        DateTimeFormatter hFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String asunto = "Registro eliminado";
        String cuerpo = "Hola, tu registro del día " + last.format(formatter) + " ha sido eliminado por tu administrador.";

        try {
            emailService.enviarEmail(destinatario, asunto, cuerpo);
        } catch (Exception e) {
            System.err.println("Error al enviar el correo electrónico: " + e.getMessage());
        }
        timeStampRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByEmployeeId(Long employeeId) {
        timeStampRepository.deleteAllByEmployee_Id(employeeId);
    }

    @Override
    public List<Last3Dto> getLastThreeTimestamps() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        return timeStampRepository.findTop3ByOrderByIdDesc().stream()
                .map(t -> new Last3Dto(
                        t.getId(),
                        t.getTimestamp().format(formatter),
                        t.getEmployee().getDni()
                ))
                .collect(Collectors.toList());
    }
}
