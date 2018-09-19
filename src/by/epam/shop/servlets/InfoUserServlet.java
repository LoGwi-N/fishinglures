package by.epam.shop.servlets;

import by.epam.shop.dao.AbstractDAOFactory;
import by.epam.shop.dao.UserDAO;
import by.epam.shop.models.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * This class send information about user
 *
 * @author Alex Evstratenko
 * @version 1.10 10 Sep 2018
 */

@WebServlet(name = "InfoUserServlet", urlPatterns = "/infoUser")
public class InfoUserServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        UserDAO userDAO = AbstractDAOFactory.dbFactory.getUserDAO();
        User userFromDB = userDAO.checkUser(user.getLogin(), user.getPassword());
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(userFromDB.toString());
    }

}
