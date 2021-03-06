package org.narrativeandplay.hypedyn.plugins.storyviewer

import scalafx.scene.control.Control

import org.narrativeandplay.hypedyn.plugins.PluginsController
import org.narrativeandplay.hypedyn.plugins.narrativeviewer.NarrativeViewer

/**
 * Controller for story viewer plugins
 */
object NarrativeViewersController {
  val NarrativeViewers = PluginsController.plugins collect { case (name, plugin: Control with NarrativeViewer) =>
      name -> plugin
  }

  val DefaultViewer = NarrativeViewers("Default Story Viewer")
}
