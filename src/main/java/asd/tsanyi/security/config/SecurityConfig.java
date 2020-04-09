package asd.tsanyi.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import asd.tsanyi.security.enums.Roles;
import asd.tsanyi.security.utilities.AuthenticationFilter;
import asd.tsanyi.security.utilities.AuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final RequestMatcher PROTECTED_URLS = new OrRequestMatcher(new AntPathRequestMatcher("/user/**"), new AntPathRequestMatcher("/user"));

	AuthenticationProvider provider;

	public SecurityConfig(final AuthenticationProvider authenticationProvider) {
		super();
		this.provider = authenticationProvider;
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(provider);
	}

	@Override
	public void configure(final WebSecurity webSecurity) {
		webSecurity.ignoring().antMatchers("/h2-console/**", "/authentication");
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling().and().authenticationProvider(provider).addFilterBefore(authenticationFilter(), AnonymousAuthenticationFilter.class)
		.authorizeRequests()
		.antMatchers(HttpMethod.POST,"/user").hasAnyAuthority(Roles.ADMIN.toString())
		.antMatchers("/user/**").hasAnyAuthority(Roles.ADMIN.toString())
		.antMatchers(HttpMethod.GET,"/user").hasAnyAuthority(Roles.USER.toString(),Roles.ADMIN.toString())
		.and().csrf().disable().formLogin().disable().httpBasic().disable().logout().disable();	
	}

	@Bean
	AuthenticationFilter authenticationFilter() throws Exception {
		final AuthenticationFilter filter = new AuthenticationFilter(PROTECTED_URLS);
		filter.setAuthenticationManager(authenticationManager());

		return filter;
	}

	@Bean
	AuthenticationEntryPoint forbiddenEntryPoint() {
		return new HttpStatusEntryPoint(HttpStatus.FORBIDDEN);
	}
}
