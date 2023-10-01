package com.festago.staff.application;

import com.festago.festival.domain.Festival;
import com.festago.staff.domain.StaffCode;

public interface StaffCodeProvider {

    StaffCode provide(Festival festival);
}
