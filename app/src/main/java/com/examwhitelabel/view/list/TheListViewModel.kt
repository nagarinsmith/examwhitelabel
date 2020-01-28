package com.examwhitelabel.view.list

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.examwhitelabel.data.TheItem
import com.examwhitelabel.data.database.TheDatabaseInterface
import com.examwhitelabel.data.networking.TheNetworkingInterface
import com.examwhitelabel.data.websocket.TheWebSocketInterface
import com.examwhitelabel.util.TheDispatchers
import com.examwhitelabel.view.shared.StateLayout
import com.examwhitelabel.view.shared.isNetworkAvailable
import com.examwhitelabel.view.shared.registerNetworkCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList

class TheListViewModel(
    context: Context,
    private val networking: TheNetworkingInterface,
    private val database: TheDatabaseInterface,
    private val webSockets: TheWebSocketInterface,
    private val dispatchers: TheDispatchers
) : ViewModel() {

    private val _viewState = MutableLiveData<StateLayout.State>(StateLayout.State.Normal)
    val viewState: LiveData<StateLayout.State> get() = _viewState

    private val _itemList = MutableLiveData<List<TheItem>>()
    val itemList: LiveData<List<TheItem>> get() = _itemList

    private val _isAddEnabled = MutableLiveData<Boolean>(true)
    val isAddEnabled: LiveData<Boolean> get() = _isAddEnabled

    init {
        context.registerNetworkCallback(onConnected = ::onNetworkAvailable, onDisconnected = ::onNetworkUnavailable)
        loadItemList(networking, database, dispatchers)
        _isAddEnabled.value = context.isNetworkAvailable()

        viewModelScope.launch(dispatchers.IO) {

            // Reloads every $POLL_TIME millis
//            manualPoll()

            // Reloads on ws received info
            try {
                webSocketPoll()
            } catch (_: Exception) {
            }
        }
    }

    private tailrec suspend fun manualPoll() {
        delay(POLL_TIME)
        loadItemList(networking, database, dispatchers)
        manualPoll()
    }

    private suspend fun webSocketPoll() {
        for (unit in webSockets.getUpdates()) {
            loadItemList(networking, database, dispatchers)
        }
    }

    private fun loadItemList(networking: TheNetworkingInterface, database: TheDatabaseInterface, dispatchers: TheDispatchers) {
        _viewState.postValue(StateLayout.State.Loading)
        viewModelScope.launch(dispatchers.IO) {
            val (items, state) = try {
                val items = networking.getItems()
                try {
                    database.updateItems(items)
                } catch (_: Exception) {
                }
                val state = if (items.isEmpty()) StateLayout.State.Empty else StateLayout.State.Normal
                items to state
            } catch (_: Exception) {
                val items = database.getItems()
                val state = if (items.isEmpty()) StateLayout.State.Empty else StateLayout.State.Normal
                items to state
            } catch (_: Exception) {
                null to StateLayout.State.Error.NetworkError { loadItemList(networking, database, dispatchers) }
            }
            items?.toImmutableList().let(_itemList::postValue)
            _viewState.postValue(state)
        }
    }

    private fun onNetworkAvailable() {
        loadItemList(networking, database, dispatchers)
        _isAddEnabled.postValue(true)
    }

    private fun onNetworkUnavailable() {
        _isAddEnabled.postValue(false)
    }

    companion object {

        private const val POLL_TIME = 10000L
    }
}
