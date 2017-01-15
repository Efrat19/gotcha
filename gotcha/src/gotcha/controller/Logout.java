package gotcha.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import gotcha.globals.Globals;
import gotcha.model.User;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(request, response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(request, response);
	}
	
	private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/JSON; charset=UTF-8");
		PrintWriter out = response.getWriter();
		String data;
		data = "{"
			+ 		"\"status\": \"success\","
			+ 		"\"route\": \"login\","
			+ 		"\"notification\": {"
			+ 			"\"selector\": \".logout-notification\","
			+ 			"\"message\": \"Logged out successfully\""
			+ 		"}"
			+  "}"
			;
		HttpSession session = request.getSession();
		updateUserStatus(session);
		session.invalidate();
		out.println(data);
		out.close();
	}
	
	private void updateUserStatus (HttpSession session) {
		User user = (User)session.getAttribute("user");
		ArrayList<Object> values = new ArrayList<Object>();
		ArrayList<Object> where = new ArrayList<Object>();
		
		String status = "away";
		Timestamp last_seen = new Timestamp(System.currentTimeMillis());
		
		values.add(status);
		values.add(last_seen);
		
		where.add(user.username());
		
		Globals.executeUpdate(Globals.UPDATE_USER_STATUS, values, where);
	}
}
