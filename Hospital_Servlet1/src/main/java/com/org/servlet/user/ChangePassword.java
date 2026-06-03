package com.org.servlet.user;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.org.dao.UserDao;

@WebServlet("/userChangePassword")
public class ChangePassword extends HttpServlet {

	private static final String ERROR_MSG_ATTR = "errorMsg";
	private static final String CHANGE_PASSWORD_PAGE = "change_password.jsp";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		int uid = Integer.parseInt(req.getParameter("uid"));
		String oldPassword = req.getParameter("oldPassword");
		String newPassword = req.getParameter("newPassword");

		UserDao dao = new UserDao();
		HttpSession session = req.getSession();

		if (dao.checkOldPassword(uid, oldPassword)) {

			if (dao.changePassword(uid, newPassword)) {
				session.setAttribute("succMsg", "Password Change Sucessfully");
				resp.sendRedirect(CHANGE_PASSWORD_PAGE);

			} else {
				session.setAttribute(ERROR_MSG_ATTR, "Something wrong on server");
				resp.sendRedirect(CHANGE_PASSWORD_PAGE);
			}

		} else {
			session.setAttribute(ERROR_MSG_ATTR, "Old Password Incorrect");
			resp.sendRedirect(CHANGE_PASSWORD_PAGE);
		}

	}

}

