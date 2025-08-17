package ru.markn.engine.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import ru.markn.engine.utils.withDebounce
import ru.markn.engine.utils.withDelayOrCancelDebounce
import kotlin.time.Duration.Companion.seconds

abstract class MviViewModel<S: IMviState>(
    initialState: S
) : ViewModel() {
    private val _stateFlow = MutableStateFlow(initialState)

    private val _observingFlow: Flow<Unit> = flow {
        emit(Unit)
        coroutineScope {
            observableFlows.map {
                async(Dispatchers.IO) {
                    it.collect()
                }
            }.awaitAll()
        }
    }.flowOn(Dispatchers.IO)

    protected open val observableFlows: List<Flow<*>> = listOf()

    companion object {
        private val _eventsNavChannel = Channel<MviNavEvent>(Channel.UNLIMITED)
        val eventsNavFlow: Flow<MviNavEvent> = _eventsNavChannel.receiveAsFlow()
    }

    val stateFlow: SharedFlow<S> =
        combine(_stateFlow, _observingFlow) { s, _ -> s }
            .distinctUntilChanged()
            .shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    val state: S
        get() = _stateFlow.value

    fun updateState(transform: S.() -> S): S = runBlocking(Dispatchers.Main.immediate) {
        val newState = state.transform()
        _stateFlow.emit(newState)
        newState.also { println("updateState: $it") }
    }

    /**
     * Перейти к экрану по указанному пути навигации.
     * Если такого пути не существует в лог выведется ошибка [IllegalArgumentException]
     *
     * @param route узел целевого экрана
     */
    suspend fun navigateTo(
        route: Any,
    ) = _eventsNavChannel.send(MviNavEvent.NavigateTo(route))

    /**
     * Вернуться к последнему вхождению указанного экрана в стеке навигации
     * или закрыть текущее окно приложения, если в стеке не нашлось подходящего для перехода экрана.
     *
     * @param popUpTo узел целевого экрана
     * @param inclusive также закрыть указанный экран
     */
    suspend fun navigateBackTo(
        popUpTo: Any,
        inclusive: Boolean = false,
    ) = _eventsNavChannel.send(MviNavEvent.NavigateBackTo(popUpTo, inclusive))

    /**
     * Поведение как при нажатии системной кнопки назад - вернуться на предыдущий экран в стеке навигации
     * или закрыть окно приложения, если стек пуст.
     */
    suspend fun navigateBack() =
        _eventsNavChannel.send(MviNavEvent.NavigateBack)

    /**
     * Перейти к экрану выше в иерархии графа навигации
     * или закрыть окно приложения, если находились на самом верхнем уровне.
     */
    suspend fun navigateUp() =
        _eventsNavChannel.send(MviNavEvent.NavigateUp)

    fun intent(
        name: String,
        updateStateImmediate: (S.() -> S)? = null,
        block: suspend MviViewModel<S>.() -> Unit = {},
    ) {
        runCatching {
            println("Intent: $name")
            updateStateImmediate?.let { updateState(it) }
            viewModelScope.launch(Dispatchers.IO) {
                block()
            }
        }.onFailure {
            println("Error in intent: ${it.message}")
        }
    }

    /**
     * Можно использовать, например, для кнопок
     */
    fun skipDebounceIntent(
        name: String,
        updateStateImmediate: (S.() -> S)? = null,
        block: suspend MviViewModel<S>.() -> Unit = {},
    ) {
        runCatching {
            println("Intent: $name")
            updateStateImmediate?.let { updateState(it) }
            viewModelScope.launch(Dispatchers.IO) {
                withDebounce(name, 1.seconds) {
                    runCatching {
                        block()
                    }.onFailure {
                        if (it !is CancellationException) throw it
                    }
                }
            }
        }.onFailure {
            println("Error in intent: ${it.message}")
        }
    }

    /**
     * Можно использовать, например, для поля ввода поиска
     */
    fun delayDebounceIntent(
        name: String,
        updateStateImmediate: (S.() -> S)? = null,
        block: suspend MviViewModel<S>.() -> Unit = {},
    ) {
        runCatching {
            println("Intent: $name")
            updateStateImmediate?.let { updateState(it) }
            viewModelScope.launch(Dispatchers.IO) {
                withDelayOrCancelDebounce(name, 1.seconds) {
                    runCatching {
                        block()
                    }.onFailure {
                        if (it !is CancellationException) throw it
                    }
                }
            }
        }.onFailure {
            println("Error in intent: ${it.message}")
        }
    }
}