package by.epam.shop.servlets;

import by.epam.shop.dao.AbstractDAOFactory;
import by.epam.shop.dao.ProductDAO;
import by.epam.shop.models.Product;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class edit products
 *
 * @author Alex Evstratenko
 * @version 1.10 10 Sep 2018
 */

@WebServlet(name = "EditProductServlet", urlPatterns = "/editProduct")
public class EditProductServlet extends HttpServlet {

    final Logger log = Logger.getLogger(this.getClass());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ProductDAO productDAO = AbstractDAOFactory.dbFactory.getProductDAO();
        req.setCharacterEncoding("utf-8");
        String id = req.getParameter("id");
        String title = req.getParameter("title");
        String desc = req.getParameter("desc");
        String price = req.getParameter("price");
        String amount = req.getParameter("amount");
        String status = req.getParameter("status");
        Product product = new Product(Integer.parseInt(id), title, desc, Integer.parseInt(price), Integer.parseInt(amount), Integer.parseInt(status));
        log.info("Product was edited: " + product);
        String msg = String.valueOf(productDAO.updateProduct(product));
        resp.getWriter().write(msg);
    }

}
