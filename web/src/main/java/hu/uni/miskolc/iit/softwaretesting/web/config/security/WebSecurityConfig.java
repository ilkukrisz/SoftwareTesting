package hu.uni.miskolc.iit.softwaretesting.web.config.security;

import hu.uni.miskolc.iit.softwaretesting.dao.BookDAO;
import hu.uni.miskolc.iit.softwaretesting.dao.BookDaoXMLImpl;
import hu.uni.miskolc.iit.softwaretesting.service.LibrarianBookService;
import hu.uni.miskolc.iit.softwaretesting.service.ReaderBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Map;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BasicAuthEntryPoint authenticationEntryPoint;

    @Autowired
    private LibrarianBookService librarianService;

    @Autowired
    private ReaderBookService readerService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        Map<String, String> readers = readerService.getReaderCredentials();
        Map<String, String> librarians = librarianService.getLibrarianCredentials();

        this.registerUsers(auth, readers, "READER");
        this.registerUsers(auth, librarians, "LIBRARIAN");
    }

    void registerUsers (AuthenticationManagerBuilder authBuilder, Map<String, String> credentials, String role) throws Exception {
        for (String key : credentials.keySet()) {
            String username = key;
            String passHash = credentials.get(key);

            authBuilder.inMemoryAuthentication()
                    .withUser(username)
                    .password(passHash)
                    .roles(role);
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //disable CSRF token to allow requests with curl/Postman/etc.
        http.csrf().disable();

        //protect reader api
        http.authorizeRequests()
                .antMatchers("/reader/*").hasAnyRole("READER", "LIBRARIAN")
                .and()
                .httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint);

        //protect librarian api
        http.authorizeRequests()
                .antMatchers("/librarian/*").hasRole("LIBRARIAN")
                .and()
                .httpBasic()
                .authenticationEntryPoint(authenticationEntryPoint);
        http.addFilterAfter(new CustomFilter(), BasicAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}