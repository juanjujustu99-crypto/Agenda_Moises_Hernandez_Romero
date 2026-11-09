
package vistas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import modelo.AdaptadorContacto
import modelo.Conexion
import modelo.Contacto

class MainActivity : AppCompatActivity() {

    private lateinit var lista: RecyclerView
    private lateinit var btnAgregar: FloatingActionButton
    private lateinit var conexion: Conexion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lista = findViewById(R.id.lista)
        btnAgregar = findViewById(R.id.btnAgregar)

        conexion = Conexion(this)

        lista.layoutManager = LinearLayoutManager(this)

        btnAgregar.setOnClickListener {
            val intent = Intent(this, AgregarActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarContactos()
    }

    private fun cargarContactos() {
        val contactos = conexion.consultar()

        lista.adapter = AdaptadorContacto(
            contactos,
            alSeleccionar = { contacto ->
                editarContacto(contacto)
            },
            alMantenerPresionado = { contacto ->
                confirmarEliminacion(contacto)
            }
        )
    }

    private fun editarContacto(contacto: Contacto) {
        val intent = Intent(this, AgregarActivity::class.java)
        intent.putExtra("contacto_id", contacto.id)
        startActivity(intent)
    }

    private fun confirmarEliminacion(contacto: Contacto) {

        AlertDialog.Builder(this)
            .setTitle("Eliminar contacto")
            .setMessage(
                "¿Deseas eliminar a ${contacto.nombre}?"
            )
            .setPositiveButton("Eliminar") { _, _ ->

                conexion.eliminar(contacto.id)
                cargarContactos()

            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroy() {
        conexion.close()
        super.onDestroy()
    }
}
