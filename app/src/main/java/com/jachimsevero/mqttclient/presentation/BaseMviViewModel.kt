package com.jachimsevero.mqttclient.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseMVIViewModel<State : MviState, Event : MviEvent, Effect : MviEffect>(
    initialState: State
) : ViewModel() {

  private val _state = MutableStateFlow(initialState)
  val state = _state.asStateFlow()

  private val _effect = Channel<Effect>(Channel.BUFFERED)
  val effect = _effect.receiveAsFlow()

  fun setEvent(event: Event) {
    viewModelScope.launch { handleEvent(event) }
  }

  protected abstract suspend fun handleEvent(event: Event)

  protected fun setState(reducer: State.() -> State) {
    _state.update { currentState -> reducer(currentState) }
  }

  protected fun setEffect(sideEffect: () -> Effect) {
    viewModelScope.launch { _effect.send(sideEffect()) }
  }
}

interface MviState

interface MviEvent

interface MviEffect
