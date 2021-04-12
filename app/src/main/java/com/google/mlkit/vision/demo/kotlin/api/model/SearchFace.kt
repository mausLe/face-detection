package com.google.mlkit.vision.demo.kotlin.api.model

import api.data

class SearchFace (var token: String, var data: data) {

    fun searchFace(token: String, data: data) {
        this.token = token
        this.data = data
    }
}