package com.teegarcs.dynamicadapter

import android.graphics.Canvas
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.min

class DynamicSectionHeader(private val isSticky: Boolean) : RecyclerView.ItemDecoration() {

    /**
     * Keeps track of headers currently displayed to the user.
     * key - Header Resource Layout ID
     * value - Inflated View.
     */
    private val headerTracker = HashMap<Int, View>()

    /**
     * This function manages offsetting the header owners so that the header doesn't cover up
     * the adapter item.
     */
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val pos = parent.getChildAdapterPosition(view)
        if (pos == RecyclerView.NO_POSITION) {
            return
        }

        /**
         * If the view is a section header, we need to offset the start of the item
         *
         */
        if (isSectionHeader(pos, parent)) {
            //Based on position and the positions around it, we should be a section header.
            getModelFromList(pos, parent)?.getHeaderLayoutId()?.let {
                if (headerTracker[it] == null) {
                    buildHeaderView(parent, it)
                }

                outRect.top = headerTracker[it]?.height ?: 0
            }
        }
    }


    /**
     * this function takes care of drawing all the headers on the screen. Its important to note
     * that this gets called quite frequently - especially as items scroll. Its also important to
     * note that this function draws more than just one header at a time, it needs to loop through
     * the RecyclerView content and draw everything the user can see.
     */
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        var previousSection: Int? = null
        var previousSectionLayout: Int? = null

        val tempHeaderTracker = HashMap<Int, View>()
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)

            if (position == RecyclerView.NO_POSITION) {
                continue
            }

            getModelFromList(position, parent)?.let { model ->

                if (isSectionHeader(position, parent) || (model.getHeaderLayoutId() != null
                            && model.getHeaderLayoutId() != previousSectionLayout
                            && model.sectionMatcher() != previousSection)
                ) {


                    val headerView = buildHeaderView(parent, model.getHeaderLayoutId()!!)

                    //add to temp so we can clean up the parent later.
                    tempHeaderTracker[model.getHeaderLayoutId()!!] = headerView

                    model.bindHeaderLayout(headerView)
                    //draw it
                    fixLayoutSize(headerView, parent)
                    drawHeader(
                        parent,
                        c,
                        child,
                        headerView,
                        position
                    )

                    previousSection = model.sectionMatcher()
                    previousSectionLayout = model.getHeaderLayoutId()
                }
            }
        }


        /*
        to avoid holding onto views that have long since been unused, we clear the tracker map
        and fill it back up with the temp tracker - the temp would be items that were used on this
        drawing pass. If an item went unused, then its eligible to be cleaned up and we should not
        hold reference to it anymore.
         */
        headerTracker.clear()
        headerTracker.putAll(tempHeaderTracker)

    }

    /**
     * this function takes care of drawing a specific headerview on the canvas.
     */
    private fun drawHeader(
        parent: RecyclerView,
        c: Canvas,
        child: View,
        headerView: View,
        currentHeaderPos: Int
    ) {
        c.save()
        if (isSticky) {
            // where should the top of our header be? Top of the screen or further down?
            var top = max(0, child.top - headerView.height)
            if (top == 0) {
                /**
                 * If our top is at position 0, then eventually it may need to be pushed out by a
                 * child that has no header or a child that has a different header.
                 */
                getNextDifferentChild(parent, currentHeaderPos)?.let {
                    if (it <= headerView.height) {
                        top = min(0, it - headerView.height)
                    }
                }
            }
            c.translate(child.left.toFloat(), top.toFloat())
        } else {
            c.translate(child.left.toFloat(), (child.top - headerView.height).toFloat())
        }

        headerView.draw(c)
        c.restore()
    }


    /**
     * Gets the header for a specific item from the header tracker. If the header doesn't
     * exist, it inflates it and returns the view.
     */
    private fun buildHeaderView(parent: RecyclerView, layoutId: Int): View {
        headerTracker[layoutId]?.let {
            return it
        }

        //it didn't exist so we need to inflate it.
        val headerView = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)

        fixLayoutSize(headerView, parent)
        headerTracker[layoutId] = headerView
        return headerView
    }


    /**
     * This function returns the next child after a specific header position that either doesn't
     * have a header or is providing a section header of its own.
     */
    private fun getNextDifferentChild(
        parent: RecyclerView,
        currentHeaderPos: Int
    ): Int? {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val childPos = parent.getChildAdapterPosition(child)

            if (childPos == RecyclerView.NO_POSITION || childPos == currentHeaderPos) {
                continue
            }

            val childIsHeader = isSectionHeader(childPos, parent) ||
                    getModelFromList(childPos, parent)?.getHeaderLayoutId() == null

            val childHeaderHeight = getModelFromList(childPos, parent)?.getHeaderLayoutId()?.let {
                headerTracker[it]?.height
            } ?: 0

            if (childIsHeader) {
                return child.top - childHeaderHeight
            }
        }
        return null
    }

    private fun isSectionHeader(pos: Int, recyclerView: RecyclerView): Boolean {
        val current = getModelFromList(pos, recyclerView)
        val previous = getModelFromList(pos - 1, recyclerView)
        if (current?.getHeaderLayoutId() == null) {
            return false
        }
        if (pos == 0) {
            return true
        }

        return current.sectionMatcher() !=
                previous?.sectionMatcher()

    }

    private fun getModelFromList(pos: Int, recyclerView: RecyclerView): DynamicModel? {
        (recyclerView.adapter as? DynamicAdapter)?.run {
            return if (pos < 0 || pos >= currentList.size) {
                null
            } else {
                currentList[pos]
            }
        }

        return null
    }


    /**
     * Measures the header view to make sure its size is greater than 0 and will be drawn
     * https://yoda.entelect.co.za/view/9627/how-to-android-recyclerview-item-decorations
     */
    private fun fixLayoutSize(view: View, parent: ViewGroup) {

        val widthSpec = View.MeasureSpec.makeMeasureSpec(
            parent.width,
            View.MeasureSpec.EXACTLY
        )
        val heightSpec = View.MeasureSpec.makeMeasureSpec(
            parent.height,
            View.MeasureSpec.UNSPECIFIED
        )
        val childWidth = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            view.layoutParams.width
        )
        val childHeight = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )

        view.measure(childWidth, childHeight)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }
}