/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.mycompany.transporteshirata.GUI;

import com.mycompany.transporteshirata.Datos.PiezaInventarioDao;
import com.mycompany.transporteshirata.Logica.PiezaInventario;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author pccas
 */
public class GuiInventarioPiezas extends javax.swing.JInternalFrame {

    private final PiezaInventarioDao piezaDao = new PiezaInventarioDao();
    private final JTable tblPiezas = new JTable();
    private final JTextField txtId = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtDescripcion = new JTextField();
    private final JTextField txtCantidad = new JTextField();
    private final JTextField txtStockMinimo = new JTextField();
    private final JTextField txtUbicacion = new JTextField();
    private final JTextField txtCantidadUsada = new JTextField();
    private final JButton btGuardar = new JButton("Guardar");
    private final JButton btEditar = new JButton("Editar");
    private final JButton btEliminar = new JButton("Eliminar");
    private final JButton btCancelar = new JButton("Cancelar");
    private final JButton btUsar = new JButton("Registrar uso");

    public GuiInventarioPiezas() {
        initPantalla();
        cargarTabla();
        cambiarAModoNuevo();
    }

    private void initPantalla() {
        setClosable(true);
        setTitle("Inventario de piezas");
        setPreferredSize(new Dimension(980, 560));

        txtId.setEditable(false);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos de la pieza"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        agregarCampo(panelFormulario, gbc, 0, "ID", txtId);
        agregarCampo(panelFormulario, gbc, 1, "Nombre", txtNombre);
        agregarCampo(panelFormulario, gbc, 2, "Descripcion", txtDescripcion);
        agregarCampo(panelFormulario, gbc, 3, "Cantidad", txtCantidad);
        agregarCampo(panelFormulario, gbc, 4, "Stock minimo", txtStockMinimo);
        agregarCampo(panelFormulario, gbc, 5, "Ubicacion", txtUbicacion);
        agregarCampo(panelFormulario, gbc, 6, "Cantidad usada", txtCantidadUsada);

        JPanel panelBotones = new JPanel(new GridBagLayout());
        GridBagConstraints gbcBoton = new GridBagConstraints();
        gbcBoton.insets = new Insets(4, 4, 4, 4);
        gbcBoton.fill = GridBagConstraints.HORIZONTAL;
        gbcBoton.gridx = 0;
        gbcBoton.weightx = 1;

        JButton[] botones = {btGuardar, btEditar, btEliminar, btUsar, btCancelar};
        for (int i = 0; i < botones.length; i++) {
            gbcBoton.gridy = i;
            panelBotones.add(botones[i], gbcBoton);
        }

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        panelFormulario.add(panelBotones, gbc);

        tblPiezas.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Nombre", "Descripcion", "Cantidad", "Stock minimo", "Ubicacion", "Estado"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        tblPiezas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                seleccionarPieza();
            }
        });

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Piezas registradas"));
        panelTabla.add(new JScrollPane(tblPiezas), BorderLayout.CENTER);

        JPanel contenido = new JPanel(new BorderLayout(8, 8));
        contenido.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        contenido.add(panelFormulario, BorderLayout.WEST);
        contenido.add(panelTabla, BorderLayout.CENTER);
        setContentPane(contenido);

        btGuardar.addActionListener(e -> guardarPieza());
        btEditar.addActionListener(e -> editarPieza());
        btEliminar.addActionListener(e -> eliminarPieza());
        btUsar.addActionListener(e -> registrarUso());
        btCancelar.addActionListener(e -> cambiarAModoNuevo());

        pack();
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, JTextField campo) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        panel.add(new JLabel(etiqueta), gbc);

        campo.setPreferredSize(new Dimension(180, 26));
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(campo, gbc);
    }

    private void cargarTabla() {
        String[] columnas = {"ID", "Nombre", "Descripcion", "Cantidad", "Stock minimo", "Ubicacion", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (PiezaInventario pieza : piezaDao.listarPiezas()) {
            String estado = pieza.getCantidad() <= pieza.getStockMinimo() ? "Bajo stock" : "Disponible";
            Object[] fila = {
                pieza.getIdPieza(),
                pieza.getNombre(),
                pieza.getDescripcion(),
                pieza.getCantidad(),
                pieza.getStockMinimo(),
                pieza.getUbicacion(),
                estado
            };
            modelo.addRow(fila);
        }
        tblPiezas.setModel(modelo);
    }

    private void seleccionarPieza() {
        int fila = tblPiezas.getSelectedRow();
        if (fila == -1) {
            return;
        }

        txtId.setText(tblPiezas.getValueAt(fila, 0).toString());
        txtNombre.setText(tblPiezas.getValueAt(fila, 1).toString());
        txtDescripcion.setText(valorTabla(fila, 2));
        txtCantidad.setText(tblPiezas.getValueAt(fila, 3).toString());
        txtStockMinimo.setText(tblPiezas.getValueAt(fila, 4).toString());
        txtUbicacion.setText(valorTabla(fila, 5));
        txtCantidadUsada.setText("");
        cambiarAModoEdicion();
    }

    private String valorTabla(int fila, int columna) {
        Object valor = tblPiezas.getValueAt(fila, columna);
        return valor == null ? "" : valor.toString();
    }

    private void guardarPieza() {
        PiezaInventario pieza = leerFormulario(false);
        if (pieza == null) {
            return;
        }

        if (piezaDao.registrarPieza(pieza)) {
            JOptionPane.showMessageDialog(this, "Pieza registrada correctamente.");
            cargarTabla();
            cambiarAModoNuevo();
        }
    }

    private void editarPieza() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una pieza de la tabla para editar.");
            return;
        }

        PiezaInventario pieza = leerFormulario(true);
        if (pieza == null) {
            return;
        }

        if (piezaDao.modificarPieza(pieza)) {
            JOptionPane.showMessageDialog(this, "Pieza actualizada correctamente.");
            cargarTabla();
            cambiarAModoNuevo();
        }
    }

    private void eliminarPieza() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una pieza de la tabla para eliminar.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "Esta seguro de eliminar esta pieza?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            int idPieza = Integer.parseInt(txtId.getText());
            if (piezaDao.eliminarPieza(idPieza)) {
                JOptionPane.showMessageDialog(this, "Pieza eliminada correctamente.");
                cargarTabla();
                cambiarAModoNuevo();
            }
        }
    }

    private void registrarUso() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una pieza de la tabla para registrar su uso.");
            return;
        }

        try {
            int idPieza = Integer.parseInt(txtId.getText());
            int cantidadUsada = Integer.parseInt(txtCantidadUsada.getText().trim());

            if (cantidadUsada <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad usada debe ser mayor a 0.");
                return;
            }

            if (piezaDao.descontarStock(idPieza, cantidadUsada)) {
                JOptionPane.showMessageDialog(this, "Uso registrado y stock actualizado.");
                cargarTabla();
                cambiarAModoNuevo();
            } else {
                JOptionPane.showMessageDialog(this, "No hay stock suficiente para registrar ese uso.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad usada valida.");
        }
    }

    private PiezaInventario leerFormulario(boolean incluirId) {
        String nombre = txtNombre.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String ubicacion = txtUbicacion.getText().trim();

        if (nombre.isEmpty() || txtCantidad.getText().trim().isEmpty() || txtStockMinimo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete nombre, cantidad y stock minimo.");
            return null;
        }

        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            int stockMinimo = Integer.parseInt(txtStockMinimo.getText().trim());

            if (cantidad < 0 || stockMinimo < 0) {
                JOptionPane.showMessageDialog(this, "Cantidad y stock minimo no pueden ser negativos.");
                return null;
            }

            PiezaInventario pieza = new PiezaInventario();
            if (incluirId) {
                pieza.setIdPieza(Integer.parseInt(txtId.getText()));
            }
            pieza.setNombre(nombre);
            pieza.setDescripcion(descripcion);
            pieza.setCantidad(cantidad);
            pieza.setStockMinimo(stockMinimo);
            pieza.setUbicacion(ubicacion);
            return pieza;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad y stock minimo deben ser numeros validos.");
            return null;
        }
    }

    private void cambiarAModoEdicion() {
        btGuardar.setEnabled(false);
        btEditar.setEnabled(true);
        btEliminar.setEnabled(true);
        btUsar.setEnabled(true);
        btCancelar.setEnabled(true);
    }

    private void cambiarAModoNuevo() {
        limpiarFormulario();
        tblPiezas.clearSelection();
        btGuardar.setEnabled(true);
        btEditar.setEnabled(false);
        btEliminar.setEnabled(false);
        btUsar.setEnabled(false);
        btCancelar.setEnabled(false);
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtCantidad.setText("");
        txtStockMinimo.setText("");
        txtUbicacion.setText("");
        txtCantidadUsada.setText("");
    }
}
