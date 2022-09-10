package com.nearsg.jobportal.jpa;

import com.nearsg.jobportal.domain.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
}
