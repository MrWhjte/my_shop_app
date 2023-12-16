package com.example.myappshop.models

class User constructor(
    val id:String = "",
    val firstName: String = "",
    val lastName: String  = "",
    val emaiAdd :String = "",
    val nickName:String = "",
    val image: String = "",
    val mobile: Long = 0,
    val gender: String = "",
    val birthDay: String = "",
    val address: String = "",
    val fcmToken:String = "",
    val profileCompleted:Int = 0,
    val isAdministrator:Int = 0,
) {

}