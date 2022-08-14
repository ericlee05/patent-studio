package com.ericlee.pstudio.alpha.domain.patent.service;

import com.ericlee.pstudio.alpha.domain.patent.entity.Patent;
import com.ericlee.pstudio.alpha.domain.patent.entity.PatentDetail;
import com.ericlee.pstudio.alpha.domain.patent.entity.SingleComponent;
import com.ericlee.pstudio.alpha.domain.patent.entity.SingleComponentId;
import com.ericlee.pstudio.alpha.domain.patent.exception.PatentAccessDeniedException;
import com.ericlee.pstudio.alpha.domain.patent.exception.PatentNotFoundException;
import com.ericlee.pstudio.alpha.domain.patent.presentation.dto.request.PatentCreationRequest;
import com.ericlee.pstudio.alpha.domain.patent.repository.PatentRepository;
import com.ericlee.pstudio.alpha.domain.patent.type.SingleComponentType;
import com.ericlee.pstudio.alpha.domain.user.entity.User;
import com.ericlee.pstudio.alpha.domain.user.exception.UnauthorizedUserException;
import com.ericlee.pstudio.alpha.domain.user.facade.UserFacade;
import com.ericlee.pstudio.alpha.global.utils.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PatentSimpleService {
    private final PatentRepository patentRepository;
    private final UserFacade userFacade;
    private final HttpUtil httpUtil;

    private List<SingleComponent> getNecessaryComponents(Patent patent) {
        return Arrays.stream(SingleComponentType.values())
                .map(type -> SingleComponent.builder()
                        .id(SingleComponentId.builder().singleComponentType(type).patent(patent).build())
                        .content(String.format("%s에 대해 작성해주세요", type.getDescription()))
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void createPatent(PatentCreationRequest request, HttpServletResponse response) {
        User user = userFacade.queryCurrentUser(true)
                .orElseThrow(UnauthorizedUserException::new);

        Patent patent = Patent.builder()
                .koreanName(request.getKoreanName())
                .englishName(request.getEnglishName())
                .detail(PatentDetail.builder()
                        .createdAt(LocalDateTime.now()).build())
                .drawings(new ArrayList<>())
                .singleComponents(new ArrayList<>())
                .multiComponents(new ArrayList<>())
                .claims(new ArrayList<>())
                .organization(user.getOrganization())
                .build();
        patent = patentRepository.save(patent);
        patent.getSingleComponents().addAll(getNecessaryComponents(patent));
        user.getOrganization().addPatent(patent);

        httpUtil.redirectTo(response, String.format("/patents/%s", patent.getId()));
    }

    @Transactional
    public void deletePatent(Long patentId, HttpServletResponse response) {
        User actionUser = userFacade.queryCurrentUser(true)
                .orElseThrow(UnauthorizedUserException::new);

        Patent patent = patentRepository.findById(patentId)
                .orElseThrow(PatentNotFoundException::new);

        if(!patent.getOrganization().getUsers().contains(actionUser))
            throw new PatentAccessDeniedException();
        patentRepository.delete(patent);

        httpUtil.redirectTo(response, "/patents");
    }
}
