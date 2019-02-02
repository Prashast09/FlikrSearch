package com.rastogi.prashast.flikrsearch.repo

import com.rastogi.prashast.flikrsearch.api.RetrofitInstance
import com.rastogi.prashast.flikrsearch.model.FlikrSearchResult
import com.rastogi.prashast.flikrsearch.service.FlikrService
import io.reactivex.Single

class FlikrRepo {

    private lateinit var service: FlikrService

    init {
        initFlikrApiService()
    }

    private fun initFlikrApiService() {
        service = RetrofitInstance.getInstance()!!.create(FlikrService::class.java)
    }

    fun getSearchResults(query: String, page: Int): Single<FlikrSearchResult> {
        return service.getQueryResult(
            "flickr.photos.search",
            "3e7cc266ae2b0e0d78e279ce8e361736",
            "json",
            1,
            1,
            query, page
        )
    }
}