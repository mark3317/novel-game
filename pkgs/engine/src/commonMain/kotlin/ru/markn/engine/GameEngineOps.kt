package ru.markn.engine

import kotlinx.coroutines.flow.MutableSharedFlow
import org.koin.core.annotation.Single

@Single
class GameEngineOps {
    val isFinishedGameFlow = MutableSharedFlow<Unit>()
}