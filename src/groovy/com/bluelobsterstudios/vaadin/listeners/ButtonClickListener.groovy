package com.bluelobsterstudios.vaadin.listeners

import com.vaadin.ui.Button
import groovy.transform.CompileStatic

/**
 * User: youngsoul
 *
 * ButtonClickListener that delegates to a Closure.
 *
 * Usage example
 * saveUserBtn.addClickListener(new ButtonClickListener({
 *  if( !selectedUser ) {
 *    updateSelectedUser(new User())
 *  }
 *  selectedUser.with {
 *    userName = userNameTF.value
 *    lastName = lastNameTF.value
 *    firstName = firstNameTF.value
 *  }
 * surveyScriptService.saveOrUpdateUser(selectedUser)
 * refreshUserTable()
 * }))
 *
 */
@CompileStatic
class ButtonClickListener implements Button.ClickListener {
  Closure listenerClosure

  /**
   * Constructor with required delegate Closure
   * @param closure
   */
  ButtonClickListener(Closure closure) {
    listenerClosure = closure
  }

  /**
   * Implementation of the buttonClick interface to delegate to the Closure
   *
   * @param clickEvent
   */
  void buttonClick(Button.ClickEvent clickEvent) {
    if ( listenerClosure ) {
      listenerClosure(clickEvent)
    }
  }
}
