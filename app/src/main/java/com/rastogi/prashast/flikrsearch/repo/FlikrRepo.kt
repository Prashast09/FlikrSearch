package com.rastogi.prashast.flikrsearch.repo

import android.graphics.Bitmap
import android.util.LruCache
import com.rastogi.prashast.flikrsearch.api.RetrofitInstance
import com.rastogi.prashast.flikrsearch.model.FlikrSearchResult
import com.rastogi.prashast.flikrsearch.service.FlikrService
import io.reactivex.Single

class FlikrRepo {

    private lateinit var service: FlikrService
    private var mMemoryCache: LruCache<String, Bitmap>? = null

    init {
        initFlikrApiService()
        initLRUCache()
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

    private fun initLRUCache() {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8
        mMemoryCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024
            }
        }
    }

    fun addBitmapToMemoryCache(key: String, bitmap: Bitmap?) {
        if (bitmap != null && getBitmapFromMemCache(key) == null) {
            mMemoryCache!!.put(key, bitmap)
        }
    }

    fun getBitmapFromMemCache(key: String): Bitmap? {
        return mMemoryCache!!.get(key)
    }
}