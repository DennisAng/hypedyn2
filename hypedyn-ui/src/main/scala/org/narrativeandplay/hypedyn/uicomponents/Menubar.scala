package org.narrativeandplay.hypedyn.uicomponents

import scalafx.Includes._
import scalafx.application.Platform
import scalafx.event.ActionEvent
import scalafx.scene.control.{Menu, MenuBar, MenuItem, SeparatorMenuItem}

import org.narrativeandplay.hypedyn.events.UiEventDispatcher
import org.narrativeandplay.hypedyn.keycombinations.KeyCombinations
import org.narrativeandplay.hypedyn.utils.System

object Menubar extends MenuBar {
  useSystemMenuBar = true
  menus.addAll(fileMenu, editMenu, helpMenu)

  /**
   * File Menu
   */
  private lazy val fileMenu = new Menu("File") {
    items.addAll(newStory, openStory, saveStory, saveAs, new SeparatorMenuItem(), editStoryProperties, new SeparatorMenuItem(), exit)
  }

  private lazy val newStory = new MenuItem("New") {
    accelerator = KeyCombinations.New

    onAction = { ae: ActionEvent =>
      UiEventDispatcher.requestNewStory()
    }
  }

  private lazy val openStory = new MenuItem("Open"){
    accelerator = KeyCombinations.Open

    onAction = { ae: ActionEvent =>
      UiEventDispatcher.requestLoad()
    }
  }

  private lazy val saveStory = new MenuItem("Save") {
    accelerator = KeyCombinations.Save

    onAction = { ae: ActionEvent =>
      UiEventDispatcher.requestSave()
    }
  }

  private lazy val saveAs = new MenuItem("Save As...") {
    accelerator = KeyCombinations.SaveAs

    onAction = { ae: ActionEvent =>
      UiEventDispatcher.requestSaveAs()
    }
  }

  private lazy val editStoryProperties = new MenuItem("Properties") {
    onAction = { _ => UiEventDispatcher.requestEditStoryProperties() }
  }

  private lazy val exit = new MenuItem("Exit") {
    onAction = { actionEvent: ActionEvent =>
      Platform.exit()
    }
  }

  /**
   * Edit Menu
   */
  private lazy val editMenu = new Menu("Edit") {
    items.addAll(undo, redo, new SeparatorMenuItem(), cut, copy, paste)
  }
  private lazy val undo = new MenuItem("Undo") {
    accelerator = KeyCombinations.Undo

    onAction = { actionEvent: ActionEvent =>
      UiEventDispatcher.requestUndo()
    }
  }

  private lazy val redo = new MenuItem("Redo") {
    accelerator = if (System.isWindows) KeyCombinations.RedoWin else KeyCombinations.RedoUnix

    onAction = { actionEvent: ActionEvent =>
      UiEventDispatcher.requestRedo()
    }
  }

  private lazy val cut = new MenuItem("Cut") {
    accelerator = KeyCombinations.Cut

    onAction = { actionEvent: ActionEvent =>
      UiEventDispatcher.requestCut()
    }
  }

  private lazy val copy = new MenuItem("Copy") {
    accelerator = KeyCombinations.Copy

    onAction = { actionEvent: ActionEvent =>
      UiEventDispatcher.requestCopy()
    }
  }

  private lazy val paste = new MenuItem("Paste") {
    accelerator = KeyCombinations.Paste

    onAction = { actionEvent: ActionEvent =>
      UiEventDispatcher.requestPaste()
    }
  }

  /**
   * Help Menu
   */
  private lazy val helpMenu = new Menu("Help") {
    items.addAll(about)
  }

  private lazy val about = new MenuItem("About")
}
