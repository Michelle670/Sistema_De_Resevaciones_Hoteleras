package goHotel.controller;
import goHotel.model.DAO.ReservaDAO;
import goHotel.model.Reserva;
import goHotel.model.Servicio;
import goHotel.view.RegistroReserva;
import goHotel.view.ReservaBuscarHabitacion;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ReservaBusquedaController implements ActionListener 
{

    private final Reserva modelo;
    private final ReservaDAO consultas;
    private final ReservaBuscarHabitacion vista;
    private final String correoUsuario;
    
     private int filaSeleccionada = -1;

    public ReservaBusquedaController(Reserva modelo, ReservaDAO consultas, ReservaBuscarHabitacion vista, String correoUsuario)
    {
        this.modelo = modelo;
        this.consultas = consultas;
        this.vista = vista;
        this.correoUsuario = correoUsuario;
        inicializarTablaServicios();

        // LISTENERS
        vista.cbNombresHoteles.addActionListener(this);
        vista.cbServicios.addActionListener(this);
        vista.btnBuscar.addActionListener(this);
        vista.btnAgregar.addActionListener(this);
        vista.btnQuitar.addActionListener(this);
        vista.btnReservar.addActionListener(this);
        vista.btnLimpiar.addActionListener(this);
        vista.btnCancelar.addActionListener(this);
    }

    // ============================
    // INICIAR VISTA
    // ============================
    public void iniciar() {
        vista.setTitle("Búsqueda de Habitación");
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);

        consultas.cargarHoteles(vista.cbNombresHoteles);
        consultas.cargarServicios(vista.cbServicios);
        inicializarModeloVacíoTabla();
        actualizarLineasTabla();
    }

    // ============================
    // TABLA SERVICIOS
    // ============================
    private void inicializarTablaServicios() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Descripción"}, 0
        );
        limpiarTablaInicial();
        vista.jtServicios.setModel(modelo);
        actualizarCantidadRegistros();
        
    }

    // ============================
    // CARGA INICIAL DE HABITACIONES
    // ============================
    private void cargarTablaInicial() {
        DefaultTableModel modelo = (DefaultTableModel) vista.jtBusquedaHabitacion.getModel();
        consultas.cargarHabitaciones(modelo);
        limpiarTablaInicial();
        inicializarModeloVacíoTabla();
        actualizarLineasTabla();
        
    }

    // ============================
    // BOTONES
    // ============================
    @Override
    public void actionPerformed(ActionEvent e) {

        // ======================
        // BOTÓN BUSCAR
        // ======================
        if (e.getSource() == vista.btnBuscar) {

            String hotel = vista.cbNombresHoteles.getSelectedItem().toString();
            Date fechaEntrada = vista.jdateFechaEntrada.getDate();
            Date fechaSalida = vista.jdateFechaSalida.getDate();
            int numPersonas = (Integer) vista.jsNumPersonas.getValue();

            // VALIDACIONES
            if (fechaEntrada != null & fechaSalida != null) 
            {
                if (fechaSalida.before(fechaEntrada)) 
                {
                    JOptionPane.showMessageDialog(null, "La fecha de salida no puede ser antes que la de entrada");
                    return;
                } 
            }
            
        // VALIDACIÓN: Fecha de entrada no puede ser en el pasado
        if (fechaEntrada != null) 
        {
            // Crear fecha actual SIN hora (00:00:00)
            Date hoy = new Date(); 
            hoy = java.sql.Date.valueOf(
                    new java.text.SimpleDateFormat("yyyy-MM-dd")
                    .format(hoy)
            );

            if (fechaEntrada.before(hoy)) 
            {
                JOptionPane.showMessageDialog(null, 
                     "La fecha de entrada no puede ser una fecha pasada");
                return;
            }
        }


            // OBTENER SERVICIOS
            List<String> serviciosSeleccionados = new ArrayList<>();
            DefaultTableModel modServ = (DefaultTableModel) vista.jtServicios.getModel();

            for (int i = 0; i < modServ.getRowCount(); i++) {
                serviciosSeleccionados.add(modServ.getValueAt(i, 0).toString());
            }

            // LLAMAR DAO
            DefaultTableModel resultado = consultas.buscarHabitaciones(
                    hotel,
                    fechaEntrada,
                    fechaSalida,
                    numPersonas,
                    serviciosSeleccionados
            );

            vista.jtBusquedaHabitacion.setModel(resultado);
            actualizarCantidadRegistros();
            return;
        }

        // ======================
        // BOTÓN AGREGAR SERVICIO
        // ======================
        if (e.getSource() == vista.btnAgregar) {

            String nombreServicio = vista.cbServicios.getSelectedItem().toString();

            if (nombreServicio.equals("--- Ninguno ---")) {
//                JOptionPane.showMessageDialog(null, "Seleccione un servicio válido");
                return;
            }

            Servicio s = consultas.obtenerServicioPorNombre(nombreServicio);

            if (s == null) {
                JOptionPane.showMessageDialog(null, "Error: servicio no encontrado");
                return;
            }

            DefaultTableModel modelo = (DefaultTableModel) vista.jtServicios.getModel();

            // EVITAR DUPLICADOS
            for (int i = 0; i < modelo.getRowCount(); i++) {
                if ((int) modelo.getValueAt(i, 0) == s.getIdServicio()) {
                    JOptionPane.showMessageDialog(null, "Este servicio ya está agregado");
                    return;
                }
            }

            // AGREGAR FILA
            modelo.addRow(new Object[]{
                s.getIdServicio(),
                s.getNombreServicio(),
                s.getDescripcion()
            });

            return;
        }

        // ======================
        // BOTÓN QUITAR SERVICIO
        // ======================
        if (e.getSource() == vista.btnQuitar) {
            DefaultTableModel modelo = (DefaultTableModel) vista.jtServicios.getModel();

            int fila = vista.jtServicios.getSelectedRow();

            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione un servicio para quitar");
                return;
            }

            modelo.removeRow(fila);
            return;
        }

        // ======================
        // BOTÓN LIMPIAR
        // ======================
        if (e.getSource() == vista.btnLimpiar) {

            vista.cbNombresHoteles.setSelectedIndex(0);
            vista.cbServicios.setSelectedIndex(0);
            vista.jdateFechaEntrada.setDate(null);
            vista.jdateFechaSalida.setDate(null);
            vista.jsNumPersonas.setValue(1);

            ((DefaultTableModel) vista.jtServicios.getModel()).setRowCount(0);

            inicializarModeloVacíoTabla();
            actualizarLineasTabla();
            actualizarCantidadRegistros();
            return;
        }

        // ======================
        // BOTÓN CANCELAR
        // ======================
        if (e.getSource() == vista.btnCancelar) 
        {
            vista.dispose();
            return;
        }

        // ======================
        // BOTÓN RESERVAR 
        // ======================
        if (e.getSource() == vista.btnReservar) 
        {
          filaSeleccionada = vista.jtBusquedaHabitacion.getSelectedRow();
            
            if (filaSeleccionada == -1) 
            {
                JOptionPane.showMessageDialog(null, 
                    "Por favor seleccione una habitación de la tabla");
                return;
            }
            
            Reserva modelo = new Reserva();
            ReservaDAO consultas = new ReservaDAO();
            RegistroReserva vista = new RegistroReserva();
            ReservaRegistroController control = new ReservaRegistroController(modelo, consultas, vista,"Agregar",correoUsuario,0,null);
            control.iniciar();
        }
        }
    
    private void actualizarLineasTabla() {
    DefaultTableModel modelo = (DefaultTableModel) vista.jtBusquedaHabitacion.getModel();

    if (modelo.getRowCount() == 0) {
        // Tabla vacía → sin líneas
        vista.jtBusquedaHabitacion.setShowGrid(false);
        vista.jtBusquedaHabitacion.setIntercellSpacing(new Dimension(0, 0));
    } else {
        // Tabla con datos → mostrar líneas
        vista.jtBusquedaHabitacion.setShowGrid(true);
        vista.jtBusquedaHabitacion.setIntercellSpacing(new Dimension(1, 1));
    }
}
    
    private void inicializarModeloVacíoTabla() {
    String[] columnas = {"Hotel", "Habitación", "Tipo", "Capacidad", "Precio Base"};
    DefaultTableModel modeloVacio = new DefaultTableModel(columnas, 0);
    vista.jtBusquedaHabitacion.setModel(modeloVacio);

    // Al inicio no mostrar líneas
    vista.jtBusquedaHabitacion.setShowGrid(false);
    vista.jtBusquedaHabitacion.setIntercellSpacing(new Dimension(0, 0));
    // Opcional: que llene el viewport para que se vea el rectángulo grande
    vista.jtBusquedaHabitacion.setFillsViewportHeight(true);
}
    
    private void actualizarCantidadRegistros() 
    {
    int filas = vista.jtBusquedaHabitacion.getRowCount();
    vista.jlCantidadRegistros.setText("Registros: " + filas);
}
    
    private void limpiarTablaInicial() {
    DefaultTableModel modelo = (DefaultTableModel) vista.jtBusquedaHabitacion.getModel();
    modelo.setRowCount(0); // elimina las filas "dummy" de NetBeans
}



}
