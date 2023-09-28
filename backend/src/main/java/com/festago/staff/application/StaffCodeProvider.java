package com.festago.staff.application;

import com.festago.festival.domain.Festival;

public interface StaffCodeProvider {

    String provide(Festival festival);
}
