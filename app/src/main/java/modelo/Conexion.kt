
package modelo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

class Conexion(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_NAME = "agenda.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_CONTACTOS = "contactos"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val tabla = """
            CREATE TABLE $TABLE_CONTACTOS (
                id INTEGER PRIMARY KEY,
                nombre TEXT NOT NULL,
                telefono TEXT,
                correo TEXT,
                direccion TEXT
            )
        """.trimIndent()

        db.execSQL(tabla)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTOS")
        onCreate(db)
    }

    fun agregar(contacto: Contacto): Long {
        val db = writableDatabase

        val valores = ContentValues().apply {
            put("id", contacto.id)
            put("nombre", contacto.nombre)
            put("telefono", contacto.telefono)
            put("correo", contacto.correo)
            put("direccion", contacto.direccion)
        }

        return db.insertOrThrow(TABLE_CONTACTOS, null, valores)
    }

    fun consultar(): MutableList<Contacto> {
        val contactos = mutableListOf<Contacto>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_CONTACTOS ORDER BY id ASC",
            null
        )


        fun obtenerPorId(id: Int): Contacto? {
            val db = readableDatabase

            val cursor = db.rawQuery(
                "SELECT * FROM contactos WHERE id = ?",
                arrayOf(id.toString())
            )

            cursor.use {
                if (it.moveToFirst()) {
                    return Contacto(
                        id = it.getInt(it.getColumnIndexOrThrow("id")),
                        nombre = it.getString(it.getColumnIndexOrThrow("nombre")),
                        telefono = it.getString(it.getColumnIndexOrThrow("telefono")),
                        correo = it.getString(it.getColumnIndexOrThrow("correo")),
                        direccion = it.getString(it.getColumnIndexOrThrow("direccion"))
                    )
                }
            }

            return null
        }



        fun actualizar(contacto: Contacto): Int {
            val db = writableDatabase

            val valores = ContentValues().apply {
                put("nombre", contacto.nombre)
                put("telefono", contacto.telefono)
                put("correo", contacto.correo)
                put("direccion", contacto.direccion)
            }

            return db.update(
                "contactos",
                valores,
                "id = ?",
                arrayOf(contacto.id.toString())
            )
        }

        fun eliminar(id: Int): Int {
            val db = writableDatabase

            return db.delete(
                "contactos",
                "id = ?",
                arrayOf(id.toString())
            )
        }



        cursor.use {
            while (it.moveToNext()) {
                contactos.add(
                    Contacto(
                        id = it.getInt(it.getColumnIndexOrThrow("id")),
                        nombre = it.getString(it.getColumnIndexOrThrow("nombre")),
                        telefono = it.getString(it.getColumnIndexOrThrow("telefono")),
                        correo = it.getString(it.getColumnIndexOrThrow("correo")),
                        direccion = it.getString(it.getColumnIndexOrThrow("direccion"))
                    )
                )
            }
        }

        return contactos
    }

    fun obtenerPorId(id: Int): Contacto? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM contactos WHERE id = ?",
            arrayOf(id.toString())
        )

        cursor.use {
            if (it.moveToFirst()) {
                return Contacto(
                    id = it.getInt(it.getColumnIndexOrThrow("id")),
                    nombre = it.getString(it.getColumnIndexOrThrow("nombre")),
                    telefono = it.getString(it.getColumnIndexOrThrow("telefono")),
                    correo = it.getString(it.getColumnIndexOrThrow("correo")),
                    direccion = it.getString(it.getColumnIndexOrThrow("direccion"))
                )
            }
        }
        return null
    }

    fun actualizar(contacto: Contacto): Int {
        val valores = ContentValues().apply {
            put("nombre", contacto.nombre)
            put("telefono", contacto.telefono)
            put("correo", contacto.correo)
            put("direccion", contacto.direccion)
        }

        return writableDatabase.update(
            "contactos",
            valores,
            "id = ?",
            arrayOf(contacto.id.toString())
        )
    }

    fun eliminar(id: Int): Int {
        return writableDatabase.delete(
            "contactos",
            "id = ?",
            arrayOf(id.toString())
        )
    }
}
