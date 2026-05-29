#  Sistema de Gestión de Biblioteca - UMG (Opción B)

Este es un proyecto completo desarrollado en **Java (Java Swing)** que implementa un sistema automatizado para la administración y control de una biblioteca universitaria. Permite gestionar un catálogo de materiales (libros y revistas), administrar usuarios (estudiantes y docentes), realizar préstamos/devoluciones con persistencia en formato CSV y auditar cada movimiento mediante una bitácora automatizada.

##  Características Principales y Criterios de Rúbrica Cubiertos

### 1. Modelo de Programación Orientada a Objetos (40%)
* **Clases Abstractas y Herencia:** Uso de una estructura jerárquica robusta con la clase abstractas `Material` (padre de `Book` y `Magazine`) y la clase base `User` (padre de `Student` y `Teacher`).
* **Polimorfismo Dinámico (Multas):** Implementación polimórfica pura en el cálculo de multas personalizado (`calcularmulta(int diasRetraso)`). El sistema deduce automáticamente en tiempo de ejecución si se debe aplicar una tasa de **Q2.50 por día** si es un libro o **Q1.50 por día** si es una revista.
* **Encapsulamiento:** Atributos sensibles protegidos (`protected` o `private`) con acceso exclusivo mediante *Getters* y *Setters* que incluyen validaciones lógicas nativas (bloqueo de ingresos vacíos).

### 2. Interfaz Gráfica y UX (25%)
* **Diseño Visual Swing:** Interfaz corporativa unificada a través de un panel principal (`MainFrame.java`), botones interactivos con efectos de cambio de color dinámico (*hover*) y cursores adaptativos.
* **Validaciones Robustas:** Control estricto de campos mediante expresiones regulares (*Regex*), obligatoriedad de datos y bloqueo automático de claves duplicadas (códigos o carnets existentes).
* **Mensajes Claros:** Sistema dinámico de alertas visuales en pantalla (`lblError`) que utiliza colores condicionales (verde para transacciones exitosas y rojo para errores o multas).

### 3. Persistencia de Datos Fiable (15%)
La aplicación implementa persistencia en tiempo real en archivos de texto plano utilizando codificación de caracteres `UTF-8` para garantizar la compatibilidad de eñes y acentos:
* `materiales.csv`: Inventario físico del catálogo.
* `users.csv`: Base de datos de miembros de la universidad (alumnos/docentes).
* `loans.csv`: Control e historial transaccional de préstamos activos.

### 4. Trabajo colaborativo (10%)
* **Tablero de Trello (Planificación Ágil):**https://trello.com/b/QfTqxC3A/proyecto-de-programacion-biblioteca
* **Repositorio de GitHub (Control de Versiones):** https://github.com/tito124-web/Biblioteca#

### 5. Módulo de Auditoría y Bitácora 
* El sistema cuenta con un componente de auditoría automatizado (`GestorBitacora.java`) configurado mediante métodos estáticos lógicos. 
* Cada vez que un operador realiza un alta, baja, préstamo o devolución desde las ventanas de la UI, el sistema escribe de forma transparente un registro cronológico en el archivo `bitacora.csv`, guardando la marca de tiempo precisa (`LocalDateTime`), el tipo de acción realizada y el detalle de los identificadores afectados.

---

##  Estructura del Proyecto (Paquetes)

El código fuente está modularizado bajo la siguiente arquitectura limpia de software:

* `Main`: Contiene la clase `Main.java` encargada de iniciar y encender el hilo principal de la interfaz gráfica.
* `Material`: Lógica de negocio para los recursos bibliográficos físicos (`Material`, `Book`, `Magazine`, `GestorMaterial`).
* `Usuario`: Lógica de negocio para el control de miembros de la comunidad (`User`, `Student`, `Teacher`, `GestorUser`).
* `prestamos`: Controlador transaccional que enlaza materiales con usuarios y gestiona plazos automáticos de 15 días (`Loan`, `PrestamoDevolver`).
* `Bitacora`: Componente estático global para el registro y almacenamiento de logs del sistema (`GestorBitacora`).
* `ui`: Capa de presentación visual construida en Java Swing (`MainFrame`, `Catalog`, `User`, `Loans`).

---

## 🛠️ Requisitos e Instalación

1.  **Java Development Kit (JDK):** Versión 11.0.30.
2.  **IDE Recomendado:** Eclipse IDE, NetBeans o IntelliJ IDEA.

### Pasos para la ejecución de la Demo:
1.  Importar la carpeta `Biblioteca` en su entorno de desarrollo preferido.
2.  Verificar que el directorio `src` esté añadido como carpeta de fuentes en el *Build Path* de Java.
3.  Abrir el archivo ejecutable `src/Main/Main.java`.
4.  Hacer clic derecho y seleccionar **Run As > Java Application**.

---

##  Equipo de Desarrollo

* **Felipe Sandoval** (Desarrollo y diseño de la mitad de la interfaz gráfica, integración estructural entre la capa del backend y la UI, y labores de optimización y limpieza de código).
* **Gabriela Medrano** (Análisis y planteamiento inicial del diagrama de clases estructural, y desarrollo y diseño de la otra mitad de la interfaz gráfica de usuario).
* **Josué Martinez** (Desarrollo y programación de las clases de lógica del backend, diseño de la arquitectura de almacenamiento y persistencia de archivos planos CSV, comentarios del código fuente, labores de limpieza y soporte en la unificación del backend con la UI).
* **Omar Cabrera** (Desarrollo de la otra mitad de las clases lógicas del backend, incluyendo el diseño, almacenamiento e implementación completa del módulo de Bitácora y auditoría del sistema).)
