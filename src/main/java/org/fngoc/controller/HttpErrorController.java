package org.fngoc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Controller
public class HttpErrorController implements ErrorController {

    private final Logger LOGGER = LoggerFactory.getLogger(HttpErrorController.class);

    @RequestMapping("/error")
    public String handleError(Locale locale, Model model, HttpServletRequest request, Exception ex) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Map<String, String> metaData = new HashMap<>();

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                LOGGER.info("Called 403");
                metaData.put("errorName", "Forbidden 403");
            }
            else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                LOGGER.info("Called 404");
                metaData.put("errorName", "Not Found 404");
            }
            else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                LOGGER.info("Called 500");
                metaData.put("errorName", "Internal Server Error 500");
            }
        }
        model.addAllAttributes(metaData);
        return "error/error";
    }

}
