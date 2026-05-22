/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transporteshirata.Datos;
import com.mycompany.transporteshirata.Logica.MantenimientoEquipoOficina;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
/**
 *
 * @author Nicolas
 */
public class MantenimientoEquipoDao {
    private static final List<MantenimientoEquipoOficina> lista = new ArrayList<>();
    private static final AtomicInteger SEQ = new AtomicInteger(1);

    public void insertar(MantenimientoEquipoOficina m) {
        m.setIdMantenimiento(SEQ.getAndIncrement());
        lista.add(m);
    }

    public List<MantenimientoEquipoOficina> listarTodos() {
        return new ArrayList<>(lista);
    }

    public List<MantenimientoEquipoOficina> listarPorEquipoId(int idEquipo) {
        List<MantenimientoEquipoOficina> res = new ArrayList<>();
        for (MantenimientoEquipoOficina m : lista) {
            if (m.getEquipo() != null && m.getEquipo().getIdEquipo() == idEquipo) {
                res.add(m);
            }
        }
        return res;
    }
}

