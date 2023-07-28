package com.festago.festago.data.repository

import com.festago.festago.domain.model.Festival
import com.festago.festago.domain.repository.FestivalRepository
import kotlinx.coroutines.delay
import java.time.LocalDate

class FestivalDefaultRepository : FestivalRepository {
    override suspend fun loadFestivals(): Result<List<Festival>> {
        delay(1000)
        return Result.success(
            List(20) {
                Festival(
                    it.toLong(),
                    "테코대학교 $it",
                    LocalDate.of(2023, 5, 15),
                    LocalDate.of(2023, 5, 19),
                    "",
                )
            },
        )
    }
}
