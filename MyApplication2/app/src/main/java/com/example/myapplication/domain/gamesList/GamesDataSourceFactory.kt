package com.oxcoding.moviemvvm.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.myapplication.data.Game
import com.example.myapplication.data.Genres
import com.example.myapplication.data.TheGameDBInterface
import io.reactivex.disposables.CompositeDisposable

class GamesDataSourceFactory (private val apiService : TheGameDBInterface, private val compositeDisposable: CompositeDisposable,private var search:String,private var genres:Int)
    : DataSource.Factory<Int, Game>() {

    val genresLiveDataSource =  MutableLiveData<GamesDataSource>()

    override fun create(): DataSource<Int, Game> {
        val genresDataSource = GamesDataSource(apiService,compositeDisposable,search,genres)

        genresLiveDataSource.postValue(genresDataSource)
        return genresDataSource
    }
}