package com.service.net_security_fp.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApiController {

    private final Counter publicPageCounter;
    private final Counter privatePageCounter;

    public ApiController(MeterRegistry registry) {
        this.publicPageCounter = Counter.builder("app.pages.views")
                .tag("type", "public")
                .description("Количество просмотров главной страницы")
                .register(registry);
        this.privatePageCounter = Counter.builder("app.pages.views")
                .tag("type", "private")
                .description("Количество успешных входов в приватную зону")
                .register(registry);
    }

    @GetMapping("/")
    public String publicPage() {
        publicPageCounter.increment();
        return "index";
    }

    @GetMapping("/private")
    public String privatePage(Model model, @AuthenticationPrincipal OAuth2User principal) {
        privatePageCounter.increment();
        if (principal != null) {
            model.addAttribute("name", principal.getAttribute("name"));
        }
        return "private";
    }
}
