package com.festago.festival.repository;

import com.festago.festival.domain.Festival;
import java.util.List;

public class FestivalPage {

    private final boolean lastPage;
    private final List<Festival> festivals;

    public FestivalPage(boolean lastPage, List<Festival> festivals) {
        this.lastPage = lastPage;
        this.festivals = festivals;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public List<Festival> getFestivals() {
        return festivals;
    }
}
