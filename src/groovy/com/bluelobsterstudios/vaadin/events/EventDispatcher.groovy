package com.bluelobsterstudios.vaadin.events


/**
 * User: youngsoul
 *
 * A lightweight event dispatcher that dispatches events based solely on event name, to a Closure associated
 * with the event name.  When dispatching the event, any arbitrary data object can be passed to the
 * Closure listener.
 *
 * To use in a class, typically a View component you use the @Delegate annotation.  For example
 * @Delegate EventDispatcher eventDispatcher = new EventDispatcher()
 *
 * Any number of event listeners can be associated with the event name.
 *
 * Adding Event Listener
 * myEventDispatcher.addEventListener("myEvent", { myData -> println "myEvent handler for ${myData?.toString()}" })
 *
 * To use a locally defined Closure use the .& Groovy operation
 * myEventDispatcher.addEventListener("myEvent", this.&myEventHandler)
 *
 * Dispatching an Event
 * myEventDispatcher.dispatchEvent("myEvent", "My Event Data")
 *
 */
class EventDispatcher {

  Map<String, List<Closure>> eventListenerMap = [:]

  /**
   * Add an event listener Closure for the given event name
   * @param name of the event
   * @param listener Closure representing the listener
   */
  void addEventListener(String name, Closure listener ) {
    if ( eventListenerMap != null ) {
      if ( eventListenerMap[name] == null ) {
        eventListenerMap[name] = new ArrayList<Closure>()
      }
      eventListenerMap[name] << listener
    }
  }

  /**
   * Dispatch an event to all associated listeners for the given event name
   * @param name of the event
   * @param data arbitrary data to pass to listener Closure
   */
  void dispatchEvent(String name, Object data=null ) {
    if ( eventListenerMap ) {
      List<Closure> listeners = eventListenerMap[name]
      listeners?.each { it.call(data)}
    }
  }

}
