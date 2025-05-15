package org.tfg.timetrackapi.services;


import org.springframework.stereotype.Service;
import org.tfg.timetrackapi.dto.EmployeeScheduleDTO;
import org.tfg.timetrackapi.entity.EmployeeSchedule;
import org.tfg.timetrackapi.entity.User;
import org.tfg.timetrackapi.repository.EmployeeScheduleRepository;
import org.tfg.timetrackapi.repository.UserRepository;

@Service
public class EmployeeScheduleImpl implements EmployeeScheduleService{

    private final EmployeeScheduleRepository employeeScheduleRepository;

    private final UserRepository userRepository;

    public EmployeeScheduleImpl(EmployeeScheduleRepository employeeScheduleRepository, UserRepository userRepository) {
        this.employeeScheduleRepository = employeeScheduleRepository;
        this.userRepository = userRepository;
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
}
