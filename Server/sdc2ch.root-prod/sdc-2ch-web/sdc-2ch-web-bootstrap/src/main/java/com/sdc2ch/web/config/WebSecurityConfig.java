package com.sdc2ch.web.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.sdc2ch.core.security.auth.I2CHAuthorization;
import com.sdc2ch.token.ajax.AjaxAuthenticationProvider;
import com.sdc2ch.token.jwt.JwtAuthenticationProvider;
import com.sdc2ch.token.jwt.JwtTokenAuthenticationProcessingFilter;
import com.sdc2ch.token.jwt.SkipPathRequestMatcher;
import com.sdc2ch.token.jwt.extractor.TokenExtractor;
import com.sdc2ch.token.portal.PortalAuthenticationProvider;
import com.sdc2ch.web.common.RequestParamInterceptor;
import com.sdc2ch.web.filter.AjaxLoginProcessingFilter;
import com.sdc2ch.web.filter.CustomCorsFilter;
import com.sdc2ch.web.filter.PortalLoginProcessingFilter;
import com.sdc2ch.web.filter.PostLoginProcessingFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    public static final String AUTHENTICATION_URL = "/ajax/login";
    public static final String LOGIN = "/user/login";
    public static final String PORTAL_LOGIN = "/portal/login";
    public static final String REFRESH_TOKEN_URL = "/api/auth/token";
    public static final String API_ROOT_URL = "/api/**";

    @Autowired private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired private AuthenticationSuccessHandler successHandler;
    @Autowired private AuthenticationFailureHandler failureHandler;
    @Autowired private AjaxAuthenticationProvider ajaxAuthenticationProvider;
    @Autowired private PortalAuthenticationProvider portalAuthenticationProvider;
    @Autowired private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired private TokenExtractor tokenExtractor;

    @Autowired private AuthenticationManager authenticationManager;

    @Autowired private ObjectMapper objectMapper;
    
    @Autowired I2CHAuthorization auth;

    protected AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter(String loginEntryPoint) throws Exception {
        AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(loginEntryPoint, successHandler, failureHandler, objectMapper);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    protected PostLoginProcessingFilter buildPostLoginProcessingFilter(String loginEntryPoint) throws Exception {
        PostLoginProcessingFilter filter = new PostLoginProcessingFilter(loginEntryPoint, successHandler, failureHandler, objectMapper);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }
    
    protected PortalLoginProcessingFilter buildPortalLoginProcessingFilter(String loginEntryPoint) throws Exception {
    	PortalLoginProcessingFilter filter = new PortalLoginProcessingFilter(loginEntryPoint, successHandler, failureHandler, objectMapper);
    	filter.setAuthenticationManager(this.authenticationManager);
    	return filter;
    }
    
    protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter(List<String> pathsToSkip, String pattern) throws Exception {
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, pattern);
        JwtTokenAuthenticationProcessingFilter filter
            = new JwtTokenAuthenticationProcessingFilter(failureHandler, tokenExtractor, matcher);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return new Jackson2ObjectMapperBuilderCustomizer() {
            @Override
            public void customize(Jackson2ObjectMapperBuilder builder) {
                builder.simpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
                builder.timeZone(TimeZone.getTimeZone("UTC"));
            }           
        };
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
    	auth.authenticationProvider(portalAuthenticationProvider);
    	auth.authenticationProvider(ajaxAuthenticationProvider);
        auth.authenticationProvider(jwtAuthenticationProvider);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<String> permitAllEndpointList = Arrays.asList(



            "/m/verify",
        	"m/default/token",
        	PORTAL_LOGIN,
        	"/user/login",
        	"m/api/*",
        	"m/api/loc/**",
        	"smt/**"
        );

        http
            .csrf().disable() 
            .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
            .authenticationEntryPoint(this.authenticationEntryPoint)

            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
                .authorizeRequests()
                .antMatchers(permitAllEndpointList.toArray(new String[permitAllEndpointList.size()]))
                .permitAll()



            .and()
                .addFilterBefore(new CustomCorsFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildAjaxLoginProcessingFilter(AUTHENTICATION_URL), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildPostLoginProcessingFilter(LOGIN), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildPortalLoginProcessingFilter(PORTAL_LOGIN), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(permitAllEndpointList, "/m/user/**"), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(permitAllEndpointList, "/adm/**"), UsernamePasswordAuthenticationFilter.class)
        		.addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(permitAllEndpointList, "/m/ebt/**"), UsernamePasswordAuthenticationFilter.class);
    }
    
    private AccessDeniedHandler accessDeniedHandler() {

		return new AccessDeniedHandler() {
			
			@Override
			public void handle(HttpServletRequest request, HttpServletResponse response,
					AccessDeniedException accessDeniedException) throws IOException, ServletException {




				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, accessDeniedException.getMessage());
			}
		};
	}

	
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/WEB-INF/resources/");

        registry.addResourceHandler("/mobile/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

    }
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*");;
            }
        };
    }
    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestParamInterceptor(auth, objectMapper))
                .addPathPatterns("/m/**", "/adm/**");
    }
    
    @Bean
    public Module datatypeHibernateModule() {
      return new Hibernate5Module();
    }
}
