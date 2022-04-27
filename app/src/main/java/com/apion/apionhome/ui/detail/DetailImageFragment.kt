package com.apion.apionhome.ui.detail

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.databinding.FragmentDetailImageBinding
import com.apion.apionhome.ui.adapter.ImageAdapter
import com.apion.apionhome.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailImageFragment :
    BindingFragment<FragmentDetailImageBinding>(FragmentDetailImageBinding::inflate) {

    override val viewModel by viewModel<DetailViewModel>()

    private val adapter = ImageAdapter({})

    private val args by navArgs<DetailImageFragmentArgs>()

    override fun setupView() {
        binding.lifecycleOwner = this
        binding.images = args.images.toList()
        binding.imageDetail.adapter = adapter
        binding.textViewCancel.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.imageDetail.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.current = position + 1
            }
        })
    }
}
