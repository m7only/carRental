package m7.only.carrental.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

    public WebSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Конфигурирование эндпоинтов и правил доступа к ним
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/",
                                "/registration/**",
                                "/prices",
                                "/cars/**",
                                "/about",
                                "/terms",
                                "/css/**",
                                "/images/**",
                                "/js/**",
                                "/accessDenied"
                        )
                        .permitAll()
                        .requestMatchers("/account/**", "/order/**")
                        .authenticated()
                        .requestMatchers("/admin/**")
                        .hasAnyRole("MANAGER", "ADMIN")
                        .anyRequest().denyAll()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) -> logout
                        .permitAll()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                ).exceptionHandling().accessDeniedPage("/accessDenied");

        return http.build();
    }

    /**
     * Конфигурирование аутентификации на базе {@code DetailsService}. Шифрование не настроено.
     */
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}