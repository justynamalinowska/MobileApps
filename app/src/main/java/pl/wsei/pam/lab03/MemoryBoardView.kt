package pl.wsei.pam.lab03

import GameStates
import MemoryGameEvent
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import pl.wsei.pam.lab01.R
import java.util.Stack
import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.AnimatorListenerAdapter
import android.view.animation.DecelerateInterpolator
import java.util.Random


class MemoryBoardView(
    private val gridLayout: androidx.gridlayout.widget.GridLayout,
    private val cols: Int,
    private val rows: Int
) {
    private val deckResource: Int = R.drawable.baseline_diamond_24
    private var onGameChangeStateListener: (MemoryGameEvent) -> Unit = { (e) -> }
    private val matchedPair: Stack<Tile> = Stack()
    private val logic: MemoryGameLogic = MemoryGameLogic(cols * rows / 2)
    val activity = gridLayout.context as Lab03Activity

    private val tiles: MutableMap<String, Tile> = mutableMapOf()
    private val icons: List<Int> = listOf(
        R.drawable.baseline_rocket_launch_24,
        R.drawable.baseline_cookie_24,
        R.drawable.baseline_face_24,
        R.drawable.baseline_android_24,
        R.drawable.baseline_directions_car_24,
        R.drawable.baseline_flight_24,
        R.drawable.baseline_local_bar_24,
        R.drawable.baseline_recycling_24,
        R.drawable.baseline_pets_24,
        R.drawable.baseline_camera_alt_24,
        R.drawable.baseline_cake_24,
        R.drawable.baseline_favorite_24,
        R.drawable.baseline_icecream_24,
        R.drawable.baseline_sports_tennis_24,
        R.drawable.baseline_local_cafe_24,
        R.drawable.baseline_cruelty_free_24,
        R.drawable.baseline_tsunami_24,
        R.drawable.baseline_grade_24,
        R.drawable.baseline_dark_mode_24,
        R.drawable.baseline_attach_money_24,
    )

    init {
        val shuffledIcons: MutableList<Int> = mutableListOf<Int>().also {
            it.addAll(icons.subList(0, cols * rows / 2))
            it.addAll(icons.subList(0, cols * rows / 2))
            it.shuffle()
        }

        gridLayout.columnCount = cols
        gridLayout.rowCount = rows

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val button = ImageButton(gridLayout.context)

                button.tag = "$row-$col"
                button.scaleType = ImageView.ScaleType.FIT_CENTER

                val displayMetrics = gridLayout.resources.displayMetrics
                val screenWidth = displayMetrics.widthPixels
                val screenHeight = displayMetrics.heightPixels

                val horizontalPadding = (32 * displayMetrics.density).toInt()
                val verticalPadding = (32 * displayMetrics.density).toInt()

                val availableWidth = screenWidth - horizontalPadding
                val availableHeight = screenHeight - verticalPadding

                val tileWidth = availableWidth / cols
                val tileHeight = availableHeight / rows

                val tileSize = minOf(tileWidth, tileHeight)

                val layoutParams = GridLayout.LayoutParams().apply {
                    width = tileSize
                    height = tileSize
                    setMargins(4, 4, 4, 4)
                    setGravity(Gravity.CENTER)
                    columnSpec = GridLayout.spec(col, 1, 1f)
                    rowSpec = GridLayout.spec(row, 1, 1f)
                }

                button.layoutParams = layoutParams
                gridLayout.addView(button)

                val tileResource = shuffledIcons.removeAt(0)
                val tile = Tile(button, tileResource, deckResource)

                tiles[button.tag.toString()] = tile

                button.setOnClickListener(::onClickTile)
            }
        }
    }

    private fun onClickTile(v: View) {
        val tile = tiles[v.tag]
        matchedPair.push(tile)
        val matchResult = logic.process {
            tile?.tileResource ?: -1
        }

        onGameChangeStateListener(MemoryGameEvent(matchedPair.toList(), matchResult))

        if (matchResult == GameStates.Match) {
            if (activity.isSound) {
                activity.completionPlayer.start()
            }


            matchedPair.forEach { tile ->
                animatePairedButton(tile.button, Runnable {
                    tile.button.isEnabled = false
                })
            }
            matchedPair.clear()
        } else if (matchResult == GameStates.NoMatch) {
            if (activity.isSound) {
                activity.negativePlayer.start()
            }


            matchedPair.forEach { tile ->
                animateWrongPair(tile.button, Runnable {
                    tile.revealed = false
                    tile.updateImage()
                    tile.button.rotation = 0f
                })
            }
            matchedPair.clear()
        }
    }

    private fun animatePairedButton(button: ImageButton, action: Runnable) {
        val set = AnimatorSet()
        val random = Random()

        button.pivotX = random.nextFloat() * 200f
        button.pivotY = random.nextFloat() * 200f

        val rotation = ObjectAnimator.ofFloat(button, "rotation", 1080f)
        val scaleX = ObjectAnimator.ofFloat(button, "scaleX", 1f, 4f)
        val scaleY = ObjectAnimator.ofFloat(button, "scaleY", 1f, 4f)
        val fade = ObjectAnimator.ofFloat(button, "alpha", 1f, 0f)

        set.startDelay = 500
        set.duration = 2000
        set.interpolator = DecelerateInterpolator()
        set.playTogether(rotation, scaleX, scaleY, fade)

        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                button.scaleX = 1f
                button.scaleY = 1f
                button.alpha = 0f
                action.run()
            }
        })

        set.start()
    }

    private fun animateWrongPair(button: ImageButton, action: Runnable) {
        val set = AnimatorSet()

        val rotateLeft = ObjectAnimator.ofFloat(button, "rotation", 0f, -15f)
        val rotateRight = ObjectAnimator.ofFloat(button, "rotation", -15f, 15f)
        val rotateCenter = ObjectAnimator.ofFloat(button, "rotation", 15f, 0f)

        set.playSequentially(rotateLeft, rotateRight, rotateCenter)
        set.duration = 500
        set.interpolator = DecelerateInterpolator()

        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                action.run()
            }
        })

        set.start()
    }


    fun setOnGameChangeListener(listener: (event: MemoryGameEvent) -> Unit) {
        onGameChangeStateListener = listener
    }

    private fun addTile(button: ImageButton, resourceImage: Int) {
        button.setOnClickListener(::onClickTile)
        val tile = Tile(button, resourceImage, deckResource)
        tiles[button.tag.toString()] = tile
    }
    fun getState(): IntArray {
        val state = IntArray(rows * cols)
        tiles.values.forEachIndexed { index, tile ->
            state[index] = if (tile.revealed) tile.tileResource else -1
        }
        return state
    }

    fun setState(state: IntArray) {
        val shuffledIcons: MutableList<Int> = mutableListOf<Int>().also {
            it.addAll(icons.subList(0, cols * rows / 2))
            it.addAll(icons.subList(0, cols * rows / 2))
            it.shuffle()
        }

        tiles.values.forEachIndexed { index, tile ->
            if (state[index] == -1) {
                tile.tileResource = shuffledIcons.removeAt(0)
                tile.revealed = false
            } else {
                tile.tileResource = state[index]
                tile.revealed = true
            }
            tile.updateImage()
        }
    }
}
