package com.example.myapplication.domain.genreslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.myapplication.data.*
import com.oxcoding.moviemvvm.data.repository.GamesDataSource
import com.oxcoding.moviemvvm.data.repository.GamesDataSourceFactory
import io.reactivex.disposables.CompositeDisposable

class GamesPagedListRepository (private val apiService : TheGameDBInterface) {

    lateinit var genresPagedList: LiveData<PagedList<Game>>
    lateinit var gamesDataSourceFactory: GamesDataSourceFactory

    fun fetchLiveGamePagedList (compositeDisposable: CompositeDisposable,search:String,genres: Int) : LiveData<PagedList<Game>> {
        gamesDataSourceFactory = GamesDataSourceFactory(apiService, compositeDisposable,search,genres)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        genresPagedList = LivePagedListBuilder(gamesDataSourceFactory, config).build()

        return genresPagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<GamesDataSource, NetworkState>(
            gamesDataSourceFactory.genresLiveDataSource, GamesDataSource::networkState)
    }
}