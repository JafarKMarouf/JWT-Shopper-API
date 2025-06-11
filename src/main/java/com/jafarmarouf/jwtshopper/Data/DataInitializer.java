package com.jafarmarouf.jwtshopper.Data;

import com.jafarmarouf.jwtshopper.models.User;
import com.jafarmarouf.jwtshopper.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;

    /**
     * @param event ApplicationReadyEvent
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        createDefaultUserIfNotExists();
    }

    public void createDefaultUserIfNotExists() {
        for (int i = 1; i <= 5; i++) {
            String defaultEmail = "email" + i + "@gmail.com";
            if (userRepository.existsUserByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setEmail(defaultEmail);
            user.setPassword("password" + i);
            user.setFirstName("first" + i);
            user.setLastName("last" + i);
            userRepository.save(user);
        }
    }

    /**
     * @return boolean
     */
    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
