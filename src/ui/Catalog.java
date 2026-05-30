package ui;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import bitacora.GestorBitacora;
import material.Book;
import material.GestorMaterial;
import material.Magazine;
import material.Material;

// Ventana para administrar el catálogo de materiales (libros y revistas)
public class Catalog extends JFrame {

    private static final long serialVersionUID = 1L;

    // Paleta de colores de la interfaz
    private static final Color COLOR_PRIMARY    = new Color(55, 80, 80);    
    private static final Color COLOR_SECONDARY  = new Color(100, 100, 100); 
    private static final Color COLOR_HOVER      = new Color(110, 110, 110); 
    private static final Color COLOR_BACKGROUND = new Color(250, 250, 250); 
    private static final Color COLOR_TEXT       = Color.WHITE;              
    private static final Color COLOR_ERROR      = new Color(220, 53, 69);   
    private static final Color COLOR_SUCCESS    = new Color(40, 167, 69);   

    private JPanel contentPane;

    // Campos del formulario
    private JTextField txtCode;
    private JTextField txtTitle;
    private JTextField txtAuthor;
    private JTextField txtYear;
    private JComboBox<String> comboType; // Selector para elegir entre Libro o Revista
    private JLabel lblError;             // Mensajes de retroalimentación al usuario

    // Tabla y su modelo de datos
    private JTable table;
    private DefaultTableModel model;

    private GestorMaterial gestorMaterial; // Gestiona la lista de materiales

    // Constructor: carga los datos y construye la interfaz
    public Catalog() {
        gestorMaterial = new GestorMaterial();
        try {
            gestorMaterial.load(); // Intentamos cargar materiales desde el archivo
        } catch (IOException e) {
            System.out.println("No se encontraron materiales previos.");
        }

        // Configuración de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null); // Centrar en pantalla
        setTitle("Gestión de Catálogo");

        contentPane = new JPanel();
        contentPane.setBackground(COLOR_BACKGROUND);
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        contentPane.add(createBanner(), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBackground(COLOR_BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(createFormPanel(),  BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);

        contentPane.add(mainPanel, BorderLayout.CENTER);

        actualizarTabla(gestorMaterial.getMaterials()); // Mostramos todos los materiales al abrir
    }

    // Banner superior con el título de la sección
    private JPanel createBanner() {
        JPanel banner = new JPanel();
        banner.setBackground(COLOR_PRIMARY);
        banner.setPreferredSize(new Dimension(0, 70));
        banner.setLayout(new BorderLayout());
        banner.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel title = new JLabel("GESTIÓN DE CATÁLOGO");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(COLOR_TEXT);
        banner.add(title, BorderLayout.WEST);

        return banner;
    }

    // Panel del formulario para agregar o administrar materiales
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Agregar / Administrar Material");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(COLOR_PRIMARY);

        // Grilla de 5 filas: tipo, código, título, autor, año
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(5, 2, 15, 10));
        fieldsPanel.setOpaque(false);

        fieldsPanel.add(createLabel("Tipo de Material"));
        comboType = new JComboBox<>(new String[]{"BOOK", "MAGAZINE"}); // Solo dos opciones posibles
        comboType.setFont(new Font("Arial", Font.PLAIN, 11));
        fieldsPanel.add(comboType);

        fieldsPanel.add(createLabel("Código"));
        txtCode = createTextField();
        fieldsPanel.add(txtCode);

        fieldsPanel.add(createLabel("Título"));
        txtTitle = createTextField();
        fieldsPanel.add(txtTitle);

        fieldsPanel.add(createLabel("Autor"));
        txtAuthor = createTextField();
        fieldsPanel.add(txtAuthor);

        fieldsPanel.add(createLabel("Año de Publicación"));
        txtYear = createTextField();
        fieldsPanel.add(txtYear);

        JPanel footerPanel = new JPanel(new BorderLayout(10, 10));
        footerPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        // Botones de acción del formulario
        JButton btnSave   = createButton("Guardar");
        JButton btnClear  = createButton("Limpiar");
        JButton btnSearch = createButton("Buscar por Título");
        JButton btnDelete = createButton("Eliminar Seleccionado");
        JButton btnBack   = createButton("Volver");

        // El botón eliminar tiene un color diferente para advertir al usuario
        btnDelete.setBackground(COLOR_ERROR);

        buttonPanel.add(btnSave);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnBack);

        lblError = new JLabel("");
        lblError.setFont(new Font("Arial", Font.PLAIN, 11));
        lblError.setForeground(COLOR_ERROR);
        lblError.setHorizontalAlignment(SwingConstants.CENTER);

        footerPanel.add(buttonPanel, BorderLayout.CENTER);
        footerPanel.add(lblError, BorderLayout.SOUTH);

        // Conectamos cada botón con su método correspondiente
        btnSave.addActionListener(e   -> guardarMaterial());
        btnClear.addActionListener(e  -> limpiarCampos());
        btnSearch.addActionListener(e -> buscarMaterial());
        btnDelete.addActionListener(e -> eliminarMaterial());
        btnBack.addActionListener(e   -> {
            new MainFrame().setVisible(true); // Regresamos al menú principal
            dispose();
        });

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout(0, 15));
        container.setOpaque(false);

        container.add(title,        BorderLayout.NORTH);
        container.add(fieldsPanel,  BorderLayout.CENTER);
        container.add(footerPanel,  BorderLayout.SOUTH);

        panel.add(container, BorderLayout.NORTH);

        return panel;
    }

    // Panel con la tabla donde se listan todos los materiales registrados
    private JPanel createTablePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Materiales Registrados");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(COLOR_PRIMARY);
        panel.add(title, BorderLayout.NORTH);

        // Definimos las columnas de la tabla de catálogo
        model = new DefaultTableModel();
        model.addColumn("Tipo");
        model.addColumn("Código");
        model.addColumn("Título");
        model.addColumn("Autor");
        model.addColumn("Año");
        model.addColumn("Estado");

        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 11));
        table.setRowHeight(25);
        table.getTableHeader().setBackground(COLOR_SECONDARY);
        table.getTableHeader().setForeground(COLOR_TEXT);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));

        // Al seleccionar una fila, copiamos el código al campo de texto para facilitar operaciones
        table.getSelectionModel().addListSelectionListener(e -> {
            int filaSeleccionada = table.getSelectedRow();
            if (filaSeleccionada != -1) {
                String codigo = model.getValueAt(filaSeleccionada, 1).toString();
                txtCode.setText(codigo);
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Guarda un nuevo material en el sistema después de validar los datos
    private void guardarMaterial() {
        String tipo   = comboType.getSelectedItem().toString();
        String codigo = txtCode.getText().trim();
        String titulo = txtTitle.getText().trim();
        String autor  = txtAuthor.getText().trim();
        String anio   = txtYear.getText().trim();

        lblError.setText("");

        // Verificamos que todos los campos estén llenos
        if (codigo.isEmpty() || titulo.isEmpty() || autor.isEmpty() || anio.isEmpty()) {
            mostrarMensaje("Todos los campos son obligatorios.", COLOR_ERROR);
            return;
        }

        // No permitimos duplicados: verificamos si ya existe ese código
        if (gestorMaterial.existsCode(codigo)) { 
            mostrarMensaje("Error: El código '" + codigo + "' ya está registrado.", COLOR_ERROR);
            return;
        }

        try {
            // Creamos el objeto correcto según el tipo seleccionado
            Material nuevoMaterial;
            if (tipo.equals("BOOK")) {
                nuevoMaterial = new Book(codigo, anio, titulo, autor);
            } else {
                nuevoMaterial = new Magazine(codigo, anio, titulo, autor);
            }

            gestorMaterial.add(nuevoMaterial);   // Agregamos a la lista en memoria
            gestorMaterial.save();               // Persistimos en el archivo CSV
         // Registrar en la bitácora
            GestorBitacora.registrar("ALTA_MATERIAL", "Se agrego el codigo: " + codigo + " (" + tipo + ")");

            actualizarTabla(gestorMaterial.getMaterials()); // Refrescamos la tabla
            limpiarCampos();
            mostrarMensaje("Material guardado y sincronizado exitosamente.", COLOR_SUCCESS);

        } catch (IllegalArgumentException ex) {
            mostrarMensaje(ex.getMessage(), COLOR_ERROR);
        } catch (IOException ioEx) {
            mostrarMensaje("Error crítico al escribir en materiales.csv", COLOR_ERROR);
            ioEx.printStackTrace();
        }
    }

    // Elimina el material cuyo código está en el campo de texto
    private void eliminarMaterial() {
        String codigo = txtCode.getText().trim();
        lblError.setText("");

        if (codigo.isEmpty()) {
            mostrarMensaje("Por favor, introduce el Código del material o selecciónalo en la tabla.", COLOR_ERROR);
            return;
        }

        // Pedimos confirmación antes de eliminar para evitar errores accidentales
        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Estás seguro de que deseas eliminar el material con código: " + codigo + "?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean eliminado = gestorMaterial.remove(codigo); // Intentamos eliminar de la lista

            if (eliminado) {
                try {
                    gestorMaterial.save();    
                 // Registrar en la bitácora
                    GestorBitacora.registrar("BAJA_MATERIAL", "Se elimino el codigo: " + codigo);
                    // Guardamos los cambios en el CSV
                    actualizarTabla(gestorMaterial.getMaterials()); // Actualizamos la vista
                    limpiarCampos();
                    mostrarMensaje("Material eliminado correctamente de la base de datos.", COLOR_SUCCESS);
                } catch (IOException e) {
                    mostrarMensaje("Error al actualizar el archivo CSV.", COLOR_ERROR);
                    e.printStackTrace();
                }
            } else {
                mostrarMensaje("No se encontró ningún material con el código ingresado.", COLOR_ERROR);
            }
        }
    }

    // Filtra la tabla según el título escrito; si está vacío, muestra todo
    private void buscarMaterial() {
        String query = txtTitle.getText().trim();
        if (query.isEmpty()) {
            actualizarTabla(gestorMaterial.getMaterials()); // Sin filtro: mostramos todo
            mostrarMensaje("Mostrando catálogo completo.", COLOR_SECONDARY);
            return;
        }

        // Buscamos materiales que coincidan con el texto ingresado
        java.util.ArrayList<Material> filtrados = gestorMaterial.findByTitle(query); 
        actualizarTabla(filtrados);
        mostrarMensaje("Se encontraron " + filtrados.size() + " coincidencias.", COLOR_PRIMARY);
    }

    // Recarga la tabla con la lista de materiales recibida como parámetro
    private void actualizarTabla(java.util.ArrayList<Material> lista) {
        model.setRowCount(0); // Limpiamos las filas actuales
        for (Material m : lista) {
            // Determinamos el tipo legible y el estado de disponibilidad
            String tipoReal   = (m instanceof Book) ? "Libro" : "Revista";
            String estadoReal = m.isDisponible() ? "Disponible" : "Prestado";

            model.addRow(new Object[]{
                tipoReal,
                m.getCode(),
                m.getTitle(),
                m.getAuthor(),
                m.getYear(),
                estadoReal
            });
        }
    }

    // Limpia todos los campos del formulario y deselecciona la tabla
    private void limpiarCampos() {
        txtCode.setText("");
        txtTitle.setText("");
        txtAuthor.setText("");
        txtYear.setText("");
        lblError.setText("");
        table.clearSelection();
    }

    // Muestra un mensaje en la etiqueta lblError con el color indicado
    private void mostrarMensaje(String texto, Color color) {
        lblError.setText(texto);
        lblError.setForeground(color);
    }

    // Botón con estilo base; el botón "Eliminar" tiene su propio manejo de hover
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 11));
        button.setForeground(COLOR_TEXT);
        button.setBackground(COLOR_PRIMARY);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        // El botón de eliminar mantiene su color rojo incluso al salir el mouse
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!button.getText().equals("Eliminar Seleccionado")) {
                    button.setBackground(COLOR_HOVER);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!button.getText().equals("Eliminar Seleccionado")) {
                    button.setBackground(COLOR_PRIMARY);
                } else {
                    button.setBackground(COLOR_ERROR); // Vuelve al rojo al salir el mouse
                }
            }
        });
        return button;
    }

    // Campo de texto con borde gris claro y tamaño fijo
    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 11));
        field.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        field.setPreferredSize(new Dimension(150, 30));
        return field;
    }

    // Etiqueta de texto con estilo secundario (gris)
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        label.setForeground(COLOR_SECONDARY);
        return label;
    }
}