package org.hankyu.simpleboard_v1.config;

import lombok.RequiredArgsConstructor;
import org.hankyu.simpleboard_v1.config.auth.CustomAccessDeniedHandler;
import org.hankyu.simpleboard_v1.config.auth.CustomAuthenticationEntryPoint;
import org.hankyu.simpleboard_v1.config.auth.CustomOAuth2UserService;
import org.hankyu.simpleboard_v1.config.auth.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(encoder());
    }

    /* static 관련설정은 무시 */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring().antMatchers( "/css/**", "/js/**", "/img/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/image/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/signUp", "/loginProc").permitAll()
                    .antMatchers(HttpMethod.DELETE, "/member/{id}/**").authenticated()
                    .antMatchers(HttpMethod.POST, "/categories/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.DELETE, "/categories/**").hasRole("ADMIN")
                    .antMatchers(HttpMethod.GET, "/**").permitAll()
                    .anyRequest().hasAnyRole("ADMIN")
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/")
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                .and()
                    .oauth2Login()
                    .userInfoEndpoint()
                    .userService(customOAuth2UserService)
                .and()

                .and()
                    .exceptionHandling()
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                    .exceptionHandling()
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
    }
}
