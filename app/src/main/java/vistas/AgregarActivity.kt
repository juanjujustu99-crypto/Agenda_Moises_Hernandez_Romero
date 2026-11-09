package vistas

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.agenda.R
import modelo.Contacto
import modelo.Conexion

class AgregarActivity : AppCompatActivity() {

    private lateinit var txtId: EditText
    private lateinit var txtNombre: EditText
    private lateinit var txtTelefono: EditText
    private lateinit var txtCorreo: EditText
    private lateinit var txtDireccion: EditText
    private lateinit var btnGuardar: Button

    private var idEdicion: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar)

        txtId = findViewById(R.id.txtId)
        txtNombre = findViewById(R.id.txtNombre)
        txtTelefono = findViewById(R.id.txtTelefono)
        txtCorreo = findViewById(R.id.txtCorreo)
        txtDireccion = findViewById(R.id.txtDireccion)
        btnGuardar = findViewById(R.id.btnGuardar)

        // Si recibimos un ID, estamos editando un contacto existente.
        idEdicion = intent.getIntExtra("contacto_id", -1)
            .takeIf { it != -1 }

        if (idEdicion != null) {
            cargarContacto(idEdicion!!)
            btnGuardar.text = "ACTUALIZAR CONTACTO"
        }

        btnGuardar.setOnClickListener {
            guardarContacto()
        }
    }

    private fun cargarContacto(id: Int) {
        val conexion = Conexion(this)

        try {
            val contacto = conexion.obtenerPorId(id)

            if (contacto != null) {
                txtId.setText(contacto.id.toString())
                txtId.isEnabled = false
                txtNombre.setText(contacto.nombre)
                txtTelefono.setText(contacto.telefono)
                txtCorreo.setText(contacto.correo)
                txtDireccion.setText(contacto.direccion)
            } else {
                Toast.makeText(
                    this,
                    "No se encontró el contacto",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        } finally {
            conexion.close()
        }
    }

    private fun guardarContacto() {
        val id = txtId.text.toString().trim().toIntOrNull()
        val nombre = txtNombre.text.toString().trim()
        val telefono = txtTelefono.text.toString().trim()
        val correo = txtCorreo.text.toString().trim()
        val direccion = txtDireccion.text.toString().trim()

        if (id == null || id <= 0) {
            txtId.error = getString(R.string.ingresa_un_id_valido)
            return
        }

        if (nombre.isEmpty()) {
            txtNombre.error = getString(R.string.ingresa_el_nombre)
            return
        }

        val contacto = Contacto(
            id = id,
            nombre = nombre,
            telefono = telefono,
            correo = correo,
            direccion = direccion
        )

        val conexion = Conexion(this)

        try {
            if (idEdicion != null) {
                val filasActualizadas = conexion.actualizar(contacto)

                if (filasActualizadas > 0) {
                    Toast.makeText(
                        this,
                        "Contacto actualizado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "No se pudo actualizar el contacto",
                        Toast.LENGTH_LONG
                    ).show()
                    return
                }
            } else {
                conexion.agregar(contacto)

                Toast.makeText(
                    this,
                    getString(R.string.contacto_guardado_correctamente),
                    Toast.LENGTH_SHORT
                ).show()
            }

            finish()

        } catch (e: SQLiteConstraintException) {
            Toast.makeText(
                this,
                getString(R.string.ese_id_ya_existe_utiliza_otro),
                Toast.LENGTH_LONG
            ).show()

        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Error al guardar: ${e.message}",
                Toast.LENGTH_LONG
            ).show()

        } finally {
            conexion.close()
        }
    }
}