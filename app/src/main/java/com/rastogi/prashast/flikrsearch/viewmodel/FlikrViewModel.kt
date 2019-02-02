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

    public var photoList = MutableLiveData<List<Photo>>()
    var page: Int = 0
    var flikrRepo = FlikrRepo()

    fun getSearchResult(query: String) {
        page++

        flikrRepo.getSearchResults(query, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry(3)
            .subscribe(
                {
                    photoList.value = it.getPhotos()!!.photo
                },
                {
                    Log.e(TAG, "error occurred")
                }
            )
    }
}