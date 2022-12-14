package com.nearsg.jobportal.endpoint;

import com.nearsg.jobportal.domain.Badge;
import com.nearsg.jobportal.domain.Event;
import com.nearsg.jobportal.domain.User;
import com.nearsg.jobportal.exception.DataNotFound;
import com.nearsg.jobportal.jpa.BadgeRepository;
import com.nearsg.jobportal.jpa.EventRepository;
import com.nearsg.jobportal.jpa.UserRepository;
import com.nearsg.jobportal.model.EventRequest;
import com.nearsg.jobportal.util.EndpointUtil;
import io.swagger.v3.oas.annotations.Operation;
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
     *
     * @param eventRepository eventRepository
     * @param badgeRepository badgeRepository
     * @param userRepository userRepository
     */
    public EventEndpoint(EventRepository eventRepository, BadgeRepository badgeRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.badgeRepository = badgeRepository;
        this.userRepository = userRepository;
    }

    /**
     *
     * @param eventRequest
     * @return Event
     */
    @Operation(summary = "Saves event details")
    @PostMapping()
    public Event save(@RequestBody EventRequest eventRequest) {
        logger.debug("Save a new event with name {}", eventRequest.getName());
        Event event = new Event(eventRequest.getName(),
                eventRequest.getDescription(),
                eventRequest.getStartDate(),
                eventRequest.getExpiryDate());
        return eventRepository.save(event);
    }

    /**
     * Gets users who claimed badge for an event.
     *
     * @param eventId eventId
     * @return list of users
     */
    @Operation(summary = "Gets users who claimed badge for an event")
    @GetMapping("{eventId}/users")
    public List<User> getUsersForEvent(@PathVariable Long eventId) {
        logger.debug("Get users who has claimed the Badge from an event[{}]", eventId);
        return eventRepository.findUsersByEventId(eventId);
    }

    /**
     * Creates a {@link com.nearsg.jobportal.domain.Badge)} with user who is
     * logged-in, and witht eventId from teh parameter.
     *
     * @param eventId eventId
     * @return Badge
     */
    @Operation(summary = "Claims badge for an event (by logged-in user)")
    @PostMapping("{eventId}/user")
    public Badge saveUserClaimForEvent(@PathVariable Long eventId) {
        logger.debug("Create user badge for an event[{}]", eventId);
        String address = EndpointUtil.getLoggedInAddress();
        User user = userRepository.findByEthAddress(address);
        Badge badge = new Badge("", eventId, user.getId());
        return badgeRepository.save(badge);
    }

    /**
     * It's helper method for admins
     * Creates a {@link com.nearsg.jobportal.domain.Badge)} with user and event from the parameters
     * UIID of event used in parameter, because the sent event link contains only this.
     *
     * @param eventUUID uuid of event
     * @param address wallet address of user
     * @return Badge
     */
    @Operation(summary = "Helper call for admins to claim badge with wallet address for an event")
    @PostMapping("{eventUUID}/user/{address}")
    public Badge saveUserClaimForEvent(@PathVariable String eventUUID, @PathVariable String address) {
        logger.debug("Create user badge with address '{}' for an event '{}'", address, eventUUID);
        User user = userRepository.findByEthAddress(address);
        Event event = eventRepository.findByUuid(eventUUID);
        Badge badge = new Badge("", event.getId(), user.getId());
        return badgeRepository.save(badge);
    }

    /**
     * Gets all events.
     *
     * @return list of events
     */
    @Operation(summary = "Gets events")
    @GetMapping("all")
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    /**
     * Retrieve an event
     *
     * @return an events
     */
    @Operation(summary = "Gets event details")
    @GetMapping("{eventId}")
    public Event getEvent(@PathVariable Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(DataNotFound::new);
    }

}
