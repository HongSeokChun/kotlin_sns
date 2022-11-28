package com.android.example.hongseokchun.model

class AlarmDTO {
    var destinationUid : String? = null
    var userId : String? = null
    var uid : String? = null
    //어떤 알람인지 구분해주는 값
    // 0 : 좋아요
    // 1 : 댓글
    // 2 : 팔로우
    var kind : Int? = null
    var message : String? = null
    var timestamp : Long? = null
    var uploadDate :String? =null
}