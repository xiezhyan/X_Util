package com.sanq.product.security.filters;

import com.google.common.collect.Lists;
import com.sanq.product.config.utils.string.StringUtil;
import com.sanq.product.security.utils.ParamUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

/**
 * com.sanq.product.security.filters.SecurityFilter
 *
 * @author sanq.Yan
 * @date 2019/8/3
 */
public class SecurityFilter implements Filter {

    private String exclusion;
    private List<String> exclusions;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.exclusion = filterConfig.getInitParameter("exclusion");

        if (StringUtil.isEmpty(this.exclusion))
            exclusions = Lists.newArrayList();
        else
            exclusions = Arrays.asList(this.exclusion.split(","));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SecurityHttpServletRequestWrapper wrap = null;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;

            if (!"get".equalsIgnoreCase(httpServletRequest.getMethod().toUpperCase())
                    && !exclusions.contains(httpServletRequest.getServletPath())) {
                wrap = new SecurityHttpServletRequestWrapper(httpServletRequest);
                ParamUtils.getInstance().set(wrap.getJson());
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

    private String json;

    public SecurityHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        ServletInputStream stream = this.getRequest().getInputStream();
        json = IOUtils.toString(stream, "UTF-8");
    }

    @Override
    public ServletInputStream getInputStream() {
        byte[] buffer = null;
        try {
            buffer = json.toString().getBytes("UTF-8");
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

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

}