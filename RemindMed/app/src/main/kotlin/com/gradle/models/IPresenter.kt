package com.gradle.models

import com.gradle.ui.views.ISubscriber

abstract class IPresenter {
    private val subscribers = mutableListOf<ISubscriber>()
    fun notifySubscribers() {
        subscribers.forEach() {
            it.update()
        }
    }

    fun subscribe(subscriber: ISubscriber) {
        subscribers.add(subscriber)
    }

    fun unsubscribe(subscriber: ISubscriber) {
        subscribers.remove(subscriber)
    }
}