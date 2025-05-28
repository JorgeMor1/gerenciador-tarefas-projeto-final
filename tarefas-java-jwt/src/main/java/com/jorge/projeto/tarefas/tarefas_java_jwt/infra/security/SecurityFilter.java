package com.jorge.projeto.tarefas.tarefas_java_jwt.infra.security;

import com.jorge.projeto.tarefas.tarefas_java_jwt.infra.UserDetailsImpl;
import com.jorge.projeto.tarefas.tarefas_java_jwt.model.user.User;
import com.jorge.projeto.tarefas.tarefas_java_jwt.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static  final Logger looger = LoggerFactory.getLogger(SecurityFilter.class);

    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) || path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = this.recoverToken(request);
        logger.info("Interceptando requisição para: {}", request.getRequestURI());
        logger.info("Token recebido: {}", token);

        if (token != null) {
            String login = tokenService.validateToken(token);
            logger.info("Login extraído do token: {}", login);

            if (login != null) {
                User user = userRepository.findByEmail(login)
                        .orElseThrow(() -> new RuntimeException("User Not Found"));

                UserDetailsImpl userDetails =  UserDetailsImpl.build(user);
                var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
                //var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
                var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                logger.info("Usuário autenticado: {}", user.getEmail());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.warn("Token inválido ou expirado.");
            }
        } else {
            logger.warn("Nenhum token encontrado no header Authorization.");
        }

        filterChain.doFilter(request, response);



    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "").trim();
    }


}