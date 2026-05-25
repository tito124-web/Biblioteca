package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Catalog extends JFrame {

    private static final long serialVersionUID = 1L;

    // Colores
    private static final Color COLOR_PRIMARY = new Color(55, 80, 80);
    private static final Color COLOR_SECONDARY = new Color(100, 100, 100);
    private static final Color COLOR_HOVER = new Color(110, 110, 110);
    private static final Color COLOR_BACKGROUND = new Color(250, 250, 250);
    private static final Color COLOR_TEXT = Color.WHITE;

    // Componentes
    private JPanel contentPane;

    private JTextField txtCode;
    private JTextField txtTitle;
    private JTextField txtAuthor;
    private JTextField txtType;
    private JTextField txtStatus;

    private JTable table;
    private DefaultTableModel model;

    // Constructor
    public Catalog() {

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

        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(createFormPanel(), BorderLayout.NORTH);

        mainPanel.add(createTablePanel(), BorderLayout.CENTER);

        contentPane.add(mainPanel, BorderLayout.CENTER);
    }

    // Banner superior
    private JPanel createBanner() {

        JPanel banner = new JPanel();

        banner.setBackground(COLOR_PRIMARY);

        banner.setPreferredSize(new Dimension(0, 70));

        banner.setLayout(new BorderLayout());

        banner.setBorder(
                BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel title = new JLabel("GESTIÓN DE CATÁLOGO");

        title.setFont(new Font("Arial", Font.BOLD, 28));

        title.setForeground(COLOR_TEXT);

        banner.add(title, BorderLayout.WEST);

        return banner;
    }

    // Panel principal
    private JPanel createFormPanel() {

        JPanel panel = new JPanel();

        panel.setLayout(new BorderLayout());

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Agregar Material");

        title.setFont(new Font("Arial", Font.BOLD, 16));

        title.setForeground(COLOR_PRIMARY);

        JPanel fieldsPanel = new JPanel();

        fieldsPanel.setLayout(new GridLayout(5, 2, 15, 10));

        fieldsPanel.setOpaque(false);

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

        // Tipo
        fieldsPanel.add(createLabel("Tipo"));

        txtType = createTextField();

        fieldsPanel.add(txtType);

        // Estado
        fieldsPanel.add(createLabel("Estado"));

        txtStatus = createTextField();

        fieldsPanel.add(txtStatus);

        JPanel buttonPanel = new JPanel();

        buttonPanel.setOpaque(false);

        JButton btnSave = createButton("Guardar");

        JButton btnClear = createButton("Limpiar");

        JButton btnSearch = createButton("Buscar");
        
        JButton btnBack = createButton("Volver");
        
        buttonPanel.add(btnSave);

        buttonPanel.add(btnClear);

        buttonPanel.add(btnSearch);
        
        buttonPanel.add(btnBack);
        
        btnBack.addActionListener(e -> { // Para volver a la Pantalla Principal
            new MainFrame().setVisible(true);
            dispose();
        });

        JPanel container = new JPanel();

        container.setLayout(new BorderLayout(0, 15));

        container.setOpaque(false);

        container.add(title, BorderLayout.NORTH);

        container.add(fieldsPanel, BorderLayout.CENTER);

        container.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(container, BorderLayout.NORTH);

        return panel;
    }

    // Panel de tabla
    private JPanel createTablePanel() {

        JPanel panel = new JPanel();

        panel.setLayout(new BorderLayout());

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Materiales Registrados");

        title.setFont(new Font("Arial", Font.BOLD, 16));

        title.setForeground(COLOR_PRIMARY);

        panel.add(title, BorderLayout.NORTH);

        // Modelo de tabla
        model = new DefaultTableModel();

        model.addColumn("Código");
        model.addColumn("Título");
        model.addColumn("Autor");
        model.addColumn("Tipo");
        model.addColumn("Estado");

        table = new JTable(model);

        table.setFont(new Font("Arial", Font.PLAIN, 11));

        table.setRowHeight(25);

        table.getTableHeader().setBackground(COLOR_SECONDARY);

        table.getTableHeader().setForeground(COLOR_TEXT);

        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 11));

        JScrollPane scrollPane = new JScrollPane(table);

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        new Color(200, 200, 200), 1));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Crear botones
    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.PLAIN, 11));

        button.setForeground(COLOR_TEXT);

        button.setBackground(COLOR_PRIMARY);

        button.setBorderPainted(false);

        button.setFocusPainted(false);

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

    // Crear cajas de texto
    private JTextField createTextField() {

        JTextField field = new JTextField();

        field.setFont(new Font("Arial", Font.PLAIN, 11));

        field.setBorder(
                BorderFactory.createLineBorder(
                        new Color(200, 200, 200), 1));

        field.setPreferredSize(new Dimension(150, 30));

        return field;
    }

    // Crear etiquetas
    private JLabel createLabel(String text) {

        JLabel label = new JLabel(text);

        label.setFont(new Font("Arial", Font.PLAIN, 11));

        label.setForeground(COLOR_SECONDARY);

        return label;
    }
}
