package com.khoai.attendance_backend.security;

import com.khoai.attendance_backend.model.LoginSession;
import com.khoai.attendance_backend.repository.LoginSessionRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class AuthFilter extends GenericFilterBean {
    private final LoginSessionRepository loginSessionRepository;

    public AuthFilter(LoginSessionRepository loginSessionRepository) {
        this.loginSessionRepository = loginSessionRepository;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader("Authorization");

        if (token != null && !token.isEmpty()) {
            String rawToken = token.substring("Bearer ".length());
            System.out.println("ahuhuhuhu "+ rawToken);
            Optional<LoginSession> loginSession = loginSessionRepository.findByToken(rawToken);
            var u = loginSessionRepository.findByToken("f48da883-8af7-46b3-abd1-145db945c0aa").get();
            System.out.println("pppppp "+ u.getUser().getUsername());
            if (loginSession.isPresent()) {
                Set<SimpleGrantedAuthority> authorities = new HashSet<>();
                authorities.add(new SimpleGrantedAuthority("USER"));
                Authentication authentication = new UsernamePasswordAuthenticationToken(loginSession.get().getUser(), "", authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }


}
