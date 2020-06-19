/*********************************************************************
 * The Initial Developer of the content of this file is NETCONOMY.
 * All portions of the code written by NETCONOMY are property of
 * NETCONOMY. All Rights Reserved.
 *
 * NETCONOMY Software & Consulting GmbH
 * Hilmgasse 4, A-8010 Graz (Austria)
 * FN 204360 f, Landesgericht fuer ZRS Graz
 * Tel: +43 (316) 815 544
 * Fax: +43 (316) 815544-99
 * www.netconomy.net
 *
 * (c) 2020 by NETCONOMY Software & Consulting GmbH
 *********************************************************************/

package com.ronin.sportbook.common.security;

import com.ronin.sportbook.common.security.RestAuthenticationEntryPoint;
import com.ronin.sportbook.common.security.TokenAuthenticationFilter;
import com.ronin.sportbook.user.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
            .userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .csrf()
            .disable()
            .formLogin()
            .disable()
            .httpBasic()
            .disable()
            .exceptionHandling()
            .authenticationEntryPoint(new RestAuthenticationEntryPoint())
            .and()
            .authorizeRequests()
            .antMatchers("/",
                         "/error",
                         "/favicon.ico",
                         "/**/*.png",
                         "/**/*.gif",
                         "/**/*.svg",
                         "/**/*.jpg",
                         "/**/*.html",
                         "/**/*.css",
                         "/**/*.js")
            .permitAll()
            .antMatchers("/api/auth/**")
            .permitAll()
            .anyRequest()
            .authenticated();

        // Add our custom Token based authentication filter
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
