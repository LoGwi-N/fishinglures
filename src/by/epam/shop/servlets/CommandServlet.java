package by.epam.shop.servlets;

import by.epam.shop.dao.AbstractDAOFactory;
import by.epam.shop.dao.OrderDAO;
import by.epam.shop.dao.ProductDAO;
import by.epam.shop.dao.UserDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This universal class of some commands.
 *
 * @author Alex Evstratenko
 * @version 1.10 10 Sep 2018
 */

@WebServlet(name = "CommandServlet", urlPatterns = "/command")
public class CommandServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        int id = Integer.parseInt(req.getParameter("id"));
        String model = req.getParameter("model");
        String command = req.getParameter("command");
        String msg = null;
        switch (model) {
            case "user": {
                UserDAO userDAO = AbstractDAOFactory.dbFactory.getUserDAO();
                switch (command) {
                    case "statusActive": {
                        msg = String.valueOf(userDAO.changeStatusUser(id, 1));
                        break;
                    }
                    case "statusBlackList": {
                        msg = String.valueOf(userDAO.changeStatusUser(id, 2));
                        break;
                    }
                    case "statusBanned": {
                        msg = String.valueOf(userDAO.changeStatusUser(id, 0));
                        break;
                    }
                }
                break;
            }
            case "product": {
                ProductDAO productDAO = AbstractDAOFactory.dbFactory.getProductDAO();
                msg = String.valueOf(productDAO.commandProduct(id, command));
                break;
            }
            case "order": {
                OrderDAO orderDAO = AbstractDAOFactory.dbFactory.getOrderDAO();
                msg = String.valueOf(orderDAO.changeStatusOrder(id));
                break;
            }
        }
        req.setCharacterEncoding("utf-8");
        resp.getWriter().write(msg);
    }

}
