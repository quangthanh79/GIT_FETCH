package com.apion.apionhome.ui.search

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.apion.apionhome.base.BindingFragment
import com.apion.apionhome.data.model.local.ILocation
import com.apion.apionhome.data.model.local.Province
import com.apion.apionhome.databinding.BottomsheetSearchLocationBinding
import com.apion.apionhome.ui.adapter.SearchLocationAdapter
import com.apion.apionhome.utils.RxSearchView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.concurrent.TimeUnit

abstract class SearchLocationFragment<I : ILocation>() :
    BindingFragment<BottomsheetSearchLocationBinding>(BottomsheetSearchLocationBinding::inflate) {

    override val viewModel by sharedViewModel<SearchViewModel>()
    val searchViewModelTmp by sharedViewModel<SearchViewModelTmp>()
    val searchViewModelHome by sharedViewModel<SearchViewModelHome>()

    abstract fun onItemClick(item: I)
    abstract fun onGetAll(query: String)
    abstract fun getHint(): String

    private val suggestionsAdapter = SearchLocationAdapter(::onItemSearchClick)

    private var disposeable: Disposable? = null
    val includeAll by lazy {
        arguments?.getBoolean("includeAll", true) ?: true
    }

    fun setUpViewModel(): SearchViewModel {
        val isShare = arguments?.getBoolean("shareData", true) ?: true
        val isHome = arguments?.getBoolean("isHome", false) ?: false
        if (isHome) return searchViewModelHome
        return if (isShare) viewModel else searchViewModelTmp
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread {
            onGetAll("")
        }.start()
    }

    open fun onItemSearchClick(item: I) {
        onItemClick(item)
        this.findNavController().popBackStack()
    }

    override fun setupView() {
        binding.lifecycleOwner = this
        binding.searchViewModel = setUpViewModel()
        binding.recyclerView.adapter = suggestionsAdapter
        binding.search.queryHint = getHint()
        setUpSearchView()
    }

    private fun setUpSearchView() {
        binding.apply {
            textViewCancel.setOnClickListener {
                findNavController().popBackStack()
            }
            disposeable = RxSearchView.fromSearchView(binding.search) {
                search.setQuery("", true)
                search.clearFocus()
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