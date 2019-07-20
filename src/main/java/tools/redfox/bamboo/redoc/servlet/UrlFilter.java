package tools.redfox.bamboo.redoc.servlet;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

public class UrlFilter implements Filter {
    private static final Map<Pattern, String> urlMappings = new HashMap();

    static {
        putRegex(urlMappings, "/browse/([A-Z0-9]{2,})-([A-Z0-9]{2,}(?:-[A-Z][A-Z0-9]{1,})?)-([0-9]+)/redoc", "/build/result/viewBuildReDoc.action?buildKey=$1-$2&buildNumber=$3");
        putRegex(urlMappings, "/browse/([A-Z0-9]{2,})-([A-Z0-9]{2,}(?:-[A-Z][A-Z0-9]{1,})?)-([0-9]+)/redoc", "/chain/result/viewChainReDoc.action?buildKey=$1-$2&buildNumber=$3");
    }

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

        final String finalPath = path.replaceAll("/$", "");

        return StreamSupport.stream(urlMappings.entrySet().spliterator(), false)
                .filter(entry -> {
                    Pattern regex = (Pattern) entry.getKey();
                    Matcher matcher = regex.matcher(finalPath);
                    return matcher.matches();
                })
                .map(entry -> {
                    Matcher m = entry.getKey().matcher(finalPath);
                    return m.replaceAll(entry.getValue());
                })
                .findFirst()
                .orElse(null);
    }

    private static void putRegex(Map<Pattern, String> map, String regex, String replacement) {
        map.put(Pattern.compile(regex), replacement);
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
