package com.nearsg.jobportal;

import com.nearsg.jobportal.domain.User;
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
    CommandLineRunner initDatabase(UserRepository repository) {
        return args -> {
            User u = new User("Hope", "Baker");
            u.setNickName("hope");
            u.setEthAddress("0x000000000001");
            u.setAboutMe("Chemical equipment operator");
            log.info("Preloading " + repository.save(u));
        };
    }

}