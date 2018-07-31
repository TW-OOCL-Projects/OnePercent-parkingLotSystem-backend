package com.onepercent.ParkingLotApplication.repository;


import com.onepercent.ParkingLotApplication.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by linyuan on 2017/12/8.
 */

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByName(String name);
}
