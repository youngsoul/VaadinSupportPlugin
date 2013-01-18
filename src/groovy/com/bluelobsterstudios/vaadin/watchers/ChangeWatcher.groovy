package com.bluelobsterstudios.vaadin.watchers

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

/**
 * User: youngsoul
 *
 * ChangeWatcher will look for changes to the reference it is watching and
 * update all watch binding.  This class can update UI elements or
 * delegate to a Closure.
 *
 * This class has two ways to trigger the watch bindings.
 *
 * 1) The actual value that is being watch is changed.
 * A typical scenario of this usage is selection of an item in a table that has to
 * update form fields.  Updating the valueToWatch value to the selected item from the
 * table, will cause the watch specifications to fire.  This scenario does NOT required
 * the valueToWatch to implement the @Bindable annotation
 *
 * 2) A property on the value being watched is changed.
 * In this scenario, the valueToWatch does not change, but a property on the valueToWatch
 * changes and that property was marked with the @Bindable annotation ( or the entire
 * class was marked with the @Bindable annotation )  This scenario requires the
 * PropertyChangeListeners pattern to be used.
 *
 * Typical Usage:
 *
 *     ChangeWatcher userChangeWatcher = new ChangeWatcher()
 *     userChangeWatcher.valueToWatch = selectedUser
 *     ...
 *     lastNameTF = new TextField("LastName:")
 *     userChangeWatcher.watch("lastName", lastNameTF, "value", "")
 * Developer Note:
 * http://groovy.codehaus.org/gapi/groovy/beans/Bindable.html
 * Since the Bindable annotation is a source level annotation, there are really no artifacts left
 * that would tell me if something ( either property or class ) were bindable except the
 * additional methods to management the property change listeners that are left as a result of the
 * Bindable AST.
 */
class ChangeWatcher implements PropertyChangeListener {

  /**
   * reference to the value being watched
   */
    def valueToWatch

  /**
   * watch bindings
   * key = name of source property
   * value = List of watch bindings
   */
    Map bindingMap = [:]

    /**
     * watchers that are triggered when the value being watched is changed
     * instead of just a particular property.
     */
    List valueChangeClosures = []

  /**
   * Because the @Bindable is a source level annotation there is no way to
   * know if the item is Bindable.
   * @return
   */
    protected boolean isBindable() {
        if ( valueToWatch?.metaClass?.respondsTo(valueToWatch, 'removePropertyChangeListener') ||
                valueToWatch?.metaClass?.respondsTo(valueToWatch, 'addPropertyChangeListener') ||
                valueToWatch?.metaClass?.respondsTo(valueToWatch, 'firePropertyChange') ) {
            return true
        } else {
            return false
        }
    }

  /**
   * apply the new value to the propertyname of the valueToWatch object
   * @param newValue
   * @param propertyName
   */
    protected void applyNewValue(Object newValue, String propertyName ) {
      List watchBindings = bindingMap[propertyName] as List
      watchBindings?.each { Expando expando ->
        if ( expando.properties.containsKey("callBack")) {
          expando.callBack.call( newValue)
        } else {
          def targetObj = expando.targetObj
          def targetPropName = expando.targetPropName
          targetObj."$targetPropName" = newValue ?: expando.nullValue
        }
      }
    }

  /**
   * set the valueToWatch and update any watching bindings
   *
   * @param value
   */
    void setValueToWatch(def value ) {
        // if we have an existing watcher, then stop watching
        if( valueToWatch && isBindable() ) {
            valueToWatch.removePropertyChangeListener(this)
        }

        valueToWatch = value
        if ( valueToWatch && isBindable() ) {
            List<PropertyChangeListener> propChangeListeners = valueToWatch.getPropertyChangeListeners()
            if ( propChangeListeners ) {
                if ( !propChangeListeners.contains(this)) {
                    valueToWatch.addPropertyChangeListener(this)
                }
            } else {
                valueToWatch.addPropertyChangeListener(this)
            }
        }

        // when a new valueToWatch is set, update all of the binding mappings
        // with the view value
        if ( bindingMap ) {
            bindingMap.each {String k, List v ->
                Object newValue = null
                if ( valueToWatch ) newValue = valueToWatch."$k"
                applyNewValue(newValue, k)
            }
        }
        
 
        if( valueChangeClosures ) {
            valueChangeClosures.each { Closure callBack ->
                callBack.call(valueToWatch)
            }
        }

    }

  /**
   * Implementation of the propertyChange listener interface that will cause the
   * watch bindings to fire.
   *
   * @param propertyChangeEvent
   */
    void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        applyNewValue(propertyChangeEvent.newValue, propertyChangeEvent.propertyName)
    }

  /**
   * Create a watch binding from the source property name of the valueToWatch to the
   * target objects, target property name.
   *
   * Typical Usage:
   * userChangeWatcher.watch("lastName", lastNameTF, "value", "")
   * @param sourcePropName property name of the valueToWatch that is the source
   * @param targetObj object that is the target
   * @param targetPropName property name of the target object
   * @param nullValue value to use if the valueToWatch is null
   */
    void watch(String sourcePropName, Object targetObj, String targetPropName, Object nullValue = null) {
        if( !bindingMap[sourcePropName] ) bindingMap[sourcePropName] = []
        bindingMap[sourcePropName] << new Expando(targetObj: targetObj, targetPropName: targetPropName, nullValue: nullValue)
    }

  /**
   * Create a watch binding that calls a Closure instead of updating a particular
   * target objects property value.
   *
   * Typical Usage:
   * Typical Usage:
   * userChangeWatcher.watch("lastName", { println "lastName changed" })
   *
   * @param sourcePropName property name of the valueToWatch that is the source
   * @param callBack - Closure to call when the watch binding fires
   */
    void watch( String sourcePropName, Closure callBack) {
        if( !bindingMap[sourcePropName] ) bindingMap[sourcePropName] = []
        bindingMap[sourcePropName] << new Expando(callBack: callBack)
    }

    /**
     * Create a watch binding that calls a Closure when the valuing being watched
     * is changed.
     * @param callBack - Closure to call when the value to watch is changed.
     */
    void watch(Closure callBack ) {
        if( callBack) valueChangeClosures << callBack
    }


}
