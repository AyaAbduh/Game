package com.example.myapplication.data

import com.google.gson.annotations.SerializedName

//Games
data class GameResponse(
        val count: Int,
        @SerializedName("results")
        val gameList: List<Game>)

data class Game(
        val id: Int,
        val name: String,
        val rating: Double,
        val background_image: String)


data class GameDetails(
        val name: String,
        val description: String,
        val released: String,
        val background_image: String,
        val rating: Double)

data class GenresResponse(
        val count: Int,
        @SerializedName("results")
        val genresList: List<Genres>)

data class Genres(
        val id: Int,
        val name: String,
        val image_background: String)



