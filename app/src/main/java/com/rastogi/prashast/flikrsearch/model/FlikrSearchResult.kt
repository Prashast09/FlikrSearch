package com.rastogi.prashast.flikrsearch.model

import com.google.gson.annotations.SerializedName


class FlikrSearchResult {
    @SerializedName("photos")
    private var photos: Photos? = null

    @SerializedName("stat")
    private var stat: String? = null

    fun getPhotos(): Photos? {
        return photos
    }

    fun setPhotos(photos: Photos) {
        this.photos = photos
    }

    fun getStat(): String? {
        return stat
    }

    fun setStat(stat: String) {
        this.stat = stat
    }
}