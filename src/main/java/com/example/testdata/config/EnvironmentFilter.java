package com.example.testdata.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class EnvironmentFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            String env = req.getParameter("env");
            if (env != null && !env.isBlank()) EnvironmentContext.set(env.trim());
            chain.doFilter(request, response);
        } finally {
            EnvironmentContext.clear();
        }
    }
}
