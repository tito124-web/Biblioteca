package ui;

import java.awt.*;
import javax.swing.*;

public class MainFrame {

	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Inicio");
        frame.setSize(1500, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        
     
        JButton CatalogoButton = new JButton("Catálogo");
        
        frame.setTitle("Bienvenido");
        frame.setLayout(new java.awt.FlowLayout());
        frame.setVisible(true);
        frame.add(CatalogoButton);
	}

}
