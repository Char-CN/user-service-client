package org.blazer.userserviceclient.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Index extends HttpServlet {

	private static final long serialVersionUID = -6509436693211289992L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("key", "hyyyyyyyyy");
		
		//resp.getWriter().print("ooooookkkkkkkkkk~~~~~~~~~~~~~");
		req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, resp);
	}

}
