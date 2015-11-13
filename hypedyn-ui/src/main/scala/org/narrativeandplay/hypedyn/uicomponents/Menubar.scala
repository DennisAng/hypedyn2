package org.narrativeandplay.hypedyn.uicomponents

import scalafx.Includes._
import scalafx.application.Platform
import scalafx.beans.property.ReadOnlyBooleanProperty
import scalafx.event.ActionEvent
import scalafx.scene.control.{Menu, MenuBar, SeparatorMenuItem}

import org.narrativeandplay.hypedyn.events.UiEventDispatcher
import org.narrativeandplay.hypedyn.keycombinations.KeyCombinations
import org.narrativeandplay.hypedyn.utils.System

/**
 * Menu bar for the application
 */
class Menubar(mainStageFocused: ReadOnlyBooleanProperty) extends MenuBar {
  useSystemMenuBar = true
  menus.addAll(fileMenu, editMenu, helpMenu)

  /**
   * Extension to the standard menu item class to disable it whenever the main window is not in focus
   *
   * @param name The display name of the menu item
   */
  class MenuItem(name: String) extends scalafx.scene.control.MenuItem(name) {
    // Disable the menu items when the main window is not being selected,effectively preventing the shortcuts from
    // propagating to node editor windows. The function `flatMap` is a Java method call to a generic method. Due to
    // limitations in the Scala compiler, the type parameters of these method calls must be provided for Scala to
    // correctly type the results.
    disable <== !mainStageFocused
  }

  /**
   * File Menu
   */
  private lazy val fileMenu = new Menu("File") {
    items.addAll(newStory, openStory, saveStory, saveAs, export, new SeparatorMenuItem(), editStoryProperties, new SeparatorMenuItem(), exit)
  }

  private lazy val newStory = new MenuItem("New") {
    accelerator = KeyCombinations.New

    onAction = { ae: ActionEvent =>
      UiEventDispatcher.requestExit() foreach { createNewStory =>
        if (createNewStory) UiEventDispatcher.requestNewStory()
      }
    }
  }

  private lazy val openStory = new MenuItem("Open"){
    accelerator = KeyCombinations.Open

    onAction = { ae: ActionEvent =>
      UiEventDispatcher.requestExit() foreach { loadStory =>
        if (loadStory) UiEventDispatcher.requestLoad()
      }
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

  private lazy val export = new MenuItem("Export...") {
    //accelerator = KeyCombinations.Export

    onAction = { ae: ActionEvent =>
      UiEventDispatcher.requestExport()
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

    disable <== !UiEventDispatcher.undoAvailable || !mainStageFocused

    onAction = { actionEvent: ActionEvent =>
      UiEventDispatcher.requestUndo()
    }
  }

  private lazy val redo = new MenuItem("Redo") {
    accelerator = if (System.isWindows) KeyCombinations.RedoWin else KeyCombinations.RedoUnix

    disable <== !UiEventDispatcher.redoAvailable || !mainStageFocused

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
