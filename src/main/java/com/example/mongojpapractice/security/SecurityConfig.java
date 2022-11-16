//package com.example.mongojpapractice.security;
//
//import com.aibizon.aitax.backbone.security.core.CustomAuthenticationEntryPoint;
//import com.aibizon.aitax.backbone.security.core.CustomHttpSessionStrategy;
//import java.util.Arrays;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
//import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
//import org.springframework.security.access.hierarchicalroles.RoleHierarchyUtils;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.session.web.http.HttpSessionIdResolver;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//
//@RequiredArgsConstructor
//@EnableWebSecurity
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.httpBasic().disable()
//            .csrf().disable()
//            .cors().configurationSource(corsConfigurationSource()).and()
//            .formLogin().disable()
//            .logout().disable();
//
//        // Common
//        http.authorizeRequests()
//            .antMatchers("/api/v2/143d85801cf356fb/health").permitAll()
//            .antMatchers("/api/v2/register").permitAll()
//            .antMatchers("/api/v2/login/**").permitAll()
//            .antMatchers("/api/v2/login-with-gowid/**").permitAll()
//            .antMatchers("/api/v2/legacy-login").permitAll()
//            .antMatchers("/api/v2/code/**").permitAll()
//            .antMatchers("/api/v2/find-passwd").permitAll()
//            .antMatchers("/error").permitAll();
//
//        // GOWID ADMIN url: /api/v2/gowid/ (*ROLE_SUPER*, *ROLE_ADMIN*, ROLE_GOWID)
//        // 나중에 ADMIN 은 제거.
//        http.authorizeRequests()
//            .antMatchers("/api/v2/gowid/login/**").permitAll()
//            .antMatchers("/api/v2/gowid/**").hasAnyRole("GOWID");
//
//        // Shiftee
//        http.authorizeRequests()
//            .antMatchers("/api/v2/shiftee/**").permitAll()
//            .antMatchers("/api/v2/login-with-shiftee/**").permitAll();
//
//        http.authorizeRequests()
//            .antMatchers("/api/v2/logout", "/api/v2/passwd-check", "/api/v2/passwd", "/api/v2/user/**").hasAnyRole("TEMPUSER")
//            .antMatchers("/api/v2/certificate/**", "/api/v2/register-*", "/api/v2/organization/**",
//                "/api/v2/business/**", "/api/v2/statistics/**", "/api/v2/resource/**",
//                "/api/v2/partnership/**", "/api/v2/payee/**", "/api/v2/payroll/**",
//                "/api/v2/document/**")
//            .hasAnyRole("USER")
//            .antMatchers("/api/v2/**").hasAnyRole("ADMIN")
//            .anyRequest().authenticated();
//
//        http.exceptionHandling()
//            .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
//
//        return http.build();
//    }
//
//    /**
//     *  Role Hierarchy 설정 Bean
//     *
//     *  SUPER > ADMIN > USER > TEMPUSER
//     *        > GOWID
//     *        > SHIFTEE
//     *
//     * @return RoleHierarchy
//     */
//    @Bean
//    public RoleHierarchy roleHierarchy(){
//        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//
//        Map<String, List<String>> roleHierarchyMap = new LinkedHashMap<>();
//        roleHierarchyMap.put("ROLE_SUPER", Arrays.asList("ROLE_GOWID", "ROLE_ADMIN", "ROLE_SHIFTEE"));
//        roleHierarchyMap.put("ROLE_ADMIN", List.of("ROLE_USER"));
//        roleHierarchyMap.put("ROLE_USER", List.of("ROLE_TEMPUSER"));
//
//        String roles = RoleHierarchyUtils.roleHierarchyFromMap(roleHierarchyMap);
//
//        roleHierarchy.setHierarchy(roles);
//
//        return roleHierarchy;
//    }
//
//    /**
//     * 세션 데이터를 헤더의 X-AUTH-TOKEN 형식으로 반환
//     *
//     * @return HeaderHttpSessionIdResolver
//     */
//    @Bean
//    public HttpSessionIdResolver httpSessionIdResolver() {
//        return new CustomHttpSessionStrategy("X-Auth-Token");
//    }
//
//    /**
//     * 패스워드 평문 인코더
//     *
//     * @return org.springframework.security.crypto.password.PasswordEncoder
//     */
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }
//
//    /**
//     * 인증 관리 Bean
//     *
//     * @return AuthenticationManager
//     * @throws Exception Exception
//     */
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//    /**
//     * CORS 설정
//     *
//     * @return CorsConfigurationSource
//     */
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//
//        configuration.addExposedHeader("X-Auth-Token");
//        configuration.addAllowedOriginPattern("*");
//        configuration.addAllowedHeader("*");
//        configuration.addAllowedMethod("*");
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//
//}
