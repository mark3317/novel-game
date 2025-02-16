package ru.markn.engine

import org.koin.core.KoinApplication
import org.koin.ksp.generated.defaultModule

fun KoinApplication.engineModule() : KoinApplication = defaultModule()