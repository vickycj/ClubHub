package com.vicky.apps.datapoints.data.remote

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap
import java.util.*

interface ApiService {
    @GET("api/action/datastore_search/")
    fun getDataFromService(@QueryMap params: Map<String,String>): Single<Objects>
}
