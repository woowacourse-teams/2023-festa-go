package com.festago.festago.presentation.ui.schooldetail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SchoolDetailArgs(val id: Long, val name: String, val profileImageUrl: String) :
    Parcelable
