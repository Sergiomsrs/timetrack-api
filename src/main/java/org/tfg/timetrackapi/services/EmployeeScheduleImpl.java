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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeScheduleImpl implements EmployeeScheduleService {

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

    /* Revisar cada dos horas*/
    @Scheduled(cron = "0 0 */1 * * *", zone = "Europe/Madrid")
    /* Revisar cada minuto*/
    // @Scheduled(cron = "0 * * * * *", zone = "Europe/Madrid")
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
                if (horario.getHora() == null || horario.getHora().isAfter(ahora)) {
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
                        String cuerpo2 = "Hola, se ha detectado una falta de fichaje del empleado " + user.getName() + " " + user.getLastName() + " a las " + horario.getHora();


                        try {
                            emailService.enviarEmail(destinatario, asunto, cuerpo);
                            emailService.enviarEmail("admin@correo.com", asunto, cuerpo2);
                        } catch (Exception e) {
                            System.err.println("Error al enviar el correo electrónico: " + e.getMessage());
                        }

                        // Guardar notificación para evitar duplicados
                        AbsenceNotification notificacion = new AbsenceNotification();
                        notificacion.setUser(user);
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

    @Override
    public List<EmployeeScheduleDTO> getSchedulesByUserId(Long userId) {
        return employeeScheduleRepository.findAllByUserId(userId)
                .stream()
                .map(schedule -> {
                    EmployeeScheduleDTO dto = new EmployeeScheduleDTO();
                    dto.setDni(schedule.getUser().getDni());
                    dto.setHora(schedule.getHora());
                    dto.setDia(schedule.getDia());
                    dto.setDayNumber(schedule.getDia().getValue());
                    dto.setId(schedule.getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeScheduleDTO> updateAll(List<EmployeeScheduleDTO> dtoList) {
        List<EmployeeScheduleDTO> updatedList = new ArrayList<>();

        for (EmployeeScheduleDTO dto : dtoList) {
            if (dto.getId() == null) {
                System.out.println("Se ignoró un DTO sin ID: " + dto);
                continue;
            }

            Optional<EmployeeSchedule> optionalSchedule = employeeScheduleRepository.findById(dto.getId());

            if (optionalSchedule.isPresent()) {
                EmployeeSchedule schedule = optionalSchedule.get();

                // Si la hora es null, se deja como null
                schedule.setHora(dto.getHora());
                employeeScheduleRepository.save(schedule);

                // Devolver DTO actualizado
                EmployeeScheduleDTO updatedDTO = new EmployeeScheduleDTO();
                updatedDTO.setId(schedule.getId());
                updatedDTO.setHora(schedule.getHora());
                updatedDTO.setDia(schedule.getDia());
                updatedDTO.setDni(schedule.getUser().getDni());
                updatedDTO.setDayNumber(dto.getDayNumber());

                updatedList.add(updatedDTO);
            } else {
                System.out.println("No se encontró el horario con ID: " + dto.getId());
            }
        }

        return updatedList;
    }

    @Override
    public void guardarHorarioPorDefecto(String dni) {
        User user = userRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (employeeScheduleRepository.existsByUser(user)) {
            // Si ya tiene horarios no se hace nada
            return;
        }

        List<EmployeeSchedule> horariosPorDefecto = Arrays.asList(
                // Turno generico de mañana
                new EmployeeSchedule(null, LocalTime.of(8, 0), DayOfWeek.MONDAY, user),
                new EmployeeSchedule(null, LocalTime.of(8, 0), DayOfWeek.TUESDAY, user),
                new EmployeeSchedule(null, LocalTime.of(8, 0), DayOfWeek.WEDNESDAY, user),
                new EmployeeSchedule(null, LocalTime.of(8, 0), DayOfWeek.THURSDAY, user),
                new EmployeeSchedule(null, LocalTime.of(8, 0), DayOfWeek.FRIDAY, user),
                new EmployeeSchedule(null, null, DayOfWeek.SATURDAY, user),
                new EmployeeSchedule(null, null, DayOfWeek.SUNDAY, user),

                // Turno generico tarde
                new EmployeeSchedule(null, LocalTime.of(16, 0), DayOfWeek.MONDAY, user),
                new EmployeeSchedule(null, LocalTime.of(16, 0), DayOfWeek.TUESDAY, user),
                new EmployeeSchedule(null, LocalTime.of(16, 0), DayOfWeek.WEDNESDAY, user),
                new EmployeeSchedule(null, LocalTime.of(16, 0), DayOfWeek.THURSDAY, user),
                new EmployeeSchedule(null, LocalTime.of(16, 0), DayOfWeek.FRIDAY, user),
                new EmployeeSchedule(null, null, DayOfWeek.SATURDAY, user),
                new EmployeeSchedule(null, null, DayOfWeek.SUNDAY, user)
        );

        employeeScheduleRepository.saveAll(horariosPorDefecto);
    }


}


