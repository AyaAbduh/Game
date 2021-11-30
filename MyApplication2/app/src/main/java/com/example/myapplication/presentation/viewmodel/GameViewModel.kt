package com.example.myapplication.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.myapplication.data.*
import com.example.myapplication.domain.genreslist.GamesPagedListRepository
import io.reactivex.disposables.CompositeDisposable


    class GameViewModel(var search:String) : ViewModel() {

        private val compositeDisposable = CompositeDisposable()
        private  val apiService : TheGameDBInterface = TheGameDBClient.getClient()

        private  var gamesRepository = GamesPagedListRepository(apiService)

        val  gamesPagedList : LiveData<PagedList<Game>> by lazy {
            gamesRepository.fetchLiveGamePagedList(compositeDisposable,search)
        }

        val  networkState : LiveData<NetworkState> by lazy {
            gamesRepository.getNetworkState()
        }

        fun listIsEmpty(): Boolean {
            return gamesPagedList.value?.isEmpty() ?: true
        }

        override fun onCleared() {
            super.onCleared()
            compositeDisposable.dispose()
        }

    }
