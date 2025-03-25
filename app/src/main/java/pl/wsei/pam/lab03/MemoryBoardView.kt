package pl.wsei.pam.lab03

import GameStates
import MemoryGameEvent
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import androidx.camera.viewfinder.core.ScaleType
import pl.wsei.pam.lab01.R
import java.util.Stack

class MemoryBoardView(
    private val gridLayout: androidx.gridlayout.widget.GridLayout,
    private val cols: Int,
    private val rows: Int
) {
    private val deckResource: Int = R.drawable.baseline_diamond_24
    private var onGameChangeStateListener: (MemoryGameEvent) -> Unit = { (e) -> }
    private val matchedPair: Stack<Tile> = Stack()
    private val logic: MemoryGameLogic = MemoryGameLogic(cols * rows / 2)

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

                val tileSize = minOf(tileWidth, tileHeight) // aby kafelki były kwadratowe i mieściły się

                val layoutParams = GridLayout.LayoutParams().apply {
                    width = tileSize
                    height = tileSize
                    setMargins(4, 4, 4, 4) // można też obliczyć dynamicznie
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
        if (matchResult != GameStates.Matching) {
            matchedPair.clear()
        }
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
