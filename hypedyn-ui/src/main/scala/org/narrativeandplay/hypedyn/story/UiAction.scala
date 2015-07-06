package org.narrativeandplay.hypedyn.story

import javafx.beans.property.SimpleMapProperty

import scalafx.Includes._
import scalafx.beans.property.StringProperty

import org.narrativeandplay.hypedyn.story.rules.Actionable

class UiAction(initActionType: String, initParams: Map[String, String]) extends Actionable {
  val actionTypeProperty = StringProperty(initActionType)
  val paramsProperty = new SimpleMapProperty[String, String]()

  initParams foreach { case (k, v) => paramsProperty.put(k, v) }

  override def actionType: String = actionTypeProperty()

  override def params: Map[String, String] = paramsProperty.toMap

  override def toString: String = s"UiAction($actionType, $params)"
}
