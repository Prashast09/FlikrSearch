package com.rastogi.prashast.flikrsearch.adapter.callback

import android.support.v7.util.DiffUtil
import com.rastogi.prashast.flikrsearch.model.Photo

class PhotoDiffCallback : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(p0: Photo, p1: Photo): Boolean {
        return p0.id != p1.id
    }

    override fun areContentsTheSame(p0: Photo, p1: Photo): Boolean {
        return p0.farm != p1.farm
    }

}