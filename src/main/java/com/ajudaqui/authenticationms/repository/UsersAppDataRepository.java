package com.ajudaqui.authenticationms.repository;

import java.util.Optional;

import com.ajudaqui.authenticationms.entity.UsersAppData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UsersAppDataRepository extends JpaRepository<UsersAppData, Long> {

    Optional<UsersAppData> findByUsersId(Long usersId);

}
