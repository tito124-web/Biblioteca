package ui;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Usuario.GestorUser;
import Usuario.Student;
import Usuario.Teacher;

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
    private JTextField textFieldExtra; // Sirve para Carrera o Departamento
    private JComboBox<String> comboTipo; // Filtro de Selección de tipo
    private JLabel lblExtraLabel; // Cambia dinámicamente el texto de la etiqueta
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblError;

    private GestorUser gestorUser; 

    // Constructor vacío para compatibilidad con MainFrame
    public User() {
        this(new GestorUser()); 
    }

    public User(GestorUser gestorUser) {
        this.gestorUser = gestorUser;
     
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 650);
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

        // Cargar los datos desde el archivo CSV al iniciar la interfaz
        cargarTabla();
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
            dispose(); 
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
        panelCampos.setLayout(new GridLayout(4, 2, 15, 10)); 
        panelCampos.setOpaque(false);
        
        // Selector de tipo
        panelCampos.add(crearLabel("Tipo de Usuario"));
        comboTipo = new JComboBox<>(new String[]{"STUDENT", "TEACHER"});
        comboTipo.setFont(new Font("Arial", Font.PLAIN, 11));
        comboTipo.addActionListener(e -> {
            if (comboTipo.getSelectedItem().equals("STUDENT")) {
                lblExtraLabel.setText("Carrera");
            } else {
                lblExtraLabel.setText("Departamento");
            }
        });
        panelCampos.add(comboTipo);
        
        // Campo Carnet
        panelCampos.add(crearLabel("Carnet"));
        txtFieldCarnet = crearTextField();
        panelCampos.add(txtFieldCarnet);
        
        // Campo Nombre
        panelCampos.add(crearLabel("Nombre"));
        textFieldNombre = crearTextField();
        panelCampos.add(textFieldNombre);
        
        // Campo Variable (Carrera o Departamento)
        lblExtraLabel = crearLabel("Carrera");
        panelCampos.add(lblExtraLabel);
        textFieldExtra = crearTextField();
        panelCampos.add(textFieldExtra);
        
        // PANEL BOTÓN + ERRORES
        JPanel panelAccion = new JPanel();
        panelAccion.setLayout(new BorderLayout(10, 0));
        panelAccion.setOpaque(false);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton btnAgregar = crearBoton("Agregar");
        JButton btnEliminar = crearBoton("Eliminar");
        
        btnEliminar.setBackground(COLOR_ERROR);
        
        buttonPanel.add(btnAgregar);
        buttonPanel.add(btnEliminar);
        
        btnAgregar.addActionListener(e -> agregarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
        
        lblError = new JLabel();
        lblError.setFont(new Font("Arial", Font.PLAIN, 11));
        lblError.setForeground(COLOR_ERROR);
        
        panelAccion.add(buttonPanel, BorderLayout.WEST);
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
        model.addColumn("Tipo");
        model.addColumn("Carnet");
        model.addColumn("Nombre");
        model.addColumn("Carrera/Depto");
        model.addColumn("Préstamos");
        model.addColumn("Máx.");
        
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 11));
        table.setRowHeight(25);
        table.getTableHeader().setBackground(COLOR_SECONDARY);
        table.getTableHeader().setForeground(COLOR_TEXT);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        table.setSelectionBackground(new Color(52, 152, 219));
        
        table.getSelectionModel().addListSelectionListener(e -> {
            int filaSeleccionada = table.getSelectedRow();
            if (filaSeleccionada != -1) {
                String carnet = model.getValueAt(filaSeleccionada, 1).toString();
                txtFieldCarnet.setText(carnet);
            }
        });
        
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
                if (!boton.getText().equals("Eliminar")) {
                    boton.setBackground(COLOR_HOVER);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!boton.getText().equals("Eliminar")) {
                    boton.setBackground(COLOR_PRIMARY);
                } else {
                    boton.setBackground(COLOR_ERROR);
                }
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
        String tipo = comboTipo.getSelectedItem().toString();
        String carnet = txtFieldCarnet.getText().trim();
        String nombre = textFieldNombre.getText().trim();
        String extra = textFieldExtra.getText().trim();
        
        lblError.setText("");
        
        // Validaciones generales
        if (carnet.isEmpty() || nombre.isEmpty() || extra.isEmpty()) {
            mostrarError("Todos los campos son obligatorios.");
            return;
        }
        
        if (!carnet.matches("[a-zA-Z0-9-]+")) {
            mostrarError("El carnet solo puede contener letras, números y guiones.");
            return;
        }
        
        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            mostrarError("El nombre solo puede contener letras.");
            return;
        }
        
        // Validar duplicados mediante el Backend
        if (gestorUser.existsId(carnet)) {
            mostrarError("El carnet ya está registrado.");
            return;
        }
        
        try {
            if (tipo.equals("STUDENT")) {
                Student nuevoEstudiante = new Student(nombre, carnet, extra);
                gestorUser.add(nuevoEstudiante);
            } else {
                Teacher nuevoProfesor = new Teacher(nombre, carnet, extra);
                gestorUser.add(nuevoProfesor);
            }
            
            // Guardar cambios en el backend
            gestorUser.save();
            
            // Limpiar la tabla visual y recargar todo desde cero de forma limpia
            cargarTabla();
            
            // Limpiar cajas de texto
            txtFieldCarnet.setText("");
            textFieldNombre.setText("");
            textFieldExtra.setText("");
            
            lblError.setText("Usuario registrado y guardado exitosamente.");
            lblError.setForeground(COLOR_SUCCESS);
            
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        } catch (IOException ioEx) {
            mostrarError("Error crítico al guardar en el archivo CSV.");
            ioEx.printStackTrace();
        }
    }
    
    private void eliminarUsuario() {
        String carnet = txtFieldCarnet.getText().trim();
        lblError.setText("");

        if (carnet.isEmpty()) {
            mostrarError("Selecciona un usuario de la tabla.");
            return;
        }
        int conf = JOptionPane.showConfirmDialog(this, "¿Deseas eliminar el usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            if (gestorUser.remove(carnet)) {
                try {
                    gestorUser.save();
                    cargarTabla();
                    txtFieldCarnet.setText("");
                    textFieldNombre.setText("");
                    textFieldExtra.setText("");
                    table.clearSelection();
                    lblError.setText("Usuario eliminado correctamente.");
                    lblError.setForeground(COLOR_SUCCESS);
                } catch (IOException e) {
                    mostrarError("Error al actualizar el archivo.");
                }
            } else {
                mostrarError("No se encontró el carnet.");
            }
        }
    }
    
    // Carga los usuarios del CSV a la tabla al abrir la ventana
    private void cargarTabla() {
        // Limpiamos las filas actuales del modelo para evitar duplicación visual
        model.setRowCount(0);
        
        try {
            gestorUser.load();
        } catch (IOException e) {
            System.out.println("No hay datos previos.");
        }

        for (Usuario.User u : gestorUser.getUsers()) {
            if (u instanceof Student) {
                Student s = (Student) u;
                model.addRow(new Object[]{
                    "Estudiante", 
                    s.getId(), 
                    s.getName(), 
                    s.getMajor(),
                    s.getActiveLoans() + "/" + s.getMaxLoans(),
                    s.getMaxLoans()
                });
            } else if (u instanceof Teacher) {
                Teacher t = (Teacher) u;
                model.addRow(new Object[]{
                    "Docente", 
                    t.getId(), 
                    t.getName(), 
                    t.getDepartment(),
                    t.getActiveLoans() + "/" + t.getMaxLoans(),
                    t.getMaxLoans()
                });
            }
        }
    }

    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
        lblError.setForeground(COLOR_ERROR);
    }
}