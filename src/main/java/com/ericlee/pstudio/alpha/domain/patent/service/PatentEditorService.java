package com.ericlee.pstudio.alpha.domain.patent.service;

import com.ericlee.pstudio.alpha.domain.patent.entity.*;
import com.ericlee.pstudio.alpha.domain.patent.exception.PatentComponentNotFoundException;
import com.ericlee.pstudio.alpha.domain.patent.exception.PatentNotFoundException;
import com.ericlee.pstudio.alpha.domain.patent.presentation.dto.common.MultiComponentDto;
import com.ericlee.pstudio.alpha.domain.patent.presentation.dto.request.DrawingUpdateRequest;
import com.ericlee.pstudio.alpha.domain.patent.presentation.dto.response.ClaimResponse;
import com.ericlee.pstudio.alpha.domain.patent.presentation.dto.response.ClaimUpdateRequest;
import com.ericlee.pstudio.alpha.domain.patent.repository.MultiComponentRepository;
import com.ericlee.pstudio.alpha.domain.patent.repository.PatentRepository;
import com.ericlee.pstudio.alpha.domain.patent.type.MultiComponentType;
import com.ericlee.pstudio.alpha.domain.patent.type.SingleComponentType;
import com.ericlee.pstudio.alpha.domain.user.entity.User;
import com.ericlee.pstudio.alpha.domain.user.exception.UnauthorizedUserException;
import com.ericlee.pstudio.alpha.domain.user.facade.UserFacade;
import com.ericlee.pstudio.alpha.global.aop.ValidPatentAccessAspect;
import com.ericlee.pstudio.alpha.global.exception.CustomException;
import com.ericlee.pstudio.alpha.global.utils.HttpUtil;
import com.swcns.reflcrypt.annotation.DecryptReturns;
import com.swcns.reflcrypt.annotation.EncryptParams;
import com.swcns.reflcrypt.annotation.SecurityParam;
import com.swcns.reflcrypt.util.ObjectDecryptor;
import com.swcns.reflcrypt.util.ObjectEncryptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class PatentEditorService {
    private final PatentRepository patentRepository;
    private final MultiComponentRepository multiComponentRepository;
    private final UserFacade userFacade;
    private final ObjectDecryptor objectDecryptor;
    private final ObjectEncryptor objectEncryptor;
    private final HttpUtil httpUtil;

    @ValidPatentAccessAspect.ValidPatentAccess
    @Transactional(readOnly = true)
    public void loadEditor(@ValidPatentAccessAspect.PatentId Long patentId, Model model) {
        Patent patent = patentRepository.findById(patentId)
                .orElseThrow(PatentNotFoundException::new);

        model.addAttribute("title", String.format("수정 '%s'", patent.getKoreanName()));
        model.addAttribute("patent", patent);
    }

    @DecryptReturns
    @ValidPatentAccessAspect.ValidPatentAccess
    @Transactional(readOnly = true)
    public String getSingleComponent(@ValidPatentAccessAspect.PatentId Long patentId, Integer typeOrdinal) {
        Patent patent = patentRepository.findById(patentId)
                .orElseThrow(PatentNotFoundException::new);

        SingleComponentType type = Arrays.stream(SingleComponentType.values()).filter(it -> it.ordinal() == typeOrdinal).findFirst()
                .orElseThrow(PatentComponentNotFoundException::new);

        return patent.getSingleComponentByType(type).getContent();
    }

    @EncryptParams
    @ValidPatentAccessAspect.ValidPatentAccess
    @Transactional
    public void updateSingleComponent(@ValidPatentAccessAspect.PatentId Long patentId, Integer typeOrdinal, @SecurityParam String jsonData) {
        User user = userFacade.queryCurrentUser(true)
                .orElseThrow(UnauthorizedUserException::new);

        Patent patent = patentRepository.findById(patentId)
                .orElseThrow(PatentNotFoundException::new);

        SingleComponentType type = Arrays.stream(SingleComponentType.values()).filter(it -> it.ordinal() == typeOrdinal).findFirst()
                .orElseThrow(PatentComponentNotFoundException::new);

        patent.getSingleComponentByType(type).update(user, jsonData);
    }

    @ValidPatentAccessAspect.ValidPatentAccess
    @Transactional(readOnly = true)
    public List<MultiComponentDto> getMultiComponents(@ValidPatentAccessAspect.PatentId Long patentId, Integer typeOrdinal) {
        Patent patent = patentRepository.findById(patentId)
                .orElseThrow(PatentNotFoundException::new);

        MultiComponentType type = Arrays.stream(MultiComponentType.values()).filter(it -> it.ordinal() == typeOrdinal).findFirst()
                .orElseThrow(PatentComponentNotFoundException::new);

        return patent.getMultiComponentsByType(type).stream().map(it -> objectDecryptor.getDecryptedObject(
                new MultiComponentDto(it.getId().getComponentIdentifier(), it.getContent())
                ))
                .collect(Collectors.toList());
    }

    @ValidPatentAccessAspect.ValidPatentAccess
    @Transactional
    public void updateMultiComponent(@ValidPatentAccessAspect.PatentId Long patentId, Integer typeOrdinal, List<MultiComponentDto> changeData) {
        User user = userFacade.queryCurrentUser(true)
                .orElseThrow(UnauthorizedUserException::new);

        Patent patent = patentRepository.findById(patentId)
                .orElseThrow(PatentNotFoundException::new);

        MultiComponentType type = Arrays.stream(MultiComponentType.values()).filter(it -> it.ordinal() == typeOrdinal).findFirst()
                .orElseThrow(PatentComponentNotFoundException::new);

        multiComponentRepository.deleteByPatentAndType(patent, type);
        patent.updateMultiComponents(user, changeData.stream().map(it ->
                MultiComponent.builder()
                        .id(MultiComponentId.builder()
                                .patent(patent)
                                .multiComponentType(type)
                                .componentIdentifier(it.getIdentifier())
                                .build())
                        .content(objectEncryptor.getEncryptedObject(it.getContent()))
                        .order(changeData.indexOf(it))
                        .build()).collect(Collectors.toList()));
    }

    @ValidPatentAccessAspect.ValidPatentAccess
    @Transactional
    public ResponseEntity<byte[]> getDrawingFile(@ValidPatentAccessAspect.PatentId Long patentId, Long drawingId) {
        Patent patent = patentRepository.findById(patentId)
                .orElseThrow(PatentNotFoundException::new);

        PatentDrawing drawing = patent.getDrawings().stream().filter(it -> it.getId().getDrawingId().equals(drawingId))
                .findFirst()
                .orElseThrow();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(String.format("%s.png", drawing), StandardCharsets.UTF_8)
                .build());

        return new ResponseEntity<byte[]>(drawing.getImage(), headers, HttpStatus.OK);
    }

    @ValidPatentAccessAspect.ValidPatentAccess
    @Transactional
    public void uploadDrawing(@ValidPatentAccessAspect.PatentId Long patentId, MultipartFile imageFile, HttpServletResponse response) {
        Patent patent = patentRepository.findById(patentId)
                .orElseThrow(PatentNotFoundException::new);

        try {
            PatentDrawing drawing = PatentDrawing.builder()
                    .id(DrawingId.builder()
                            .patent(patent)
                            .drawingId(System.currentTimeMillis())
                            .build())
                    .image(imageFile.getBytes())
                    .identifier(String.format("도면 %s", imageFile.getOriginalFilename().substring(0, Math.min(10, imageFile.getOriginalFilename().length() - 1))))
                    .description("도면의 설명")
                    .isRepresentative(false)
                    .build();
            patent.getDrawings().add(drawing);
        } catch (IOException ex) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류");
        }

        httpUtil.redirectTo(response, String.format("/patents/%d/editor", patentId));
    }

    @ValidPatentAccessAspect.ValidPatentAccess
    @Transactional
    public void updateDrawing(@ValidPatentAccessAspect.PatentId Long patentId, Long drawingId, DrawingUpdateRequest request, HttpServletResponse response) {
        User user = userFacade.queryCurrentUser(true)
                .orElseThrow(UnauthorizedUserException::new);

        Patent patent = patentRepository.findById(patentId)
                .orElseThrow(PatentNotFoundException::new);

        PatentDrawing drawing = patent.getDrawings().stream().filter(it -> it.getId().getDrawingId().equals(drawingId))
                .findFirst()
                .orElseThrow();

        drawing.update(user, request.getIdentifier(), request.getDescription());
        httpUtil.redirectTo(response, String.format("/patents/%d/editor", patentId));
    }

    @ValidPatentAccessAspect.ValidPatentAccess
    @Transactional
    public void setDrawingAsRepresentative(@ValidPatentAccessAspect.PatentId Long patentId, Long drawingId) {
        User user = userFacade.queryCurrentUser(true)
                .orElseThrow(UnauthorizedUserException::new);

        Patent patent = patentRepository.findById(patentId)
                .orElseThrow(PatentNotFoundException::new);

        PatentDrawing drawing = patent.getDrawings().stream().filter(it -> it.getId().getDrawingId().equals(drawingId))
                .findFirst()
                .orElseThrow();

        patent.getDrawings().forEach(drawingItem -> drawingItem.setRepresentative(false));
        drawing.setRepresentative(true);
    }

    @ValidPatentAccessAspect.ValidPatentAccess
    @Transactional
    public void deleteDrawing(@ValidPatentAccessAspect.PatentId Long patentId, Long drawingId, HttpServletResponse response) {
        Patent patent = patentRepository.findById(patentId)
                .orElseThrow(PatentNotFoundException::new);

        PatentDrawing drawing = patent.getDrawings().stream().filter(it -> it.getId().getDrawingId().equals(drawingId))
                .findFirst()
                .orElseThrow();
        patent.getDrawings().remove(drawing);

        httpUtil.redirectTo(response, String.format("/patents/%d/editor", patentId));
    }

    @ValidPatentAccessAspect.ValidPatentAccess
    @Transactional(readOnly = true)
    public List<ClaimResponse> getClaims(@ValidPatentAccessAspect.PatentId Long patentId) {
        Patent patent = patentRepository.findById(patentId)
                .orElseThrow(PatentNotFoundException::new);

        return patent.getClaims().stream()
                .sorted(Comparator.comparingInt(it -> it.getNumber()))
                .map(it -> ClaimResponse.builder()
                        .content(objectDecryptor.getDecryptedObject(it.getContent()))
                        .build())
                .collect(Collectors.toList());
    }

    @ValidPatentAccessAspect.ValidPatentAccess
    @Transactional
    public void updateClaims(@ValidPatentAccessAspect.PatentId Long patentId, ClaimUpdateRequest request) {
        Patent patent = patentRepository.findById(patentId)
                .orElseThrow(PatentNotFoundException::new);

        List<Claim> claims = request.getContent().stream().map(it -> Claim.builder()
                        .parentClaims(new ArrayList<>())
                        .childClaims(new ArrayList<>())
                        .number(request.getContent().indexOf(it))
                        .patent(patent)
                        .content(objectEncryptor.getEncryptedObject(it))
                        .build())
                .collect(Collectors.toList());
        patent.getClaims().addAll(claims);
    }
}
