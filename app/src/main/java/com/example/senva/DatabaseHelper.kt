package com.example.senva

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "SenvaDB"
        private const val DATABASE_VERSION = 3 // Incrementar versión para agregar campo estado

        // Tabla de citas
        private const val TABLE_CITAS = "citas"
        private const val COLUMN_ID = "id"
        private const val COLUMN_DIRECCION = "direccion"
        private const val COLUMN_PROVINCIA = "provincia"
        private const val COLUMN_DISTRITO = "distrito"
        private const val COLUMN_LATITUD = "latitud"
        private const val COLUMN_LONGITUD = "longitud"
        private const val COLUMN_ESPECIALIDAD = "especialidad"
        private const val COLUMN_USUARIO = "usuario"
        private const val COLUMN_DOCTOR = "doctor"
        private const val COLUMN_FECHA_HORA = "fecha_hora"
        private const val COLUMN_MOTIVO = "motivo"
        private const val COLUMN_FECHA_CREACION = "fecha_creacion"
        private const val COLUMN_ESTADO = "estado"

        private const val TABLE_DOCTORES = "doctores"
        private const val COLUMN_DOCTOR_ID = "id"
        private const val COLUMN_DOCTOR_NOMBRE = "nombre"
        private const val COLUMN_DOCTOR_ESPECIALIDAD = "especialidad"
        private const val COLUMN_DOCTOR_IMAGEN = "imagenResId"
        private const val TABLE_DISPONIBILIDAD = "disponibilidad_doctor"
        private const val COLUMN_DISPONIBILIDAD_ID = "id"
        private const val COLUMN_DISPONIBILIDAD_DOCTOR_ID = "doctor_id"
        private const val COLUMN_DISPONIBILIDAD_DIA = "dia"
        private const val COLUMN_DISPONIBILIDAD_HORA = "hora"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_CITAS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DIRECCION TEXT,
                $COLUMN_PROVINCIA TEXT,
                $COLUMN_DISTRITO TEXT,
                $COLUMN_LATITUD REAL,
                $COLUMN_LONGITUD REAL,
                $COLUMN_ESPECIALIDAD TEXT,
                $COLUMN_USUARIO TEXT,
                $COLUMN_DOCTOR TEXT,
                $COLUMN_FECHA_HORA TEXT,
                $COLUMN_MOTIVO TEXT,
                $COLUMN_FECHA_CREACION DATETIME DEFAULT CURRENT_TIMESTAMP,
                $COLUMN_ESTADO TEXT DEFAULT 'pendiente'
            )
        """.trimIndent()
        db.execSQL(createTable)
        val createTableDoctores = """
            CREATE TABLE $TABLE_DOCTORES (
                $COLUMN_DOCTOR_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DOCTOR_NOMBRE TEXT,
                $COLUMN_DOCTOR_ESPECIALIDAD TEXT,
                $COLUMN_DOCTOR_IMAGEN INTEGER
            )
        """.trimIndent()
        db.execSQL(createTableDoctores)
        val createTableDisponibilidad = """
            CREATE TABLE $TABLE_DISPONIBILIDAD (
                $COLUMN_DISPONIBILIDAD_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DISPONIBILIDAD_DOCTOR_ID INTEGER,
                $COLUMN_DISPONIBILIDAD_DIA TEXT,
                $COLUMN_DISPONIBILIDAD_HORA TEXT,
                FOREIGN KEY($COLUMN_DISPONIBILIDAD_DOCTOR_ID) REFERENCES $TABLE_DOCTORES($COLUMN_DOCTOR_ID)
            )
        """.trimIndent()
        db.execSQL(createTableDisponibilidad)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 3) {
            // Agregar columna estado si no existe
            try {
                db.execSQL("ALTER TABLE $TABLE_CITAS ADD COLUMN $COLUMN_ESTADO TEXT DEFAULT 'pendiente'")
            } catch (e: Exception) {
                // Si la columna ya existe, ignorar el error
            }
        }
    }

    // Insertar una nueva cita
    fun insertarCita(
        direccion: String,
        provincia: String,
        distrito: String,
        latitud: Double,
        longitud: Double,
        especialidad: String,
        usuario: String,
        doctor: String,
        fechaHora: String,
        motivo: String,
        estado: String = "pendiente"
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DIRECCION, direccion)
            put(COLUMN_PROVINCIA, provincia)
            put(COLUMN_DISTRITO, distrito)
            put(COLUMN_LATITUD, latitud)
            put(COLUMN_LONGITUD, longitud)
            put(COLUMN_ESPECIALIDAD, especialidad)
            put(COLUMN_USUARIO, usuario)
            put(COLUMN_DOCTOR, doctor)
            put(COLUMN_FECHA_HORA, fechaHora)
            put(COLUMN_MOTIVO, motivo)
            put(COLUMN_ESTADO, estado)
        }
        return db.insert(TABLE_CITAS, null, values)
    }

    // Insertar un doctor
    fun insertarDoctor(nombre: String, especialidad: String, imagenResId: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DOCTOR_NOMBRE, nombre)
            put(COLUMN_DOCTOR_ESPECIALIDAD, especialidad)
            put(COLUMN_DOCTOR_IMAGEN, imagenResId)
        }
        return db.insert(TABLE_DOCTORES, null, values)
    }

    // Insertar disponibilidad para un doctor
    fun insertarDisponibilidadDoctor(doctorId: Long, dia: String, hora: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DISPONIBILIDAD_DOCTOR_ID, doctorId)
            put(COLUMN_DISPONIBILIDAD_DIA, dia)
            put(COLUMN_DISPONIBILIDAD_HORA, hora)
        }
        return db.insert(TABLE_DISPONIBILIDAD, null, values)
    }

    // Inicializar doctores y disponibilidad predefinidos (llamar una sola vez)
    fun inicializarDoctoresPredefinidos() {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_DOCTORES", null)
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        if (count > 0) return // Ya hay doctores, no volver a poblar

        // Doctor 1
        val idJuan = insertarDoctor(
            nombre = "Dr. Raivid Fernadez",
            especialidad = "Cardiología",
            imagenResId = R.drawable.doctorunos
        )
        insertarDisponibilidadDoctor(idJuan, "10/07/2024", "08:00")
        insertarDisponibilidadDoctor(idJuan, "10/07/2024", "09:00")
        insertarDisponibilidadDoctor(idJuan, "12/07/2024", "10:00")

        // Doctor 2
        val idAna = insertarDoctor(
            nombre = "Dra. Juan Saul",
            especialidad = "Dermatología",
            imagenResId = R.drawable.doctordos
        )
        insertarDisponibilidadDoctor(idAna, "11/07/2024", "14:00")
        insertarDisponibilidadDoctor(idAna, "11/07/2024", "15:00")

        // Doctor 3
        val idPedro = insertarDoctor(
            nombre = "Dr. Asbel Cristobal",
            especialidad = "Pediatría",
            imagenResId = R.drawable.doctortres
        )
        insertarDisponibilidadDoctor(idPedro, "13/07/2024", "11:00")
        insertarDisponibilidadDoctor(idPedro, "13/07/2024", "12:00")
    }

    // Obtener todas las citas
    fun obtenerTodasLasCitas(): List<Cita> {
        val citas = mutableListOf<Cita>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_CITAS,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_FECHA_CREACION DESC"
        )

        with(cursor) {
            while (moveToNext()) {
                val cita = Cita(
                    id = getInt(getColumnIndexOrThrow(COLUMN_ID)),
                    direccion = getString(getColumnIndexOrThrow(COLUMN_DIRECCION)),
                    provincia = getString(getColumnIndexOrThrow(COLUMN_PROVINCIA)),
                    distrito = getString(getColumnIndexOrThrow(COLUMN_DISTRITO)),
                    latitud = getDouble(getColumnIndexOrThrow(COLUMN_LATITUD)),
                    longitud = getDouble(getColumnIndexOrThrow(COLUMN_LONGITUD)),
                    especialidad = getString(getColumnIndexOrThrow(COLUMN_ESPECIALIDAD)),
                    usuario = getString(getColumnIndexOrThrow(COLUMN_USUARIO)),
                    doctor = getString(getColumnIndexOrThrow(COLUMN_DOCTOR)),
                    fechaHora = getString(getColumnIndexOrThrow(COLUMN_FECHA_HORA)),
                    motivo = getString(getColumnIndexOrThrow(COLUMN_MOTIVO)),
                    fechaCreacion = getString(getColumnIndexOrThrow(COLUMN_FECHA_CREACION)),
                    estado = getString(getColumnIndexOrThrow(COLUMN_ESTADO))
                )
                citas.add(cita)
            }
        }
        cursor.close()
        return citas
    }

    // Obtener la última cita
    fun obtenerUltimaCita(): Cita? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_CITAS,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_FECHA_CREACION DESC",
            "1"
        )

        return if (cursor.moveToFirst()) {
            val cita = Cita(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                direccion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIRECCION)),
                provincia = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROVINCIA)),
                distrito = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISTRITO)),
                latitud = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUD)),
                longitud = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUD)),
                especialidad = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ESPECIALIDAD)),
                usuario = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USUARIO)),
                doctor = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DOCTOR)),
                fechaHora = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_HORA)),
                motivo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOTIVO)),
                fechaCreacion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FECHA_CREACION)),
                estado = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ESTADO))
            )
            cursor.close()
            cita
        } else {
            cursor.close()
            null
        }
    }

    // Obtener todos los doctores con su disponibilidad
    fun obtenerDoctoresConDisponibilidad(): List<Doctor> {
        val db = this.readableDatabase
        val doctores = mutableListOf<Doctor>()
        val cursor = db.query(TABLE_DOCTORES, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_DOCTOR_ID))
            val nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DOCTOR_NOMBRE))
            val especialidad = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DOCTOR_ESPECIALIDAD))
            val imagenResId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DOCTOR_IMAGEN))
            val disponibilidad = obtenerDisponibilidadDoctor(id)
            doctores.add(Doctor(id, nombre, especialidad, imagenResId, disponibilidad))
        }
        cursor.close()
        return doctores
    }

    // Obtener disponibilidad de un doctor
    fun obtenerDisponibilidadDoctor(doctorId: Long): List<Disponibilidad> {
        val db = this.readableDatabase
        val disponibilidad = mutableListOf<Disponibilidad>()
        val cursor = db.query(
            TABLE_DISPONIBILIDAD,
            null,
            "$COLUMN_DISPONIBILIDAD_DOCTOR_ID = ?",
            arrayOf(doctorId.toString()),
            null, null, null
        )
        while (cursor.moveToNext()) {
            val dia = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISPONIBILIDAD_DIA))
            val hora = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISPONIBILIDAD_HORA))
            disponibilidad.add(Disponibilidad(dia, hora))
        }
        cursor.close()
        return disponibilidad
    }

    // Eliminar una cita por ID
    fun eliminarCita(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_CITAS, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    // Actualizar la especialidad de una cita
    fun actualizarEspecialidadCita(id: Int, especialidad: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ESPECIALIDAD, especialidad)
        }
        return db.update(TABLE_CITAS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    // Actualizar el estado de una cita
    fun actualizarEstadoCita(id: Int, estado: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ESTADO, estado)
        }
        return db.update(TABLE_CITAS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    // Borrar todas las citas
    fun borrarTodasLasCitas() {
        val db = this.writableDatabase
        db.delete(TABLE_CITAS, null, null)
    }
}

// Clase de datos para representar una cita
data class Cita(
    val id: Int,
    val direccion: String,
    val provincia: String,
    val distrito: String,
    val latitud: Double,
    val longitud: Double,
    val especialidad: String,
    val usuario: String,
    val doctor: String,
    val fechaHora: String,
    val motivo: String,
    val fechaCreacion: String,
    val estado: String
)

// Modelo de datos para Doctor y Disponibilidad

data class Doctor(
    val id: Long,
    val nombre: String,
    val especialidad: String,
    val imagenResId: Int,
    val disponibilidad: List<Disponibilidad>
)

data class Disponibilidad(
    val dia: String, // "dd/MM/yyyy" o "Lunes"
    val hora: String // "08:00"
) 