package com.rastogi.prashast.flikrsearch.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.rastogi.prashast.flikrsearch.model.Photo
import com.rastogi.prashast.flikrsearch.repo.FlikrRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FlikrViewModel : ViewModel() {

    private val TAG = javaClass.name

    public var photoList = MutableLiveData<ArrayList<Photo>>()
    var page: Int = 1
    var flikrRepo = FlikrRepo()
    var query: String = ""
    var currentCountPerPage = 0


    fun getSearchResult(query: String?) {
        if (query == null || this.query.equals(query)) {
            page++
        } else {
            page = 1
            this.query = query
        }

        flikrRepo.getSearchResults(this.query, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(3)
            .subscribe(
                {

                    currentCountPerPage = it.getPhotos()!!.perpage!!
                    if (photoList.value != null) {
                        photoList.value!!.addAll(it.getPhotos()!!.photo!!)
                        notifyPhotoListItem()
                    }else{
                        photoList.value = it.getPhotos()!!.photo!!
                    }
                },
                {
                    Log.e(TAG, "error occurred")
                }
            )
    }

    private fun notifyPhotoListItem() {
        if (photoList.value != null) {
            val list = photoList.value
            photoList.value = list
        }
    }
}