package com.festago.mock.domain;

public interface MockDataInitializer {

    boolean canInitialize();

    void initialize();
}
