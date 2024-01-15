package com.example.electronic.booth.service.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private MyUserDetailsService userDetailsService;
    @Autowired
    private TokenManager tokenManager;
    @Value("${secret}")
    String secret;
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain chain) throws ServletException, IOException {
        // look for Bearer auth header
        String tokenHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;
        System.out.println(request.getRequestURI() + " " + tokenHeader);
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            token = tokenHeader.substring(7);
            System.out.println(token);
            try {
                username = tokenManager.getUsernameFromToken(token);
                System.out.println(username);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println(e.getMessage());
                System.out.println("JWT Token has expired");
            }
        } else {
            System.out.println("Bearer String not found in token");
        }
        if (null != username && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (tokenManager.validateTokenAndGetUsername(token, userDetails)) {
                UsernamePasswordAuthenticationToken
                        authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null,
                        userDetails.getAuthorities());
                authenticationToken.setDetails(new
                        WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}