# 🏥 SENVA - Sistema de Gestión de Citas Médicas

<div align="center">

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)
![Google Maps](https://img.shields.io/badge/Google%20Maps-4285F4?style=for-the-badge&logo=googlemaps&logoColor=white)
![Material Design](https://img.shields.io/badge/Material%20Design-757575?style=for-the-badge&logo=materialdesign&logoColor=white)

**Aplicación Android nativa para la gestión integral de citas médicas**

*Desarrollado en Octubre-Noviembre 2024*

[📱 Características](#-características-principales) • [🛠 Tecnologías](#-stack-tecnológico) • [🏗 Arquitectura](#-arquitectura) • [📦 Instalación](#-instalación)

</div>

---

## 📋 Descripción del Proyecto

**SENVA** es una aplicación móvil Android nativa diseñada para facilitar la gestión de citas médicas, permitiendo a los usuarios:
- Reservar citas con profesionales de salud
- Gestionar información de familiares
- Consultar historial médico y recetas
- Recibir notificaciones de citas programadas
- Localizar consultorios médicos mediante mapas interactivos

El proyecto fue desarrollado como una solución completa de telemedicina, implementando las mejores prácticas de desarrollo Android moderno y diseño Material Design 3.

---

## ✨ Características Principales

### 👤 Gestión de Usuarios
- ✅ **Autenticación Completa**
  - Login con email y contraseña
  - Registro de nuevos usuarios
  - Autenticación con Google Sign-In
  - Validación de formularios en tiempo real
  - Visibilidad/ocultación de contraseñas

- 👨‍👩‍👧‍👦 **Administración de Familiares**
  - Agregar perfiles de familiares
  - Editar información familiar
  - Vincular citas a diferentes miembros de la familia
  - Gestión de DNI y datos personales

### 🏥 Sistema de Citas Médicas

- 📅 **Reserva de Citas**
  - Calendario interactivo para selección de fechas
  - Visualización de disponibilidad de doctores
  - Selección de especialidades médicas
  - Horarios disponibles por día
  - Sistema de confirmación de citas

- 📊 **Historial y Seguimiento**
  - Historial completo de citas anteriores
  - Visualización de citas próximas
  - Estado de citas (confirmadas, pendientes, canceladas)
  - Comprobantes (vouchers) de citas reservadas

### 👨‍⚕️ Directorio Médico

- 🔍 **Búsqueda de Profesionales**
  - Listado de especialidades médicas
  - Doctores disponibles por especialidad
  - Información detallada de cada profesional
  - Filtrado por cercanía geográfica

- 📍 **Geolocalización**
  - Integración con Google Maps
  - Ubicación de consultorios médicos
  - Cálculo de distancias
  - Navegación a consultorios

### 💊 Gestión Médica

- 📋 **Diagnósticos**
  - Registro de diagnósticos médicos
  - Visualización de historial de diagnósticos
  - Asociación con profesionales de salud

- 💊 **Recetas Médicas**
  - Gestión de recetas médicas
  - Información de medicamentos
  - Fechas de prescripción
  - Historial de recetas

### 🔔 Sistema de Notificaciones

- 🔕 **Centro de Notificaciones**
  - Recordatorios de citas programadas
  - Notificaciones de confirmación
  - Campañas de salud
  - Badge contador en el menú

### 👤 Perfil de Usuario

- ⚙️ **Configuración Personal**
  - Visualización de datos personales
  - Edición de perfil
  - Foto de perfil (cámara/galería)
  - Gestión de ubicación (Departamento, Provincia, Distrito)

---

## 🛠 Stack Tecnológico

### Lenguaje y Framework
```kotlin
Kotlin: 2.0.21
- Lenguaje moderno, seguro y conciso
- Null-safety y coroutines nativas
- Extension functions
- Data classes para modelos
```

### Platform & SDK
```gradle
Android SDK
├── compileSdk: 35 (Android 15)
├── minSdk: 24 (Android 7.0 Nougat)
└── targetSdk: 35 (Android 15)

Build System
├── Gradle: 9.0-milestone-1
└── Android Gradle Plugin: 8.11.0
```

### Bibliotecas Core

#### 🎨 UI/UX
```gradle
Material Design 3: 1.12.0
├── Material Components
├── Bottom Navigation
├── Navigation Drawer
├── Dialogs personalizados
├── Snackbars y alertas
└── Ripple effects

AndroidX Libraries
├── AppCompat: 1.7.1
├── Core KTX: 1.16.0
├── Activity: 1.10.1
├── ConstraintLayout: 2.2.1
└── RecyclerView (latest)
```

#### 🔥 Backend & Cloud
```gradle
Firebase (BOM 33.15.0) - Base de datos principal
├── Firebase Authentication
│   ├── Email/Password auth
│   └── Google Sign-In
├── Firebase Firestore
│   ├── Base de datos NoSQL en tiempo real
│   ├── Sincronización automática
│   └── Consultas en tiempo real
├── Firebase Storage
│   ├── Almacenamiento de fotos de perfil
│   └── Documentos médicos
└── Firebase Analytics
    └── Seguimiento de eventos
```

#### 🗺️ Servicios de Google
```gradle
Google Play Services
└── Google Maps: 19.2.0
    ├── Mapas interactivos
    ├── Marcadores personalizados
    └── Geolocalización
```

#### 💾 Persistencia de Datos
```kotlin
Firebase Firestore (Base de datos principal)
├── Colección: usuarios
├── Colección: familiares
├── Colección: citas
├── Colección: doctores
├── Colección: especialidades
├── Colección: recetas
├── Colección: diagnósticos
└── Colección: notificaciones

SQLite (Caché local y modo offline)
├── Tabla: citas (almacenamiento temporal)
├── Tabla: doctores (caché)
└── Tabla: disponibilidad_doctor (horarios)
```

#### 🧪 Testing
```gradle
Testing Framework (Configurado pero no implementado)
├── JUnit: 4.13.2
├── AndroidX Test: 1.2.1
└── Espresso: 3.6.1

Nota: El proyecto incluye la configuración de testing
pero solo contiene los tests de ejemplo generados
automáticamente por Android Studio.
```

### 🏗 Componentes de Arquitectura

```
Arquitectura MVVM + Repository Pattern
│
├── Activities (20+)
│   ├── MainActivity
│   ├── LoginActivity
│   ├── RegisterActivity
│   ├── HomeActivity
│   ├── ReservarCitaActivity
│   ├── HistorialCitasActivity
│   └── ...
│
├── Fragments
│   ├── InicioFragmento
│   ├── MiCitaFragmento
│   ├── FamiliaFragmento
│   ├── PerfilFragmento
│   └── DiagnosticoFragments (1, 2, 3)
│
├── Dialog Fragments
│   ├── AgregarFamiliarDialogFragment
│   ├── SeleccionarDiaHoraDialogFragment
│   └── VoucherDialogFragment
│
├── Adapters (RecyclerView)
│   ├── DoctorAdapter
│   ├── EspecialidadesAdapter
│   ├── FamiliaAdapter
│   ├── HistorialCitasAdapter
│   ├── NotificacionAdapter
│   ├── RecetaAdapter
│   └── CampanaAdapter
│
├── Models (Data Classes)
│   ├── Doctor
│   ├── Familiar
│   ├── Persona
│   ├── Receta
│   ├── Campana
│   └── DoctorDisponible
│
└── Utils & Helpers
    ├── DatabaseHelper
    └── FamiliaUtils
```

---

## 🏗 Arquitectura

### Patrón de Diseño

El proyecto implementa una **arquitectura en capas** con separación de responsabilidades:

```
┌─────────────────────────────────────────┐
│         PRESENTATION LAYER              │
│  (Activities, Fragments, Adapters)      │
├─────────────────────────────────────────┤
│         BUSINESS LOGIC LAYER            │
│      (ViewModels, Use Cases)            │
├─────────────────────────────────────────┤
│           DATA LAYER                    │
│  (Firebase Repository, DatabaseHelper)  │
├─────────────────────────────────────────┤
│         EXTERNAL SERVICES               │
│  (Firebase 🔥, Google Maps, SQLite)     │
│  Firestore (Principal) | SQLite (Caché) │
└─────────────────────────────────────────┘
```

### Flujo de Navegación

```
SplashScreen
    ↓
MainActivity (Welcome)
    ├→ LoginActivity
    │   └→ HomeActivity
    │       ├→ InicioFragmento (Home)
    │       ├→ MiCitaFragmento (Appointments)
    │       ├→ FamiliaFragmento (Family)
    │       ├→ PerfilFragmento (Profile)
    │       └→ ReservarCitaActivity
    │           ├→ EspecialidadesActivity
    │           ├→ ListaDoctoresActivity
    │           └→ ConfirmacionActivity
    │               └→ VoucherActivity
    │
    └→ RegisterActivity
        └→ HomeActivity (auto-login)
```

### Gestión de Estado

- **Firebase Firestore**: Base de datos principal en tiempo real
- **SharedPreferences**: Persistencia de sesión de usuario
- **SQLite**: Caché local para modo offline (solo citas y doctores)
- **LiveData/Observers**: Actualizaciones reactivas de UI desde Firestore

---

## 📦 Instalación

### Prerrequisitos

```bash
# Versiones requeridas
- Android Studio: Ladybug | 2024.2.1 o superior
- JDK: 17 o superior
- Kotlin: 2.0.21
- Android SDK: API 35
- Gradle: 9.0+
```
---

## 📱 Requisitos del Sistema

### Dispositivos Compatibles
- **Sistema Operativo**: Android 7.0 (Nougat) o superior
- **Arquitectura**: ARM, ARM64, x86, x86_64
- **RAM**: Mínimo 2GB recomendado
- **Almacenamiento**: 50MB libres

### Permisos Requeridos

```xml
<!-- Internet y red -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<!-- Ubicación -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

<!-- Cámara -->
<uses-permission android:name="android.permission.CAMERA" />

<!-- Almacenamiento -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

<!-- Notificaciones -->
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

---

## 🎨 Diseño UI/UX

### Paleta de Colores

```xml
<!-- Material Design 3 Theme -->
<color name="primary">#388E3C</color>        <!-- Verde principal -->
<color name="secondary">#81C784</color>      <!-- Verde secundario -->
<color name="background">#FFFFFF</color>     <!-- Fondo blanco -->
<color name="surface">#F5F5F5</color>        <!-- Superficie gris claro -->
<color name="error">#B00020</color>          <!-- Rojo error -->
<color name="onPrimary">#FFFFFF</color>      <!-- Texto sobre primario -->
```

### Componentes Personalizados

- ✅ **Botones Redondeados**: Corners de 12dp con elevación
- ✅ **Cards**: Material Cards con sombras sutiles
- ✅ **Bottom Navigation**: 4 tabs principales con iconos personalizados
- ✅ **Navigation Drawer**: Menú lateral con header personalizado
- ✅ **Calendario Personalizado**: RecyclerView con lógica custom
- ✅ **Badges**: Contadores numéricos en notificaciones

---

## 🗄️ Estructura de Base de Datos

### Firebase Firestore (Principal)

La mayoría de los datos se almacenan en **Firebase Firestore**, una base de datos NoSQL en tiempo real. Estructura de colecciones:

```
Firestore Collections:
├── usuarios/
│   ├── {userId}
│   │   ├── nombre, apellido, email
│   │   ├── dni, telefono, fecha_nacimiento
│   │   ├── direccion, departamento, provincia, distrito
│   │   └── foto_perfil, created_at
│   └── familiares/ (subcollection)
│
├── citas/
│   └── {citaId}
│       ├── usuario_id, familiar_id, doctor_id
│       ├── fecha, hora, estado
│       ├── motivo, observaciones
│       └── voucher_codigo, created_at
│
├── doctores/
│   └── {doctorId}
│       ├── nombre, apellido, especialidad
│       ├── cmp, telefono, email
│       ├── consultorio_direccion
│       ├── latitud, longitud
│       └── foto_perfil
│
├── especialidades/
├── recetas/
├── diagnosticos/
└── notificaciones/
```

### SQLite Local (Caché/Offline)

```sql
-- Solo para almacenamiento temporal y modo offline
-- Tabla Citas (caché local)
CREATE TABLE citas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    direccion TEXT,
    provincia TEXT,
    distrito TEXT,
    latitud REAL,
    longitud REAL,
    especialidad TEXT,
    usuario TEXT,
    doctor TEXT,
    fecha_hora TEXT,
    motivo TEXT,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado TEXT DEFAULT 'pendiente'
);

-- Tabla Doctores (caché)
CREATE TABLE doctores (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT,
    especialidad TEXT,
    imagenResId INTEGER
);

-- Tabla Disponibilidad
CREATE TABLE disponibilidad_doctor (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    doctor_id INTEGER,
    dia TEXT,
    hora TEXT,
    FOREIGN KEY (doctor_id) REFERENCES doctores(id)
);
```

---

## 🧪 Testing

El proyecto incluye la configuración básica de testing proporcionada por Android Studio:

### Unit Tests
```kotlin
// Test unitario de ejemplo (auto-generado)
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}
```

### Instrumented Tests
```kotlin
// Test instrumentado de ejemplo (auto-generado)
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
        assertEquals("com.example.senva", appContext.packageName)
    }
}
```

### Ejecutar Tests
```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest
```

> **Nota**: El proyecto actualmente solo contiene los tests de ejemplo generados por Android Studio. La implementación de tests comprehensivos está en el roadmap de mejoras futuras.

---

## 🚀 Roadmap de Desarrollo

### ✅ Fase 1 - Completada (Octubre-Noviembre 2024)
- [x] Sistema de autenticación con Firebase
- [x] CRUD de citas médicas
- [x] Gestión de familiares
- [x] Integración con Google Maps
- [x] Sistema de notificaciones
- [x] Calendario interactivo
- [x] Firebase Firestore como base de datos principal
- [x] SQLite para caché local

### 🔄 Fase 2 - Futuras Mejoras
- [ ] **Implementar tests comprehensivos** (Unit, Integration, UI)
- [ ] Chat en tiempo real con doctores (Firebase Realtime)
- [ ] Videollamadas para teleconsultas
- [ ] Integración con sistemas de pago
- [ ] Recordatorios automáticos por SMS/FCM
- [ ] Exportación de historial médico en PDF
- [ ] Multi-idioma (ES/EN)
- [ ] Modo offline mejorado con sincronización
- [ ] CI/CD pipeline

### 🌟 Fase 3 - Innovación
- [ ] IA para sugerencia de especialidades
- [ ] Análisis de síntomas con ML
- [ ] Integración con wearables
- [ ] Dashboard de salud personalizado
- [ ] Recomendaciones de medicina preventiva

---

## 🔐 Seguridad

### Prácticas Implementadas

- ✅ **Encriptación de Contraseñas**: Hash con algoritmos seguros
- ✅ **Firebase Authentication**: Manejo seguro de tokens
- ✅ **ProGuard**: Ofuscación de código en release
- ✅ **SSL/TLS**: Comunicaciones encriptadas
- ✅ **Validación de Datos**: Sanitización de inputs
- ✅ **Permisos Granulares**: Solo los necesarios

### Configuración ProGuard

```proguard
# app/proguard-rules.pro
-keep class com.example.senva.model.** { *; }
-keepclassmembers class com.example.senva.** {
    public <init>(...);
}
```

## 👨‍💻 Desarrollador

**Elvyn Edinson**
- GitHub: [@elvynedinson](https://github.com/elvynedinson)
- LinkedIn: [Elvyn Edinson](https://www.linkedin.com/in/elvyn-paucar-ponce/)
- Email: elvyn.paucar.ponce@gmail.com

---

## 🙏 Agradecimientos

- **JetBrains** por Kotlin
- **Google** por Android y Material Design
- **Firebase** por los servicios backend
- **Comunidad Android** por el soporte continuo

---

## 📅 Contexto Temporal del Proyecto

Este proyecto fue desarrollado en **Octubre-Noviembre 2024**, periodo caracterizado por:

### 🐋 Tecnologías del Momento
- **Kotlin 2.0.21** - Lanzado el 10 de octubre de 2024
- **Android Studio Ladybug** - Versión estable de finales de 2024
- **Material Design 3** - Última versión del sistema de diseño de Google
- **Docker** - En pleno anuncio de sus nuevos planes (Noviembre 5, 2024)

### 📊 Versiones Utilizadas
Todas las dependencias reflejan las versiones más actuales disponibles en el Q4 2024:
- Android Gradle Plugin 8.11.0 (cutting-edge)
- Gradle 9.0-milestone-1 (pre-release)
- AndroidX Core KTX 1.16.0
- Material Components 1.12.0
- Google Play Services Maps 19.2.0

### 🎯 Decisiones Tecnológicas
El proyecto aprovecha las últimas características de Kotlin 2.0:
- Compilador K2 mejorado
- Mejor rendimiento en tiempo de compilación
- Nuevas funcionalidades del lenguaje

---

<div align="center">

### ⭐ Si este proyecto te resultó útil, considera darle una estrella en GitHub

**Desarrollado con ❤️ usando Android + Kotlin**

</div>
