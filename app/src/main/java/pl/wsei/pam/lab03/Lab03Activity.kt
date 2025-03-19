package pl.wsei.pam.lab03

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pl.wsei.pam.lab01.R
import kotlin.concurrent.schedule

class Lab03Activity : AppCompatActivity() {
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

        val mBoardModel = MemoryBoardView(mBoard, cols, rows)

        // OBSŁUGA ZDARZEŃ GRY!
        mBoardModel.setOnGameChangeListener { event ->
            when (event.state) {
                GameStates.Matching -> {
                    event.tiles.forEach { it.revealed = true }
                }

                GameStates.Match -> {
                    event.tiles.forEach { it.revealed = true }
                }

                GameStates.NoMatch -> {
                    event.tiles.forEach { it.revealed = true }

                    java.util.Timer().schedule(2000) {
                        runOnUiThread {
                            event.tiles.forEach { it.revealed = false }
                        }
                    }
                }

                GameStates.Finished -> {
                    runOnUiThread {
                        event.tiles.forEach { it.revealed = true }
                        android.widget.Toast.makeText(this, "Gra zakończona!", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}


