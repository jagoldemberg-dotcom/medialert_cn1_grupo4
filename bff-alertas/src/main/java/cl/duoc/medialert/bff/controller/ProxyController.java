package cl.duoc.medialert.bff.controller;

import cl.duoc.medialert.bff.service.ProxyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bff")
public class ProxyController {
    private final ProxyService proxy;

    public ProxyController(ProxyService proxy) {
        this.proxy = proxy;
    }

    @RequestMapping(value = {"/alertas", "/alertas/**"}, method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> alertas(HttpServletRequest request, @RequestBody(required = false) String body) {
        return proxy.alertas(HttpMethod.valueOf(request.getMethod()), suffix(request, "/api/bff/alertas"), request.getQueryString(), body);
    }

    @RequestMapping(value = {"/resumenes", "/resumenes/**"}, method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> resumenes(HttpServletRequest request, @RequestBody(required = false) String body) {
        return proxy.resumenes(HttpMethod.valueOf(request.getMethod()), suffix(request, "/api/bff/resumenes"), request.getQueryString(), body);
    }

    @GetMapping({"/streaming", "/streaming/**"})
    public ResponseEntity<String> streaming(HttpServletRequest request) {
        return proxy.streaming(HttpMethod.GET, suffix(request, "/api/bff/streaming"), request.getQueryString());
    }

    private String suffix(HttpServletRequest request, String prefix) {
        String uri = request.getRequestURI();
        return uri.length() <= prefix.length() ? "" : uri.substring(prefix.length());
    }
}
