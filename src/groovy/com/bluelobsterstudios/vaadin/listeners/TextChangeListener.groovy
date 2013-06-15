package com.bluelobsterstudios.vaadin.listeners

import com.vaadin.event.FieldEvents
import groovy.transform.CompileStatic

/**
 * User: youngsoul
 *
 * TextChangeListener that delegates to a Closure
 *
 * Usage example
 *
 *
 */
@CompileStatic
class TextChangeListener implements FieldEvents.TextChangeListener {
    Closure listenerClosure

    TextChangeListener(Closure closure) {
        listenerClosure = closure
    }

    void textChange(FieldEvents.TextChangeEvent event) {
        if( listenerClosure ) {
            listenerClosure(event)
        }
    }
}
