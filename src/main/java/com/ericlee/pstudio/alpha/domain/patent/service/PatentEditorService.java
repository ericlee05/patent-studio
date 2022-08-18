package com.ericlee.pstudio.alpha.domain.patent.service;

import com.ericlee.pstudio.alpha.domain.patent.entity.MultiComponent;
import com.ericlee.pstudio.alpha.domain.patent.entity.MultiComponentId;
import com.ericlee.pstudio.alpha.domain.patent.entity.Patent;
import com.ericlee.pstudio.alpha.domain.patent.exception.PatentComponentNotFoundException;
import com.ericlee.pstudio.alpha.domain.patent.exception.PatentNotFoundException;
import com.ericlee.pstudio.alpha.domain.patent.presentation.dto.common.MultiComponentDto;
import com.ericlee.pstudio.alpha.domain.patent.repository.MultiComponentRepository;
import com.ericlee.pstudio.alpha.domain.patent.repository.PatentRepository;
import com.ericlee.pstudio.alpha.domain.patent.type.MultiComponentType;
import com.ericlee.pstudio.alpha.domain.patent.type.SingleComponentType;
import com.ericlee.pstudio.alpha.domain.user.entity.User;
import com.ericlee.pstudio.alpha.domain.user.exception.UnauthorizedUserException;
import com.ericlee.pstudio.alpha.domain.user.facade.UserFacade;
import com.ericlee.pstudio.alpha.global.aop.ValidPatentAccessAspect;
import com.swcns.reflcrypt.annotation.DecryptReturns;
import com.swcns.reflcrypt.annotation.EncryptParams;
import com.swcns.reflcrypt.annotation.SecurityParam;
import com.swcns.reflcrypt.util.ObjectDecryptor;
import com.swcns.reflcrypt.util.ObjectEncryptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Arrays;
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
}
