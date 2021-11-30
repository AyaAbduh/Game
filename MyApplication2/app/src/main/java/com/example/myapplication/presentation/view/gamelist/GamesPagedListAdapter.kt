package com.example.myapplication.presentation.view.gamelist

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.data.Game
import com.example.myapplication.data.NetworkState
import com.example.myapplication.presentation.view.singlegamedetails.SingleGame
import kotlinx.android.synthetic.main.movie_list_item.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*


class GamesPagedListAdapter(public val context: Context) : PagedListAdapter<Game, RecyclerView.ViewHolder>(
        MovieDiffCallback()
    ) {

        val GAME_VIEW_TYPE = 1
        val NETWORK_VIEW_TYPE = 2

        private var networkState: NetworkState? = null


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view: View

            if (viewType == GAME_VIEW_TYPE) {
                view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
                return MovieItemViewHolder(view)
            } else {
                view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
                return NetworkStateItemViewHolder(view)
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (getItemViewType(position) == GAME_VIEW_TYPE) {
                (holder as MovieItemViewHolder).bind(getItem(position),context)
            }
            else {
                (holder as NetworkStateItemViewHolder).bind(networkState)
            }
        }


        private fun hasExtraRow(): Boolean {
            return networkState != null && networkState != NetworkState.LOADED
        }

        override fun getItemCount(): Int {
            return super.getItemCount() + if (hasExtraRow()) 1 else 0
        }

        override fun getItemViewType(position: Int): Int {
            return if (hasExtraRow() && position == itemCount - 1) {
                NETWORK_VIEW_TYPE
            } else {
                GAME_VIEW_TYPE
            }
        }




        class MovieDiffCallback : DiffUtil.ItemCallback<Game>() {
            override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
                return oldItem == newItem
            }

        }


        class MovieItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {

            fun bind(game: Game?, context: Context) {
                itemView.cv_movie_title.text = game?.name
                //itemView.cv_movie_release_date.text =  game?.rating

                Glide.with(itemView.context)
                    .load(game?.background_image)
                    .into(itemView.cv_iv_movie_poster);

                itemView.setOnClickListener{
                    val intent = Intent(context, SingleGame::class.java)
                    intent.putExtra("id", game?.id)
                    context.startActivity(intent)
                }

            }

        }

        class NetworkStateItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {

            fun bind(networkState: NetworkState?) {
                if (networkState != null && networkState == NetworkState.LOADING) {
                    itemView.progress_bar_item.visibility = View.VISIBLE;
                }
                else  {
                    itemView.progress_bar_item.visibility = View.GONE;
                }

                if (networkState != null && networkState == NetworkState.ERROR) {
                    itemView.error_msg_item.visibility = View.VISIBLE;
                    itemView.error_msg_item.text = networkState.msg;
                }
                else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                    itemView.error_msg_item.visibility = View.VISIBLE;
                    itemView.error_msg_item.text = networkState.msg;
                }
                else {
                    itemView.error_msg_item.visibility = View.GONE;
                }
            }
        }


        fun setNetworkState(newNetworkState: NetworkState) {
            val previousState = this.networkState
            val hadExtraRow = hasExtraRow()
            this.networkState = newNetworkState
            val hasExtraRow = hasExtraRow()

            if (hadExtraRow != hasExtraRow) {
                if (hadExtraRow) {                             //hadExtraRow is true and hasExtraRow false
                    notifyItemRemoved(super.getItemCount())    //remove the progressbar at the end
                } else {                                       //hasExtraRow is true and hadExtraRow false
                    notifyItemInserted(super.getItemCount())   //add the progressbar at the end
                }
            } else if (hasExtraRow && previousState != newNetworkState) { //hasExtraRow is true and hadExtraRow true and (NetworkState.ERROR or NetworkState.ENDOFLIST)
                notifyItemChanged(itemCount - 1)       //add the network message at the end
            }

        }
}
