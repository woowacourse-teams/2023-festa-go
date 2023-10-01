package com.festago.support;

import com.festago.festival.domain.Festival;
import com.festago.staff.domain.Staff;
import com.festago.staff.domain.StaffCode;

public class StaffFixture {

    private Long id;
    private StaffCode code = new StaffCode("festa1234");
    private Festival festival = FestivalFixture.festival().build();

    private StaffFixture() {
    }

    public static StaffFixture staff() {
        return new StaffFixture();
    }

    public StaffFixture id(Long id) {
        this.id = id;
        return this;
    }

    public StaffFixture codeValue(String code) {
        this.code = new StaffCode(code);
        return this;
    }

    public StaffFixture code(StaffCode code) {
        this.code = code;
        return this;
    }

    public StaffFixture festival(Festival festival) {
        this.festival = festival;
        return this;
    }

    public Staff build() {
        return new Staff(id, code, festival);
    }
}
