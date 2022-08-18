package com.ericlee.pstudio.alpha.domain.patent.presentation;

import com.ericlee.pstudio.alpha.domain.patent.presentation.dto.common.MultiComponentDto;
import com.ericlee.pstudio.alpha.domain.patent.service.PatentEditorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
