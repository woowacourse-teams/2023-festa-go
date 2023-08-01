package com.festago.festago.presentation.ui.customview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.festago.festago.databinding.FragmentOkDialogBinding

class OkDialogFragment : DialogFragment() {

    private var _binding: FragmentOkDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var context: Context

    private val window
        get() = dialog?.window ?: throw IllegalStateException()

    private val widthPixels: Int
        get() = context.resources.displayMetrics.widthPixels

    private val Int.dpToPx: Int
        get() = (this * context.resources.displayMetrics.density).toInt()

    lateinit var listener: OnClickListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOkDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(KEY_MESSAGE)?.let { message ->
            binding.tvMessage.text = message
        }

        context = requireContext()

        binding.btnOk.setOnClickListener {
            listener.invoke()
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()

        isCancelable = false

        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        window.attributes = WindowManager.LayoutParams().apply {
            copyFrom(window.attributes)
            width = widthPixels - 32.dpToPx
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = android.view.Gravity.CENTER
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun interface OnClickListener {
        fun invoke()
    }

    companion object {
        private const val KEY_MESSAGE = "KEY_MESSAGE"

        fun newInstance(message: String): OkDialogFragment {
            return OkDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_MESSAGE, message)
                }
            }
        }
    }
}
