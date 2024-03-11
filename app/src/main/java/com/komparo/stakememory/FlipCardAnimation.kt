package com.komparo.stakememory

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView

@SuppressLint("ResourceType")
class FlipCardAnimation(context: Context, private val frontView: ImageView, private val backView: ImageView) {
    private val leftIn: AnimatorSet = AnimatorInflater.loadAnimator(context, R.anim.card_flip_left_in) as AnimatorSet
    private val leftOut: AnimatorSet = AnimatorInflater.loadAnimator(context, R.anim.card_flip_left_out) as AnimatorSet
    private val rightIn: AnimatorSet = AnimatorInflater.loadAnimator(context, R.anim.card_flip_right_in) as AnimatorSet
    private val rightOut: AnimatorSet = AnimatorInflater.loadAnimator(context, R.anim.card_flip_right_out) as AnimatorSet
    private var showingBack = false

    init {
        leftIn.setTarget(backView)
        leftOut.setTarget(frontView)
        rightIn.setTarget(backView)
        rightOut.setTarget(frontView)
    }

    fun flip() {
        if (showingBack) {
            backView.visibility = View.GONE
            frontView.visibility = View.VISIBLE
            showingBack = false

            leftIn.start()
            leftOut.start()
        } else {
            backView.visibility = View.VISIBLE
            frontView.visibility = View.GONE
            showingBack = true

            rightIn.start()
            rightOut.start()
        }
    }
}