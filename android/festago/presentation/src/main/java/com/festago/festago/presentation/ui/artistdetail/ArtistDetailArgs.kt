package com.festago.festago.presentation.ui.artistdetail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArtistDetailArgs(val id: Long, val name: String, val profileUrl: String) : Parcelable
