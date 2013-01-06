package com.bluelobsterstudios.vaadin.events

import com.vaadin.ui.Component
import groovy.transform.CompileStatic

/**
 * User: youngsoul
 *
 */
@CompileStatic
class BaseEvent extends Component.Event {


  BaseEvent(Component source) {
    super(source)
  }

}
