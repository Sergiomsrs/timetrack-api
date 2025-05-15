package org.tfg.timetrackapi.services;

import org.springframework.stereotype.Service;
import org.tfg.timetrackapi.dto.AbsenceDTO;
import org.tfg.timetrackapi.dto.EmployeeScheduleDTO;
import org.tfg.timetrackapi.entity.AbsenceNotification;
import org.tfg.timetrackapi.entity.EmployeeSchedule;
import org.tfg.timetrackapi.entity.User;
import org.tfg.timetrackapi.repository.AbsenceNotificationRepository;
import org.tfg.timetrackapi.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AbsenceNotificationServiceImpl implements AbsenceNotificationService {

    private final AbsenceNotificationRepository absenceNotificationRepository;
    private final UserRepository userRepository;

    public AbsenceNotificationServiceImpl(AbsenceNotificationRepository absenceNotificationRepository, UserRepository userRepository) {
        this.absenceNotificationRepository = absenceNotificationRepository;
        this.userRepository = userRepository;
    }


    public List<AbsenceDTO> getLast10SchedulesDTO() {
        List<AbsenceNotification> schedules = absenceNotificationRepository.findTop10ByOrderByIdDesc();

        return schedules.stream().map(schedule -> {

            User user = userRepository.findById(schedule.getUserId())
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + schedule.getUserId()));


            AbsenceDTO dto = new AbsenceDTO();
            dto.setDia(schedule.getDia());
            dto.setFecha(schedule.getFecha());
            dto.setHora(schedule.getHora());
            dto.setHora(schedule.getHora());
            dto.setEnviadoEn(schedule.getEnviadoEn());
            dto.setUserId(schedule.getUserId());
            dto.setNombre(user.getName());
            dto.setApellido(user.getLastName());

            return dto;
        }).toList();
    }
}
