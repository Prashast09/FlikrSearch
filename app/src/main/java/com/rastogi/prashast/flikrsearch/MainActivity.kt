package com.rastogi.prashast.flikrsearch

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.rastogi.prashast.flikrsearch.viewmodel.FlikrViewModel
import com.rastogi.prashast.photoloader.adapter.PhotoAdapter
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), MenuItem.OnActionExpandListener {

    private var photosRV: RecyclerView? = null
    private var rvAdapter: PhotoAdapter? = null
    private lateinit var flikrViewModel: FlikrViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initViews()
        initRecyclerView()
        initViewModel()
        initSubscribeUi()

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
        searchItem.setOnActionExpandListener(this)
        val searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        initSearchView(searchView)
        return true
    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(this, 3)
        photosRV!!.layoutManager = layoutManager
        rvAdapter = PhotoAdapter()
        photosRV!!.adapter = rvAdapter

    }

    private fun initViews() {
        photosRV = findViewById(R.id.photos_rv)
    }

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
        return false
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        return false
    }

    @SuppressLint("CheckResult")
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
                subscribe.onNext(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                subscribe.onNext(query)
                return false
            }
        }
    }


}
