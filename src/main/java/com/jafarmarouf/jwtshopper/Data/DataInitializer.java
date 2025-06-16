package com.jafarmarouf.jwtshopper.Data;

import com.jafarmarouf.jwtshopper.models.Role;
import com.jafarmarouf.jwtshopper.models.User;
import com.jafarmarouf.jwtshopper.repository.RolesRepository;
import com.jafarmarouf.jwtshopper.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Transactional
@RequiredArgsConstructor
@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * @param event ApplicationReadyEvent
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Set<String> roles = Set.of("ROLE_ADMIN", "ROLE_CUSTOMER");
        createDefaultRoleIfNotExists(roles);
        createDefaultCustomerIfNotExists();
        createDefaultAdminIfNotExists();
    }

    private void createDefaultRoleIfNotExists(Set<String> roles) {
        roles.stream()
                .filter(role -> rolesRepository.findByName(role).isEmpty())
                .map(Role::new)
                .forEach(rolesRepository::save);
    }

    private void createDefaultCustomerIfNotExists() {
        Role role = rolesRepository.findByName("ROLE_CUSTOMER").get();
        for (int i = 1; i <= 5; i++) {
            String defaultEmail = "customer" + i + "@gmail.com";
            if (userRepository.existsUserByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setFirstName("Customer" + i);
            user.setLastName("Customer" + i);
            user.setCreatedAt(LocalDateTime.now());
            user.setRoles(Set.of(role));
            userRepository.save(user);
            System.out.println("Default Customer: " + user + " created Successfully");
        }
    }

    private void createDefaultAdminIfNotExists() {
        Role role = rolesRepository.findByName("ROLE_ADMIN").get();
        for (int i = 1; i <= 2; i++) {
            String defaultEmail = "admin" + i + "@gmail.com";
            if (userRepository.existsUserByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setFirstName("Admin" + i);
            user.setLastName("Admin" + i);
            user.setCreatedAt(LocalDateTime.now());
            user.setRoles(Set.of(role));
            userRepository.save(user);
            System.out.println("Default admin: " + user + " created Successfully");
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
