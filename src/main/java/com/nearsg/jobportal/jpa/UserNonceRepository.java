package com.nearsg.jobportal.jpa;

import com.nearsg.jobportal.domain.UserNonce;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNonceRepository extends JpaRepository<UserNonce, String> {
}
