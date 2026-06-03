package com.org.servlet.admin;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.org.dao.DoctorDao;

@WebServlet("/deleteDoctor")
public class DeleteDoctor extends HttpServlet{
 
 	@Override
 	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       	 
       	int id = Integer.parseInt(req.getParameter("id")) ;
       	
       	DoctorDao dao = new DoctorDao() ;
       	HttpSession hs = req.getSession() ;
       	
       	if(dao.deleteDoctor(id)) {
            	hs.setAttribute("succMsg", "Doctor deleted Successfully");
            	resp.sendRedirect("admin/view_doctor.jsp");
       	}else {
            	hs.setAttribute("errorMsg", "Something Went Wrong");
            	resp.sendRedirect("admin/view_doctor.jsp");
       	}
 	}
 	
}
 
