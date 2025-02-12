package com.interview.fetchapp.network

import com.interview.fetchapp.data.ListItem
import retrofit2.http.GET

interface ApiService {
    @GET("hiring.json")
    suspend fun getListItems(): List<ListItem>
}