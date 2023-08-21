package co.mobileaction.openweathermap.security;

import co.mobileaction.openweathermap.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Value("${ma.security.secure-key-token}")
    public String SECURE_KEY_TOKEN;

    @Value("${ma.security.admin-token}")
    public String ADMIN_KEY_TOKEN;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        log.info("Configuring authentication manager");

        // since we don't use a password we accept low level security for encoding for better performance
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> configurer = auth.inMemoryAuthentication();
        configurer.passwordEncoder(encoder);

        if (SECURE_KEY_TOKEN != null)
        {
            String[] keys = SECURE_KEY_TOKEN.split(",");
            if (keys.length > 1)
            {
                configurer.withUser(keys[0]).password(encoder.encode("")).roles(SecurityUtils.USER);
                configurer.withUser(keys[1]).password(encoder.encode("")).roles(SecurityUtils.USER);
            }
        }
        else
        {
            log.warn("Received null Secure Key Token!");
        }

        if (ADMIN_KEY_TOKEN != null)
        {
            configurer.withUser(ADMIN_KEY_TOKEN)
                    .password(encoder.encode(""))
                    .roles(SecurityUtils.ADMIN);
        }
        else
        {
            log.warn("Received null Administrative Key Token-1!");
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        super.configure(http);
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.formLogin().disable();
    }
}
