package com.ericlee.pstudio.alpha.domain.patent.presentation;

import com.ericlee.pstudio.alpha.domain.patent.service.PatentViewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/patents")
@Controller
public class PatentController {
    private final PatentViewService patentViewService;

    @GetMapping
    public String patentDashboard(Model model) {
        patentViewService.patentDashboard(model);
        model.addAttribute("template", "patent/view");

        return "global/dashboard_frame";
    }

    @GetMapping("/{patent-id}")
    public String patentDetail(@PathVariable("patent-id") Long patentId, Model model) {
        

        model.addAttribute("template", "patent/detailViewer");
        return "global/dashboard_frame";
    }

}
