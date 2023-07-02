package com.zerobase.hseungho.restaurantreservation.global.config;

import com.zerobase.hseungho.restaurantreservation.global.constants.PermitApiConstants;
import com.zerobase.hseungho.restaurantreservation.global.security.CustomAccessDeniedHandler;
import com.zerobase.hseungho.restaurantreservation.global.security.jwt.JwtAuthenticationFailureFilter;
import com.zerobase.hseungho.restaurantreservation.global.security.jwt.JwtAuthenticationFilter;
import com.zerobase.hseungho.restaurantreservation.global.security.jwt.JwtComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtComponent jwtComponent;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PermitApiConstants.PERMIT_APIS_TO_ARRAY);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic()
                .and()
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().frameOptions().disable()
                .and()
                .authorizeHttpRequests(request ->
                        request.requestMatchers(PermitApiConstants.PERMIT_APIS_TO_ARRAY).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtComponent), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthenticationFailureFilter(), JwtAuthenticationFilter.class)
                .logout(Customizer.withDefaults())
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD","POST","GET","DELETE","PUT","PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
