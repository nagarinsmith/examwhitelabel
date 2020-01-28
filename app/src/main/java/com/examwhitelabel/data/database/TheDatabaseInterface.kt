package com.examwhitelabel.data.database

import com.examwhitelabel.data.TheItem

interface TheDatabaseInterface {

    suspend fun getItems(): List<TheItem>

    suspend fun updateItems(items: List<TheItem>)
}
