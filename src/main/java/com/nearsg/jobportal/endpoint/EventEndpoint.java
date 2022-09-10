package com.nearsg.jobportal.endpoint;

import com.nearsg.jobportal.domain.Event;
import com.nearsg.jobportal.domain.User;
import com.nearsg.jobportal.jpa.EventRepository;
import com.nearsg.jobportal.jpa.UserRepository;
import com.nearsg.jobportal.model.EventRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventEndpoint {

    Logger logger = LoggerFactory.getLogger(AuthEndpoint.class);
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    /**
     * Constructor.
     *
     * @param eventRepository eventRepository
     * @param userRepository userRepository
     */
    public EventEndpoint(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @PostMapping()
    public Event save(@RequestBody EventRequest eventRequest) {
        logger.debug("Save a new event with name {}", eventRequest.getName());
//        org.springframework.security.core.userdetails.User userDetails =
//                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String address = userDetails.getUsername();
//        User user;
//        if (EthUtil.isEthAddress(address)) {
//            user = userRepository.findByEthAddress(address);
//        } else {
//            user = userRepository.findByNearAddress(address);
//        }
        Event event = new Event(eventRequest.getName(), eventRequest.getDescription());
        return eventRepository.save(event);
    }

    @GetMapping("{eventId}/users")
    public List<User> getUsersForEvent(@PathVariable String eventId) {
        logger.debug("Get users who has claimed the Badge from an event[{}]", eventId);
        return eventRepository.findUsersByEventId(eventId);
    }

    /**
     * TODO remove this endpoint, only for debugging purpose
     * @return all events
     */
    @GetMapping("/events")
    public List<Event> getEvents() {
        return eventRepository.findAll();
    }
}
