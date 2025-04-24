package org.tfg.timetrackapi.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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
    public void addTimeStampWithData(Long employeeId, LocalDateTime newTimestamp) {
        User user = userRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + employeeId));

        TimeStamp newTimeStamp = new TimeStamp();
        newTimeStamp.setEmployee(user);
        newTimeStamp.setTimestamp(newTimestamp);

        timeStampRepository.save(newTimeStamp);
    }

    @Override
    public void editTimeStampWithData(Long timeStId, LocalDateTime newTimestamp) {
        TimeStamp timeStamp = timeStampRepository.findById(timeStId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No se encontró el fichaje con ID: " + timeStId
                ));

        timeStamp.setTimestamp(newTimestamp);
        timeStampRepository.save(timeStamp);
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

    @Override
    public void deleteRecord(Long id) {
        timeStampRepository.deleteById(id);
    }
}
