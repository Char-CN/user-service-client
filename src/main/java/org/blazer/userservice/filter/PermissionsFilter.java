package org.blazer.userservice.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.blazer.userservice.model.UserModel;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PermissionsFilter implements Filter {

	private static final String SESSION_KEY = "MYSESSIONID";
	private static ThreadLocal<HttpServletRequest> HttpRequestThreadLocalHolder = new ThreadLocal<HttpServletRequest>(); 
	private String systemName = null;
	private String serviceUrl = null;

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
				sb.append(line + NL);
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
			System.out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		chain.doFilter(req, resp);
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
		System.out.println("init filter : " + systemName);
		System.out.println("init filter : " + serviceUrl);
	}

	public void destroy() {
	}

}
