package com.rastogi.prashast.photoloader.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.rastogi.prashast.flikrsearch.R
import com.rastogi.prashast.flikrsearch.adapter.callback.PhotoDiffCallback
import com.rastogi.prashast.flikrsearch.model.Photo
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.BufferedInputStream
import java.io.IOException
import java.net.URL


class PhotoAdapter : ListAdapter<Photo, RecyclerView.ViewHolder>(PhotoDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val photo = getItem(position)
        if (viewHolder is PhotoViewHolder) {
            viewHolder.bind(photo)
        }
    }

    class PhotoViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var photoIV: ImageView = v.findViewById(R.id.photo_iv)
        fun bind(photo: Photo) {
            Observable.fromCallable {
                getImageBitmap(getImagePathName(photo))
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    photoIV.setImageBitmap(it)
                }
        }

        private fun getImagePathName(photo: Photo): String {
            return "http://farm" + photo.farm + ".static.flickr.com/" + photo.server + "/" + photo.id + "_" + photo.secret + ".jpg"
        }

        private fun getImageBitmap(url: String): Bitmap? {
            var bm: Bitmap? = null
            try {
                val aURL = URL(url)
                val conn = aURL.openConnection()
                conn.connect()
                val `is` = conn.getInputStream()
                val bis = BufferedInputStream(`is`)
                bm = BitmapFactory.decodeStream(bis)
                bis.close()
                `is`.close()
            } catch (e: IOException) {
                Log.e(javaClass.name, "Error getting bitmap", e)
            }

            return bm
        }
    }

}