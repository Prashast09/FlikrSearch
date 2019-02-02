package com.rastogi.prashast.flikrsearch.service

import com.rastogi.prashast.flikrsearch.model.FlikrSearchResult
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FlikrService {


    //  https://api.flickr.com/services/rest?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&format=json&nojsoncallback=1&safe_search=1&text=kittens
    //
    @GET("/services/rest")
    fun getQueryResult(
        @Query("method") method: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String,
        @Query("nojsoncallback") noJSONFormat: Int,
        @Query("safe_search") safeSearch: Int,
        @Query("text") query: String,
        @Query("page") page: Int
    ): Single<FlikrSearchResult>

}