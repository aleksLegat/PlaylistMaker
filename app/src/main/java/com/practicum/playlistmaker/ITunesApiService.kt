package com.practicum.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {
    @GET("search")
    fun searchSong(
        @Query("entity") entity : String = "song",
        @Query("term") term : String
    ) : Call<TracksResponse>
}