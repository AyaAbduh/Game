package com.oxcoding.moviemvvm.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class GameDetailsNetworkDataSource (private val apiService : TheGameDBInterface, private val compositeDisposable: CompositeDisposable) {

    private val _networkState  = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState                   //with this get, no need to implement get function to get networkSate

    private val _downloadedGameDetailsResponse =  MutableLiveData<GameDetails>()
    val downloadedGameResponse: LiveData<GameDetails>
        get() = _downloadedGameDetailsResponse

    fun fetchGameDetails(gameId: Int) {

        _networkState.postValue(NetworkState.LOADING)


        try {
            compositeDisposable.add(
                apiService.getGameDetails(gameId,"6b028c60421148cd8cae86906ddd1a4d")
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            //on success
                        {
                            _downloadedGameDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },//on failure
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("GameDetailsDataSource", it.message.toString())
                        }
                    )
            )
        }

        catch (e: Exception){
            Log.e("GameDetailsDataSource",e.message.toString())
        }

    }
}