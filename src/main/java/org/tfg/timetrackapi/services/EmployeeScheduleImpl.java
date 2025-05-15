package org.tfg.timetrackapi.services;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.tfg.timetrackapi.dto.EmployeeScheduleDTO;
import org.tfg.timetrackapi.entity.AbsenceNotification;
import org.tfg.timetrackapi.entity.EmployeeSchedule;
import org.tfg.timetrackapi.entity.User;
import org.tfg.timetrackapi.repository.AbsenceNotificationRepository;
import org.tfg.timetrackapi.repository.EmployeeScheduleRepository;
import org.tfg.timetrackapi.repository.TimeStampRepository;
import org.tfg.timetrackapi.repository.UserRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeScheduleImpl implements EmployeeScheduleService{

    private final EmployeeScheduleRepository employeeScheduleRepository;

    private final UserRepository userRepository;

    private final TimeStampRepository timeStampRepository;

    private final EmailService emailService;

    private final AbsenceNotificationRepository absenceNotificationRepository;

    public EmployeeScheduleImpl(EmployeeScheduleRepository employeeScheduleRepository, UserRepository userRepository, TimeStampRepository timeStampRepository, EmailService emailService, AbsenceNotificationRepository absenceNotificationRepository) {
        this.employeeScheduleRepository = employeeScheduleRepository;
        this.userRepository = userRepository;
        this.timeStampRepository = timeStampRepository;
        this.emailService = emailService;
        this.absenceNotificationRepository = absenceNotificationRepository;
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
    public void delete(Long id) {
        try {
            employeeScheduleRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {

            System.out.println("No se encontró ningún registro con ID: " + id);
        } catch (Exception e) {

            System.out.println("Error al eliminar el registro con ID: " + id);
            e.printStackTrace();
        }
    }


    @Override
    public EmployeeScheduleDTO update(Long id, EmployeeScheduleDTO dto) {
        Optional<EmployeeSchedule> optionalSchedule = employeeScheduleRepository.findById(id);

        if (optionalSchedule.isPresent()) {
            EmployeeSchedule schedule = optionalSchedule.get();

            // Buscar el usuario por DNI
            Optional<User> optionalUser = userRepository.findByDni(dto.getDni());

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                // Actualizar campos
                schedule.setHora(dto.getHora());
                schedule.setDia(dto.getDia());
                schedule.setUser(user);

                // Guardar cambios
                EmployeeSchedule updated = employeeScheduleRepository.save(schedule);

                // Devolver DTO actualizado
                EmployeeScheduleDTO result = new EmployeeScheduleDTO();
                result.setHora(updated.getHora());
                result.setDia(updated.getDia());
                result.setDni(updated.getUser().getDni());
                return result;
            } else {
                System.out.println("No se encontró ningún usuario con DNI: " + dto.getDni());
                return null;
            }

        } else {
            System.out.println("No se encontró ningún horario con ID: " + id);
            return null;
        }
    }

    @Scheduled(cron = "0 0 8-20/2 * * MON-FRI", zone = "Europe/Madrid")
    //@Scheduled(cron = "0 * * * * MON-FRI", zone = "Europe/Madrid")
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

                if (!fichajeHecho) {
                    boolean notificado = absenceNotificationRepository
                            .existsByUserIdAndFechaAndHora(user.getId(), hoy, horario.getHora());

                    if (!notificado) {


                        String destinatario = user.getEmail();
                        String asunto = "Ausencia detectada";
                        String cuerpo = "Hola, se ha detectado una falta de fichaje a las " + horario.getHora();
                        String cuerpo2 = "Hola, se ha detectado una falta de fichaje del empleado " + user.getName()+  " "+ user.getLastName()  + " a las " + horario.getHora();


                            try {
                                emailService.enviarEmail(destinatario, asunto, cuerpo);
                                emailService.enviarEmail("admin@correo.com", asunto, cuerpo2);
                            } catch (Exception e) {
                                System.err.println("Error al enviar el correo electrónico: " + e.getMessage());
                            }

                        // Guardar notificación para evitar duplicados
                        AbsenceNotification notificacion = new AbsenceNotification();
                        notificacion.setUserId(user.getId());
                        notificacion.setFecha(hoy);
                        notificacion.setDia(dia);
                        notificacion.setHora(horario.getHora());
                        notificacion.setEnviadoEn(LocalDateTime.now());

                        absenceNotificationRepository.save(notificacion);
                    }


                }


                }
            }
        }
}


