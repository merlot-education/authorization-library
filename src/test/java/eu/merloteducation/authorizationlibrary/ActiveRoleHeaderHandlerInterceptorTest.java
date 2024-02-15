package eu.merloteducation.authorizationlibrary;

import eu.merloteducation.authorizationlibrary.authorization.ActiveRoleHeaderHandlerInterceptor;
import eu.merloteducation.authorizationlibrary.authorization.AuthorityChecker;
import eu.merloteducation.authorizationlibrary.authorization.OrganizationRoleGrantedAuthority;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = { ActiveRoleHeaderHandlerInterceptor.class , AuthorityChecker.class })
class ActiveRoleHeaderHandlerInterceptorTest {

    @Autowired
    ActiveRoleHeaderHandlerInterceptor activeRoleHeaderHandlerInterceptor;

    SecurityContext securityContext = Mockito.mock(SecurityContext.class);

    @BeforeEach
    void setSecurityContext() {

        Mockito.when(securityContext.getAuthentication()).thenReturn(getTestAuthentication());
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void preHandleTrue() {

        assertThat(
            activeRoleHeaderHandlerInterceptor.preHandle(getTestHttpServletRequest("OrgLegRep_10"), getTestHttpServletResponse(),
                new Object())).isTrue();

        assertThat(
            activeRoleHeaderHandlerInterceptor.preHandle(getTestHttpServletRequest("FedAdmin_30"), getTestHttpServletResponse(),
                new Object())).isTrue();
    }

    @Test
    void preHandleFalse() {

        HttpServletResponse testHttpServletResponse = getTestHttpServletResponse();
        Object handler = new Object();

        HttpServletRequest orgLegRep30 = getTestHttpServletRequest("OrgLegRep_30");
        Exception e1 = assertThrows(ResponseStatusException.class,
            () -> activeRoleHeaderHandlerInterceptor.preHandle(orgLegRep30,
                testHttpServletResponse, handler));

        assertThat(e1.getMessage()).isEqualTo(HttpStatus.FORBIDDEN.toString());

        HttpServletRequest fedAdmin10 = getTestHttpServletRequest("FedAdmin_10");
        Exception e2 = assertThrows(ResponseStatusException.class,
            () -> activeRoleHeaderHandlerInterceptor.preHandle(fedAdmin10,
                testHttpServletResponse, handler));

        assertThat(e2.getMessage()).isEqualTo(HttpStatus.FORBIDDEN.toString());
    }

    HttpServletRequest getTestHttpServletRequest(String activeRole) {

        return new HttpServletRequest() {
            @Override
            public String getAuthType() {

                return null;
            }

            @Override
            public Cookie[] getCookies() {

                return new Cookie[0];
            }

            @Override
            public long getDateHeader(String s) {

                return 0;
            }

            @Override
            public String getHeader(String s) {

                return activeRole;
            }

            @Override
            public Enumeration<String> getHeaders(String s) {

                return null;
            }

            @Override
            public Enumeration<String> getHeaderNames() {

                return null;
            }

            @Override
            public int getIntHeader(String s) {

                return 0;
            }

            @Override
            public String getMethod() {

                return null;
            }

            @Override
            public String getPathInfo() {

                return null;
            }

            @Override
            public String getPathTranslated() {

                return null;
            }

            @Override
            public String getContextPath() {

                return null;
            }

            @Override
            public String getQueryString() {

                return null;
            }

            @Override
            public String getRemoteUser() {

                return null;
            }

            @Override
            public boolean isUserInRole(String s) {

                return false;
            }

            @Override
            public Principal getUserPrincipal() {

                return null;
            }

            @Override
            public String getRequestedSessionId() {

                return null;
            }

            @Override
            public String getRequestURI() {

                return null;
            }

            @Override
            public StringBuffer getRequestURL() {

                return null;
            }

            @Override
            public String getServletPath() {

                return null;
            }

            @Override
            public HttpSession getSession(boolean b) {

                return null;
            }

            @Override
            public HttpSession getSession() {

                return null;
            }

            @Override
            public String changeSessionId() {

                return null;
            }

            @Override
            public boolean isRequestedSessionIdValid() {

                return false;
            }

            @Override
            public boolean isRequestedSessionIdFromCookie() {

                return false;
            }

            @Override
            public boolean isRequestedSessionIdFromURL() {

                return false;
            }

            @Override
            public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {

                return false;
            }

            @Override
            public void login(String s, String s1) throws ServletException {

            }

            @Override
            public void logout() throws ServletException {

            }

            @Override
            public Collection<Part> getParts() throws IOException, ServletException {

                return null;
            }

            @Override
            public Part getPart(String s) throws IOException, ServletException {

                return null;
            }

            @Override
            public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) throws IOException, ServletException {

                return null;
            }

            @Override
            public Object getAttribute(String s) {

                return null;
            }

            @Override
            public Enumeration<String> getAttributeNames() {

                return null;
            }

            @Override
            public String getCharacterEncoding() {

                return null;
            }

            @Override
            public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

            }

            @Override
            public int getContentLength() {

                return 0;
            }

            @Override
            public long getContentLengthLong() {

                return 0;
            }

            @Override
            public String getContentType() {

                return null;
            }

            @Override
            public ServletInputStream getInputStream() throws IOException {

                return null;
            }

            @Override
            public String getParameter(String s) {

                return null;
            }

            @Override
            public Enumeration<String> getParameterNames() {

                return null;
            }

            @Override
            public String[] getParameterValues(String s) {

                return new String[0];
            }

            @Override
            public Map<String, String[]> getParameterMap() {

                return null;
            }

            @Override
            public String getProtocol() {

                return null;
            }

            @Override
            public String getScheme() {

                return null;
            }

            @Override
            public String getServerName() {

                return null;
            }

            @Override
            public int getServerPort() {

                return 0;
            }

            @Override
            public BufferedReader getReader() throws IOException {

                return null;
            }

            @Override
            public String getRemoteAddr() {

                return null;
            }

            @Override
            public String getRemoteHost() {

                return null;
            }

            @Override
            public void setAttribute(String s, Object o) {

            }

            @Override
            public void removeAttribute(String s) {

            }

            @Override
            public Locale getLocale() {

                return null;
            }

            @Override
            public Enumeration<Locale> getLocales() {

                return null;
            }

            @Override
            public boolean isSecure() {

                return false;
            }

            @Override
            public RequestDispatcher getRequestDispatcher(String s) {

                return null;
            }

            @Override
            public int getRemotePort() {

                return 0;
            }

            @Override
            public String getLocalName() {

                return null;
            }

            @Override
            public String getLocalAddr() {

                return null;
            }

            @Override
            public int getLocalPort() {

                return 0;
            }

            @Override
            public ServletContext getServletContext() {

                return null;
            }

            @Override
            public AsyncContext startAsync() throws IllegalStateException {

                return null;
            }

            @Override
            public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
                throws IllegalStateException {

                return null;
            }

            @Override
            public boolean isAsyncStarted() {

                return false;
            }

            @Override
            public boolean isAsyncSupported() {

                return false;
            }

            @Override
            public AsyncContext getAsyncContext() {

                return null;
            }

            @Override
            public DispatcherType getDispatcherType() {

                return null;
            }

            @Override
            public String getRequestId() {

                return null;
            }

            @Override
            public String getProtocolRequestId() {

                return null;
            }

            @Override
            public ServletConnection getServletConnection() {

                return null;
            }
        };
    }

    HttpServletResponse getTestHttpServletResponse() {

        return new HttpServletResponse() {
            @Override
            public void addCookie(Cookie cookie) {

            }

            @Override
            public boolean containsHeader(String s) {

                return false;
            }

            @Override
            public String encodeURL(String s) {

                return null;
            }

            @Override
            public String encodeRedirectURL(String s) {

                return null;
            }

            @Override
            public void sendError(int i, String s) throws IOException {

            }

            @Override
            public void sendError(int i) throws IOException {

            }

            @Override
            public void sendRedirect(String s) throws IOException {

            }

            @Override
            public void setDateHeader(String s, long l) {

            }

            @Override
            public void addDateHeader(String s, long l) {

            }

            @Override
            public void setHeader(String s, String s1) {

            }

            @Override
            public void addHeader(String s, String s1) {

            }

            @Override
            public void setIntHeader(String s, int i) {

            }

            @Override
            public void addIntHeader(String s, int i) {

            }

            @Override
            public void setStatus(int i) {

            }

            @Override
            public int getStatus() {

                return 0;
            }

            @Override
            public String getHeader(String s) {

                return null;
            }

            @Override
            public Collection<String> getHeaders(String s) {

                return null;
            }

            @Override
            public Collection<String> getHeaderNames() {

                return null;
            }

            @Override
            public String getCharacterEncoding() {

                return null;
            }

            @Override
            public String getContentType() {

                return null;
            }

            @Override
            public ServletOutputStream getOutputStream() throws IOException {

                return null;
            }

            @Override
            public PrintWriter getWriter() throws IOException {

                return null;
            }

            @Override
            public void setCharacterEncoding(String s) {

            }

            @Override
            public void setContentLength(int i) {

            }

            @Override
            public void setContentLengthLong(long l) {

            }

            @Override
            public void setContentType(String s) {

            }

            @Override
            public void setBufferSize(int i) {

            }

            @Override
            public int getBufferSize() {

                return 0;
            }

            @Override
            public void flushBuffer() throws IOException {

            }

            @Override
            public void resetBuffer() {

            }

            @Override
            public boolean isCommitted() {

                return false;
            }

            @Override
            public void reset() {

            }

            @Override
            public void setLocale(Locale locale) {

            }

            @Override
            public Locale getLocale() {

                return null;
            }
        };
    }

    Authentication getTestAuthentication() {

        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {

                List<OrganizationRoleGrantedAuthority> list = new ArrayList<>();
                list.add(new OrganizationRoleGrantedAuthority("OrgLegRep_10"));
                list.add(new OrganizationRoleGrantedAuthority("OrgLegRep_20"));
                list.add(new OrganizationRoleGrantedAuthority("FedAdmin_30"));
                return list;
            }

            @Override
            public Object getCredentials() {

                return null;
            }

            @Override
            public Object getDetails() {

                return null;
            }

            @Override
            public Object getPrincipal() {

                return null;
            }

            @Override
            public boolean isAuthenticated() {

                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {

                return null;
            }
        };
    }
}
