package dev.renz.photoeditor

import android.view.View
import java.util.*

class PhotoEditorViewState {

    private var currentSelectedView: View? = null
    private var addedViews = mutableListOf<View>()
    private var redoViews = Stack<View>()

    fun getCurrentSelectedView(): View? {
        return currentSelectedView
    }

    fun setCurrentSelectedView(currentSelectedView: View?) {
        this.currentSelectedView = currentSelectedView
    }

    fun clearCurrentSelectedView() {
        currentSelectedView = null
    }

    fun getAddedView(index: Int): View {
        return addedViews[index]
    }

    fun getAddedViewsCount(): Int {
        return addedViews.size
    }

    fun clearAddedViews() {
        addedViews.clear()
    }

    fun addAddedView(view: View) {
        addedViews.add(view)
    }

    fun removeAddedView(view: View) {
        addedViews.remove(view)
    }

    fun removeAddedView(index: Int): View {
        return addedViews.removeAt(index)
    }

    fun containsAddedView(view: View): Boolean {
        return addedViews.contains(view)
    }

    /**
     * Replaces a view in the current "added views" list.
     *
     * @param view The view to replace
     * @return true if the view was found and replaced, false if the view was not found
     */
    fun replaceAddedView(view: View): Boolean {
        val i = addedViews.indexOf(view)
        if (i > -1) {
            addedViews[i] = view
            return true
        }
        return false
    }

    fun clearRedoViews() {
        redoViews.clear()
    }

    fun pushRedoView(view: View) {
        redoViews.push(view)
    }

    fun popRedoView(): View? {
        return redoViews.pop()
    }

    fun getRedoViewsCount(): Int {
        return redoViews.size
    }

    fun getRedoView(index: Int): View? {
        return redoViews[index]
    }
}