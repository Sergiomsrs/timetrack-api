package org.tfg.timetrackapi.services;

import org.springframework.stereotype.Service;
import org.tfg.timetrackapi.dto.AbsenceDTO;
import org.tfg.timetrackapi.entity.AbsenceNotification;
import org.tfg.timetrackapi.repository.AbsenceNotificationRepository;
import org.tfg.timetrackapi.repository.UserRepository;

import java.util.List;

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

            AbsenceDTO dto = new AbsenceDTO();
            dto.setDia(schedule.getDia());
            dto.setFecha(schedule.getFecha());
            dto.setHora(schedule.getHora());
            dto.setEnviadoEn(schedule.getEnviadoEn());
            dto.setUserId(schedule.getUser().getId());
            dto.setNombre(schedule.getUser().getName());
            dto.setApellido(schedule.getUser().getLastName());

            return dto;
        }).toList();
    }

    @Override
    public void deleteByUserId(Long id) {
        absenceNotificationRepository.deleteByUserId(id);
    }
}
