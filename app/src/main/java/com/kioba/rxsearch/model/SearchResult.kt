package com.kioba.rxsearch.model

data class SearchResult(val text: String) {
  companion object {
    fun empty() = SearchResult("Empty")
  }
}
