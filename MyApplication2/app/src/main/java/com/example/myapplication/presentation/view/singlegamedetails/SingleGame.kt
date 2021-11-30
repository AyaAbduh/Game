package com.example.myapplication.presentation.view.singlegamedetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.*
import com.example.myapplication.presentation.viewmodel.SingleGameViewModel
import kotlinx.android.synthetic.main.activity_single_movie.*


class SingleGame : AppCompatActivity() {

    private lateinit var viewModel: SingleGameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)

        val gameId: Int = intent.getIntExtra("id",3)
        viewModel = getViewModel(gameId)  //object of view model

        viewModel.gameDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    fun bindUI( it: GameDetails){
        name.text = it.name
        release_date.text = it.released
        rating.text = it.rating.toString()

        Glide.with(this)
            .load(it.background_image)
            .into(poster);
    }


    //get object of ViewModel
    private fun getViewModel(gameId:Int): SingleGameViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SingleGameViewModel(gameId) as T
            }
        })[SingleGameViewModel::class.java]
    }
}
