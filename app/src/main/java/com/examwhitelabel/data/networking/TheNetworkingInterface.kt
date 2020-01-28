package com.examwhitelabel.data.networking

import com.examwhitelabel.data.TheItem

interface TheNetworkingInterface {

    suspend fun getItems(): List<TheItem>

    suspend fun addItem(item: TheItem)

    suspend fun updateItem(item: TheItem)

    suspend fun deleteItem(itemId: String)
}
