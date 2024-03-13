package com.komparo.stakememory

import java.util.Random

class Key {
    fun generateKey(): String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!#$%&*"
        val random = Random()
        var key = ""
        for (i in 0 until 20) {
            key += chars[random.nextInt(chars.length)]
        }
        return key
    }
}