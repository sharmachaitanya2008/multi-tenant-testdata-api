package com.example.testdataservice.config;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
@Component
public class EnvironmentFilter implements Filter {
    @Value("${app.api.key}") private String apiKey;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String key = req.getHeader("X-API-KEY");
        if (key == null || !key.equals(apiKey)) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Missing or invalid API key");
            return;
        }
        try {
            String env = req.getParameter("env"); if (env != null && !env.isBlank()) EnvironmentContext.set(env.trim());
            chain.doFilter(request, response);
        } finally { EnvironmentContext.clear(); }
    }
}
