package com.ericlee.pstudio.alpha.domain.development.presentation;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@RequestMapping("/development")
@Controller
public class DevelopmentController {
    @GetMapping("/{path}/{file}")
    public String getView(@PathVariable("path") String base, @PathVariable("file") String fileName, Model model) {
        String resource = String.format("%s/%s", base, fileName);
        log.info(resource);
        model.addAttribute("title", "페이지: " + fileName);
        model.addAttribute("template", resource);

        return "global/dashboard_frame";
    }
}
