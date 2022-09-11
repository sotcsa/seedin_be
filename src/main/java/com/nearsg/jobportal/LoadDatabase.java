package com.nearsg.jobportal;

import com.nearsg.jobportal.domain.Badge;
import com.nearsg.jobportal.domain.Event;
import com.nearsg.jobportal.domain.User;
import com.nearsg.jobportal.jpa.BadgeRepository;
import com.nearsg.jobportal.jpa.EventRepository;
import com.nearsg.jobportal.jpa.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository repository,
                                   EventRepository eventRepository,
                                   BadgeRepository badgeRepository) {
        return args -> {
            User user = new User("Near", "Hacker");
            user.setId(1L);
            user.setNickName("ice");
            user.setEthAddress("0x000000000001");
            user.setAboutMe("Web3 Hacker");
            log.info("Created new User: " + repository.save(user));

            Event event = new Event(1L, "NearCon Event", "This event loaded from startup script", null, null);
            log.info("Created new Event: " + eventRepository.save(event));

            Badge badge = new Badge(1L, "Participated in NearCon", event.getId(), user.getId());
            log.info("Created new Badge: " + badgeRepository.save(badge));
        };
    }

}