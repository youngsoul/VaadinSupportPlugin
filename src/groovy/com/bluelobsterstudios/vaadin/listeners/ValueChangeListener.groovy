package com.bluelobsterstudios.vaadin.listeners

import com.vaadin.data.Property
import groovy.transform.CompileStatic

/**
 * User: youngsoul
 *
 * ValueChangeListener that delegates to a Closure
 *
 * Usage example
 * citySelect = new ListSelect("Please select a city")
 * citySelect.addValueChangeListener(new ValueChangeListener({event ->
 *      Notification.show("Selected city: " + event.getProperty().value )
 *      cityChangeWatcher.valueToWatch = event.getProperty().value
 *    })
 * )
 *
 */
@CompileStatic
class ValueChangeListener implements Property.ValueChangeListener {

  Closure listenerClosure

  /**
   * Constructor with required delegate Closure
   * @param closure
   */
  ValueChangeListener(Closure closure) {
    listenerClosure = closure
  }

  /**
   * Implementation of the valueChange interface to delegate to the Closure
   * @param valueChangeEvent
   */
  void valueChange(Property.ValueChangeEvent valueChangeEvent) {
    if ( listenerClosure ) {
      listenerClosure(valueChangeEvent)
    }
  }
}
