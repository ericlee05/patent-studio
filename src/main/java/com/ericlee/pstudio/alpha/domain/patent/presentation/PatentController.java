package com.ericlee.pstudio.alpha.domain.patent.presentation;

import com.ericlee.pstudio.alpha.domain.patent.exception.PatentNotFoundException;
import com.ericlee.pstudio.alpha.domain.patent.presentation.dto.request.PatentCreationRequest;
import com.ericlee.pstudio.alpha.domain.patent.service.PatentSimpleService;
import com.ericlee.pstudio.alpha.domain.patent.service.PatentViewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/patents")
@Controller
public class PatentController {
    private final PatentViewService patentViewService;
    private final PatentSimpleService patentSimpleService;

    @GetMapping
    public String patentDashboard(Model model) {
        patentViewService.patentDashboard(model);
        model.addAttribute("template", "patent/view");

        return "global/dashboard_frame";
    }

    @PostMapping
    public void createPatent(@ModelAttribute PatentCreationRequest request, HttpServletResponse response) {
        patentSimpleService.createPatent(request, response);
    }

    @GetMapping("/{patent-id}")
    public String patentDetail(@PathVariable("patent-id") Long patentId, Model model) {


        model.addAttribute("template", "patent/detailViewer");
        return "global/dashboard_frame";
    }

    @PostMapping("/{patent-id}/deletion")
    public void deletePatent(@PathVariable("patent-id") Long patentId, HttpServletResponse response) {
        patentSimpleService.deletePatent(patentId, response);
    }

    @ExceptionHandler(PatentNotFoundException.class)
    public String handleSignInFailure(PatentNotFoundException ex, Model model) {
        patentViewService.patentDashboard(model);
        model.addAttribute("template", "patent/view");
        model.addAttribute("message", ex.getMessage());
        return "global/dashboard_frame";
    }

}
