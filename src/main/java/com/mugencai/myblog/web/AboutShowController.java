package com.mugencai.myblog.web;

import org.hibernate.engine.spi.Mapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutShowController {

    @GetMapping("/about")
    public String AboutPage() {
        return "/about";
    }


}
