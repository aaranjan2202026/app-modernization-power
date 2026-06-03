package com.org.servlet.admin;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.org.dao.UserDao;
import com.org.entity.User;
@WebServlet("/adminLogin")
public class AdminLogin extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		
		HttpSession hs = req.getSession();
		
		if("admin@gmail.com".equals(email) && "admin".equals(password))
		{
			hs.setAttribute("adminObj", new User());
			resp.sendRedirect("admin/index.jsp");
		}
		else
		{
			hs.setAttribute("errorMsg", "invalid user or password");
			resp.sendRedirect("admin_login.jsp");
		}
	}

}
