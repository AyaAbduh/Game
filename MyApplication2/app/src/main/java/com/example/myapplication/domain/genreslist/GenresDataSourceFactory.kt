package com.oxcoding.moviemvvm.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.myapplication.data.Genres
import com.example.myapplication.data.TheGameDBInterface
import io.reactivex.disposables.CompositeDisposable

class GenresDataSourceFactory (private val apiService : TheGameDBInterface, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, Genres>() {

    val genresLiveDataSource =  MutableLiveData<GenresDataSource>()

    override fun create(): DataSource<Int, Genres> {
        val genresDataSource = GenresDataSource(apiService,compositeDisposable)

        genresLiveDataSource.postValue(genresDataSource)
        return genresDataSource
    }
}