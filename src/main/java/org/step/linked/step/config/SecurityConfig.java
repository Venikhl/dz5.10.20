package org.step.linked.step.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${admin.username}")
    private String username;
    @Value("${admin.password}")
    private String password;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser(username)
                .password(passwordEncoder.encode(password))
                .roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests() // все запросы должны быть авторизованы (нет анонимности)
//                .antMatchers(HttpMethod.GET, "/api/v1/profiles/public").permitAll() // дать разрешение всем
                .antMatchers("/api/v1/public/**")
                .permitAll() // дать разрешение любым урлам после /api/v1/public/
                .anyRequest() // все запросы
                .authenticated() // должны быть аутентифицированы
                .and() // и
                .formLogin() // базовая страница логина спринга
                .and() // и
                .logout() // логаут /logout
                .and() // и
                .httpBasic(); // http 1.1
    }
}
