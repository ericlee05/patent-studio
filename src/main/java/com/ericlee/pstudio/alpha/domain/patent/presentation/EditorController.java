package com.ericlee.pstudio.alpha.domain.patent.presentation;

import com.ericlee.pstudio.alpha.domain.patent.presentation.dto.common.MultiComponentDto;
import com.ericlee.pstudio.alpha.domain.patent.presentation.dto.request.DrawingUpdateRequest;
import com.ericlee.pstudio.alpha.domain.patent.presentation.dto.response.ClaimResponse;
import com.ericlee.pstudio.alpha.domain.patent.presentation.dto.response.ClaimUpdateRequest;
import com.ericlee.pstudio.alpha.domain.patent.service.PatentEditorService;
import com.swcns.reflcrypt.annotation.SecurityParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/edit-patent")
@RestController
public class EditorController {
    private final PatentEditorService editorService;

    @GetMapping("/{patent-id}/single/{type-ordinal}")
    public String getSingleComponent(@PathVariable("patent-id") Long patentId, @PathVariable("type-ordinal") Integer typeOrdinal) {
        return editorService.getSingleComponent(patentId, typeOrdinal);
    }
    @PostMapping("/{patent-id}/single/{type-ordinal}")
    public void updateSingleComponent(@PathVariable("patent-id") Long patentId, @PathVariable("type-ordinal") Integer typeOrdinal, @RequestBody String jsonBody) {
        editorService.updateSingleComponent(patentId, typeOrdinal, jsonBody);
    }

    @GetMapping("/{patent-id}/multi/{type-ordinal}")
    public List<MultiComponentDto> getMultiComponents(@PathVariable("patent-id") Long patentId, @PathVariable("type-ordinal") Integer typeOrdinal) {
        return editorService.getMultiComponents(patentId, typeOrdinal);
    }

    @PostMapping("/{patent-id}/multi/{type-ordinal}")
    public void updateMultiComponents(@PathVariable("patent-id") Long patentId, @PathVariable("type-ordinal") Integer typeOrdinal, @RequestBody List<MultiComponentDto> multiComponents) {
        editorService.updateMultiComponent(patentId, typeOrdinal, multiComponents);
    }

    @PostMapping("/{patent-id}/drawings")
    public void uploadDrawing(@PathVariable("patent-id") Long patentId, @RequestParam("drawing") MultipartFile file, HttpServletResponse response) {
        editorService.uploadDrawing(patentId, file, response);
    }

    @PostMapping("/{patent-id}/drawings/{drawing-identifier}")
    public void updateDrawing(@PathVariable("patent-id") Long patentId, @PathVariable("drawing-identifier") Long identifier, @ModelAttribute DrawingUpdateRequest request, HttpServletResponse response) {
        editorService.updateDrawing(patentId, identifier, request, response);
    }

    @DeleteMapping("/{patent-id}/drawings/{drawing-identifier}")
    public void deleteDrawing(@PathVariable("patent-id") Long patentId, @PathVariable("drawing-identifier") Long identifier, HttpServletResponse response) {
        editorService.deleteDrawing(patentId, identifier, response);
    }

    @PatchMapping("/{patent-id}/drawings/{drawing-identifier}/representative")
    public void setDrawingAsRepresentative(@PathVariable("patent-id") Long patentId, @PathVariable("drawing-identifier") Long identifier) {
        editorService.setDrawingAsRepresentative(patentId, identifier);
    }

    @GetMapping("/{patent-id}/claims")
    public List<ClaimResponse> getClaims(@PathVariable("patent-id") Long patentId) {
        return editorService.getClaims(patentId);
    }

    @PostMapping("/{patent-id}/claims")
    public void updateClaims(@PathVariable("patent-id") Long patentId, @RequestBody ClaimUpdateRequest request) {
        editorService.updateClaims(patentId, request);
    }

}
