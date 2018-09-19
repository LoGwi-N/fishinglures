package by.epam.shop.servlets;

import by.epam.shop.service.MessageManager;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

@WebServlet(name = "LocalizationServlet", urlPatterns = "/localization")
public class LocalizationServlet extends HttpServlet {

    final Logger log = Logger.getLogger(this.getClass());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        String lang = String.valueOf(session.getAttribute("locale"));

        if (lang == null) {
            Locale locale = request.getLocale();
            lang = String.valueOf(locale);
        }
        Map<String, String> resourceBundleToMap;
        switch (lang) {
            case "ru": {
                resourceBundleToMap = MessageManager.convertResourceBundleToMap(MessageManager.bundleRU);
                break;
            }
            case "en": {
                resourceBundleToMap = MessageManager.convertResourceBundleToMap(MessageManager.bundleEN);
                break;
            }
            default: {
                resourceBundleToMap = MessageManager.convertResourceBundleToMap(MessageManager.bundleDefault);
                break;
            }
        }

        String localizationJSON = MessageManager.createJSON(resourceBundleToMap);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.valueOf(localizationJSON));
    }

}
