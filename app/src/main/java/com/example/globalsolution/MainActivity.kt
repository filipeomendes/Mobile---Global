import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.globalsolution.R
import com.example.globalsolution.activity.CadastroClienteActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val cadastroTextView: TextView = findViewById(R.id.tvLogin)


        cadastroTextView.setOnClickListener {

            val intent = Intent(this, CadastroClienteActivity::class.java)
            startActivity(intent)
        }
    }
}
