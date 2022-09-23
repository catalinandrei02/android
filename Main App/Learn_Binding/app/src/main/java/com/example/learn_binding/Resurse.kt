package com.example.learn_binding

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ResurseOnline(
    var onlWater: Double,
    var onlMilk: Double,
    var onlBeans: Double,
    var onlCups: Double,
    var onlMoney: Double)