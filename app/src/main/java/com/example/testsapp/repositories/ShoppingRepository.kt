package com.example.testsapp.repositories

import androidx.lifecycle.LiveData
import com.example.testsapp.data.local.ShoppingItem
import com.example.testsapp.data.remote.responses.ImageResponse
import com.example.testsapp.other.Resource

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}