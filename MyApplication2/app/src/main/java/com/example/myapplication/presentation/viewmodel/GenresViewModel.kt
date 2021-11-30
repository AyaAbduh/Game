package com.example.myapplication.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.myapplication.data.Genres
import com.example.myapplication.data.NetworkState
import com.example.myapplication.data.TheGameDBClient
import com.example.myapplication.data.TheGameDBInterface
import com.example.myapplication.domain.genreslist.GenresPagedListRepository
import io.reactivex.disposables.CompositeDisposable

class GenresViewModel() : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private  val apiService : TheGameDBInterface = TheGameDBClient.getClient()

     private  var genresRepository = GenresPagedListRepository(apiService)

    val  genresPagedList : LiveData<PagedList<Genres>> by lazy {
        genresRepository.fetchLiveGamePagedList(compositeDisposable)
    }

    val  networkState : LiveData<NetworkState> by lazy {
        genresRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return genresPagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}