package by.epam.shop.servlets;

import by.epam.shop.dao.AbstractDAOFactory;
import by.epam.shop.dao.ProductDAO;
import by.epam.shop.models.Product;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * This class send information about all products using in admin panel
 *
 * @author Alex Evstratenko
 * @version 1.10 10 Sep 2018
 */

@WebServlet("/allProducts")

public class ShowAllProductsServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ProductDAO productDAO = AbstractDAOFactory.dbFactory.getProductDAO();
        List<Product> productList = productDAO.getAll();
        resp.setCharacterEncoding("UTF-8");
        StringBuffer sb = new StringBuffer("{");
        String sign = "";
        for (Product pr : productList) {
            sb.append(sign + "\"" + pr.getId() + "\": " + pr.toString());
            sign = ",";
        }
        sb.append("}");
        resp.getWriter().write(String.valueOf(sb));
    }
}
