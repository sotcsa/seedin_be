package com.nearsg.jobportal.jpa;

import com.nearsg.jobportal.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEthAddress(String ethAddress);
    User findByNearAddress(String ethAddress);
}
