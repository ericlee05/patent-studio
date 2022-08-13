package com.ericlee.pstudio.alpha.domain.patent.service;

import com.ericlee.pstudio.alpha.domain.patent.presentation.dto.response.PatentResponse;
import com.ericlee.pstudio.alpha.domain.user.entity.User;
import com.ericlee.pstudio.alpha.domain.user.exception.UnauthorizedUserException;
import com.ericlee.pstudio.alpha.domain.user.facade.UserFacade;
import com.ericlee.pstudio.alpha.global.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PatentViewService {
    private final DateUtil dateUtil;
    private final UserFacade userFacade;

    @Transactional(readOnly = true)
    public void patentDashboard(Model model) {
        User user = userFacade.queryCurrentUser(true)
                .orElseThrow(UnauthorizedUserException::new);

        List<PatentResponse> patents = user.getOrganization().getPatents().stream()
                .map(patent -> PatentResponse.builder()
                        .patentId(patent.getId())
                        .nameWithKorean(patent.getKoreanName())
                        .nameWithEnglish(patent.getEnglishName())
                        .lastModified(dateUtil.yearMonthDate(patent.getDetail().getLastModified()))
                        .lastModifierName(Optional.ofNullable(patent.getDetail().getLastModifier()).orElse(
                                User.builder().loginId("-").build()
                        ).getLoginId())
                        .build())
                .collect(Collectors.toList());

        model.addAttribute("patents", patents);
    }

}
