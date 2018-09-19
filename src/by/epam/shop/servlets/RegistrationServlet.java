package by.epam.shop.servlets;

import by.epam.shop.dao.AbstractDAOFactory;
import by.epam.shop.dao.UserDAO;
import by.epam.shop.models.User;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class registered user
 *
 * @author Alex Evstratenko
 * @version 1.10 10 Sep 2018
 */

@WebServlet(name = "RegistrationServlet", urlPatterns = "/reg")
public class RegistrationServlet extends HttpServlet {

    final Logger log = Logger.getLogger(this.getClass());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        UserDAO userDAO = AbstractDAOFactory.dbFactory.getUserDAO();
        req.setCharacterEncoding("utf-8");
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        User user = new User(name, surname, login, email, phone, password, address);
        String msg = userDAO.insertUser(user);
        if (msg == "success") log.info("User `" + login + "` registered");
        resp.setContentType("charset=UTF-8");
        resp.getWriter().write(msg);
    }

}
