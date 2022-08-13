package com.ericlee.pstudio.alpha.domain.patent.service;

import com.ericlee.pstudio.alpha.domain.patent.entity.Patent;
import com.ericlee.pstudio.alpha.domain.patent.exception.PatentAccessDeniedException;
import com.ericlee.pstudio.alpha.domain.patent.exception.PatentNotFoundException;
import com.ericlee.pstudio.alpha.domain.patent.repository.PatentRepository;
import com.ericlee.pstudio.alpha.domain.user.entity.User;
import com.ericlee.pstudio.alpha.domain.user.exception.UnauthorizedUserException;
import com.ericlee.pstudio.alpha.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@RequiredArgsConstructor
@Service
public class PatentEditorService {
    private final PatentRepository patentRepository;
    private final UserFacade userFacade;

    @Transactional(readOnly = true)
    public void loadEditor(Long patentId, Model model) {
        User actionUser = userFacade.queryCurrentUser(true)
                .orElseThrow(UnauthorizedUserException::new);

        Patent patent = patentRepository.findById(patentId)
                .orElseThrow(PatentNotFoundException::new);

        if(!patent.getOrganization().getUsers().contains(actionUser))
            throw new PatentAccessDeniedException();

        model.addAttribute("title", String.format("수정 '%s'", patent.getKoreanName()));
        model.addAttribute("patent", patent);
    }
}
