package com.web.community.config;

import com.web.community.oauth.CustomOAuth2Provider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.web.community.domain.enums.SocialType.*;


@Configuration
@EnableWebSecurity
//@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {

/*    @Autowired
    private OAuth2ClientContext oAuth2ClientContext;*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        http.
                authorizeRequests()
                    .antMatchers("/", "/oauth2/**","/login/**", "/css/**", "/images/**", "/js/**", "/console/**").permitAll()
                    .antMatchers("/facebook").hasAnyAuthority(FACEBOOK.getRoleType())
                    .antMatchers("/google").hasAnyAuthority(GOOGLE.getRoleType())
                    .antMatchers("/kakao").hasAnyAuthority(KAKAO.getRoleType())
                    .anyRequest().authenticated()
                .and()
                    .oauth2Login()
                    .defaultSuccessUrl("/loginSuccess")
                    .failureUrl("/loginFailure")
                .and()
                    .headers().frameOptions().disable() // disable() : 거부 / sameOrigin() : 동일한 도메인에서는 iframe 접근 가능
                .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                .and()
                    .formLogin()
                    .successForwardUrl("/board/list")
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                .and()
                    .addFilterBefore(filter, CsrfFilter.class)
                    /*.addFilterBefore(oauth2Filter(), BasicAuthenticationFilter.class)*/
                .csrf().disable();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties oAuth2ClientProperties, @Value("${custom.oauth2.kakao.client-id}")String kakaoClientId){
        List<ClientRegistration> registrations = oAuth2ClientProperties.getRegistration().keySet().stream().map(client -> getRegistration(oAuth2ClientProperties, client))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        registrations.add(CustomOAuth2Provider.KAKAO.getBuilder("kakao")
                                            .clientId(kakaoClientId)
                                            .clientSecret("test")
                                            .jwkSetUri("test")
                                            .build());

        return new InMemoryClientRegistrationRepository(registrations);
    }

    private ClientRegistration getRegistration(OAuth2ClientProperties clientProperties, String client){
        if("google".equals(client)){
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("google");
            return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                    .clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret())
                    .scope("email","profile")
                    .build();
        }
        if("facebook".equals(client)){
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("facebook");
            return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
                    .clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret())
                    .userInfoUri("https://graph.facebook.com/me?fields=id,name,email,link")
                    .scope("email")
                    .build();
        }
        return null;
    }


    /*@Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

    private Filter oauth2Filter() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(oauth2Filter(facebook(), "/login/facebook", FACEBOOK));
        filters.add(oauth2Filter(google(), "/login/google", GOOGLE));
        filters.add(oauth2Filter(kakao(), "/login/kakao", KAKAO));
        filter.setFilters(filters);
        return filter;
    }

    private Filter oauth2Filter(ClientResources clientResources, String path, SocialType socialType) {

        //인증이 수행될 경로를 넣어 OAuth2 클라이언트용 인증처리 필터를 생성
        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(path);

        //권한 서버와의 통신을 위해 OAuth2RestTemplate을 생성합니다.
        OAuth2RestTemplate template = new OAuth2RestTemplate(clientResources.getClient(), oAuth2ClientContext);

        filter.setRestTemplate(template);

        //User의 권한을 최적회해서 생성하고자 UserInfoTokenServices를 상속받은 UserTokenService를 생성했습니다.
        //OAuth2 AccessToken 검증을 위해 생성한 UserTokenService를 필터의 토큰 서비스로 등록합니다.
        filter.setTokenServices(new UserTokenService(clientResources, socialType));

        // 인증이 성공적으로 이루어지면 필터에 리다이렉트될 URL을 설정합니다.
        filter.setAuthenticationSuccessHandler(
                (request, response, authentication) -> response.sendRedirect("/" + socialType.getValue() + "/complete"));
        // 인증이 실패하면 이필터에 리다이렉트될 URL을 설정합니다.
        filter.setAuthenticationFailureHandler((request, response, exception) -> response.sendRedirect("/error"));
        return filter;
    }

    @Bean
    @ConfigurationProperties("facebook")
    public ClientResources facebook() {
        return new ClientResources();
    }

    @Bean
    @ConfigurationProperties("google")
    public ClientResources google() {
        return new ClientResources();
    }

    @Bean
    @ConfigurationProperties("kakao")
    public ClientResources kakao() {
        return new ClientResources();
    }*/

}
