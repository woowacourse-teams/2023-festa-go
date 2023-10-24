package com.festago.festago.data.datasource

import com.festago.festago.data.dao.FestivalDao
import com.festago.festago.model.Festival
import javax.inject.Inject

class FestivalLocalDateSource @Inject constructor(
    private val festivalDao: FestivalDao,
) {
    fun loadFestivals(): Result<List<Festival>> =
        runCatching { festivalDao.getFestivals().map { it.toDomain() } }
}
