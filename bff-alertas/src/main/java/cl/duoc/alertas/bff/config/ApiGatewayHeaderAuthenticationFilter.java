package cl.duoc.alertas.bff.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class ApiGatewayHeaderAuthenticationFilter extends OncePerRequestFilter {

    @Value("${app.gateway.security.enabled:true}")
    private boolean gatewaySecurityEnabled;

    @Value("${app.gateway.header-name:X-Api-Gateway-Secret}")
    private String gatewayHeaderName;

    @Value("${app.gateway.secret:}")
    private String gatewaySecret;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (!gatewaySecurityEnabled || isPublicRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String receivedSecret = request.getHeader(gatewayHeaderName);
        if (StringUtils.hasText(gatewaySecret) && gatewaySecret.equals(receivedSecret)) {
            var authentication = new UsernamePasswordAuthenticationToken(
                    "aws-api-gateway",
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_API_GATEWAY"))
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
            return;
        }

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        response.getWriter().write("{\"mensaje\":\"Acceso denegado: la API debe ingresar mediante AWS API Gateway.\"}");
    }

    private boolean isPublicRequest(HttpServletRequest request) {
        String path = request.getRequestURI();
        return "OPTIONS".equalsIgnoreCase(request.getMethod())
                || path.equals("/api/bff/health")
                || path.equals("/actuator/health")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs");
    }
}
