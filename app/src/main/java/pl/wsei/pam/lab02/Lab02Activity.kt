package pl.wsei.pam.lab02

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pl.wsei.pam.lab01.R
import pl.wsei.pam.lab03.Lab03Activity

class Lab02Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lab02)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun onBoardSizeButtonClicked(v: View) {
        val buttonView: Button = v as Button
        val intent = Intent(this, Lab03Activity::class.java)

        val buttonText = buttonView.text.toString()

        val parts = buttonText.split(" x ")

        if (parts.size == 2) {
            val rows = parts[0].toInt()
            val columns = parts[1].toInt()

            intent.putExtra("rows", rows)
            intent.putExtra("columns", columns)
        } else {
            intent.putExtra("rows", 3)
            intent.putExtra("columns", 3)
        }

        startActivity(intent)
    }
}