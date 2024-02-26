package com.festago.festago.presentation.ui.festivaldetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.festago.festago.presentation.databinding.FragmentFestivalDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FestivalDetailFragment : Fragment() {

    private var _binding: FragmentFestivalDetailBinding? = null
    private val binding get() = _binding!!

    private val vm: FestivalDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFestivalDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = requireArguments().getLong(KEY_ID)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val KEY_ID = "ID"

        fun newInstance(id: Long) = FestivalDetailFragment().apply {
            arguments = Bundle().apply {
                putLong(KEY_ID, id)
            }
        }
    }
}
