package pl.wsei.pam.lab03

import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pl.wsei.pam.lab01.R
import kotlin.concurrent.schedule

class Lab03Activity : AppCompatActivity() {
    private lateinit var mBoardModel: MemoryBoardView
    lateinit var completionPlayer: MediaPlayer
    lateinit var negativePlayer: MediaPlayer
    var isSound = true


    override fun onResume() {
        super.onResume()
        completionPlayer = MediaPlayer.create(applicationContext, R.raw.completion)
        negativePlayer = MediaPlayer.create(applicationContext, R.raw.negative_guitar)
    }

    override fun onPause() {
        super.onPause()
        completionPlayer.release()
        negativePlayer.release()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lab03)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mBoard: androidx.gridlayout.widget.GridLayout = findViewById(R.id.gridLayout)

        val cols = intent.getIntExtra("columns", 3)
        val rows = intent.getIntExtra("rows", 3)

        mBoard.columnCount = cols
        mBoard.rowCount = rows

        mBoardModel = MemoryBoardView(mBoard, cols, rows)

        mBoardModel.setOnGameChangeListener { event ->
            when (event.state) {
                GameStates.Matching -> {
                    event.tiles.forEach { it.revealed = true }
                }

                GameStates.Match -> {
                    event.tiles.forEach { it.revealed = true }
                    if (isSound) {
                        completionPlayer.start()
                    }

                }

                GameStates.NoMatch -> {
                    event.tiles.forEach { it.revealed = true }
                    if (isSound) {
                        negativePlayer.start()
                    }

                    java.util.Timer().schedule(900) {
                        runOnUiThread {
                            event.tiles.forEach { it.revealed = false }
                        }
                    }
                }

                GameStates.Finished -> {
                    runOnUiThread {
                        event.tiles.forEach { it.revealed = true }
                        android.widget.Toast.makeText(this, "You won!", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val state = mBoardModel.getState()
        outState.putIntArray("game_state", state)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.board_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //return super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.board_activity_sound -> {
                isSound = !isSound

                if (isSound) {
                    item.setIcon(R.drawable.baseline_volume_up_24)
                    Toast.makeText(this, "Sound turned on", Toast.LENGTH_SHORT).show()
                } else {
                    item.setIcon(R.drawable.baseline_volume_off_24)
                    Toast.makeText(this, "Sound turned off", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return true
    }
}


