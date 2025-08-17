package ru.markn.engine.mvi

sealed interface MviNavEvent {
    /**
     * Вернуться к последнему вхождению указанного экрана в стеке навигации
     * или закрыть текущее окно приложения, если в стеке не нашлось подходящего для перехода экрана.
     *
     * @param popUpTo узел целевого экрана
     * @param inclusive также закрыть указанный экран
     */
    data class NavigateBackTo(
        val popUpTo: Any,
        val inclusive: Boolean = false,
    ) : MviNavEvent

    /**
     * Поведение как при нажатии системной кнопки "назад" - вернуться на предыдущий экран в стеке навигации
     * или закрыть текущее окно приложения, если стек пуст.
     */
    data object NavigateBack : MviNavEvent

    /**
     * Перейти к экрану выше в иерархии графа навигации
     * или закрыть текущее окно приложения, если находились на самом верхнем уровне.
     */
    data object NavigateUp : MviNavEvent

    /**
     * Перейти к экрану по указанному пути навигации.
     * Если такого пути не существует в лог выведется ошибка [IllegalArgumentException]
     *
     * @param route узел целевого экрана
     */
    data class NavigateTo(
        val route: Any,
    ) : MviNavEvent
}