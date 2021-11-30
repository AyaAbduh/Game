package com.oxcoding.moviemvvm.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.myapplication.data.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class GamesDataSource (private val apiService : TheGameDBInterface, private val compositeDisposable: CompositeDisposable,private var searchString:String)
    : PageKeyedDataSource<Int, Game>(){

   private var page = 1
    private var pageSize=1

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()


    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Game>) {

        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getGames("6b028c60421148cd8cae86906ddd1a4d",page,pageSize,searchString)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.gameList, null, page+1)
                        networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("GenresDataSource", it.message.toString())
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Game>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getGames("6b028c60421148cd8cae86906ddd1a4d",params.key,pageSize,searchString)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        var totalPages=it.count/pageSize
                        if(totalPages >= params.key) {
                            callback.onResult(it.gameList, params.key+1)
                            networkState.postValue(NetworkState.LOADED)
                        }
                        else{
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("GenresDataSource", it.message.toString())
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Game>) {

    }
}