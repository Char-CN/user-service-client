package org.blazer.userservice.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class PermissionsFilter implements Filter {

	private static final String SESSION_KEY = "MYSESSIONID";
	private static final String COOKIE_PATH = "/";
	private static final int COOKIE_SECONDS = 10;

	private String systemName = null;
	private String serviceUrl = null;
	private String noPermissionsPage = null;

	public String executeGet(String url) throws Exception {
		BufferedReader in = null;
		String content = null;
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(url);
			CloseableHttpResponse response = httpclient.execute(httpGet);
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				if (sb.length() != 0) {
					sb.append(NL);
				}
				sb.append(line);
			}
			in.close();
			content = sb.toString();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return content;
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String sessionid = getSessionId(request);
		System.out.println(request.getRequestURL());
		System.out.println(request.getRequestURI());
		System.out.println(request.getRemoteHost());
		System.out.println(request.getRemoteAddr());
		String url = request.getRequestURI();
		try {
//			String content = executeGet(serviceUrl + "/getuser.do?" + SESSION_KEY + "=" + sessionid);
//			ObjectMapper mapper = new ObjectMapper();
//			UserModel um = mapper.readValue(content, UserModel.class);
//			System.out.println(um);
			StringBuilder requestUrl = new StringBuilder(serviceUrl);
			requestUrl.append("/checkurl.do?");
			requestUrl.append(SESSION_KEY).append("=").append(sessionid);
			requestUrl.append("&").append("url").append("=").append(url);
			requestUrl.append("&").append("systemName").append("=").append(systemName);
			String content = executeGet(requestUrl.toString());
			String[] contents = content.split(",");
			delay(response, contents[1]);
			System.out.println(content);
			if ("false".equals(contents[0])) {
				System.out.println("dispatcher");
				RequestDispatcher rd = request.getRequestDispatcher(noPermissionsPage);
				rd.forward(req, resp);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		chain.doFilter(req, resp);
	}

	private void delay(HttpServletResponse response, String newSession) {
		System.out.println("delay ~ new session : " + newSession);
		Cookie cookie = new Cookie(SESSION_KEY, newSession);
		cookie.setPath(COOKIE_PATH);
		cookie.setMaxAge(COOKIE_SECONDS);
		response.addCookie(cookie);
	}

	private String getSessionId(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		Cookie sessionCookie = null;
		for (Cookie cookie : cookies) {
			if (SESSION_KEY.equals(cookie.getName())) {
				System.out.println("cookie : " + cookie.getName() + " | " + cookie.getValue());
				sessionCookie = cookie;
				break;
			}
		}
		return sessionCookie == null ? null : sessionCookie.getValue();
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		systemName = filterConfig.getInitParameter("systemName");
		serviceUrl = filterConfig.getInitParameter("serviceUrl");
		noPermissionsPage = filterConfig.getInitParameter("noPermissionsPage");
		System.out.println("init filter : " + systemName);
		System.out.println("init filter : " + serviceUrl);
		System.out.println("init filter : " + noPermissionsPage);
	}

	public void destroy() {
	}

}
