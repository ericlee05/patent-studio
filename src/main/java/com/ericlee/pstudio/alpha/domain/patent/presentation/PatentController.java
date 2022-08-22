package com.ericlee.pstudio.alpha.domain.patent.presentation;

import com.ericlee.pstudio.alpha.domain.patent.exception.PatentNotFoundException;
import com.ericlee.pstudio.alpha.domain.patent.presentation.dto.request.PatentCreationRequest;
import com.ericlee.pstudio.alpha.domain.patent.presentation.dto.response.PatentResponse;
import com.ericlee.pstudio.alpha.domain.patent.service.PatentEditorService;
import com.ericlee.pstudio.alpha.domain.patent.service.PatentSimpleService;
import com.ericlee.pstudio.alpha.domain.patent.service.PatentViewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/patents")
@Controller
public class PatentController {
    private final PatentViewService patentViewService;
    private final PatentSimpleService patentSimpleService;
    private final PatentEditorService patentEditorService;

    @GetMapping
    public String patentDashboard(Model model) {
        patentViewService.patentDashboard(model);
        model.addAttribute("template", "patent/view");

        return "global/dashboard_frame";
    }

    @ResponseBody
    @GetMapping("/json")
    public List<PatentResponse> getPatentsWithJson() {
        return patentViewService.getPatentsResponse();
    }

    @PostMapping
    public void createPatent(@ModelAttribute PatentCreationRequest request, HttpServletResponse response) {
        patentSimpleService.createPatent(request, response);
    }

    @GetMapping("/{patent-id}")
    public String patentDetail(@PathVariable("patent-id") Long patentId, Model model) {
        patentViewService.patentDetail(patentId, model);
        model.addAttribute("template", "patent/view_detail");
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

    @GetMapping("/{patent-id}/editor")
    public String editor(@PathVariable("patent-id") Long patentId, Model model) {
        patentEditorService.loadEditor(patentId, model);
        model.addAttribute("template", "patent/editor");
        return "global/dashboard_frame";
    }

    @ResponseBody
    @GetMapping("/{patent-id}/drawings/{drawing-id}")
    public ResponseEntity<byte[]> getDrawing(@PathVariable("patent-id") Long patentId, @PathVariable("drawing-id") Long drawingId) {
        return patentEditorService.getDrawingFile(patentId, drawingId);
    }

}
