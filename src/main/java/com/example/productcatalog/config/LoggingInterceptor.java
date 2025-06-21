package com.example.productcatalog.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Enumeration;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        request.setAttribute("requestWrapper", requestWrapper);
        request.setAttribute("responseWrapper", responseWrapper);

        logRequest(requestWrapper);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        ContentCachingResponseWrapper responseWrapper =
                (ContentCachingResponseWrapper) request.getAttribute("responseWrapper");
        logResponse(responseWrapper);
        responseWrapper.copyBodyToResponse();
    }

    private void logRequest(ContentCachingRequestWrapper request) throws IOException {
        StringBuilder reqInfo = new StringBuilder()
                .append("\n[REQUEST] ")
                .append(request.getMethod())
                .append(" ")
                .append(request.getRequestURI());

        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            reqInfo.append("\nHeaders:");
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                reqInfo.append("\n  ")
                        .append(headerName)
                        .append(": ")
                        .append(request.getHeader(headerName));
            }
        }

        byte[] buf = request.getContentAsByteArray();
        if (buf.length > 0) {
            reqInfo.append("\nBody:\n").append(new String(buf, request.getCharacterEncoding()));
        }

        logger.info(reqInfo.toString());
    }

    private void logResponse(ContentCachingResponseWrapper response) throws IOException {
        StringBuilder resInfo = new StringBuilder()
                .append("\n[RESPONSE] ")
                .append(response.getStatus());

        resInfo.append("\nHeaders:");
        response.getHeaderNames().forEach(headerName -> {
            resInfo.append("\n  ")
                    .append(headerName)
                    .append(": ")
                    .append(response.getHeader(headerName));
        });

        byte[] buf = response.getContentAsByteArray();
        if (buf.length > 0) {
            resInfo.append("\nBody:\n").append(new String(buf, response.getCharacterEncoding()));
        }

        logger.info(resInfo.toString());
    }
}