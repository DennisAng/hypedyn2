package org.narrativeandplay.hypedyn.story

import scalafx.Includes._
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableMap

import org.narrativeandplay.hypedyn.story.rules.Actionable
import org.narrativeandplay.hypedyn.story.rules.Actionable.ActionType
import org.narrativeandplay.hypedyn.story.rules.RuleLike.{ParamName, ParamValue}

/**
 * UI implementation for Actionable
 *
 * @param initActionType The initial action type
 * @param initParams the initial parameters and their values
 */
class UiAction(initActionType: ActionType, initParams: Map[ParamName, ParamValue]) extends Actionable {
  /**
   * Backing property for the action type
   */
  val actionTypeProperty = ObjectProperty(initActionType)

  /**
   * Backing property for the parameters
   */
  val paramsProperty = ObjectProperty(ObservableMap(initParams.toSeq: _*))

  /**
   * Returns the type of action being instanced
   */
  override def actionType: ActionType = actionTypeProperty()

  /**
   * Returns the parameters of the instanced action and their values
   */
  override def params: Map[ParamName, ParamValue] = paramsProperty().toMap

  override def toString: String = s"UiAction($actionType, $params)"
}
