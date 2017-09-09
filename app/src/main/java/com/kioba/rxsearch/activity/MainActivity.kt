package com.kioba.rxsearch.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.jakewharton.rxbinding2.widget.textChanges
import com.kioba.rxsearch.R
import com.kioba.rxsearch.model.SearchResult
import com.kioba.rxsearch.service.SearchService
import com.kioba.rxsearch.util.setRightDrawableOnTouchListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

  private var searchSubscription: Disposable? = null
  private val searchService = SearchService()
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    main_search_text.setRightDrawableOnTouchListener { text.clear() }

    searchSubscription = main_search_text
      .textChanges()
      .skip(1)
      .map { it.toString() }
      .doOnNext {
        main_progressbar.visibility = View.VISIBLE
        main_content.visibility = View.GONE
      }
      .debounce(800, TimeUnit.MILLISECONDS)
      .flatMap {
        if (it.isNotBlank()) {
          searchService.search(it)
            .subscribeOn(Schedulers.io())
        } else {
          Observable.just(SearchResult.empty())
        }
      }
      .observeOn(AndroidSchedulers.mainThread())
      .doOnError { Snackbar.make(main_coordinator, "Error while searching", Snackbar.LENGTH_SHORT).show() }
      .retry()
      .doOnEach {
        main_progressbar.visibility = View.GONE
        main_content.visibility = View.VISIBLE
      }
      .subscribe({
        main_content.text = it.text
        Log.d("MainActivity", it.text)
      }, {
        Log.e("MainActivity", it.toString())
      })
  }

  override fun onDestroy() {
    searchSubscription?.dispose()
    super.onDestroy()
  }
}
