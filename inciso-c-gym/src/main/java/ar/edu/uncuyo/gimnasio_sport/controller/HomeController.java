package ar.edu.uncuyo.gimnasio_sport.controller;

import ar.edu.uncuyo.gimnasio_sport.dto.LoginDto;
import ar.edu.uncuyo.gimnasio_sport.dto.SocioCreateFormDto;
import ar.edu.uncuyo.gimnasio_sport.error.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class HomeController {
    @GetMapping({"/", "/about", "/blog", "/contact", "/elements", "/gallery", "/pricing", "/single-blog"})
    public String page(Principal principal, HttpServletRequest request) {
        String pageName = request.getRequestURI().substring(1);
        return pageName.isEmpty() ? "home" : pageName;
    }


}
