package com.rastogi.prashast.flikrsearch.model

import com.google.gson.annotations.SerializedName


class Photo {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("owner")
    var owner: String? = null

    @SerializedName("secret")
    var secret: String? = null

    @SerializedName("server")
    var server: String? = null

    @SerializedName("farm")
    var farm: Int? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("ispublic")
    var ispublic: Int? = null

    @SerializedName("isfriend")
    var isfriend: Int? = null

    @SerializedName("isfamily")
    var isfamily: Int? = null

    public fun getUrl() : String{
        return "http://farm" + farm + ".static.flickr.com/" + server + "/" + id + "_" + secret + ".jpg"
    }


}