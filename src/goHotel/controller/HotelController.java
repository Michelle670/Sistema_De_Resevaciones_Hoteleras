package goHotel.controller;

import goHotel.model.DAO.HotelDAO;
import goHotel.model.Hotel;
import goHotel.view.GestionHoteles;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

//******************************************************************************
/**
 * AUTOR: GRUPO 3 PROYECTO SEMANA 9
 */
//<<<<<<< HEAD
//public class HotelController implements ActionListener {
//    
//    private final Hotel modelo;
//    private final HotelDAO consultas;
//    private final GestionHoteles vista;
//
//    public HotelController(Hotel modelo, HotelDAO consultas, GestionHoteles vista) {
//        this.modelo = modelo;
//        this.consultas = consultas;
//        this.vista = vista;
//        this.vista.btnAgregar.addActionListener(this);
//        this.vista.btnEditar.addActionListener(this);
//        this.vista.btnBuscar.addActionListener(this);
//        this.vista.btnEliminar.addActionListener(this);
//        this.vista.btnLimpiar.addActionListener(this);
//        this.vista.btnSalir.addActionListener(this);
//        
//        actualizarTabla();
//    }
//
//    public void iniciar() {
//        vista.setTitle("Gestión de Hoteles");
//        vista.setLocationRelativeTo(null);
//        vista.setVisible(true);
//        vista.actualizarTabla();
//    }
//
//    public void limpiar() {
//        vista.txtCodigo.setText("");
//        vista.txtNombre.setText("");
//        vista.jcPais.setSelectedIndex(0);
//        vista.txtCiudad.setText("");
//        vista.txtDireccion.setText("");
//        vista.txtCodigo.requestFocus();
//    }
//
//    public void actualizarTabla() {
//        DefaultTableModel modelo = (DefaultTableModel) vista.jtGestionHoteles.getModel();
//        consultas.cargarDatosEnTabla(modelo, null);
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        //==========================================================================
//        // BOTÓN AGREGAR
//        //==========================================================================
//        if (e.getSource() == vista.btnAgregar) {
//            System.out.println("Es el botón AGREGAR");
//            int idPais = vista.jcPais.getSelectedIndex() + 1;
//
//            if (consultas.registrarHotel(
//                    vista.txtCodigo.getText(),
//                    vista.txtNombre.getText(),
//                    idPais,
//                    vista.txtCiudad.getText(),
//                    vista.txtDireccion.getText()
//            )) {
//                vista.limpiarCampos();
//                vista.actualizarTabla();
//            }
//        }
//        //==========================================================================
//        // BOTÓN EDITAR
//        //==========================================================================
//        if (e.getSource() == vista.btnEditar) {
//            int idPais = vista.jcPais.getSelectedIndex() + 1;
//
//            if (consultas.editarHotel(
//                    vista.txtCodigo.getText(),
//                    vista.txtNombre.getText(),
//                    idPais,
//                    vista.txtCiudad.getText(),
//                    vista.txtDireccion.getText()
//            )) {
//                vista.limpiarCampos();
//                vista.actualizarTabla();
//            }
//        }
//        //==========================================================================
//        // BOTÓN BUSCAR
//        //==========================================================================
//        if (e.getSource() == vista.btnBuscar) {
//            String codigo = vista.txtCodigo.getText().trim();
//
//            if (codigo.isEmpty()) {
//                JOptionPane.showMessageDialog(vista, "Ingrese un código para buscar", "Advertencia", JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//
//            Hotel hotelEncontrado = consultas.buscarHotel(codigo);
//
//            if (hotelEncontrado != null) {
//                vista.txtNombre.setText(hotelEncontrado.getNombreHotel());
//                vista.txtCiudad.setText(hotelEncontrado.getCiudad());
//                vista.txtDireccion.setText(hotelEncontrado.getDireccion());
//
//                // Seleccionar el país por ID (ajustando el índice)
//                // Como el índice 0 es "--- Ninguno ---", el país con id_pais=1 está en índice 1
//                vista.jcPais.setSelectedIndex(hotelEncontrado.getIdPais());
//
//                // Filtrar la tabla
//                DefaultTableModel modelo = (DefaultTableModel) vista.jtGestionHoteles.getModel();
//                consultas.cargarDatosEnTabla(modelo, codigo);
//            } else {
//                JOptionPane.showMessageDialog(vista, "Hotel no encontrado", "Sin resultados", JOptionPane.WARNING_MESSAGE);
//                limpiar();
//                actualizarTabla();
//            }
//        }
//        //==========================================================================
//        // BOTÓN ELIMINAR
//        //==========================================================================
//        if (e.getSource() == vista.btnEliminar) {
//
//            try {
//                int codigo = Integer.parseInt(vista.txtCodigo.getText());
//                if (consultas.eliminarHotel(codigo, vista)) {
//                    vista.limpiarCampos();
//                    vista.actualizarTabla();
//                }
//
//            } catch (NumberFormatException ex) {
//                JOptionPane.showMessageDialog(null, "Ingrese un código válido", "Error", JOptionPane.ERROR_MESSAGE);
//            }
//        }
//        //==========================================================================
//        // BOTÓN LIMPIAR
//        //==========================================================================
//        if (e.getSource() == vista.btnLimpiar) {
//            vista.limpiarCampos();
//            vista.actualizarTabla();
//        }
//
//        //==========================================================================
//        // BOTÓN SALIR
//        //==========================================================================
//        if (e.getSource() == vista.btnSalir) {
//            int confirmacion = JOptionPane.showConfirmDialog(null,
//                    "¿Está seguro de salir?", "Confirmar Salida", JOptionPane.YES_NO_OPTION);
//            if (confirmacion == JOptionPane.YES_OPTION) {
//                vista.dispose();
//            }
//
//        }
//
//    } 
//}
//=======
//******************************************************************************
public class HotelController implements ActionListener {

    private final Hotel modelo;
    private final HotelDAO consultas;
    private final GestionHoteles vista;

    public HotelController(Hotel modelo, HotelDAO consultas, GestionHoteles vista) {
        this.modelo = modelo;
        this.consultas = consultas;
        this.vista = vista;
        this.vista.btnAgregar.addActionListener(this);
        this.vista.btnEditar.addActionListener(this);
        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnLimpiar.addActionListener(this);
        this.vista.btnSalir.addActionListener(this);
    }

    public void iniciar() {
        vista.setTitle("Gestión de Hoteles");
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
        vista.actualizarTabla();
    }

    public void limpiar() {
        vista.txtCodigo.setText("");
        vista.txtNombre.setText("");
        vista.jcPais.setSelectedIndex(0);
        vista.txtCiudad.setText("");
        vista.txtDireccion.setText("");
        vista.txtCodigo.requestFocus();
    }

    public void actualizarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) vista.jtGestionHoteles.getModel();
        consultas.cargarDatosEnTabla(modelo, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //==========================================================================
        // BOTÓN AGREGAR
        //==========================================================================
        if (e.getSource() == vista.btnAgregar) {
            System.out.println("Es el botón AGREGAR");
            int idPais = vista.jcPais.getSelectedIndex() + 1;

            if (consultas.registrarHotel(
                    vista.txtCodigo.getText(),
                    vista.txtNombre.getText(),
                    idPais,
                    vista.txtCiudad.getText(),
                    vista.txtDireccion.getText()
            )) {
                vista.limpiarCampos();
                vista.actualizarTabla();
            }
        }
        //==========================================================================
        // BOTÓN EDITAR
        //==========================================================================
        if (e.getSource() == vista.btnEditar) {
            int idPais = vista.jcPais.getSelectedIndex() + 1;

            if (consultas.editarHotel(
                    vista.txtCodigo.getText(),
                    vista.txtNombre.getText(),
                    idPais,
                    vista.txtCiudad.getText(),
                    vista.txtDireccion.getText()
            )) {
                vista.limpiarCampos();
                vista.actualizarTabla();
            }
        }
        //==========================================================================
        // BOTÓN BUSCAR
        //==========================================================================
        if (e.getSource() == vista.btnBuscar) {
            String codigo = vista.txtCodigo.getText().trim();

            if (codigo.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Ingrese un código para buscar", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Hotel hotelEncontrado = consultas.buscarHotel(codigo);

            if (hotelEncontrado != null) {
                vista.txtNombre.setText(hotelEncontrado.getNombreHotel());
                vista.txtCiudad.setText(hotelEncontrado.getCiudad());
                vista.txtDireccion.setText(hotelEncontrado.getDireccion());

                // Seleccionar el país por ID (ajustando el índice)
                // Como el índice 0 es "--- Ninguno ---", el país con id_pais=1 está en índice 1
                vista.jcPais.setSelectedIndex(hotelEncontrado.getIdPais());

                // Filtrar la tabla
                DefaultTableModel modelo = (DefaultTableModel) vista.jtGestionHoteles.getModel();
                consultas.cargarDatosEnTabla(modelo, codigo);
            } else {
                JOptionPane.showMessageDialog(vista, "Hotel no encontrado", "Sin resultados", JOptionPane.WARNING_MESSAGE);
                limpiar();
                actualizarTabla();
            }
        }
        //==========================================================================
        // BOTÓN ELIMINAR
        //==========================================================================
        if (e.getSource() == vista.btnEliminar) {

            try {
                int codigo = Integer.parseInt(vista.txtCodigo.getText());
                if (consultas.eliminarHotel(codigo, vista)) {
                    vista.limpiarCampos();
                    vista.actualizarTabla();
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Ingrese un código válido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        //==========================================================================
        // BOTÓN LIMPIAR
        //==========================================================================
        if (e.getSource() == vista.btnLimpiar) {
            vista.limpiarCampos();
            vista.actualizarTabla();
        }

        //==========================================================================
        // BOTÓN SALIR
        //==========================================================================
        if (e.getSource() == vista.btnSalir) {
            int confirmacion = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro de salir?", "Confirmar Salida", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                vista.dispose();
            }

        }

    }
}