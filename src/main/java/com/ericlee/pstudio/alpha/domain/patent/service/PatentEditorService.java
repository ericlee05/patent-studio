package com.ericlee.pstudio.alpha.domain.patent.service;

import com.ericlee.pstudio.alpha.domain.patent.entity.Patent;
import com.ericlee.pstudio.alpha.domain.patent.exception.PatentComponentNotFoundException;
import com.ericlee.pstudio.alpha.domain.patent.exception.PatentNotFoundException;
import com.ericlee.pstudio.alpha.domain.patent.repository.PatentRepository;
import com.ericlee.pstudio.alpha.domain.patent.type.SingleComponentType;
import com.ericlee.pstudio.alpha.domain.user.entity.User;
import com.ericlee.pstudio.alpha.domain.user.exception.UnauthorizedUserException;
import com.ericlee.pstudio.alpha.domain.user.facade.UserFacade;
import com.ericlee.pstudio.alpha.global.aop.ValidPatentAccessAspect;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.Arrays;

@RequiredArgsConstructor
@Service
public class PatentEditorService {
    private final PatentRepository patentRepository;
    private final UserFacade userFacade;

    @ValidPatentAccessAspect.ValidPatentAccess
    @Transactional(readOnly = true)
    public void loadEditor(@ValidPatentAccessAspect.PatentId Long patentId, Model model) {
        Patent patent = patentRepository.findById(patentId)
                .orElseThrow(PatentNotFoundException::new);

        model.addAttribute("title", String.format("수정 '%s'", patent.getKoreanName()));
        model.addAttribute("patent", patent);
    }

    @ValidPatentAccessAspect.ValidPatentAccess
    @Transactional(readOnly = true)
    public String getSingleComponent(@ValidPatentAccessAspect.PatentId Long patentId, Integer typeOrdinal) {
        Patent patent = patentRepository.findById(patentId)
                .orElseThrow(PatentNotFoundException::new);

        SingleComponentType type = Arrays.stream(SingleComponentType.values()).filter(it -> it.ordinal() == typeOrdinal).findFirst()
                .orElseThrow(PatentComponentNotFoundException::new);

        return patent.getSingleComponentByType(type).getContent();
    }

    @ValidPatentAccessAspect.ValidPatentAccess
    @Transactional
    public void updateSingleComponent(@ValidPatentAccessAspect.PatentId Long patentId, Integer typeOrdinal, String jsonData) {
        User user = userFacade.queryCurrentUser(true)
                .orElseThrow(UnauthorizedUserException::new);

        Patent patent = patentRepository.findById(patentId)
                .orElseThrow(PatentNotFoundException::new);

        SingleComponentType type = Arrays.stream(SingleComponentType.values()).filter(it -> it.ordinal() == typeOrdinal).findFirst()
                .orElseThrow(PatentComponentNotFoundException::new);

        patent.getSingleComponentByType(type).update(user, jsonData);
    }
}
