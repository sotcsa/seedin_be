package com.nearsg.jobportal.endpoint;

import com.nearsg.jobportal.domain.Badge;
import com.nearsg.jobportal.domain.Event;
import com.nearsg.jobportal.domain.User;
import com.nearsg.jobportal.jpa.BadgeRepository;
import com.nearsg.jobportal.jpa.EventRepository;
import com.nearsg.jobportal.jpa.UserRepository;
import com.nearsg.jobportal.model.EventRequest;
import com.nearsg.jobportal.util.EndpointUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventEndpoint {

    Logger logger = LoggerFactory.getLogger(AuthEndpoint.class);
    private final EventRepository eventRepository;
    private final BadgeRepository badgeRepository;
    private final UserRepository userRepository;

    /**
     * Constructor.
     * @param eventRepository eventRepository
     * @param badgeRepository badgeRepository
     * @param userRepository userRepository
     */
    public EventEndpoint(EventRepository eventRepository, BadgeRepository badgeRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.badgeRepository = badgeRepository;
        this.userRepository = userRepository;
    }

    @PostMapping()
    public Event save(@RequestBody EventRequest eventRequest) {
        logger.debug("Save a new event with name {}", eventRequest.getName());
        Event event = new Event(eventRequest.getName(), eventRequest.getDescription());
        return eventRepository.save(event);
    }

    @GetMapping("{eventId}/users")
    public List<User> getUsersForEvent(@PathVariable Long eventId) {
        logger.debug("Get users who has claimed the Badge from an event[{}]", eventId);
        return eventRepository.findUsersByEventId(eventId);
    }

    @PostMapping("{eventId}/user")
    public Badge saveUserClaimForEvent(@PathVariable Long eventId) {
        logger.debug("Create user badge for an event[{}]", eventId);
        String address = EndpointUtil.getLoggedInAddress();
        User user = userRepository.findByEthAddress(address);
        Badge badge = new Badge("", eventId, user.getId());
        return badgeRepository.save(badge);
    }

    /**
     * TODO remove this endpoint, only for debugging purpose
     * @return all events
     */
    @GetMapping("all")
    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    /**
     * TODO it's just a DUMMY implementation, pllease remove it in prod
     *
     * @param eventId
     * @param userId
     * @return
     */
    @PostMapping("create")
    public Badge createOneUserRequest(@RequestParam String name,
                                      @RequestParam Long eventId,
                                      @RequestParam Long userId) {
        Badge badge = new Badge(name, eventId, userId);
        badgeRepository.save(badge);
        return badge;
    }
}