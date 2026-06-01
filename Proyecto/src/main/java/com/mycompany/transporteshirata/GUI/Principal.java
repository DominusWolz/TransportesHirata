/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/MDIApplication.java to edit this template
 */
package com.mycompany.transporteshirata.GUI;

import com.mycompany.transporteshirata.Datos.MantenimientoDao;
import java.util.List;
import javax.swing.JFrame;

/**
 *
 * @author danie
 */
public class Principal extends javax.swing.JFrame {

    public Principal() {
        initComponents();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setTitle("     \n     Principal     \n     ");
        openMenuItem.setVisible(false);
        openMenuItem1.setVisible(false);
        bt_mantenimiento.setVisible(false);
        jMenuItem1.setVisible(false);
        mostrarAlertaMantenimiento();
    }

    private void mostrarAlertaMantenimiento() {
        MantenimientoDao mDao = new MantenimientoDao();
        List<String> pendientes = mDao.obtenerCamionesPendientesMantenimiento();
        if (!pendientes.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("ALERTA DE MANTENIMIENTO\n");
            sb.append("Los siguientes camiones requieren mantencion urgente:\n\n");
            for (String linea : pendientes) {
                sb.append("  - ").append(linea).append("\n");
            }
            sb.append("\nPor favor programe el mantenimiento a la brevedad.");
            javax.swing.JOptionPane.showMessageDialog(this, sb.toString(),
                    "Alerta de Mantenimiento", javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jMenuItem2 = new javax.swing.JMenuItem();
        jButton1 = new javax.swing.JButton();
        desktopPane = new javax.swing.JDesktopPane();
        menuBar = new javax.swing.JMenuBar();
        bt_conductor = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        openMenuItem1 = new javax.swing.JMenuItem();
        openMenuItem4 = new javax.swing.JMenuItem();
        openMenuItem2 = new javax.swing.JMenuItem();
        bt_conductor1 = new javax.swing.JMenu();
        bt_mantenimiento = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        openMenu5 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        bt_inventarioPiezas = new javax.swing.JMenuItem();
        openMenuItem3 = new javax.swing.JMenuItem();

        jMenuItem2.setText("jMenuItem2");

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jButton1.setText("Volver");
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        bt_conductor.setMnemonic('f');
        bt_conductor.setText("Registro");
        bt_conductor.setFont(new java.awt.Font("Sitka Text", 1, 24));

        openMenuItem.setFont(new java.awt.Font("Segoe UI", 3, 18));
        openMenuItem.setText("Registrar camion");
        openMenuItem.addActionListener(evt -> openMenuItemActionPerformed(evt));
        bt_conductor.add(openMenuItem);

        openMenuItem1.setFont(new java.awt.Font("Segoe UI", 3, 18));
        openMenuItem1.setText("Registrar conductor");
        openMenuItem1.addActionListener(evt -> openMenuItem1ActionPerformed(evt));
        bt_conductor.add(openMenuItem1);

        openMenuItem4.setFont(new java.awt.Font("Segoe UI", 3, 18));
        openMenuItem4.setText("Registrar Equipos de Oficina");
        openMenuItem4.addActionListener(evt -> openMenuItem4ActionPerformed(evt));
        bt_conductor.add(openMenuItem4);

        openMenuItem2.setFont(new java.awt.Font("Segoe UI", 3, 18));
        openMenuItem2.setText("Volver");
        openMenuItem2.addActionListener(evt -> openMenuItem2ActionPerformed(evt));
        bt_conductor.add(openMenuItem2);

        menuBar.add(bt_conductor);

        bt_conductor1.setMnemonic('f');
        bt_conductor1.setText("Mantenimiento");
        bt_conductor1.setFont(new java.awt.Font("Sitka Text", 1, 24));

        bt_mantenimiento.setFont(new java.awt.Font("Segoe UI", 3, 18));
        bt_mantenimiento.setText("Hacer mantenimiento");
        bt_mantenimiento.addActionListener(evt -> bt_mantenimientoActionPerformed(evt));
        bt_conductor1.add(bt_mantenimiento);

        jMenuItem1.setFont(new java.awt.Font("Segoe UI", 3, 18));
        jMenuItem1.setText("Historial Mantenimiento");
        jMenuItem1.addActionListener(evt -> jMenuItem1ActionPerformed(evt));
        bt_conductor1.add(jMenuItem1);

        openMenu5.setFont(new java.awt.Font("Segoe UI", 3, 18));
        openMenu5.setText("Mantenimiento Equipos");
        openMenu5.addActionListener(evt -> openMenu5ActionPerformed(evt));
        bt_conductor1.add(openMenu5);

        jMenuItem3.setFont(new java.awt.Font("Segoe UI", 3, 18));
        jMenuItem3.setText("Software Equipos");
        jMenuItem3.addActionListener(evt -> jMenuItem3ActionPerformed(evt));
        bt_conductor1.add(jMenuItem3);

        bt_inventarioPiezas.setFont(new java.awt.Font("Segoe UI", 3, 18));
        bt_inventarioPiezas.setText("Inventario de piezas");
        bt_inventarioPiezas.addActionListener(evt -> bt_inventarioPiezasActionPerformed(evt));
        bt_conductor1.add(bt_inventarioPiezas);

        openMenuItem3.setFont(new java.awt.Font("Segoe UI", 3, 18));
        openMenuItem3.setText("Volver");
        openMenuItem3.addActionListener(evt -> openMenuItem3ActionPerformed(evt));
        bt_conductor1.add(openMenuItem3);

        menuBar.add(bt_conductor1);
        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
        );

        pack();
    }

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        GuiRegistrarCamion v = new GuiRegistrarCamion();
        desktopPane.add(v);
        v.setVisible(true);
    }

    private void openMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        GuiRegistrarConductor c = new GuiRegistrarConductor();
        desktopPane.add(c);
        c.setVisible(true);
    }

    private void bt_mantenimientoActionPerformed(java.awt.event.ActionEvent evt) {
        GuiMantenimiento m = new GuiMantenimiento();
        desktopPane.add(m);
        m.setVisible(true);
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        GuiHistorialMantenimiento hist = new GuiHistorialMantenimiento();
        desktopPane.add(hist);
        hist.setVisible(true);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
        GuiLoginPrincipal p = new GuiLoginPrincipal();
        p.setVisible(true);
    }

    private void openMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
        GuiLoginPrincipal p = new GuiLoginPrincipal();
        p.setVisible(true);
    }

    private void openMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
        GuiLoginPrincipal p = new GuiLoginPrincipal();
        p.setVisible(true);
    }

    private void openMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {
        abrirInternalFrame(new GuiRegistrarEquipoOficina(), "Error al abrir Registrar Equipos");
    }

    private void openMenu5ActionPerformed(java.awt.event.ActionEvent evt) {
        abrirInternalFrame(new GuiMantenimientoEquipos(), "Error al abrir Mantenimiento");
    }

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {
        abrirInternalFrame(new GuiSoftwareEquipo(), "Error al abrir registro");
    }

    private void bt_inventarioPiezasActionPerformed(java.awt.event.ActionEvent evt) {
        abrirInternalFrame(new GuiInventarioPiezas(), "Error al abrir Inventario de piezas");
    }

    private void abrirInternalFrame(javax.swing.JInternalFrame frame, String mensajeError) {
        try {
            desktopPane.add(frame);
            frame.setVisible(true);
            try {
                frame.setSelected(true);
            } catch (java.beans.PropertyVetoException ex) {
                // No critico, solo intento seleccionar la ventana.
            }
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    mensajeError + ": " + ex.toString(),
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new Principal().setVisible(true));
    }

    private javax.swing.JMenu bt_conductor;
    private javax.swing.JMenu bt_conductor1;
    private javax.swing.JMenuItem bt_inventarioPiezas;
    private javax.swing.JMenuItem bt_mantenimiento;
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JButton jButton1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openMenu5;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem openMenuItem1;
    private javax.swing.JMenuItem openMenuItem2;
    private javax.swing.JMenuItem openMenuItem3;
    private javax.swing.JMenuItem openMenuItem4;
}
