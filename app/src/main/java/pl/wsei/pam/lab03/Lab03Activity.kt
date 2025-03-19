package pl.wsei.pam.lab03

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageButton
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
        val mBoard = findViewById<GridLayout>(R.id.gridLayout)

        mBoard.columnCount = intent.getIntExtra("columns", 3)
        mBoard.rowCount = intent.getIntExtra("rows", 3)

        val btn = ImageButton(this).also {
            it.tag = "${mBoard.rowCount}x${mBoard.columnCount}"
            val layoutParams = GridLayout.LayoutParams()
            it.setImageResource(R.drawable.baseline_rocket_launch_24)
            layoutParams.width = 0
            layoutParams.height = 0
            layoutParams.setGravity(Gravity.CENTER)
            layoutParams.columnSpec = GridLayout.spec(mBoard.columnCount, 1, 1f)
            layoutParams.rowSpec = GridLayout.spec(mBoard.rowCount, 1, 1f)
            it.layoutParams = layoutParams
            mBoard.addView(it)
        }


    }
}