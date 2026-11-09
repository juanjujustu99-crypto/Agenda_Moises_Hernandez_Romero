package modelo

data class Contacto(
    var id: Int = 0,
    var nombre: String = "",
    var telefono: String = "",
    var correo: String = "",
    var direccion: String = ""
)
