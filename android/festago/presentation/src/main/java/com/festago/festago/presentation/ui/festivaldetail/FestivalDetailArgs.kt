package com.festago.festago.presentation.ui.festivaldetail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FestivalDetailArgs(val id: Long, val name: String, val posterImageUrl: String) :
    Parcelable
