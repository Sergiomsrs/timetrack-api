package org.tfg.timetrackapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tfg.timetrackapi.model.User;

public interface UserRepository extends JpaRepository<User, Long> {


}
