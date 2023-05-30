package com.hq.generalsecurity.expand


var BASE_URL=""



private var user_p: User?=null

var user: User?
    get() {
        when (user_p) {
            null -> {
                // "User".loadPro()
                return null
            }
            else -> {
                return user_p
            }
        }
}
set(value) {
    user_p =value}
data class User(val token:String,    var eid:String,var type:String)
