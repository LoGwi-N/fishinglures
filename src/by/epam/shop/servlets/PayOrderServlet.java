package by.epam.shop.servlets;

import by.epam.shop.dao.AbstractDAOFactory;
import by.epam.shop.dao.OrderDAO;
import by.epam.shop.models.User;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * This class switch status of order to PAID
 *
 * @author Alex Evstratenko
 * @version 1.10 10 Sep 2018
 */

@WebServlet(name = "PayOrderServlet", urlPatterns = "/payOrder")
public class PayOrderServlet extends HttpServlet {

    final Logger log = Logger.getLogger(this.getClass());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String str = request.getParameter("id");
        User user = (User) session.getAttribute("user");
        int userId = user.getId();
        int orderID = Integer.parseInt(str);

        OrderDAO orderDAO = AbstractDAOFactory.dbFactory.getOrderDAO();
        String msg = String.valueOf(orderDAO.payOrder(userId, orderID));
        response.setContentType("charset=UTF-8");
        log.info("User `" + user.getLogin() + "` pay order# " + orderID);
        response.getWriter().write(msg);
    }

}
