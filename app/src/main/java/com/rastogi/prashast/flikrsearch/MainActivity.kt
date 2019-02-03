package com.rastogi.prashast.flikrsearch

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import com.rastogi.prashast.flikrsearch.adapter.PhotoAdapter
import com.rastogi.prashast.flikrsearch.model.Photo
import com.rastogi.prashast.flikrsearch.viewmodel.FlikrViewModel
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private var photosRV: RecyclerView? = null
    private var rvAdapter: PhotoAdapter? = null
    private lateinit var flikrViewModel: FlikrViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initViews()
        initViewModel()
        initRecyclerView()
        initSubscribeUi()
        initListeners()

    }

    private fun initListeners() {

        val listener = object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = (photosRV!!.layoutManager as LinearLayoutManager).childCount
                val totalItemCount = (photosRV!!.layoutManager as LinearLayoutManager).itemCount
                val firstVisibleItemPosition =
                    (photosRV!!.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    flikrViewModel.getSearchResult(null)
                }
            }

        }
        photosRV!!.addOnScrollListener(listener)

    }

    private fun initSubscribeUi() {
        flikrViewModel.photoList.observe(this, Observer { photoList ->
            rvAdapter!!.submitList(photoList)
        })
    }


    private fun initViewModel() {
        flikrViewModel = ViewModelProviders.of(this).get(FlikrViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_dashboard, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        initSearchView(searchView)
        return true
    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(this, 3)
        photosRV!!.layoutManager = layoutManager
        rvAdapter = PhotoAdapter(baseContext, flikrViewModel.flikrRepo)
        photosRV!!.adapter = rvAdapter
    }

    private fun initViews() {
        photosRV = findViewById(R.id.photos_rv)
    }

    private fun initSearchView(searchView: SearchView) {
        searchView.queryHint = "Start Typing here"

        Observable.create(ObservableOnSubscribe<String> { it ->
            searchView.setOnQueryTextListener(getTextQueryListener(it))
        })
            .map { text -> text.toLowerCase().trim() }
            .debounce(250, TimeUnit.MILLISECONDS)
            .distinct()
            .filter { text -> text.isNotBlank() }
            .subscribe { text ->
                flikrViewModel.getSearchResult(text)
            }
    }

    private fun getTextQueryListener(subscribe: ObservableEmitter<String>): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                rvAdapter!!.submitList(ArrayList<Photo>())
                subscribe.onNext(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        }
    }


}
