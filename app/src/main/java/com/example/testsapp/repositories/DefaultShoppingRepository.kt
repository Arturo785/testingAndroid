package com.example.testsapp.repositories

import androidx.lifecycle.LiveData
import com.example.testsapp.api.PixabayAPI
import com.example.testsapp.data.local.ShoppingDao
import com.example.testsapp.data.local.ShoppingItem
import com.example.testsapp.data.remote.responses.ImageResponse
import com.example.testsapp.other.Resource
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayAPI: PixabayAPI
) : ShoppingRepository {

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return try {
            val response = pixabayAPI.searchForImage(imageQuery)

            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.Success(it)
                } ?: Resource.Error("An unknown error occured", null)
            } else {
                Resource.Error("An unknown error occured", null)
            }
        } catch (e: Exception) {
            Resource.Error("Couldn't reach the server. Check your internet connection", null)
        }
    }
}