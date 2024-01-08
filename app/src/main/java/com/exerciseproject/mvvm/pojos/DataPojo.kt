package com.rooted.deviceinfo.mvvm.pojos

data class DataPojo(var data: List<DetailPojo>)

data class DetailPojo(var position: Int?, var title: String?, var type: String?, var description: String?, var details: List<Details>?)

data class Details(var detail_type: String?, var detail_heading: String?)