package com.rastogi.prashast.flikrsearch.model

import com.google.gson.annotations.SerializedName
import com.rastogi.prashast.flikrsearch.model.Photo


class Photos {

    @SerializedName("page")
    var page: Int? = null

    @SerializedName("pages")
    var pages: Int? = null

    @SerializedName("perpage")
    var perpage: Int? = null

    @SerializedName("total")
    var total: String? = null

    @SerializedName("photo")
    var photo: List<Photo>? = null

}