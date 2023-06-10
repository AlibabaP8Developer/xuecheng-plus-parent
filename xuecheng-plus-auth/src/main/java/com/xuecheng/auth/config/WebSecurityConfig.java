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

/**
 * @author Mr.M
 * @version 1.0
 * @description 安全管理配置
 * @date 2022/9/26 20:53
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 告诉spring security重写了DaoAuthenticationProviderCustom
     */
    @Autowired
    DaoAuthenticationProviderCustom daoAuthenticationProviderCustom;

    //配置用户信息服务
    //这里的密码是静态的，需要自定义UserDetailsService从数据库中查询用户信息
    //@Bean
    //public UserDetailsService userDetailsService() {
    //    //这里配置用户信息,这里暂时使用这种方式将用户存储在内存中
    //    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    //    manager.createUser(User.withUsername("zhangsan").password("123").authorities("p1").build());
    //    manager.createUser(User.withUsername("lisi").password("456").authorities("p2").build());
    //    return manager;
    //}

    @Bean
    public PasswordEncoder passwordEncoder() {
        //密码为明文方式
//        return NoOpPasswordEncoder.getInstance();
//        加密方式
        return new BCryptPasswordEncoder();
    }

    //配置安全拦截机制
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/r/**").authenticated()//访问/r开始的请求需要认证通过
                .anyRequest().permitAll()//其它请求全部放行
                .and()
                .formLogin().successForwardUrl("/login-success");//登录成功跳转到/login-success
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProviderCustom);
    }

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 生成密码
        for (int i = 0; i < 5; i++) {
            String encode = passwordEncoder.encode("111111");
            System.out.println(encode);
            // 校验密码
            boolean matches = passwordEncoder.matches("111111", encode);
            System.out.println(matches);
        }
    }

}
