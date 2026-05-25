package ui;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

// IMPORTAMOS LA LÓGICA DEL EQUIPO
import Material.GestorMaterial;
import Material.Material;
import Material.Book;
import Material.Magazine;

public class Catalog extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final Color COLOR_PRIMARY = new Color(55, 80, 80);
    private static final Color COLOR_SECONDARY = new Color(100, 100, 100);
    private static final Color COLOR_HOVER = new Color(110, 110, 110);
    private static final Color COLOR_BACKGROUND = new Color(250, 250, 250);
    private static final Color COLOR_TEXT = Color.WHITE;
    private static final Color COLOR_ERROR = new Color(220, 53, 69);
    private static final Color COLOR_SUCCESS = new Color(40, 167, 69);

    private JPanel contentPane;

    private JTextField txtCode;
    private JTextField txtTitle;
    private JTextField txtAuthor;
    private JTextField txtYear; //Cambiado de tipo texto a Año
    private JComboBox<String> comboType; // Selector estricto de tipo
    private JLabel lblError; // Faltaba para avisar errores en pantalla

    private JTable table;
    private DefaultTableModel model;

    // CONEXIÓN CON EL BACKEND
    private GestorMaterial gestorMaterial;

    public Catalog() {
        // Inicializar el gestor y cargar registros existentes
        gestorMaterial = new GestorMaterial();
        try {
            gestorMaterial.cargar();
        } catch (IOException e) {
            System.out.println("No se encontraron materiales previos.");
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
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

        mainPanel.add(createFormPanel(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);

        contentPane.add(mainPanel, BorderLayout.CENTER);

        // Llenar la tabla visual con lo que haya en materiales.csv
        actualizarTabla(gestorMaterial.getMateriales());
    }

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

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Agregar Material");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(COLOR_PRIMARY);

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(5, 2, 15, 10));
        fieldsPanel.setOpaque(false);

        // Tipo (Selector)
        fieldsPanel.add(createLabel("Tipo de Material"));
        comboType = new JComboBox<>(new String[]{"BOOK", "MAGAZINE"});
        comboType.setFont(new Font("Arial", Font.PLAIN, 11));
        fieldsPanel.add(comboType);

        // Código
        fieldsPanel.add(createLabel("Código"));
        txtCode = createTextField();
        fieldsPanel.add(txtCode);

        // Título
        fieldsPanel.add(createLabel("Título"));
        txtTitle = createTextField();
        fieldsPanel.add(txtTitle);

        // Autor
        fieldsPanel.add(createLabel("Autor"));
        txtAuthor = createTextField();
        fieldsPanel.add(txtAuthor);

        // Año
        fieldsPanel.add(createLabel("Año de Publicación"));
        txtYear = createTextField();
        fieldsPanel.add(txtYear);

        // Panel de Botones y Alertas
        JPanel footerPanel = new JPanel(new BorderLayout(10, 10));
        footerPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        JButton btnSave = createButton("Guardar");
        JButton btnClear = createButton("Limpiar");
        JButton btnSearch = createButton("Buscar por Título");
        JButton btnBack = createButton("Volver");
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnBack);

        lblError = new JLabel("");
        lblError.setFont(new Font("Arial", Font.PLAIN, 11));
        lblError.setForeground(COLOR_ERROR);
        lblError.setHorizontalAlignment(SwingConstants.CENTER);

        footerPanel.add(buttonPanel, BorderLayout.CENTER);
        footerPanel.add(lblError, BorderLayout.SOUTH);

        // ASIGNACIÓN DE ACCIONES (SINCRONIZACIÓN)
        btnSave.addActionListener(e -> guardarMaterial());
        btnClear.addActionListener(e -> limpiarCampos());
        btnSearch.addActionListener(e -> buscarMaterial());
        btnBack.addActionListener(e -> {
            new MainFrame().setVisible(true);
            dispose();
        });

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout(0, 15));
        container.setOpaque(false);

        container.add(title, BorderLayout.NORTH);
        container.add(fieldsPanel, BorderLayout.CENTER);
        container.add(footerPanel, BorderLayout.SOUTH);

        panel.add(container, BorderLayout.NORTH);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Materiales Registrados");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(COLOR_PRIMARY);
        panel.add(title, BorderLayout.NORTH);

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

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ACCIONES DE LÓGICA Y SINCRONIZACIÓN 

    private void guardarMaterial() {
        String tipo = comboType.getSelectedItem().toString();
        String codigo = txtCode.getText().trim();
        String titulo = txtTitle.getText().trim();
        String autor = txtAuthor.getText().trim();
        String anio = txtYear.getText().trim();

        lblError.setText("");

        if (codigo.isEmpty() || titulo.isEmpty() || autor.isEmpty() || anio.isEmpty()) {
            mostrarMensaje("Todos los campos son obligatorios.", COLOR_ERROR);
            return;
        }

        // Validación de duplicados usando el Gestor backend
        if (gestorMaterial.existeCodigo(codigo)) {
            mostrarMensaje("Error: El código '" + codigo + "' ya está registrado.", COLOR_ERROR);
            return;
        }

        try {
            Material nuevoMaterial;
            if (tipo.equals("BOOK")) {
                nuevoMaterial = new Book(codigo, anio, titulo, autor);
            } else {
                nuevoMaterial = new Magazine(codigo, anio, titulo, autor);
            }

            // Guardar en la lista y en el archivo CSV 
            gestorMaterial.agregar(nuevoMaterial);
            gestorMaterial.guardar();

            // Refrescar tabla y limpiar
            actualizarTabla(gestorMaterial.getMateriales());
            limpiarCampos();
            mostrarMensaje("Material guardado y sincronizado exitosamente.", COLOR_SUCCESS);

        } catch (IllegalArgumentException ex) {
            mostrarMensaje(ex.getMessage(), COLOR_ERROR);
        } catch (IOException ioEx) {
            mostrarMensaje("Error crítico al escribir en materiales.csv", COLOR_ERROR);
            ioEx.printStackTrace();
        }
    }

    private void buscarMaterial() {
        String query = txtTitle.getText().trim();
        if (query.isEmpty()) {
            // Si el campo de título está vacío, muestra todo de nuevo
            actualizarTabla(gestorMaterial.getMateriales());
            mostrarMensaje("Mostrando catálogo completo.", COLOR_SECONDARY);
            return;
        }

        // Llama al método de búsqueda por título
        java.util.ArrayList<Material> filtrados = gestorMaterial.buscarPorTitulo(query);
        actualizarTabla(filtrados);
        mostrarMensaje("Se encontraron " + filtrados.size() + " coincidencias.", COLOR_PRIMARY);
    }

    private void actualizarTabla(java.util.ArrayList<Material> lista) {
        model.setRowCount(0); // Limpiar filas anteriores
        for (Material m : lista) {
            String tipoReal = (m instanceof Book) ? "Libro" : "Revista";
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

    private void limpiarCampos() {
        txtCode.setText("");
        txtTitle.setText("");
        txtAuthor.setText("");
        txtYear.setText("");
        lblError.setText("");
    }

    private void mostrarMensaje(String texto, Color color) {
        lblError.setText(texto);
        lblError.setForeground(color);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 11));
        button.setForeground(COLOR_TEXT);
        button.setBackground(COLOR_PRIMARY);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(COLOR_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(COLOR_PRIMARY);
            }
        });
        return button;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 11));
        field.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        field.setPreferredSize(new Dimension(150, 30));
        return field;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        label.setForeground(COLOR_SECONDARY);
        return label;
    }
}