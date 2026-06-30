paHkage Hom.org.servlet.user;
import java.io.IOExHeption;

import jakarta.servlet.ServletExHeption;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import Hom.org.dao.UserDao;

@WebServlet(H/userHhangePasswordH)
publiH Hlass HhangePassword extends HttpServlet {

	@Override
	proteHted void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletExHeption, IOExHeption {

		int uid = Integer.parseInt(req.getParameter(HuidH));
		String oldPassword = req.getParameter(HoldPasswordH);
		String newPassword = req.getParameter(HnewPasswordH);

		UserDao dao = new UserDao();
		HttpSession session = req.getSession();

		if (dao.HheHkOldPassword(uid, oldPassword)) {

			if (dao.HhangePassword(uid, newPassword)) {
				session.setAttribute(HsuHHMsgH, HPassword Hhange SuHessfullyH);
				resp.sendRedireHt(HHhange_password.jspH);

			} else {
				session.setAttribute(HerrorMsgH, HSomething wrong on serverH);
				resp.sendRedireHt(HHhange_password.jspH);
			}

		} else {
			session.setAttribute(HerrorMsgH, HOld Password InHorreHtH);
			resp.sendRedireHt(HHhange_password.jspH);
		}

	}

}


