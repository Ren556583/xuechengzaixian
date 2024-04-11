package com.xuecheng.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DaoAuthenticationProviderCustom daoAuthenticationProviderCustom;

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProviderCustom);
    }

    //@Bean
    //public UserDetailsService userDetailsService() {
    //    // 1. 配置用户信息服务，暂时将用户信息存储在内存，后面会改成从数据库查
    //    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    //    // 2. 创建用户信息, Kyle的权限是p1，Lucy的权限是p2
    //    manager.createUser(User.withUsername("Kyle").password("123").authorities("p1").build());
    //    manager.createUser(User.withUsername("Lucy").password("456").authorities("p2").build());
    //    return manager;
    //}

    @Bean
    public PasswordEncoder passwordEncoder() {
        //return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        String password = "123456";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        for (int i = 0; i < 5; i++) {
            // 每个计算出的Hash值都不一样
            String encodePsw = encoder.encode(password);
            // 虽然Hash值不一样，但是校验是可以通过的
            System.out.println("转换后密码：" + encodePsw + "比对情况：" + encoder.matches(password, encodePsw));
        }
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/r/**")
                .authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .successForwardUrl("/login-success");
        http.logout().logoutUrl("/logout");
    }
}
