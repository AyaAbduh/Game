package com.oxcoding.moviemvvm.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.myapplication.data.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class GenresDataSource (private val apiService : TheGameDBInterface, private val compositeDisposable: CompositeDisposable)
    : PageKeyedDataSource<Int, Genres>(){

   private var page = 1
    private var pageSize=1

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()


    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Genres>) {

        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getGeners("6b028c60421148cd8cae86906ddd1a4d",page,pageSize)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.genresList, null, page+1)
                        networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("GenresDataSource", it.message.toString())
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Genres>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getGeners("6b028c60421148cd8cae86906ddd1a4d",params.key,pageSize)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        var totalPages=it.count/pageSize
                        if(totalPages >= params.key) {
                            callback.onResult(it.genresList, params.key+1)
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

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Genres>) {

    }
}