/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transporteshirata.Datos;
import com.mycompany.transporteshirata.Logica.EquipoOficina;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nicolas
 */
public class EquipoOficinaDao {
    // Lista en memoria que simula la tabla EquipoOficina
    private static List<EquipoOficina> listaEquipos = new ArrayList<>();
    private static int contadorId = 1;

    public void insertarEquipo(EquipoOficina equipo) {
        equipo.setIdEquipo(contadorId++);
        listaEquipos.add(equipo);
    }

    public List<EquipoOficina> listarEquipos() {
        return new ArrayList<>(listaEquipos);
    }

    public void eliminarEquipo(int id) {
        listaEquipos.removeIf(eq -> eq.getIdEquipo() == id);
    }

    public void actualizarEquipo(EquipoOficina equipoActualizado) {
        for (int i = 0; i < listaEquipos.size(); i++) {
            if (listaEquipos.get(i).getIdEquipo() == equipoActualizado.getIdEquipo()) {
                listaEquipos.set(i, equipoActualizado);
                break;
            }
        }
    }
}
