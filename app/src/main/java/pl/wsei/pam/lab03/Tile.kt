package pl.wsei.pam.lab03

import android.widget.ImageButton

data class Tile(
    val button: ImageButton,
    var tileResource: Int,
    val deckResource: Int
) {
    private var _revealed: Boolean = false

    var revealed: Boolean
        get() = _revealed
        set(value) {
            _revealed = value
            updateImage()
        }

    init {
        updateImage()
    }

    fun updateImage() {
        button.setImageResource(if (revealed) tileResource else deckResource)
    }

    fun removeOnClickListener() {
        button.setOnClickListener(null)
    }
}
