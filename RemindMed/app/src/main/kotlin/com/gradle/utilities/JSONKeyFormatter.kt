package com.gradle.utilities

fun formatJSONKey(key: String): String {
    val words = key.split("_")
    val formattedKey = StringBuilder()
    for (word in words) {
        formattedKey.append(word.replaceFirstChar { it.uppercase() } + " ")
    }
    return formattedKey.toString()
}