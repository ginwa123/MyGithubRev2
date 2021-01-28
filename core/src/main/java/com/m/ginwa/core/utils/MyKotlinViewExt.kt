package com.m.ginwa.core.utils

import android.app.Activity
import android.content.SharedPreferences
import android.graphics.drawable.InsetDrawable
import android.view.MotionEvent
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.m.ginwa.core.R
import java.net.UnknownHostException
import java.util.*

fun Activity.showToast(message: String?) {
    message?.let { Toast.makeText(this.application, it, Toast.LENGTH_SHORT).show() }
}

fun Activity.showToastError(e: Exception) {
    if (e is UnknownHostException) {
        this.showToast(getString(R.string.cannot_connect_to_internet))
    } else {
        this.showToast(e.message)
    }
}

fun RecyclerView.addDividerLine(
    dimenLeft: Int? = null,
    dimenTop: Int? = null,
    dimenRight: Int? = null,
    dimenBot: Int? = null
) {
    val attrs = intArrayOf(android.R.attr.listDivider)
    val a = this.context.obtainStyledAttributes(attrs)
    val divider = a.getDrawable(0)
    val insetLeft = dimenLeft?.let { this.context.resources.getDimensionPixelSize(it) }
    val insetRight = dimenRight?.let { this.context.resources.getDimensionPixelSize(it) }
    val insetTop = dimenTop?.let { this.context.resources.getDimensionPixelSize(it) }
    val insetBot = dimenBot?.let { this.context.resources.getDimensionPixelSize(it) }
    val insetDivider =
        InsetDrawable(divider, insetLeft ?: 0, insetTop ?: 0, insetRight ?: 0, insetBot ?: 0)
    a.recycle()
    val itemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
    itemDecoration.setDrawable(insetDivider)
    this.addItemDecoration(itemDecoration)
}

inline fun <reified T : Enum<T>> SharedPreferences.getEnum(key: String, defaultValue: String): T {
    val value = this.getString(key, defaultValue)?.toUpperCase(Locale.ROOT)
    if (value != null) return enumValueOf(value)
    return enumValueOf(defaultValue)
}


// https://medium.com/@BladeCoder/fixing-recyclerview-nested-scrolling-in-opposite-direction-f587be5c1a04
// kotlin extension for fixing bug when scrolling using recyclerview inside viewpager
fun RecyclerView.enforceSingleScrollDirection() {
    val enforcer =
        SingleScrollDirectionEnforcer()
    addOnItemTouchListener(enforcer)
    addOnScrollListener(enforcer)
}


private class SingleScrollDirectionEnforcer : RecyclerView.OnScrollListener(),
    RecyclerView.OnItemTouchListener {

    private var scrollState = RecyclerView.SCROLL_STATE_IDLE
    private var scrollPointerId = -1
    private var initialTouchX = 0
    private var initialTouchY = 0
    private var dx = 0
    private var dy = 0

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        when (e.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                scrollPointerId = e.getPointerId(0)
                initialTouchX = (e.x + 0.5f).toInt()
                initialTouchY = (e.y + 0.5f).toInt()
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                val actionIndex = e.actionIndex
                scrollPointerId = e.getPointerId(actionIndex)
                initialTouchX = (e.getX(actionIndex) + 0.5f).toInt()
                initialTouchY = (e.getY(actionIndex) + 0.5f).toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val index = e.findPointerIndex(scrollPointerId)
                if (index >= 0 && scrollState != RecyclerView.SCROLL_STATE_DRAGGING) {
                    val x = (e.getX(index) + 0.5f).toInt()
                    val y = (e.getY(index) + 0.5f).toInt()
                    dx = x - initialTouchX
                    dy = y - initialTouchY
                }
            }
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        val oldState = scrollState
        scrollState = newState
        if (oldState == RecyclerView.SCROLL_STATE_IDLE && newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            recyclerView.layoutManager?.let { layoutManager ->
                val canScrollHorizontally = layoutManager.canScrollHorizontally()
                val canScrollVertically = layoutManager.canScrollVertically()
                if (canScrollHorizontally != canScrollVertically) {
                    if ((canScrollHorizontally && kotlin.math.abs(dy) > kotlin.math.abs(dx))
                        || (canScrollVertically && kotlin.math.abs(dx) > kotlin.math.abs(dy))
                    ) {
                        recyclerView.stopScroll()
                    }
                }
            }
        }
    }
}