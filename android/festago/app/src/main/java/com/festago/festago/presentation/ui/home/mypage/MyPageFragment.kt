package com.festago.festago.presentation.ui.home.mypage

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.festago.festago.R
import com.festago.festago.databinding.FragmentMyPageBinding
import com.festago.festago.presentation.util.loginWithKakao
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch

class MyPageFragment : Fragment(R.layout.fragment_my_page) {

    private var _binding: FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

        lifecycleScope.launch {
            UserApiClient.loginWithKakao(binding.root.context)
        }
    }

    private fun initView() {
        val spannableStringBuilder = SpannableStringBuilder(
            getString(R.string.mypage_tv_login_description),
        ).apply {
            setSpan(
                ForegroundColorSpan(requireContext().getColor(R.color.seed)),
                COLOR_SPAN_START_INDEX,
                COLOR_SPAN_END_INDEX,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        }
        binding.tvLoginDescription.text = spannableStringBuilder
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val COLOR_SPAN_START_INDEX = 0
        private const val COLOR_SPAN_END_INDEX = 4
    }
}
