package org.tfg.timetrackapi.services;

import org.springframework.stereotype.Service;
import org.tfg.timetrackapi.dto.TimeStampDTO;
import org.tfg.timetrackapi.dto.TimeStampDataDTO;
import org.tfg.timetrackapi.entity.TimeStamp;
import org.tfg.timetrackapi.entity.User;
import org.tfg.timetrackapi.repository.TimeStampRepository;
import org.tfg.timetrackapi.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeStampServiceImpl implements TimeStampService{

    private final TimeStampRepository timeStampRepository;
    private final UserRepository userRepository;

    public TimeStampServiceImpl(TimeStampRepository timeStampRepository, UserRepository userRepository) {
        this.timeStampRepository = timeStampRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TimeStamp addTimeStamp(User user) {
        TimeStamp timeStamp = new TimeStamp();
        timeStamp.setTimestamp(LocalDateTime.now());
        timeStamp.setEmployee(user);
        return timeStampRepository.save(timeStamp);
    }

    @Override
    public void addTimeStampWithData(Long id, LocalDateTime newTimestamp) {

        TimeStamp timeStampDataDTO = timeStampRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado con ID: " + id));

            timeStampDataDTO.setTimestamp(newTimestamp);
            timeStampRepository.save(timeStampDataDTO);


    }

    public List<TimeStampDTO> getTimeStampsByEmployeeId(Long employeeId) {
        List<TimeStamp> timeStamps = timeStampRepository.findByEmployeeId(employeeId);
        return timeStamps.stream()
                .map(timeStamp -> new TimeStampDTO(
                        timeStamp.getId(),
                        timeStamp.getTimestamp(),
                        timeStamp.getEmployee().getId()
                ))
                .collect(Collectors.toList());
    }
}
