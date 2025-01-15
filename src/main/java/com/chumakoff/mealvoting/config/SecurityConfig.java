package com.chumakoff.mealvoting.config;

import com.chumakoff.mealvoting.config.security.AuthUser;
import com.chumakoff.mealvoting.model.User;
import com.chumakoff.mealvoting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
        return security.authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("api/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                )
                .sessionManagement((session) ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(frameOptionsConfig -> {
                    frameOptionsConfig.disable();
                    frameOptionsConfig.sameOrigin();
                }))
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return login -> {
            Optional<User> optionalUser = userRepository.findByLoginIgnoreCase(login);
            User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found: " + login));

            return new AuthUser(user);
        };
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
