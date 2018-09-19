package by.epam.shop.servlets;

import by.epam.shop.dao.AbstractDAOFactory;
import by.epam.shop.dao.UserDAO;
import by.epam.shop.models.User;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * This class responsible for redirect user by Role
 *
 * @author Alex Evstratenko
 * @version 1.10 10 Sep 2018
 */

@WebServlet(name = "UserServlet", urlPatterns = "/auth")
public class UserServlet extends HttpServlet {

    final Logger log = Logger.getLogger(this.getClass());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        UserDAO userDAO = AbstractDAOFactory.dbFactory.getUserDAO();
        User user = userDAO.checkUser(login, password);
        log.info("USER: " + user.getLogin() + " logged" + " IP: " + request.getRemoteAddr());
        if (user.getLogin() != null) {
            if (user.getRole() == User.Role.ADMIN) {
                session.setAttribute("user", user);
                response.sendRedirect("/admin.html");
            } else if (user.getRole() == User.Role.USER) {
                if (user.getStatus() != 2) {
                    session.setAttribute("user", user);
                    response.sendRedirect("/user.html");

                } else {
                    RequestDispatcher rd = request.getRequestDispatcher("/banned.html");
                    rd.forward(request, response);
                }
            }
        } else {
            response.sendRedirect("/index.html");
        }
    }
}
