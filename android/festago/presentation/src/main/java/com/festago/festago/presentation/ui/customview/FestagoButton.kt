package com.festago.festago.presentation.ui.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.festago.festago.presentation.R
import com.festago.festago.presentation.databinding.BtnFestagoBinding

class FestagoButton(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {

    private val binding by lazy {
        BtnFestagoBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        initAttrs(attrs)
    }

    private fun initAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FestagoButton)
        val title = typedArray.getString(R.styleable.FestagoButton_title)
        binding.tvFestagoBtn.text = title
        typedArray.recycle()
    }
}
