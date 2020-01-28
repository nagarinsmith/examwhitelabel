package com.examwhitelabel.view.addOrEdit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.examwhitelabel.data.TheItem
import com.examwhitelabel.data.networking.TheNetworkingInterface
import com.examwhitelabel.util.TheDispatchers
import com.examwhitelabel.view.shared.SingleLiveEvent
import kotlinx.coroutines.launch
import java.util.UUID

class TheAddOrEditViewModel(private val item: TheItem? = null, private val networking: TheNetworkingInterface, private val dispatchers: TheDispatchers) :
    ViewModel() {

    private val _events = SingleLiveEvent<Event>()
    val events: LiveData<Event> get() = _events

    val field1 = MutableLiveData<String>(item?.field1.orEmpty())
    val field2 = MutableLiveData<String>(item?.field2.orEmpty())
    val field3 = MutableLiveData<String>(item?.field3.orEmpty())
    val field4 = MutableLiveData<String>(item?.field4.orEmpty())

    fun add() {
        viewModelScope.launch(dispatchers.IO) {
            val addedItem = TheItem(
                UUID.randomUUID().toString(),
                field1.value.orEmpty(),
                field2.value.orEmpty(),
                field3.value.orEmpty(),
                field4.value.orEmpty()
            )
            networking.addItem(addedItem)
            _events.postValue(Event.ItemAdded)
        }
    }

    fun save() {
        viewModelScope.launch(dispatchers.IO) {
            val savedItem = TheItem(
                item?.id.orEmpty(),
                field1.value.orEmpty(),
                field2.value.orEmpty(),
                field3.value.orEmpty(),
                field4.value.orEmpty()
            )
            networking.addItem(savedItem)
            _events.postValue(Event.ItemSaved(savedItem))
        }
    }

    sealed class Event {

        object ItemAdded : Event()
        class ItemSaved(val item: TheItem) : Event()
    }
}
