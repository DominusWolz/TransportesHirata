package com.mycompany.transporteshirata.GUI;

import com.mycompany.transporteshirata.Datos.MantenimientoEquipoDao;
import com.mycompany.transporteshirata.Logica.MantenimientoEquipoOficina;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class GuiHistorialMantenimientoEquipos extends JInternalFrame {

    private final MantenimientoEquipoDao mantenimientoDao = new MantenimientoEquipoDao();
    private final JTable tblHistorial = new JTable();
    private final JTextField txtFiltro = new JTextField(22);

    public GuiHistorialMantenimientoEquipos() {
        setTitle("Historial de mantenimiento de hardware");
        setClosable(true);
        setResizable(true);
        setMaximizable(true);
        setIconifiable(true);
        setPreferredSize(new Dimension(1050, 560));
        initPantalla();
        cargarTabla("");
        pack();
    }

    private void initPantalla() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JButton btBuscar = new JButton("Buscar");
        JButton btVerTodos = new JButton("Ver todos");
        barra.add(new JLabel("Equipo, tipo o repuesto"));
        barra.add(txtFiltro);
        barra.add(btBuscar);
        barra.add(btVerTodos);

        tblHistorial.setAutoCreateRowSorter(true);
        tblHistorial.setFillsViewportHeight(true);

        JPanel contenido = new JPanel(new BorderLayout(8, 8));
        contenido.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        contenido.add(barra, BorderLayout.NORTH);
        contenido.add(new JScrollPane(tblHistorial), BorderLayout.CENTER);
        setContentPane(contenido);

        btBuscar.addActionListener(e -> cargarTabla(txtFiltro.getText().trim()));
        btVerTodos.addActionListener(e -> {
            txtFiltro.setText("");
            cargarTabla("");
        });
    }

    private void cargarTabla(String filtro) {
        String[] columnas = {
            "ID", "Fecha", "Tipo", "Descripcion", "Observaciones",
            "Equipo", "Estado", "Repuesto usado", "Cant."
        };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<MantenimientoEquipoOficina> lista = mantenimientoDao.listarMantenimientos();
        String filtroNormalizado = filtro == null ? "" : filtro.toLowerCase();
        for (MantenimientoEquipoOficina m : lista) {
            String equipo = m.getEquipo() != null ? m.getEquipo().getNombre() : "";
            String estado = m.getEquipo() != null ? m.getEquipo().getEstado() : "";
            String pieza = m.getPiezaUsada() != null ? m.getPiezaUsada().getNombre() : "Sin repuesto";

            if (!filtroNormalizado.isEmpty()
                    && !equipo.toLowerCase().contains(filtroNormalizado)
                    && !m.getTipo().toLowerCase().contains(filtroNormalizado)
                    && !pieza.toLowerCase().contains(filtroNormalizado)) {
                continue;
            }

            modelo.addRow(new Object[]{
                m.getIdMantenimiento(),
                m.getFecha() != null ? m.getFecha().toString() : "",
                m.getTipo(),
                m.getDescripcion(),
                m.getObservaciones(),
                equipo,
                estado,
                pieza,
                m.getCantidadPiezaUsada() > 0 ? m.getCantidadPiezaUsada() : ""
            });
        }
        tblHistorial.setModel(modelo);
    }
}
