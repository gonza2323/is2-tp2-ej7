package tinder.tindermascotas.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {
    @RequestMapping(value = "/error", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView renderErrorPage(HttpServletRequest request) {
        ModelAndView errorPage = new ModelAndView("error");
        String errorMsg = "";
        int httpErrorCode = (int) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        switch (httpErrorCode) {
            case 400: {
                errorMsg = "El recurso solicitado no existe";
                break;
            }
            case 401: {
                errorMsg = "No se encuentra autorizado";
                break;
            }
            case 403: {
                errorMsg = "No tiene permisos para acceder al recurso";
                break;
            }
            case 404: {
                errorMsg = "Recurso no encontrado";
                break;
            }
            case 500: {
                errorMsg = "Error en el servidor";
                break;
            }
        }
        errorPage.addObject("codigo", httpErrorCode);
        errorPage.addObject("mensaje", errorMsg);
        return errorPage;
    }
}
