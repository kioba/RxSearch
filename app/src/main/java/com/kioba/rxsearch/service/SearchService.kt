package com.kioba.rxsearch.service

import com.kioba.rxsearch.model.SearchResult
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class SearchService {
  fun search(text: String): Observable<SearchResult> = Observable.just(SearchResult(text)).delay(2000, TimeUnit.MILLISECONDS)
}
