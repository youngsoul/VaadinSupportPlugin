package com.bluelobsterstudios.vaadin.listeners

import com.bluelobsterstudios.vaadin.ui.NavigatorPanel
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener
import groovy.transform.CompileStatic

/**
 * User: youngsoul
 *
 * NavigatorViewChangeListener implements a beforeViewChange and
 * afterViewChange protocol giving the views a chance to execute
 * functionality before the old view is deactivated, and
 * before the new view is activated.  If either the old view or the new
 * view vetos ( returns false ) then the changing of the view is
 * cancelled.
 *
 * After the view has been changed, this listener will notify both
 * the old view and the new view giving them a chance to execute
 * functionality after the view switch has taken place.
 *
 * This listener assumes the views are a @see NavigatorPanel which
 * provides default implementations but allows concrete implementations
 * to create their own.
 *
 * This listener is applied to the Vaadin Navigator.
 *
 * Usage example
 *
 *  Navigator navigator = new Navigator(this,this)
 *  navigator.addViewChangeListener(new NavigatorViewChangeListener())
 *
 */
@CompileStatic
class NavigatorViewChangeListener implements ViewChangeListener {

  /**
   * Called before the view is changed.  This implementation will
   * delegate the call to the old view and the new view allowing both
   * to veto the change if necessary
   *
   * @param event
   * @return true - ok to change, false-veto and do not allow the change
   */
  boolean beforeViewChange(ViewChangeListener.ViewChangeEvent event) {
    View viewBeingDeactivated = event.getOldView()
    View viewBeingActivated = event.getNewView()
    if (viewBeingDeactivated instanceof NavigatorPanel && viewBeingActivated instanceof NavigatorPanel &&
        viewBeingDeactivated != viewBeingActivated) { // deactivating and then reactivating seems like a no-op
      NavigatorPanel navigatorPanelDeactivate = viewBeingDeactivated as NavigatorPanel
      NavigatorPanel navigatorPanelActivate = viewBeingActivated as NavigatorPanel

      if (navigatorPanelDeactivate.beforeViewChange(event)) {
        // then the old view says we can change
        return navigatorPanelActivate.beforeViewChange(event)
      } else {
        return false // the oldView says we cannot change
      }
    } else {
      return true
    }
  }

  /**
   * Called after the view is changed.  This implementation will delegate to
   * both the old view and new view.
   *
   * @param event
   */
  void afterViewChange(ViewChangeListener.ViewChangeEvent event) {
    View viewBeingDeactivated = event.getOldView()
    View viewBeingActivated = event.getNewView()
    if (viewBeingDeactivated instanceof NavigatorPanel && viewBeingActivated instanceof NavigatorPanel) {
      NavigatorPanel navigatorPanelDeactivate = viewBeingDeactivated as NavigatorPanel
      NavigatorPanel navigatorPanelActivate = viewBeingActivated as NavigatorPanel
      navigatorPanelDeactivate.afterViewChange(event)
      if (navigatorPanelDeactivate != navigatorPanelActivate) {
        navigatorPanelActivate.afterViewChange(event)
      }

    }
  }
}
