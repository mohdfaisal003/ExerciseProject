package com.rooted.deviceinfo.mvvm.pojos

data class QuotePojo(val q: String?,val a: String?,val h: String?) {
    override fun toString(): String {
        return "q=$q, a=$a, h=$h"
    }
}
