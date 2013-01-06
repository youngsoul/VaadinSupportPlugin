package com.bluelobsterstudios.vaadin.ui

import com.bluelobsterstudios.vaadin.events.EventDispatcher
import com.vaadin.navigator.Navigator
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import com.vaadin.ui.Panel
import groovy.transform.CompileStatic


/**
 * User: youngsoul
 *
 * Base Vaadin Panel implementation that coordinates the following callbacks:
 *
 * initializeView is guaranteed to be called only once and only when the view
 * is about to be shown.  This is is where all UI elements should be created.
 *
 * beforeViewChange is called everytime before enterView and gives the implementation a
 * chance to execute functionality before switching to the view.
 *
 * enterView is called everytime the Navigator switches to the NavigatorPanel
 * implementation.
 *
 * afterViewChange is called everytime after enterView and the view has been
 * switched to.
 */
@CompileStatic
abstract class NavigatorPanel extends Panel implements ViewChangeListener, View {

  @Delegate EventDispatcher eventDispatcher = new EventDispatcher()

  /**
   * reference to the navigator instance created in the Vaadin UI init method
   *
   * A typical usage would look like:
   *     Navigator navigator = new Navigator(this,this)
   *     LoginView loginView = new LoginView(navigator: navigator)
   *
   * This reference can then be used in the concrete NavigatorPanel to
   * navigate to other views e.g.
   * navigator.navigateTo("loginView")
   *
   */
  Navigator navigator

  /**
   * Flag to indicate if panel has ever been initialized
   */
  Boolean isViewInitialized = false

  /**
   * Called once when the view is navigated to but the view has not been initialized yet.
   * The method is meant to be where the view is initially created and executed once, but only
   * if the user ever navigates to the view.
   */
  abstract void initializeView()

  /**
   * Called every time the view is navigated to.  Same calling semantics as
   * as View.enter
   *
   * @param event
   */
  abstract void enterView(ViewChangeListener.ViewChangeEvent event)

  /**
   * default implementation which always returns true, meaning that the
   * view is allowed to change
   * @param event
   * @return true - allow the view to change, false - do not allow the view to change
   */
  boolean beforeViewChange(ViewChangeListener.ViewChangeEvent event) {
    return true
  }

  /**
   * default implementation called after the view has been switched to.
   *
   * @param event
   */
  void afterViewChange(ViewChangeListener.ViewChangeEvent event) {

  }

  /**
   * This is method is not meant to be overridden by subclasses.
   * Implementation from View interface.  Called when the view is navigated
   * to and is being viewed.
   *
   * This implementation will look to see if the view has been initialized yet,
   * and if not call the initializeView.
   *
   * This method then delegates to the enterView method.
   * @param event
   */
  void enter(ViewChangeListener.ViewChangeEvent event) {
    if ( !isViewInitialized ) {
      initializeView()
      isViewInitialized = true
    }
    enterView( event )
  }
}
