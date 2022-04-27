package com.apion.apionhome.ui.geting_started

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.base.RxViewModel
import com.apion.apionhome.data.model.local.ILocation
import com.apion.apionhome.databinding.BottomsheetSearchLocationRegisterBinding
import com.apion.apionhome.ui.adapter.SearchLocationAdapter
import com.apion.apionhome.utils.RxSearchView
import com.apion.apionhome.viewmodel.UserViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.concurrent.TimeUnit

abstract class SelectLocationDetailFragment<I : ILocation>() :
    BindingFragment<BottomsheetSearchLocationRegisterBinding>(BottomsheetSearchLocationRegisterBinding::inflate) {
    override val viewModel by sharedViewModel<UserViewModel>()
    abstract fun onItemClick(item: I)
    abstract fun onGetAll(query: String)
    private val suggestionsAdapter = SearchLocationAdapter(::onItemSearchClick)

    private var disposeable: Disposable? = null


    open fun onItemSearchClick(item: I){
        onItemClick(item)
        this.findNavController().popBackStack()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread {
            onGetAll("")
        }.start()
    }
    override fun setupView() {
        binding.lifecycleOwner = this
        binding.selectViewModel = viewModel
        binding.recyclerView.adapter = suggestionsAdapter
        setUpSearchView()
    }
    private fun setUpSearchView() {
        binding.apply {
            textViewCancelRegister.setOnClickListener {
                findNavController().popBackStack()
            }
            disposeable = RxSearchView.fromSearchView(binding.searchRegister) {
                searchRegister.setQuery("", true)
                searchRegister.clearFocus()
            }
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    onGetAll(it)
                }

        }

    }
}