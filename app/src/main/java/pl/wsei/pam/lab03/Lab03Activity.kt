package pl.wsei.pam.lab03

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.gridlayout.widget.GridLayout
import pl.wsei.pam.lab01.R

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

        // Tutaj MUSI być GridLayout w twoim activity_lab03.xml
        val mBoard: GridLayout = findViewById(R.id.gridLayout)

        // Pobierasz dane o rozmiarach planszy
        val cols = intent.getIntExtra("columns", 3)
        val rows = intent.getIntExtra("rows", 3)

        // Ustaw kolumny i wiersze dla GridLayout
        mBoard.columnCount = cols
        mBoard.rowCount = rows

        // Tworzysz model planszy
        val mBoardModel = MemoryBoardView(mBoard, cols, rows)
    }
}
