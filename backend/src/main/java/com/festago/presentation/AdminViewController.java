package com.festago.presentation;

import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.UnauthorizedException;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/festivals")
    public String manageFestivalPage() {
        return "admin/festival/manage-festival";
    }

    @GetMapping("/festivals/{festivalId}")
    public String manageFestivalDetailPage(@PathVariable String festivalId) {
        return "admin/festival/manage-festival-detail";
    }

    @GetMapping("/schools")
    public String manageSchoolPage() {
        return "admin/school/manage-school";
    }

    @GetMapping("/schools/{schoolId}")
    public String manageSchoolDetailPage(@PathVariable String schoolId) {
        return "admin/school/manage-school-detail";
    }

    @GetMapping("/stages/{stageId}")
    public String manageStagePage(@PathVariable String stageId) {
        return "admin/stage/manage-stage-detail";
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
