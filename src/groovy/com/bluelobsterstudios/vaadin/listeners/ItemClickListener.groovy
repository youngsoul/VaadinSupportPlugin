package com.bluelobsterstudios.vaadin.listeners

import com.vaadin.event.ItemClickEvent
import groovy.transform.CompileStatic

/**
 * User: youngsoul
 *
 * ItemClickListener that delegates to a Closure
 *
 * Usage example
 *
 * userTable.addItemClickListener(new ItemClickListener({ ItemClickEvent event ->
 *  User item = event.getItemId() as User
 *  updateSelectedUser(item)
 *}))
 *
 */
@CompileStatic
class ItemClickListener implements ItemClickEvent.ItemClickListener {
  Closure listenerClosure

  /**
   *
   * Constructor with required delegate Closure
   * @param closure
   */
  ItemClickListener(Closure closure) {
    listenerClosure = closure
  }

  /**
   * Implementation of the itemClick interface to delegate to the Closure
   * @param event
   */
  void itemClick(ItemClickEvent event) {
    if (listenerClosure) {
      listenerClosure(event)
    }
  }
}
