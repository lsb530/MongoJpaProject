package com.example.mongojpapractice.security.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class SecurityConig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder;
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("user")
            .password(passwordEncoder.encode("password"))
            .roles("USER")
            .build();

        UserDetails admin = User.withUsername("admin")
            .password(passwordEncoder.encode("admin"))
            .roles("USER", "ADMIN")
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .csrf().disable()
            .cors().configurationSource(corsConfigurationSource()).and()
            .formLogin().disable()
            .logout().disable();

        // Common
        http.authorizeRequests()
            .antMatchers("/error").permitAll()
            .antMatchers("/api/auth/**").permitAll()
            .antMatchers("/api/mflix/users").anonymous()
            .antMatchers(("/api/test**/**")).permitAll()
            .anyRequest().authenticated();

        // GOWID ADMIN url: /api/v2/gowid/ (*ROLE_SUPER*, *ROLE_ADMIN*, ROLE_GOWID)
        // 나중에 ADMIN 은 제거.
//        http.authorizeRequests()
//            .antMatchers("/api/v2/gowid/login/**").permitAll()
//            .antMatchers("/api/v2/gowid/**").hasAnyRole("GOWID");

        // Shiftee
//        http.authorizeRequests()
//            .antMatchers("/api/v2/shiftee/**").permitAll()
//            .antMatchers("/api/v2/login-with-shiftee/**").permitAll();

//        http.authorizeRequests()
//            .antMatchers("/api/v2/logout", "/api/v2/passwd-check", "/api/v2/passwd", "/api/v2/user/**").hasAnyRole("TEMPUSER")
//            .antMatchers("/api/v2/certificate/**", "/api/v2/register-*", "/api/v2/organization/**",
//                "/api/v2/business/**", "/api/v2/statistics/**", "/api/v2/resource/**",
//                "/api/v2/partnership/**", "/api/v2/payee/**", "/api/v2/payroll/**",
//                "/api/v2/document/**")
//            .hasAnyRole("USER")
//            .antMatchers("/api/v2/**").hasAnyRole("ADMIN")
//            .anyRequest().authenticated();

        http.exceptionHandling()
            .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        return http.build();
    }

    /**
     * 세션 데이터를 헤더의 X-AUTH-TOKEN 형식으로 반환
     *
     * @return HeaderHttpSessionIdResolver
     */
    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return new CustomHttpSessionStrategy("X-Auth-Token");
    }

    /**
     * 인증 관리 Bean
     *
     * @return AuthenticationManager
     * @throws Exception Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addExposedHeader("X-Auth-Token");
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
