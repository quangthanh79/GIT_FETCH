package com.apion.apionhome.ui.detail

import androidx.navigation.ActivityNavigator
import androidx.navigation.findNavController
import com.apion.apionhome.R
import com.apion.apionhome.base.BindingActivity
import com.apion.apionhome.databinding.ActivityDetailBinding

class DetailActivity : BindingActivity<ActivityDetailBinding>() {

    override fun finish() {
        super.finish()
        ActivityNavigator.applyPopAnimationsToPendingTransition(this)
    }

    override fun getLayoutResId() = R.layout.activity_detail
    override fun setupView() {
        findNavController(R.id.navHostActivityDetail)
            .setGraph(R.navigation.detail_graph, intent.extras)
    }
}