package com.jeketos.loadingbutton

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var startTime = 0L
    var endTime = 0L
    val handler = Handler()
    val runnable = object : Runnable {
        override fun run() {
            startMoveAnimation()
            handler.postDelayed(this, 2000)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setImageBackground(R.drawable.anim_button_collapse)
        animateImage.setOnClickListener {
            animateImage.isClickable = false
            setImageBackground(R.drawable.anim_button_collapse)
            hideButtonText()
            (animateImage.background as Animatable).start()
            Handler().postDelayed({
                setImageBackground(R.drawable.anim_circle)
                startTime = System.currentTimeMillis() + 500
                (animateImage.background as Animatable).start()
                interrupt.isClickable = true
                handler.postDelayed( runnable , 500)
            },500)

        }
        interrupt.setOnClickListener {
            interrupt.isClickable = false
            endTime = System.currentTimeMillis()
            handler.removeCallbacks(runnable)
            startEndAnimation()
        }
        interrupt.isClickable = false
    }

    private fun startMoveAnimation() {
        setImageBackground(R.drawable.anim_circle_move)
        (animateImage.background as AnimatedVectorDrawable).start()
    }

    private fun startEndAnimation() {
        val moduloTime = (endTime - startTime) % 2000
        Log.d("SS", "modulo - " + moduloTime)
        Handler().postDelayed({
            setImageBackground(R.drawable.anim_circle_move_end)
            (animateImage.background as AnimatedVectorDrawable).start()
            Handler().postDelayed({
                setImageBackground(R.drawable.anim_circle_end)
                (animateImage.background as AnimatedVectorDrawable).start()
                Handler().postDelayed({
                    setImageBackground(R.drawable.anim_button_expand)
                    (animateImage.background as AnimatedVectorDrawable).start()
                    Handler().postDelayed({
                        showSubmit()
                    }, 500)
                },500)
            },1000)
        }, 2000 - moduloTime)
    }

    private fun setImageBackground(animId: Int) {
        val background = ContextCompat.getDrawable(this, animId)
        background.setTint(ContextCompat.getColor(this, R.color.green))
        animateImage.background = background
    }

    private fun hideButtonText(){
        val colorAnim = ObjectAnimator.ofInt(animateImage, "textColor", ContextCompat.getColor(this, R.color.white), ContextCompat.getColor(this, R.color.white_transparent))
        colorAnim.duration = 100
        colorAnim.setEvaluator(ArgbEvaluator())
        colorAnim.start()
    }

    private fun showSubmit(){
        animateImage.text = "✓"
        val colorAnim = ObjectAnimator.ofInt(animateImage, "textColor", ContextCompat.getColor(this, R.color.white_transparent), ContextCompat.getColor(this, R.color.white))
        colorAnim.duration = 500
        colorAnim.setEvaluator(ArgbEvaluator())
        colorAnim.start()
        animateImage.isClickable = true
    }

}
