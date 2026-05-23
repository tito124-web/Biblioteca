package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class User extends JFrame {
    private static final long serialVersionUID = 1L;
    
    // Colores consistentes con MainFrame
    private static final Color COLOR_PRIMARY = new Color(55, 80, 80);
    private static final Color COLOR_SECONDARY = new Color(100, 100, 100);
    private static final Color COLOR_HOVER = new Color(110, 110, 110);
    private static final Color COLOR_BACKGROUND = new Color(250, 250, 250);
    private static final Color COLOR_TEXT = Color.WHITE;
    private static final Color COLOR_ERROR = new Color(220, 53, 69);
    private static final Color COLOR_SUCCESS = new Color(40, 167, 69);
    
    private JPanel contentPane;
    private JTextField txtFieldCarnet;
    private JTextField textFieldNombre;
    private JTextField textFieldCarrera;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblError;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                User frame = new User();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public User() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setTitle("Gestión de Usuarios");
        
        contentPane = new JPanel();
        contentPane.setBackground(COLOR_BACKGROUND);
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));
        
        // Banner
        contentPane.add(crearBanner(), BorderLayout.NORTH);
        
        // Contenido principal
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BorderLayout(20, 20));
        panelContenido.setBackground(COLOR_BACKGROUND);
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel izquierdo (botones)
        panelContenido.add(crearPanelBotones(), BorderLayout.WEST);
        
        // Panel derecho (formulario + tabla)
        panelContenido.add(crearPanelPrincipal(), BorderLayout.CENTER);
        
        contentPane.add(panelContenido, BorderLayout.CENTER);
    }
    
    //  BANNER 
    private JPanel crearBanner() {
        JPanel banner = new JPanel();
        banner.setBackground(COLOR_PRIMARY);
        banner.setPreferredSize(new Dimension(0, 70));
        banner.setLayout(new BorderLayout());
        banner.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        JLabel title = new JLabel("GESTIÓN DE USUARIOS");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(COLOR_TEXT);
        
        banner.add(title, BorderLayout.WEST);
        return banner;
    }
    
    // PANEL BOTONES (IZQUIERDA) 
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(120, 0));
        
        JButton btnInicio = crearBoton("Inicio");
        btnInicio.addActionListener(e -> {
            MainFrame win1 = new MainFrame();
            win1.setVisible(true);
            dispose(); // cierra la actual
        });
        
        
        panel.add(btnInicio);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    // PANEL PRINCIPAL (FORMULARIO + TABLA) 
    private JPanel crearPanelPrincipal() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 15));
        panel.setOpaque(false);
        
        // Formulario
        panel.add(crearPanelFormulario(), BorderLayout.NORTH);
        
        // Tabla
        panel.add(crearPanelTabla(), BorderLayout.CENTER);
        
        return panel;
    }
    
    // PANEL FORMULARIO 
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // TÍTULO
        JLabel titulo = new JLabel("Agregar Usuario");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setForeground(COLOR_PRIMARY);
        
        // PANEL DE CAMPOS
        JPanel panelCampos = new JPanel();
        panelCampos.setLayout(new GridLayout(3, 2, 15, 10));
        panelCampos.setOpaque(false);
        
        // Campo Carnet
        panelCampos.add(crearLabel("Carnet"));
        txtFieldCarnet = crearTextField();
        panelCampos.add(txtFieldCarnet);
        
        // Campo Nombre
        panelCampos.add(crearLabel("Nombre"));
        textFieldNombre = crearTextField();
        panelCampos.add(textFieldNombre);
        
        // Campo Carrera
        panelCampos.add(crearLabel("Carrera"));
        textFieldCarrera = crearTextField();
        panelCampos.add(textFieldCarrera);
        
        // PANEL BOTÓN + ERRORES
        JPanel panelAccion = new JPanel();
        panelAccion.setLayout(new BorderLayout(10, 0));
        panelAccion.setOpaque(false);
        
        JButton btnAgregar = crearBoton("Agregar");
        btnAgregar.setPreferredSize(new Dimension(120, 40));
        btnAgregar.addActionListener(e -> agregarUsuario());
        
        lblError = new JLabel();
        lblError.setFont(new Font("Arial", Font.PLAIN, 11));
        lblError.setForeground(COLOR_ERROR);
        
        panelAccion.add(btnAgregar, BorderLayout.WEST);
        panelAccion.add(lblError, BorderLayout.CENTER);
        
        // ARMAR TODO
        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BorderLayout(0, 15));
        contenedor.setOpaque(false);
        
        contenedor.add(titulo, BorderLayout.NORTH);
        contenedor.add(panelCampos, BorderLayout.CENTER);
        contenedor.add(panelAccion, BorderLayout.SOUTH);
        
        panel.add(contenedor, BorderLayout.NORTH);
        
        return panel;
    }
    
    //  PANEL TABLA 
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // TÍTULO
        JLabel titulo = new JLabel("Usuarios Registrados");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setForeground(COLOR_PRIMARY);
        
        panel.add(titulo, BorderLayout.NORTH);
        
        // TABLA
        model = new DefaultTableModel();
        model.addColumn("Carnet");
        model.addColumn("Nombre");
        model.addColumn("Carrera");
        
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 11));
        table.setRowHeight(25);
        table.getTableHeader().setBackground(COLOR_SECONDARY);
        table.getTableHeader().setForeground(COLOR_TEXT);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        table.setSelectionBackground(new Color(52, 152, 219));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    //  MÉTODOS AUXILIARES 
    
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.PLAIN, 11));
        boton.setForeground(COLOR_TEXT);
        boton.setBackground(COLOR_PRIMARY);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setOpaque(true);
        
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
        field.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        field.setPreferredSize(new Dimension(150, 30));
        return field;
    }
    
    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        label.setForeground(COLOR_SECONDARY);
        return label;
    }
    
    // VALIDACIONES Y LÓGICA 
    
    private void agregarUsuario() {
        String carnet = txtFieldCarnet.getText().trim();
        String nombre = textFieldNombre.getText().trim();
        String carrera = textFieldCarrera.getText().trim();
        
        // Limpiar mensaje de error
        lblError.setText("");
        
        // Validaciones
        if (carnet.isEmpty()) {
            mostrarError(" El carnet no puede estar vacío");
            return;
        }
        
        if (nombre.isEmpty()) {
            mostrarError("El nombre no puede estar vacío");
            return;
        }
        
        if (carrera.isEmpty()) {
            mostrarError("La carrera no puede estar vacía");
            return;
        }
        
        // Validar que el carnet sea alfanumérico
        if (!carnet.matches("[a-zA-Z0-9]+")) {
            mostrarError("El carnet solo puede contener letras y números");
            return;
        }
        
        // Validar que el nombre solo tenga letras y espacios
        if (!nombre.matches("[a-zA-Záéíóú\\s]+")) {
            mostrarError("El nombre solo puede contener letras");
            return;
        }
        
        // Validar que no exista carnet duplicado
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equals(carnet)) {
                mostrarError("El carnet ya existe");
                return;
            }
        }
        
        // Si todo está OK, agregar a la tabla
        model.addRow(new Object[] {carnet, nombre, carrera});
        
        // Limpiar campos
        txtFieldCarnet.setText("");
        textFieldNombre.setText("");
        textFieldCarrera.setText("");
        
        // Mensaje de éxito
        lblError.setText(" Usuario agregado correctamente");
        lblError.setForeground(COLOR_SUCCESS);
    }
    
    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setForeground(COLOR_ERROR);
    }
    
}