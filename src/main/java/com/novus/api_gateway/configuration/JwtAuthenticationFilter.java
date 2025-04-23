package com.novus.api_gateway.configuration;

import com.novus.api_gateway.dao.UserDaoUtils;
import com.novus.api_gateway.service.JwtTokenService;
import com.novus.shared_models.common.User.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final UserDaoUtils userDaoUtils;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        if (isNotPrivateRoute(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication userInformations = getUserInformations(request);
        if (Objects.isNull(userInformations)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You must be authenticated to perform this action.");
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(userInformations);
        filterChain.doFilter(request, response);
    }

    private Authentication getUserInformations(HttpServletRequest request) {
        String userId = jwtTokenService.resolveUserIdFromRequest(request);
        if (Objects.isNull(userId)) {
            return null;
        }

        Optional<User> optionalUser = userDaoUtils.findById(userId);

        return optionalUser.map(this::buildAuthentication).orElse(null);
    }

    private Authentication buildAuthentication(User user) {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString()));
        return new UsernamePasswordAuthenticationToken(user, null, authorities);
    }

    private boolean isNotPrivateRoute(String uri) {
        return !uri.startsWith("/private");
    }

}
