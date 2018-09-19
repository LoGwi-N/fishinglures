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
 * This class add product to catalog
 *
 * @author Alex Evstratenko
 * @version 1.10 10 Sep 2018
 */

@WebServlet(name = "AddProductsServlet", urlPatterns = "/addProduct")

public class AddProductsServlet extends HttpServlet {

    final Logger log = Logger.getLogger(this.getClass());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ProductDAO productDAO = AbstractDAOFactory.dbFactory.getProductDAO();
        String title = req.getParameter("title");
        String desc = req.getParameter("desc");
        String price = req.getParameter("price");
        String amount = req.getParameter("amount");
        Product product = new Product(title, desc, Integer.parseInt(price), Integer.parseInt(amount));
        log.info("Product was added: " + product);
        String msg = String.valueOf(productDAO.insertProduct(product));
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(msg);
    }
}
