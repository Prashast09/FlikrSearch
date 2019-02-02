package com.rastogi.prashast.flikrsearch.adapter

import android.annotation.SuppressLint
import android.content.Context
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
import com.rastogi.prashast.flikrsearch.repo.FlikrRepo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.BufferedInputStream
import java.io.IOException
import java.net.URL


class PhotoAdapter : ListAdapter<Photo, RecyclerView.ViewHolder> {

    val flikrRepo: FlikrRepo
    val context: Context

    constructor(context: Context, flikrRepo: FlikrRepo) : super(PhotoDiffCallback()) {
        this.context = context
        this.flikrRepo = flikrRepo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val photo = getItem(position)
        if (viewHolder is PhotoViewHolder) {
            viewHolder.bind(context,flikrRepo, photo)
        }
    }

    class PhotoViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var photoIV: ImageView = v.findViewById(R.id.photo_iv)
        @SuppressLint("CheckResult")
        fun bind(context: Context, repo: FlikrRepo, photo: Photo) {


            Observable.fromCallable {
                loadBitmap(context,repo, photo.getUrl(), photoIV)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { Log.e(":ssadasd", "asdjknajsd") }
                .subscribe {
                    if (it != null)
                        photoIV.setImageBitmap(it)
                }
        }


        fun loadBitmap(context: Context, repo: FlikrRepo, imageKey: String, mImageView: ImageView): Bitmap? {

            var bitmap = repo.getBitmapFromMemCache(imageKey)
            if (bitmap == null) {
                bitmap = getImageBitmap(imageKey)
                repo.addBitmapToMemoryCache(imageKey, bitmap)
            }
            if (bitmap == null) {
                return BitmapFactory.decodeResource(context.resources,R.drawable.ic_search_white)
            }
            return bitmap

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