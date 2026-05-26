package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Loans extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final Color COLOR_PRIMARY = new Color(55, 80, 80);
    private static final Color COLOR_SECONDARY = new Color(100, 100, 100);
    private static final Color COLOR_HOVER = new Color(110, 110, 110);
    private static final Color COLOR_BACKGROUND = new Color(250, 250, 250);
    private static final Color COLOR_TEXT = Color.WHITE;
    private static final Color COLOR_ERROR = new Color(220, 53, 69);
    private static final Color COLOR_SUCCESS = new Color(40, 167, 69);

    private JPanel contentPane;

    private JTextField txtCarnet;
    private JTextField txtNombre;
    private JTextField txtCodigo;
    private JTextField txtMaterial;

    private JTable table;
    private DefaultTableModel model;

    private JLabel lblError;

    public Loans() {

        // Configuración principal de ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setTitle("Gestión de Préstamos");
       
        contentPane = new JPanel();
        contentPane.setBackground(COLOR_BACKGROUND);
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
      
        contentPane.add(crearBanner(), BorderLayout.NORTH);  // Agregar banner

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBackground(COLOR_BACKGROUND);
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20));
      
        mainPanel.add(crearPanelFormulario(), BorderLayout.NORTH);  // Agregar formulario y tabla
        mainPanel.add(crearPanelTabla(), BorderLayout.CENTER);

        // Agregar al contentPane
        contentPane.add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel crearBanner() {

        JPanel banner = new JPanel();
        banner.setBackground(COLOR_PRIMARY);
        banner.setPreferredSize(new Dimension(0, 70));
        banner.setLayout(new BorderLayout());
        banner.setBorder(
                BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel title = new JLabel("GESTIÓN DE PRÉSTAMOS");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(COLOR_TEXT);

        banner.add(title, BorderLayout.WEST);

        return banner;
    }

    private JPanel crearPanelFormulario() {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Registrar Préstamo");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setForeground(COLOR_PRIMARY);

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

        panelCampos.add(crearLabel("Título Material"));   // Material
        txtMaterial = crearTextField();
        panelCampos.add(txtMaterial);

        // Footer panel
        JPanel footerPanel = new JPanel(
                new BorderLayout(10, 10));

        footerPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        // Botones
        JButton btnPrestar = createButton("Prestar");
        JButton btnDevolver = createButton("Devolver");
        JButton btnLimpiar = createButton("Limpiar");
        JButton btnBack = createButton("Volver");

        // Agregar botones
        buttonPanel.add(btnPrestar);
        buttonPanel.add(btnDevolver);
        buttonPanel.add(btnLimpiar);
        buttonPanel.add(btnBack);

        // Label de mensajes
        lblError = new JLabel("");
        lblError.setFont(new Font("Arial", Font.PLAIN, 11));
        lblError.setForeground(COLOR_ERROR);
        lblError.setHorizontalAlignment(SwingConstants.CENTER);

        // Agregar componentes
        footerPanel.add(buttonPanel, BorderLayout.CENTER);
        footerPanel.add(lblError, BorderLayout.SOUTH);

        // Eventos botones
        btnPrestar.addActionListener(e -> registrarPrestamo());  // Evento prestar      
        btnDevolver.addActionListener(e -> devolverPrestamo());// Evento devolver       
        btnLimpiar.addActionListener(e -> limpiarCampos());    // Evento limpiar  
        btnBack.addActionListener(e -> {   // Evento volver

            new MainFrame().setVisible(true);

            dispose();
        });

        // Contenedor
        JPanel container = new JPanel();

        container.setLayout(new BorderLayout(0, 15));
        container.setOpaque(false);
        container.add(titulo, BorderLayout.NORTH);
        container.add(panelCampos, BorderLayout.CENTER);
        container.add(footerPanel, BorderLayout.SOUTH);
        panel.add(container, BorderLayout.NORTH);

        return panel;
    }

    private JPanel crearPanelTabla() {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("PRÉSTAMOS ACTIVOS");

        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setForeground(COLOR_PRIMARY);
        panel.add(titulo, BorderLayout.NORTH);

        model = new DefaultTableModel();
        model.addColumn("Carnet");
        model.addColumn("Usuario");
        model.addColumn("Código");
        model.addColumn("Material");
        model.addColumn("Estado");

        // Tabla
        table = new JTable(model);

        table.setFont(new Font("Arial", Font.PLAIN, 11));
        table.setRowHeight(25);
        table.getTableHeader().setBackground(COLOR_SECONDARY);
        table.getTableHeader().setForeground(COLOR_TEXT);
        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 11));

        // Evento seleccionar fila
        table.getSelectionModel().addListSelectionListener(e -> {

            int filaSeleccionada = table.getSelectedRow();
            if (filaSeleccionada != -1) {
                txtCarnet.setText(
                        model.getValueAt(filaSeleccionada, 0).toString());

                txtNombre.setText(
                        model.getValueAt(filaSeleccionada, 1).toString());

                txtCodigo.setText(
                        model.getValueAt(filaSeleccionada, 2).toString());

                txtMaterial.setText(
                        model.getValueAt(filaSeleccionada, 3).toString());
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        new Color(200, 200, 200), 1));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void registrarPrestamo() {

        String carnet = txtCarnet.getText().trim();
        String nombre = txtNombre.getText().trim();
        String codigo = txtCodigo.getText().trim();
        String material = txtMaterial.getText().trim();

        lblError.setText("");

        // Validar campos vacíos
        if (carnet.isEmpty() ||
                nombre.isEmpty() ||
                codigo.isEmpty() ||
                material.isEmpty()) {

            mostrarMensaje(
                    "Todos los campos son obligatorios.",
                    COLOR_ERROR);

            return;
        }

        // Validar carnet
        if (!carnet.matches("[0-9]+")) { 

            mostrarMensaje(
                    "El carnet solo puede contener números.",
                    COLOR_ERROR);

            return;
        }

        // Agregar fila a tabla
        model.addRow(new Object[]{
                carnet,
                nombre,
                codigo,
                material,
                "Activo"
        });

        // Limpiar campos
        limpiarCampos();

        // Mensaje éxito
        mostrarMensaje(
                "Préstamo registrado exitosamente.",
                COLOR_SUCCESS);
    }

    private void devolverPrestamo() {

        int filaSeleccionada = table.getSelectedRow();

        // Validar selección
        if (filaSeleccionada == -1) {

            mostrarMensaje(
                    "Seleccione un préstamo en la tabla.",
                    COLOR_ERROR);

            return;
        }

        // Cambiar estado
        model.setValueAt(
                "Devuelto",
                filaSeleccionada,
                4);

        // Mensaje éxito
        mostrarMensaje(
                "Material devuelto correctamente.",
                COLOR_SUCCESS);
    }

    // Método limpiar campos
    private void limpiarCampos() {

        txtCarnet.setText("");
        txtNombre.setText("");
        txtCodigo.setText("");
        txtMaterial.setText("");

        table.clearSelection();
    }

    // Método mostrar mensajes
    private void mostrarMensaje(String texto, Color color) {

        lblError.setText(texto);
        lblError.setForeground(color);
    }

    // Método crear botones
    private JButton createButton(String texto) {

        JButton boton = new JButton(texto);

        boton.setFont(new Font("Arial", Font.PLAIN, 11));
        boton.setForeground(COLOR_TEXT);
        boton.setBackground(COLOR_PRIMARY);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setOpaque(true);

        // Hover botón
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

    private JTextField crearTextField() {

        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 11));
        field.setBorder(
                BorderFactory.createLineBorder(
                        new Color(200, 200, 200), 1));

        field.setPreferredSize(new Dimension(150, 30));
        return field;
    }
    
    private JLabel crearLabel(String texto) {

        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        label.setForeground(COLOR_SECONDARY);

        return label;
    }
}