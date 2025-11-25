/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package goHotel.model;

/**
 *
 * @author soloa
 */
public enum EstadoHabitacion 
{
    DISPONIBLE("Disponible"),
    OCUPADA("Ocupada"),
    EN_LIMPIEZA("En limpieza"),
    FUERA_DE_SERVICIO("Fuera de servicio");

    private final String label;

    EstadoHabitacion(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
