package com.example.myapplication.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.*
import com.oxcoding.moviemvvm.ui.single_movie_details.GameDetailsRepository
import io.reactivex.disposables.CompositeDisposable

class SingleGameViewModel ( gameId: Int)  : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val apiService : TheGameDBInterface = TheGameDBClient.getClient()  //interface

    private val gameRepository = GameDetailsRepository(apiService)

    val  gameDetails : LiveData<GameDetails> by lazy {
        gameRepository.fetchSingleGameDetails(compositeDisposable,gameId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        gameRepository.getGameDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }



}