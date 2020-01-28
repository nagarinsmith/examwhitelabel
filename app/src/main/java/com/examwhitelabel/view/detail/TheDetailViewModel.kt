package com.examwhitelabel.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.examwhitelabel.data.TheItem
import com.examwhitelabel.data.networking.TheNetworkingInterface
import com.examwhitelabel.util.TheDispatchers
import com.examwhitelabel.view.shared.SingleLiveEvent
import kotlinx.coroutines.launch

class TheDetailViewModel(val item: TheItem, private val networking: TheNetworkingInterface, private val dispatchers: TheDispatchers) : ViewModel() {

    private val _events = SingleLiveEvent<Event>()
    val events: LiveData<Event> get() = _events

    fun deleteItem() {
        viewModelScope.launch(dispatchers.IO) {
            networking.deleteItem(item.id)
            _events.postValue(Event.ItemDeleted)
        }
    }

    sealed class Event {

        object ItemDeleted : Event()
    }
}
