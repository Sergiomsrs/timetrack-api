package org.tfg.timetrackapi.services;


import org.springframework.stereotype.Service;
import org.tfg.timetrackapi.dto.EmployeeScheduleDTO;
import org.tfg.timetrackapi.entity.EmployeeSchedule;
import org.tfg.timetrackapi.entity.User;
import org.tfg.timetrackapi.repository.EmployeeScheduleRepository;
import org.tfg.timetrackapi.repository.TimeStampRepository;
import org.tfg.timetrackapi.repository.UserRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class EmployeeScheduleImpl implements EmployeeScheduleService{

    private final EmployeeScheduleRepository employeeScheduleRepository;

    private final UserRepository userRepository;

    private final TimeStampRepository timeStampRepository;

    private final EmailService emailService;

    public EmployeeScheduleImpl(EmployeeScheduleRepository employeeScheduleRepository, UserRepository userRepository, TimeStampRepository timeStampRepository, EmailService emailService) {
        this.employeeScheduleRepository = employeeScheduleRepository;
        this.userRepository = userRepository;
        this.timeStampRepository = timeStampRepository;
        this.emailService = emailService;
    }


    @Override
    public void guardarHorario(EmployeeScheduleDTO dto) {

        User user = userRepository.findByDni(dto.getDni())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        EmployeeSchedule horario = new EmployeeSchedule();
        horario.setUser(user);
        horario.setHora(dto.getHora());
        horario.setDia(dto.getDia());

        employeeScheduleRepository.save(horario);

    }

    @Override
    public void verificarFichajes() {

        LocalDate hoy = LocalDate.now();
        DayOfWeek dia = hoy.getDayOfWeek();
        LocalTime ahora = LocalTime.now();

        List<User> usuarios = userRepository.findAll();

        for (User user : usuarios) {

            if (user.getFechaAlta() != null && hoy.isBefore(user.getFechaAlta())) {
                continue;
            }
            if (user.getFechaBaja() != null && hoy.isAfter(user.getFechaBaja())) {
                continue;
            }

            List<EmployeeSchedule> horarios = employeeScheduleRepository.findByUserIdAndDia(user.getId(), dia);

            for (EmployeeSchedule horario : horarios) {
                // Saltar si la hora teórica aún no ha llegado
                if (horario.getHora().isAfter(ahora)) {
                    continue;
                }

                LocalDateTime desde = LocalDateTime.of(hoy, horario.getHora().minusMinutes(30));
                LocalDateTime hasta = LocalDateTime.of(hoy, horario.getHora().plusMinutes(30));

                boolean fichajeHecho = timeStampRepository.existsByEmployeeIdAndTimestampBetween(
                        user.getId(), desde, hasta
                );

                String destinatario = user.getEmail();
                String asunto = "Ausencia detectada";
                String cuerpo = "Hola, se ha detectado una falta de fichaje a las " + horario.getHora();
                String cuerpo2 = "Hola, se ha detectado una falta de fichaje del empleado " + user.getName()+  " "+ user.getLastName()  + " a las " + horario.getHora();

                if (!fichajeHecho) {
                    try {
                        emailService.enviarEmail(destinatario, asunto, cuerpo);
                        emailService.enviarEmail("admin@correo.com", asunto, cuerpo2);
                    } catch (Exception e) {
                        System.err.println("Error al enviar el correo electrónico: " + e.getMessage());
                    }
                }
            }
        }
}

}
