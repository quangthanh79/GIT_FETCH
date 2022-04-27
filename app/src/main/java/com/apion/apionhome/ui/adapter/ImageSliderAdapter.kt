package com.apion.apionhome.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.apion.apionhome.base.BaseAdapter
import com.apion.apionhome.base.BaseViewHolder
import com.apion.apionhome.data.model.dashboard.Banner
import com.apion.apionhome.databinding.ItemLayoutPagerBannerBinding

class ImageSliderAdapter(private val listener: (Banner) -> Unit) :
    BaseAdapter<Banner, ItemLayoutPagerBannerBinding>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<Banner, ItemLayoutPagerBannerBinding> =
        HomeOffersViewHolder(
            ItemLayoutPagerBannerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener
        )

    class HomeOffersViewHolder(
        private val itemLayoutPagerBinding: ItemLayoutPagerBannerBinding,
        listener: (Banner) -> Unit
    ) : BaseViewHolder<Banner, ItemLayoutPagerBannerBinding>(itemLayoutPagerBinding, listener) {
        override fun onBind(itemData: Banner) {
            super.onBind(itemData)
            itemLayoutPagerBinding.banner = itemData
        }
    }
}
