package ui;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final Color COLOR_PRIMARY = new Color(60, 80, 80);
    private static final Color COLOR_SECONDARY = new Color(100, 100, 100);
    private static final Color COLOR_HOVER = new Color(110, 110, 110);
    private static final Color COLOR_BACKGROUND = new Color(250, 250, 250);
    private static final Color COLOR_TEXT = Color.WHITE;

    public MainFrame() {
        setTitle("Biblioteca UMG");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(COLOR_BACKGROUND);

        // Banner
        JPanel panelBanner = crearBanner();

        // Contenido
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BorderLayout(20, 0));
        panelContenido.setBackground(COLOR_BACKGROUND);
        panelContenido.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel panelBotones = crearPanelBotones();
        JPanel panelDescripcion = crearPanelDescripcion();

        panelContenido.add(panelDescripcion, BorderLayout.WEST);
        panelContenido.add(panelBotones, BorderLayout.CENTER);

        add(panelBanner, BorderLayout.NORTH);
        add(panelContenido, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }

    private JPanel crearBanner() {
        JPanel panelBanner = new JPanel();
        panelBanner.setBackground(COLOR_PRIMARY);
        panelBanner.setPreferredSize(new Dimension(0, 70));
        panelBanner.setLayout(new BorderLayout());
        panelBanner.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JLabel title = new JLabel("BIBLIOTECA UMG");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(COLOR_TEXT);

        panelBanner.add(title, BorderLayout.WEST);
        return panelBanner;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel menuLabel = new JLabel("MENÚ");
        menuLabel.setFont(new Font("Arial", Font.BOLD, 12));
        menuLabel.setForeground(COLOR_SECONDARY);

        panel.add(menuLabel);
        panel.add(Box.createVerticalStrut(25));

        panel.add(crearFilaCatalogo());
        panel.add(Box.createVerticalStrut(22));

        panel.add(crearFilaUsuarios()); //  ESTE ES IMPORTANTE
        panel.add(Box.createVerticalStrut(22));

        panel.add(crearFila("Préstamos", "Administrar préstamos y devoluciones."));
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    //  FILA NORMAL
    private JPanel crearFila(String textoBoton, String descripcionTexto) {
        JPanel fila = new JPanel(new BorderLayout(12, 0));
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(350, 60));

        JButton boton = crearBoton(textoBoton);

        JLabel descripcion = new JLabel("<html><p style='font-size:10px;'>" + descripcionTexto + "</p></html>");
        descripcion.setForeground(new Color(120, 120, 120));

        fila.add(boton, BorderLayout.WEST);
        fila.add(descripcion, BorderLayout.CENTER);

        return fila;
    }
    
    //  FILA USUARIOS (CON ACCIÓN)
    private JPanel crearFilaUsuarios() {
        JPanel fila = new JPanel(new BorderLayout(12, 0));
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(350, 60));

        JButton boton = crearBoton("Usuarios");

        boton.addActionListener(e -> {
            new User().setVisible(true);
            dispose();
        });

        JLabel descripcion = new JLabel("<html><p style='font-size:10px;'>Gestionar usuarios registrados.</p></html>");
        descripcion.setForeground(new Color(120, 120, 120));

        fila.add(boton, BorderLayout.WEST);
        fila.add(descripcion, BorderLayout.CENTER);

        return fila;
    }

//  FILA CATÁLOGO (CON ACCIÓN)
private JPanel crearFilaCatalogo() {

    JPanel fila = new JPanel(new BorderLayout(12, 0));

    fila.setOpaque(false);

    fila.setMaximumSize(new Dimension(350, 60));

    JButton boton = crearBoton("Catálogo");

    boton.addActionListener(e -> {

        new Catalog().setVisible(true);

        dispose();
    });

    JLabel descripcion = new JLabel(
            "<html><p style='font-size:10px;'>Ver y buscar libros disponibles.</p></html>");

    descripcion.setForeground(new Color(120, 120, 120));

    fila.add(boton, BorderLayout.WEST);

    fila.add(descripcion, BorderLayout.CENTER);

    return fila;
}

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(110, 60));
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

    private JPanel crearPanelDescripcion() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel titulo = new JLabel("Bienvenido a la Biblioteca");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(COLOR_PRIMARY);

        JLabel contenido = new JLabel("<html>Selecciona una opción del menú para comenzar.</html>");

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(contenido, BorderLayout.CENTER);

        return panel;
    }
}