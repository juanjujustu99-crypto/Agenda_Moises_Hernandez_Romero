
package modelo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.R

class AdaptadorContacto(
    private val contactos: MutableList<Contacto>,
    private val alSeleccionar: (Contacto) -> Unit,
    private val alMantenerPresionado: (Contacto) -> Unit
) : RecyclerView.Adapter<AdaptadorContacto.ContactoViewHolder>() {

    class ContactoViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val nombre: TextView = itemView.findViewById(R.id.txtItemNombre)
        val id: TextView = itemView.findViewById(R.id.txtItemId)
        val telefono: TextView = itemView.findViewById(R.id.txtItemTelefono)
        val correo: TextView = itemView.findViewById(R.id.txtItemCorreo)
        val direccion: TextView = itemView.findViewById(R.id.txtItemDireccion)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactoViewHolder {

        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contacto, parent, false)

        return ContactoViewHolder(vista)
    }

    override fun onBindViewHolder(
        holder: ContactoViewHolder,
        position: Int
    ) {
        val contacto = contactos[position]

        holder.nombre.text = contacto.nombre
        holder.id.text = "ID: ${contacto.id}"
        holder.telefono.text = "Teléfono: ${contacto.telefono}"
        holder.correo.text = "Correo: ${contacto.correo}"
        holder.direccion.text = "Dirección: ${contacto.direccion}"

        holder.itemView.setOnClickListener {
            alSeleccionar(contacto)
        }

        holder.itemView.setOnLongClickListener {
            alMantenerPresionado(contacto)
            true
        }
    }

    override fun getItemCount(): Int = contactos.size
}
