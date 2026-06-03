package com.org.servlet.user;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.org.dao.UserDao;
import com.org.entity.User;
@WebServlet("/userLogin")
public class UserLogin extends HttpServlet{
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		
		UserDao dao = new  UserDao();
		User u = dao.Login(email, password);
		HttpSession hs = req.getSession();
		
		if(u!=null)
		{
			hs.setAttribute("userObj", u);
			resp.sendRedirect("index.jsp");
		}
		else
		{
			hs.setAttribute("errorMsg", "invalid user or password");
			resp.sendRedirect("user_login.jsp");
		}
	}

}
