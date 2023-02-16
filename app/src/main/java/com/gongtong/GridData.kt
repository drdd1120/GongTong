package com.gongtong

import com.google.firebase.database.DatabaseReference

data class GridData(
    val name: String,
    val image_url: String,
)
{
    // 파이어베이스에 클래스 단위로 올리려면 인자빈생성자 필요;
    constructor() : this("", "",)
}