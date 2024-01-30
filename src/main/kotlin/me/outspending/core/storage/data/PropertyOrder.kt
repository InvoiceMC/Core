package me.outspending.core.storage.data

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class PropertyOrder(val order: Int)
