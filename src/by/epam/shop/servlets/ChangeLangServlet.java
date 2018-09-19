package by.epam.shop.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "ChangeLangServlet", urlPatterns = "/changeLangServlet")
public class ChangeLangServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        String lang = request.getParameter("lang");
        response.setCharacterEncoding("UTF-8");
        if (lang != null) {
            session.setAttribute("locale", lang);
            response.getWriter().write("true");
        } else response.getWriter().write("false");
    }

}
