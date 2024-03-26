package com.festago.support.fixture;

import com.festago.admin.domain.Admin;

public class AdminFixture extends BaseFixture {

    private String username;
    private String password = "123456";

    private AdminFixture() {
    }

    public static AdminFixture builder() {
        return new AdminFixture();
    }

    public AdminFixture username(String username) {
        this.username = username;
        return this;
    }

    public AdminFixture password(String password) {
        this.password = password;
        return this;
    }

    public Admin build() {
        return new Admin(
            uniqueValue("admin", username),
            password
        );
    }
}
