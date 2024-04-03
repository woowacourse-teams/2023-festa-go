package com.festago.festago.presentation.ui.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.festago.festago.presentation.R

class ClearEditText(context: Context, attrs: AttributeSet) :
    AppCompatEditText(context, attrs),
    TextWatcher,
    OnTouchListener,
    OnFocusChangeListener {

    private val clearDrawable: Drawable by lazy {
        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_circle_close)!!
        DrawableCompat.wrap(drawable)
    }
    private var onFocusChangeListener: OnFocusChangeListener? = null
    private var onTouchListener: OnTouchListener? = null

    init {
        clearDrawable.setBounds(
            0,
            0,
            clearDrawable.intrinsicWidth,
            clearDrawable.intrinsicHeight,
        )
        setClearIconVisible(false)
        super.setOnTouchListener(this)
        super.setOnFocusChangeListener(this)
        addTextChangedListener(this)
    }

    override fun setOnFocusChangeListener(onFocusChangeListener: OnFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener
    }

    override fun setOnTouchListener(onTouchListener: OnTouchListener) {
        this.onTouchListener = onTouchListener
    }

    override fun onFocusChange(view: View, hasFocus: Boolean) {
        setClearIconVisible(text!!.isNotEmpty())
        if (onFocusChangeListener != null) {
            onFocusChangeListener!!.onFocusChange(view, hasFocus)
        }
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        val x = motionEvent.x.toInt()
        if (clearDrawable.isVisible && x > width - paddingRight - clearDrawable.intrinsicWidth) {
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                error = null
                text = null
                isFocusableInTouchMode = true
                val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                requestFocus()
                inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }
            return true
        }
        return if (onTouchListener != null) {
            onTouchListener!!.onTouch(view, motionEvent)
        } else {
            false
        }
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (!isFocused) return
        setClearIconVisible(s.isNotEmpty())
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

    override fun afterTextChanged(s: Editable) = Unit

    private fun setClearIconVisible(visible: Boolean) {
        clearDrawable.setVisible(visible, false)
        setCompoundDrawables(null, null, if (visible) clearDrawable else null, null)
    }
}
