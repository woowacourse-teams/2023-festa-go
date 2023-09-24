package com.festago.presentation;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.UnauthorizedException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/admin")
@Hidden
public class AdminViewController {

    @GetMapping
    public String adminPage() {
        return "admin/admin-page";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "admin/signup";
    }

    @GetMapping("/create-festival")
    public String createFestivalPage() {
        return "admin/create-festival";
    }

    @GetMapping("/create-school")
    public String createSchoolPage() {
        return "admin/school/create-school";
    }

    @GetMapping("/modify-school")
    public String updateSchoolPage() {
        return "admin/school/modify-school";
    }

    @ExceptionHandler(UnauthorizedException.class)
    public View handle(UnauthorizedException e, HttpServletResponse response) {

        if (e.getErrorCode() == ErrorCode.EXPIRED_AUTH_TOKEN) {
            return new RedirectView("/admin/login");
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return new InternalResourceView("/error/404");
    }
}
