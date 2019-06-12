package com.sanq.product.config.utils.filter.security;

import org.apache.commons.io.IOUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * com.pymob.product.books.filters.SecurityFilter
 *
 * @author sanq.Yan
 * @date 2019/6/11
 */
public class SecurityFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SecurityHttpServletRequestWrapper wrap = null;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            if (!"GET".equals(httpServletRequest.getMethod().toUpperCase())
                    && httpServletRequest.getContentType().contains("application/json")) {
                wrap = new SecurityHttpServletRequestWrapper(httpServletRequest);
                PostParams.getInstance().set(wrap.getJsonPararms());
            }
        }
        if (null != wrap) {
            chain.doFilter(wrap, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}

class SecurityHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private String jsonPararms;

    public SecurityHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        ServletInputStream stream = this.getRequest().getInputStream();
        jsonPararms = IOUtils.toString(stream, "UTF-8");
    }

    @Override
    public ServletInputStream getInputStream() {
        byte[] buffer = null;
        try {
            buffer = jsonPararms.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        ServletInputStream newStream = new ServletInputStream() {

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
        return newStream;
    }

    public String getJsonPararms() {
        return jsonPararms;
    }

    public void setJsonPararms(String jsonPararms) {
        this.jsonPararms = jsonPararms;
    }

}