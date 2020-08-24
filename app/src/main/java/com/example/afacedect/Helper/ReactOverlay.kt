package com.example.afacedect.Helper

import android.graphics.*

class ReactOverlay internal constructor(overlay: GraphicOverlay,
                                   private val bound: Rect?): GraphicOverlay.Graphic(overlay)
{


    private val rectPaint:Paint
    init {
        rectPaint= Paint()
        rectPaint.color= Color.RED
        rectPaint.strokeWidth=5.0f
        rectPaint.style=Paint.Style.STROKE
        postInvalidate()
    }


    override fun draw(canvas: Canvas?) {
       val rect=RectF(bound)
        rect.left=translateX(rect.left)
        rect.right=translateX(rect.right)
        rect.top=translateY(rect.top)
        rect.bottom=translateY(rect.bottom)
        canvas?.drawRect(rect,rectPaint)

    }
}