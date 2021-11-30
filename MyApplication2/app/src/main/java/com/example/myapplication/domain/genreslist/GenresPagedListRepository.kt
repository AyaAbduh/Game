package com.example.myapplication.domain.genreslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.myapplication.data.*
import com.oxcoding.moviemvvm.data.repository.GenresDataSource
import com.oxcoding.moviemvvm.data.repository.GenresDataSourceFactory
import io.reactivex.disposables.CompositeDisposable

class GenresPagedListRepository (private val apiService : TheGameDBInterface) {

    lateinit var genresPagedList: LiveData<PagedList<Genres>>
    lateinit var genresDataSourceFactory: GenresDataSourceFactory

    fun fetchLiveGamePagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Genres>> {
        genresDataSourceFactory = GenresDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        genresPagedList = LivePagedListBuilder(genresDataSourceFactory, config).build()

        return genresPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<GenresDataSource, NetworkState>(
            genresDataSourceFactory.genresLiveDataSource, GenresDataSource::networkState)
    }
}