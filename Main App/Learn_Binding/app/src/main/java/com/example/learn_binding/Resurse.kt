package com.example.learn_binding

import com.google.firebase.database.IgnoreExtraProperties

data class Resurse(var water: Int = -1, var milk: Int = -1, var beans: Int = -1, var cups: Int = -1, var money: Int = -1)

@IgnoreExtraProperties
data class ResurseOnline(var onlWater: Double, var onlMilk: Double, var onlBeans: Double, var onlCups: Double, var onlMoney: Double)