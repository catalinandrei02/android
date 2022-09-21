package com.example.learn_binding

import com.google.firebase.database.IgnoreExtraProperties

data class Resurse(var water: Int, var milk: Int, var beans: Int, var cups: Int, var money: Int)

@IgnoreExtraProperties
data class ResurseOnline(var onlWater: Double, var onlMilk: Double, var onlBeans: Double, var onlCups: Double, var onlMoney: Double)