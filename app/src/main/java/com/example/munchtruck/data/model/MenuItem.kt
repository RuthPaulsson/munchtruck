package com.example.munchtruck.data.model

import com.google.firebase.Timestamp

data class MenuItem(
    val id: String = "",
    val name: String = "",
    val price: Long = 0,
    val description: String = "",
    val imageUrl: String = "",
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
) 

