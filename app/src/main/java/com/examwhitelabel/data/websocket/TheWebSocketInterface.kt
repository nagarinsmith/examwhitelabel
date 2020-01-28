package com.examwhitelabel.data.websocket

import com.tinder.scarlet.ws.Receive
import kotlinx.coroutines.channels.ReceiveChannel

interface TheWebSocketInterface {

    @Receive
    fun getUpdates(): ReceiveChannel<Unit>
}
