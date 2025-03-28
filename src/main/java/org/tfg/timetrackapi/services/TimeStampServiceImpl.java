package org.tfg.timetrackapi.services;

import org.springframework.stereotype.Service;
import org.tfg.timetrackapi.entity.TimeStamp;
import org.tfg.timetrackapi.entity.User;
import org.tfg.timetrackapi.repository.TimeStampRepository;

import java.time.LocalDateTime;

@Service
public class TimeStampServiceImpl implements TimeStampService{

    private final TimeStampRepository timeStampRepository;

    public TimeStampServiceImpl(TimeStampRepository timeStampRepository) {
        this.timeStampRepository = timeStampRepository;
    }

    @Override
    public TimeStamp addTimeStamp(User user) {
        TimeStamp timeStamp = new TimeStamp();
        timeStamp.setTimestamp(LocalDateTime.now());
        timeStamp.setEmployee(user);
        return timeStampRepository.save(timeStamp);
    }
}
