package com.festago.staff.application;

import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.NotFoundException;
import com.festago.festival.domain.Festival;
import com.festago.festival.repository.FestivalRepository;
import com.festago.staff.domain.Staff;
import com.festago.staff.domain.StaffCode;
import com.festago.staff.dto.StaffResponse;
import com.festago.staff.repository.StaffCodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StaffService {

    private final StaffCodeRepository staffCodeRepository;
    private final FestivalRepository festivalRepository;
    private final StaffCodeProvider codeProvider;

    public StaffService(StaffCodeRepository staffCodeRepository,
                        FestivalRepository festivalRepository, StaffCodeProvider codeProvider) {
        this.staffCodeRepository = staffCodeRepository;
        this.festivalRepository = festivalRepository;
        this.codeProvider = codeProvider;
    }

    public StaffResponse createStaff(Long festivalId) {
        Festival festival = findFestival(festivalId);
        if (staffCodeRepository.existsByFestival(festival)) {
            throw new BadRequestException(ErrorCode.STAFF_CODE_EXIST);
        }
        StaffCode code = createVerificationCode(festival);

        Staff staff = staffCodeRepository.save(new Staff(code, festival));

        return StaffResponse.from(staff);
    }

    private StaffCode createVerificationCode(Festival festival) {
        StaffCode code;
        do {
            code = codeProvider.provide(festival);
        } while (staffCodeRepository.existsByCode(code));
        return code;
    }

    private Festival findFestival(Long festivalId) {
        return festivalRepository.findById(festivalId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.FESTIVAL_NOT_FOUND));
    }
}
