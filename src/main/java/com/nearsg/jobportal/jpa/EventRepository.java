package com.nearsg.jobportal.jpa;

import com.nearsg.jobportal.domain.Event;
import com.nearsg.jobportal.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select u from Badge b left join User u on b.userId = u.id where b.eventId = ?1 ")
    List<User> findUsersByEventId(Long eventId);

    Event findByUuid(String uuid);
}
