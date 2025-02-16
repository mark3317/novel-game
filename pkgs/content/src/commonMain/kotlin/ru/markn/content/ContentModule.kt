package ru.markn.content

import org.koin.core.KoinApplication
import org.koin.ksp.generated.defaultModule

fun KoinApplication.contentModule() : KoinApplication = defaultModule()