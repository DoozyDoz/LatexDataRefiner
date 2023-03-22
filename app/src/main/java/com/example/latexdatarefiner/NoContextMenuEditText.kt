package com.example.latexdatarefiner

import android.content.Context
import android.util.AttributeSet
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.AppCompatEditText

class NoContextMenuEditText : AppCompatEditText {

    constructor(context: Context) : super(context) {
        disableContextMenu()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        disableContextMenu()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        disableContextMenu()
    }
//    https://stackoverflow.com/a/51222179/8872691
    private fun disableContextMenu() {
        customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                // Prevent the context menu from being created
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                menu.clear()
                return false
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode) {}
        }
    }

//    override fun onTextContextMenuItem(id: Int): Boolean {
//        // Intercept the "Select all" option by checking for the matching ID
//        if (id == android.R.id.selectAll) {
//            return true
//        }
//        return super.onTextContextMenuItem(id)
//    }
}


