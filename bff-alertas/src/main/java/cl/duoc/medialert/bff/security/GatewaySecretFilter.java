package cl.duoc.medialert.bff.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class GatewaySecretFilter extends OncePerRequestFilter {
    private final String mode;
    private final String expectedSecret;

    public GatewaySecretFilter(
            @Value("${medialert.security.mode:GATEWAY}") String mode,
            @Value("${medialert.security.gateway-secret:}") String expectedSecret) {
        this.mode = mode;
        this.expectedSecret = expectedSecret;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return "OPTIONS".equalsIgnoreCase(request.getMethod())
                || path.endsWith("/health")
                || path.startsWith("/actuator/health");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if ("PERMIT_ALL".equalsIgnoreCase(mode)) {
            authenticate("desarrollo-local");
            filterChain.doFilter(request, response);
            return;
        }

        String received = request.getHeader("X-Api-Gateway-Secret");
        if (StringUtils.hasText(expectedSecret) && expectedSecret.equals(received)) {
            authenticate("aws-api-gateway");
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        response.getWriter().write("{\"error\":\"Solicitud no autorizada por el BFF\"}");
    }

    private void authenticate(String principal) {
        var auth = new UsernamePasswordAuthenticationToken(
                principal, null, List.of(new SimpleGrantedAuthority("ROLE_API_CLIENT")));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
