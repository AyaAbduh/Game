package com.example.myapplication.presentation.view.gamelist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.data.NetworkState
import com.example.myapplication.presentation.view.genreslist.GenresActivity
import com.example.myapplication.presentation.view.singlegamedetails.SingleGame
import com.example.myapplication.presentation.viewmodel.GameViewModel
import kotlinx.android.synthetic.main.activity_games.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.progress_bar_popular
import kotlinx.android.synthetic.main.activity_main.rv_movie_list
import kotlinx.android.synthetic.main.activity_main.txt_error_popular

class GamesActivity : AppCompatActivity() {

    private lateinit var viewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)

        val gamesAdapter = GamesPagedListAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 3)
        viewModel = getViewModel("",4)

        editTextTextPersonName.addTextChangedListener {
            viewModel.search(it.toString())
        }

        imageView.setOnClickListener {
            val intent = Intent(this, GenresActivity::class.java)
            startActivity(intent)
        }


        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = gamesAdapter.getItemViewType(position)
                if (viewType == gamesAdapter.GAME_VIEW_TYPE) return  1    // Movie_VIEW_TYPE will occupy 1 out of 3 span
                else return 3                                              // NETWORK_VIEW_TYPE will occupy all 3 span
            }
        };


        rv_movie_list.layoutManager = gridLayoutManager
        rv_movie_list.setHasFixedSize(true)
        rv_movie_list.adapter = gamesAdapter

        viewModel.gamesPagedList.observe(this, Observer {
            gamesAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                gamesAdapter.setNetworkState(it)
            }
        })

    }

    private fun getViewModel(search :String,genres:Int): GameViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return GameViewModel(search,genres) as T
            }
        })[GameViewModel::class.java]
    }

}