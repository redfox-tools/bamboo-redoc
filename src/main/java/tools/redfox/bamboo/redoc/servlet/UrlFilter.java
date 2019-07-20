package tools.redfox.bamboo.redoc.servlet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String servletPath = httpServletRequest.getServletPath();
        if (!servletPath.startsWith("/browse")) {
            throw new ServletException("UrlRewriteFilter only supports servlet paths starting with \"/browse\". URI:" + servletPath);
        } else {
            String newUrl = this.parseUrl(servletPath);
            if (newUrl != null) {
                this.forwardTo(newUrl, httpServletRequest, response);
            } else {
                chain.doFilter(request, response);
            }

        }
    }

    @Nullable
    public String parseUrl(@NotNull String servletPath) throws ServletException {
        String path = servletPath;
        if (servletPath.indexOf("/") != 0) {
            path = servletPath.replaceAll("^", "/");
        }

        Pattern pattern = Pattern.compile("/browse/([A-Z0-9]{2,})-([A-Z0-9]{2,}(?:-[A-Z][A-Z0-9]{1,})?)-([0-9]+)/redoc");
        Matcher matcher = pattern.matcher(path.replaceAll("/$", ""));
        if (!matcher.matches()) {
            return null;
        }

        return matcher.replaceAll("/build/result/viewBuildReDoc.action?buildKey=$1-$2&buildNumber=$3");
    }

    private void forwardTo(String path, HttpServletRequest request, ServletResponse response) throws IOException, ServletException {
        request.setAttribute("originalUrl", request.getServletPath());
        RequestDispatcher dispatcher = request.getRequestDispatcher(path);
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}
