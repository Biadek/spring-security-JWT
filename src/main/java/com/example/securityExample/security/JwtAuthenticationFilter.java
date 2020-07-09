package com.example.securityExample.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private MyUserDetailsService userService;
    private JwtToken tokenProvider;

    public JwtAuthenticationFilter(MyUserDetailsService userService, JwtToken jwtToken) {
        this.userService = userService;
        this.tokenProvider = jwtToken;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt = getToken(request);

        if (jwt != null && tokenProvider.validateToken(jwt)) {
            UserDetails userDetails = userService.loadUserByUsername(tokenProvider.getUserName(jwt));
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(tokenProvider.getHeader());
        if (token != null && token.startsWith(tokenProvider.getPrefix())) {
            return token.substring(7);
        }
        return null;
    }
}
