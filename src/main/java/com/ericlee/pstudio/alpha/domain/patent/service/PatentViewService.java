package com.ericlee.pstudio.alpha.domain.patent.service;

import com.ericlee.pstudio.alpha.domain.patent.entity.Patent;
import com.ericlee.pstudio.alpha.domain.patent.exception.PatentNotFoundException;
import com.ericlee.pstudio.alpha.domain.patent.presentation.dto.response.PatentDetailResponse;
import com.ericlee.pstudio.alpha.domain.patent.presentation.dto.response.PatentResponse;
import com.ericlee.pstudio.alpha.domain.patent.repository.PatentRepository;
import com.ericlee.pstudio.alpha.domain.patent.type.SingleComponentType;
import com.ericlee.pstudio.alpha.domain.user.entity.User;
import com.ericlee.pstudio.alpha.domain.user.exception.UnauthorizedUserException;
import com.ericlee.pstudio.alpha.domain.user.facade.UserFacade;
import com.ericlee.pstudio.alpha.global.utils.DateUtil;
import com.swcns.reflcrypt.util.ObjectDecryptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class PatentViewService {
    private final PatentRepository patentRepository;
    private final DateUtil dateUtil;
    private final UserFacade userFacade;
    private final ObjectDecryptor objectDecryptor;

    @Transactional(readOnly = true)
    public void patentDashboard(Model model) {
        model.addAttribute("patents", getPatentsResponse());
    }

    @Transactional(readOnly = true)
    public List<PatentResponse> getPatentsResponse() {
        User user = userFacade.queryCurrentUser(true)
                .orElseThrow(UnauthorizedUserException::new);

        List<PatentResponse> patents = user.getOrganization().getPatents().stream()
                .map(patent -> PatentResponse.builder()
                        .patentId(patent.getId())
                        .nameWithKorean(patent.getKoreanName())
                        .nameWithEnglish(patent.getEnglishName())
                        .lastModified(dateUtil.yearMonthDateWithTime(patent.getDetail().getLastModified()))
                        .lastModifierName(Optional.ofNullable(patent.getDetail().getLastModifier()).orElse(
                                User.builder().loginId("-").build()
                        ).getLoginId())
                        .build())
                .collect(Collectors.toList());

        return patents;
    }

    public void patentDetail(Long patentId, Model model) {
        Patent patent = patentRepository.findById(patentId)
                .orElseThrow(PatentNotFoundException::new);

        PatentDetailResponse patentSummary = PatentDetailResponse.builder()
                .patentSummary(objectDecryptor.getDecryptedObject(patent.getSingleComponentByType(SingleComponentType.SUMMARY).getContent()))
                .build();

        log.info(patentSummary.getPatentSummary());

        model.addAttribute("title", patent.getKoreanName());
        model.addAttribute("patent", patent);
        model.addAttribute("patentSummary", patentSummary);
    }
}
