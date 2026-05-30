package ui;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import bitacora.GestorBitacora;
import material.GestorMaterial;
import prestamos.PrestamoDevolver;
import usuario.GestorUser;
import prestamos.Loan;

// Ventana principal para gestionar los préstamos de materiales
// Hereda de JFrame para poder usarse como ventana
public class Loans extends JFrame {

    private static final long serialVersionUID = 1L;

    // Colores que usamos en toda la interfaz para que se vea uniforme
    private static final Color COLOR_PRIMARY    = new Color(55, 80, 80);    
    private static final Color COLOR_SECONDARY  = new Color(100, 100, 100); 
    private static final Color COLOR_HOVER      = new Color(110, 110, 110); 
    private static final Color COLOR_BACKGROUND = new Color(250, 250, 250); 
    private static final Color COLOR_TEXT       = Color.WHITE;              
    private static final Color COLOR_ERROR      = new Color(220, 53, 69);   
    private static final Color COLOR_SUCCESS    = new Color(40, 167, 69);   

    private JPanel contentPane; // Panel principal que contiene todo

    // Campos de texto del formulario
    private JTextField txtCarnet;
    private JTextField txtNombre;
    private JTextField txtCodigo;
    private JTextField txtMaterial;

    // Tabla donde se muestran los préstamos activos y su modelo de datos
    private JTable table;
    private DefaultTableModel model;

    private JLabel lblError; // Etiqueta para mostrar mensajes de error o éxito

    // Objetos de la lógica del sistema (backend)
    private GestorMaterial gestorMaterial;
    private GestorUser gestorUser;
    private PrestamoDevolver prestamoDevolver; // Maneja tanto préstamos como devoluciones

    // Constructor: inicializa todo cuando se abre la ventana
    public Loans() {
        // Creamos los gestores para manejar materiales y usuarios
        gestorMaterial = new GestorMaterial();
        gestorUser = new GestorUser();
        try {
            // Cargamos los datos desde los archivos CSV
            gestorMaterial.load(); 
            gestorUser.load();
        } catch (IOException e) {
            // Si no hay archivos todavía, simplemente lo notificamos
            System.out.println("Error al cargar bases de datos auxiliares.");
        }

        // Inicializamos el gestor de préstamos con los datos ya cargados
        prestamoDevolver = new PrestamoDevolver(gestorMaterial, gestorUser);
        try {
            prestamoDevolver.load(); // Cargamos los préstamos guardados
        } catch (IOException e) {
            System.out.println("No se encontraron préstamos previos.");
        }

        // Configuración básica de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        setTitle("Gestión de Préstamos");

        // Panel base con fondo claro
        contentPane = new JPanel();
        contentPane.setBackground(COLOR_BACKGROUND);
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        // Agregamos el banner arriba y el contenido principal en el centro
        contentPane.add(crearBanner(), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBackground(COLOR_BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margen interno

        // El formulario va arriba y la tabla abajo
        mainPanel.add(crearPanelFormulario(), BorderLayout.NORTH);
        mainPanel.add(crearPanelTabla(),      BorderLayout.CENTER);

        contentPane.add(mainPanel, BorderLayout.CENTER);

        actualizarTabla(); // Mostramos los préstamos al abrir la ventana
    }

    // Crea la barra de título verde oscuro en la parte superior
    private JPanel crearBanner() {
        JPanel banner = new JPanel();
        banner.setBackground(COLOR_PRIMARY);
        banner.setPreferredSize(new Dimension(0, 70));
        banner.setLayout(new BorderLayout());
        banner.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel title = new JLabel("GESTIÓN DE PRÉSTAMOS");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(COLOR_TEXT);

        banner.add(title, BorderLayout.WEST);

        return banner;
    }

    // Crea el panel con los campos del formulario y los botones de acción
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Registrar Préstamo");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setForeground(COLOR_PRIMARY);

        // GridLayout de 4 filas x 2 columnas: etiqueta + campo de texto
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new GridLayout(4, 2, 15, 10));
        panelCampos.setOpaque(false);

        panelCampos.add(crearLabel("Carnet Usuario"));
        txtCarnet = crearTextField();
        panelCampos.add(txtCarnet);

        panelCampos.add(crearLabel("Nombre Usuario"));
        txtNombre = crearTextField();
        panelCampos.add(txtNombre);

        panelCampos.add(crearLabel("Código Material"));
        txtCodigo = crearTextField();
        panelCampos.add(txtCodigo);

        panelCampos.add(crearLabel("Título Material"));
        txtMaterial = crearTextField();
        panelCampos.add(txtMaterial);

        // Panel inferior con los botones y el mensaje de error/éxito
        JPanel footerPanel = new JPanel(new BorderLayout(10, 10));
        footerPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        // Creamos los 4 botones principales
        JButton btnPrestar  = createButton("Prestar");
        JButton btnDevolver = createButton("Devolver");
        JButton btnLimpiar  = createButton("Limpiar");
        JButton btnBack     = createButton("Volver");

        buttonPanel.add(btnPrestar);
        buttonPanel.add(btnDevolver);
        buttonPanel.add(btnLimpiar);
        buttonPanel.add(btnBack);

        // Etiqueta que muestra mensajes al usuario (empieza vacía)
        lblError = new JLabel("");
        lblError.setFont(new Font("Arial", Font.PLAIN, 11));
        lblError.setForeground(COLOR_ERROR);
        lblError.setHorizontalAlignment(SwingConstants.CENTER);

        footerPanel.add(buttonPanel, BorderLayout.CENTER);
        footerPanel.add(lblError,    BorderLayout.SOUTH);

        // Asignamos las acciones de cada botón con lambdas
        btnPrestar.addActionListener(e  -> registrarPrestamo());
        btnDevolver.addActionListener(e -> devolverPrestamo());
        btnLimpiar.addActionListener(e  -> limpiarCampos());
        btnBack.addActionListener(e -> {
            new MainFrame().setVisible(true); // Volvemos al menú principal
            dispose(); // Cerramos esta ventana
        });

        // Contenedor que une el título, los campos y los botones
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout(0, 15));
        container.setOpaque(false);
        container.add(titulo,      BorderLayout.NORTH);
        container.add(panelCampos, BorderLayout.CENTER);
        container.add(footerPanel, BorderLayout.SOUTH);
        panel.add(container, BorderLayout.NORTH);

        return panel;
    }

    // Crea el panel inferior con la tabla de préstamos activos
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("PRÉSTAMOS ACTIVOS");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setForeground(COLOR_PRIMARY);
        panel.add(titulo, BorderLayout.NORTH);

        // Definimos las columnas de la tabla
        model = new DefaultTableModel();
        model.addColumn("Carnet");
        model.addColumn("Usuario");
        model.addColumn("Código");
        model.addColumn("Material");
        model.addColumn("Estado");

        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 11));
        table.setRowHeight(25);
        // Estilo del encabezado de la tabla
        table.getTableHeader().setBackground(COLOR_SECONDARY);
        table.getTableHeader().setForeground(COLOR_TEXT);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));

        // Cuando el usuario hace clic en una fila, llenamos los campos del formulario automáticamente
        table.getSelectionModel().addListSelectionListener(e -> {
            int filaSeleccionada = table.getSelectedRow();
            if (filaSeleccionada != -1) {
                txtCarnet.setText(model.getValueAt(filaSeleccionada, 0).toString());
                txtNombre.setText(model.getValueAt(filaSeleccionada, 1).toString());
                txtCodigo.setText(model.getValueAt(filaSeleccionada, 2).toString());
                txtMaterial.setText(model.getValueAt(filaSeleccionada, 3).toString());
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Lógica para registrar un nuevo préstamo
    private void registrarPrestamo() {
        // Obtenemos los valores de los campos y quitamos espacios en blanco
        String carnet   = txtCarnet.getText().trim();
        String nombre   = txtNombre.getText().trim();
        String codigo   = txtCodigo.getText().trim();
        String material = txtMaterial.getText().trim();

        lblError.setText(""); // Limpiamos el mensaje anterior

        // Validamos que no haya campos vacíos
        if (carnet.isEmpty() || nombre.isEmpty() || codigo.isEmpty() || material.isEmpty()) {
            mostrarMensaje("Todos los campos son obligatorios.", COLOR_ERROR);
            return;
        }

        // Llamamos al backend para intentar hacer el préstamo
        String resultadoBackend = prestamoDevolver.Loand(codigo, carnet);

        if (resultadoBackend.startsWith("OK")) {
            try {
                // Si fue exitoso, guardamos los cambios en los archivos
                prestamoDevolver.save();
                gestorMaterial.save(); 
                gestorUser.save();
             // Registrar en la bitácora
                GestorBitacora.registrar("PRESTAMO", "Material: " + codigo + " prestado al carnet: " + carnet);
                actualizarTabla();   // Refrescamos la tabla
                limpiarCampos();
                mostrarMensaje(resultadoBackend, COLOR_SUCCESS);
            } catch (IOException ex) {
                mostrarMensaje("Error al escribir los archivos de persistencia.", COLOR_ERROR);
            }
        } else {
            // El backend devolvió un mensaje de error
            mostrarMensaje(resultadoBackend, COLOR_ERROR);
        }
    }

    // Lógica para procesar la devolución de un material
    private void devolverPrestamo() {
        String carnet = txtCarnet.getText().trim();
        String codigo = txtCodigo.getText().trim();

        lblError.setText("");

        // Solo necesitamos carnet y código para devolver
        if (carnet.isEmpty() || codigo.isEmpty()) {
            mostrarMensaje("Seleccione un préstamo válido en la tabla.", COLOR_ERROR);
            return;
        }

        // Llamamos al backend para hacer la devolución
        String resultadoBackend = prestamoDevolver.returnLoan(codigo, carnet);

        if (resultadoBackend.startsWith("OK")) {
            try {
                // Guardamos los cambios si todo salió bien
                prestamoDevolver.save();
                gestorMaterial.save(); 
                gestorUser.save();
                // Registrar en la bitácora
                GestorBitacora.registrar("DEVOLUCION", "Material: " + codigo + " devuelto por el carnet: " + carnet);
                actualizarTabla();
                limpiarCampos();
                mostrarMensaje(resultadoBackend, COLOR_SUCCESS);
            } catch (IOException ex) {
                mostrarMensaje("Error al actualizar la base de datos.", COLOR_ERROR);
            }
        } else {
            mostrarMensaje(resultadoBackend, COLOR_ERROR);
        }
    }

    // Recarga la tabla con todos los préstamos activos del sistema
    private void actualizarTabla() {
        model.setRowCount(0); // Limpiamos las filas anteriores
        for (Loan l : prestamoDevolver.getLoans()) {
            // Buscamos el nombre del usuario y el título del material por sus IDs
            usuario.User u       = gestorUser.findById(l.getUserId());
            material.Material m  = gestorMaterial.findByCode(l.getMaterialCode()); 

            // Si no encontramos los datos, mostramos "Desconocido" como respaldo
            String nombreUser = (u != null) ? u.getName() : "Desconocido";
            String tituloMat  = (m != null) ? m.getTitle() : "Desconocido";

            // Agregamos una fila nueva a la tabla
            model.addRow(new Object[]{
                l.getUserId(),
                nombreUser,
                l.getMaterialCode(),
                tituloMat,
                "Activo"
            });
        }
    }

    // Borra el texto de todos los campos y deselecciona la tabla
    private void limpiarCampos() {
        txtCarnet.setText("");
        txtNombre.setText("");
        txtCodigo.setText("");
        txtMaterial.setText("");
        table.clearSelection();
    }

    // Método reutilizable para mostrar mensajes con color (rojo = error, verde = éxito)
    private void mostrarMensaje(String texto, Color color) {
        lblError.setText(texto);
        lblError.setForeground(color);
    }

    // Crea un botón con el estilo general de la app y efecto hover
    private JButton createButton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.PLAIN, 11));
        boton.setForeground(COLOR_TEXT);
        boton.setBackground(COLOR_PRIMARY);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor de manita al pasar el mouse
        boton.setOpaque(true);

        // Efecto visual: el botón cambia de color cuando el mouse entra o sale
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_PRIMARY);
            }
        });

        return boton;
    }

    // Crea un campo de texto con el estilo estándar del formulario
    private JTextField crearTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 11));
        field.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        field.setPreferredSize(new Dimension(150, 30));
        return field;
    }

    // Crea una etiqueta de texto con el estilo estándar del formulario
    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        label.setForeground(COLOR_SECONDARY);
        return label;
    }
}