package com.festago.support;

import com.festago.festival.domain.Festival;
import com.festago.staff.domain.StaffCode;

public class StaffCodeFixture {

    private Long id;
    private String code = "festa1234";
    private Festival festival = FestivalFixture.festival().build();

    private StaffCodeFixture() {
    }

    public static StaffCodeFixture staffCode() {
        return new StaffCodeFixture();
    }

    public StaffCodeFixture id(Long id) {
        this.id = id;
        return this;
    }

    public StaffCodeFixture code(String code) {
        this.code = code;
        return this;

    }

    public StaffCodeFixture festival(Festival festival) {
        this.festival = festival;
        return this;
    }

    public StaffCode build() {
        return new StaffCode(id, code, festival);
    }
}
