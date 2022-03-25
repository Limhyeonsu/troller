package com.toy.troller.security;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.toy.troller.member.service.MemberService;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private MemberService memberService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception{
		web.ignoring().antMatchers("/css/**", "/js/**", "/webjars/**", "/assets/**", "/resources/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.authorizeRequests()
				//.antMatchers("/admin/**").hasRole("ADMIN")
				//.antMatchers("/user/myinfo").hasRole("MEMBER")
				//.antMatchers("/user/joinuser").permitAll()
				//.antMatchers("/user/update").permitAll()
				.anyRequest().authenticated()
				//.antMatchers("/**").permitAll()
			.and()
				.formLogin()
				.loginPage("/user/login").permitAll()
				.loginProcessingUrl("/loginProcess").permitAll()
				//.usernameParameter("userId")
				.defaultSuccessUrl("/")
			.and()
				.csrf()
				.ignoringAntMatchers("/user/**")
			.and()
				.cors()
			.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
				.logoutSuccessUrl("/user/login")
				//.deleteCookies("userCookie")
				.invalidateHttpSession(true)
			.and()
				.exceptionHandling().accessDeniedPage("/user/login");
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
	}
}
