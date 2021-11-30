package com.oxcoding.moviemvvm.ui.single_movie_details

import androidx.lifecycle.LiveData
import com.example.myapplication.data.GameDetails
import com.example.myapplication.data.NetworkState
import com.example.myapplication.data.TheGameDBInterface
import com.oxcoding.moviemvvm.data.repository.GameDetailsNetworkDataSource

import io.reactivex.disposables.CompositeDisposable

class GameDetailsRepository (private val apiService : TheGameDBInterface) {

    lateinit var gameDetailsNetworkDataSource: GameDetailsNetworkDataSource

    fun fetchSingleGameDetails (compositeDisposable: CompositeDisposable, gameId: Int) : LiveData<GameDetails> {
        gameDetailsNetworkDataSource = GameDetailsNetworkDataSource(apiService,compositeDisposable)
        gameDetailsNetworkDataSource.fetchGameDetails(gameId)
        return gameDetailsNetworkDataSource.downloadedGameResponse
    }

    fun getGameDetailsNetworkState(): LiveData<NetworkState> {
        return gameDetailsNetworkDataSource.networkState
    }



}