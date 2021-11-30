package com.example.myapplication.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheGameDBInterface {

    //list of geners
    //https://api.rawg.io/api/genres?key=6b028c60421148cd8cae86906ddd1a4d&page_size=2&page=10
    @GET("genres")
    fun getGeners(@Query("key") key:String,
                  @Query("page") page: Int,
                  @Query("page_size") page_size: Int,): Single<GenresResponse>

    //game details
    //https://api.rawg.io/api/games/3498?key=6b028c60421148cd8cae86906ddd1a4d
    @GET("games/{id}")
    fun getGameDetails(@Path("id") id: Int,
                        @Query("key") key:String): Single<GameDetails>


    //games
   // https://api.rawg.io/api/games?key=6b028c60421148cd8cae86906ddd1a4d&genres=4&page=51&page_size=2
    @GET("games")
    fun getGames(@Query("key") key:String,
                  @Query("page") page: Int,
                  @Query("page_size") page_size: Int?,): Single<GameResponse>


    //search
    //https://api.rawg.io/api/games?key=6b028c60421148cd8cae86906ddd1a4d&search=grand&page=2&genres=4



}