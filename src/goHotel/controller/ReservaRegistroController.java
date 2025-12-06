package goHotel.controller;

import goHotel.model.DAO.ReservaDAO;
import goHotel.model.Reserva;
import goHotel.model.ReservaEstado;
import goHotel.view.RegistroReserva;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ReservaRegistroController implements ActionListener 
{
    private final Reserva modelo;
    private final ReservaDAO consultas;
    private final RegistroReserva vista;
    private final String accion;
    private final String correoUsuario;
    private final int numHabitacion;
    private final String hotelNombre;

    public ReservaRegistroController(Reserva modelo, ReservaDAO consultas, 
                                     RegistroReserva vista, String accion,
                                     String correoUsuario, int numHabitacion, 
                                     String hotelNombre)
    {
        this.modelo = modelo;
        this.consultas = consultas;
        this.vista = vista;
        this.accion = accion;
        this.correoUsuario = correoUsuario;
        this.numHabitacion = numHabitacion;
        this.hotelNombre = hotelNombre;
        
        vista.txtCodigoCliente.addFocusListener(new java.awt.event.FocusAdapter() 
        {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) 
            {
                cargarNombreCliente();
            }
        });
        
        vista.txtCodigoHotel.addFocusListener(new java.awt.event.FocusAdapter() 
        {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) 
            {
                cargarNombreHotel();
            }
        });

        vista.txtNumHabitacion.addFocusListener(new java.awt.event.FocusAdapter() 
        {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) 
            {
                cargarDescripcionHabitacion();
            }
        });

        vista.btnGuardar.addActionListener(this);
        vista.btnCancelar.addActionListener(this);
    }
    
    public void iniciar() 
    {
        vista.setTitle("Registro de Reserva");
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
        vista.jlAccion.setText(this.accion + " Registro");
        cargarEstadosReserva();
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource() == vista.btnGuardar) 
        { 
            if (validarFechasYHoras()) {
                // Aquí va tu lógica de guardar
                JOptionPane.showMessageDialog(vista, 
                    "Validaciones pasadas correctamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
        if (e.getSource() == vista.btnCancelar) 
        { 
            vista.dispose();
        }
    }
    
    //==========================================================================
    // VALIDACIONES DE FECHAS Y HORAS
    //==========================================================================
    
    private boolean validarFechasYHoras() 
    {
        // Obtener fechas de JDateChooser
        Date dateEntrada = vista.jDateFechaEntrada.getDate();
        Date dateSalida = vista.jDateFechaSalida.getDate();
        
        // Obtener horas de TimePicker
        String horaEntrada = vista.timePickerEntrada.getText().trim();
        String horaSalida = vista.timePickerSalida.getText().trim();
        
        // 1. Validar que las fechas no sean nulas
        if (dateEntrada == null || dateSalida == null) {
            JOptionPane.showMessageDialog(vista, 
                "Debe seleccionar las fechas de entrada y salida", 
                "Fechas Faltantes", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // 2. Validar que las horas no estén vacías
        if (horaEntrada.isEmpty() || horaSalida.isEmpty()) {
            JOptionPane.showMessageDialog(vista, 
                "Debe seleccionar las horas de entrada y salida", 
                "Horas Faltantes", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Convertir Date a LocalDate
        LocalDate fechaEntrada = dateEntrada.toInstant()
            .atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaSalida = dateSalida.toInstant()
            .atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate hoy = LocalDate.now();
        
        // 3. No permitir fechas pasadas
        if (fechaEntrada.isBefore(hoy)) {
            JOptionPane.showMessageDialog(vista, 
                "La fecha de entrada no puede ser anterior a hoy", 
                "Fecha Inválida", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // 4. Fecha salida debe ser >= fecha entrada
        if (fechaSalida.isBefore(fechaEntrada)) {
            JOptionPane.showMessageDialog(vista, 
                "La fecha de salida no puede ser anterior a la fecha de entrada", 
                "Fechas Inválidas", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // 5. Validar formato de horas y convertir
        LocalTime timeEntrada;
        LocalTime timeSalida;
        
        try {
            timeEntrada = LocalTime.parse(horaEntrada);
            timeSalida = LocalTime.parse(horaSalida);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, 
                "Formato de hora inválido. Use HH:mm (ejemplo: 14:00)", 
                "Error en Horas", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // 6. Si es el MISMO DÍA, hora salida debe ser > hora entrada
        if (fechaEntrada.equals(fechaSalida)) {
            if (!timeSalida.isAfter(timeEntrada)) {
                JOptionPane.showMessageDialog(vista, 
                    "Si la entrada y salida son el mismo día, " +
                    "la hora de salida debe ser posterior a la hora de entrada", 
                    "Horas Inválidas", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            // Validar mínimo 1 hora
            long minutos = ChronoUnit.MINUTES.between(timeEntrada, timeSalida);
            if (minutos < 60) {
                int respuesta = JOptionPane.showConfirmDialog(vista, 
                    "La estadía es de " + minutos + " minutos (menos de 1 hora). " +
                    "¿Desea continuar?", 
                    "Advertencia", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                    
                if (respuesta != JOptionPane.YES_OPTION) {
                    return false;
                }
            }
        }
        
        // 7. Si la entrada es HOY, la hora no puede ser pasada
        if (fechaEntrada.equals(hoy)) {
            LocalTime horaActual = LocalTime.now();
            
            if (timeEntrada.isBefore(horaActual)) {
                JOptionPane.showMessageDialog(vista, 
                    "La hora de entrada no puede ser anterior a la hora actual (" + 
                    String.format("%02d:%02d", horaActual.getHour(), horaActual.getMinute()) + ")", 
                    "Hora Inválida", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        // 8. Límite de anticipación (1 año máximo)
        LocalDate fechaMaxima = hoy.plusYears(1);
        if (fechaEntrada.isAfter(fechaMaxima)) {
            JOptionPane.showMessageDialog(vista, 
                "No se pueden hacer reservas con más de 1 año de anticipación", 
                "Anticipación Excedida", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // 9. Estadía máxima (30 días)
        long dias = ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
        if (dias > 30) {
            JOptionPane.showMessageDialog(vista, 
                "La estadía máxima es de 30 días", 
                "Estadía Excedida", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // 10. Advertencia si es inmediato (menos de 2 horas)
        if (fechaEntrada.equals(hoy)) {
            LocalTime en2Horas = LocalTime.now().plusHours(2);
            
            if (timeEntrada.isBefore(en2Horas)) {
                int respuesta = JOptionPane.showConfirmDialog(vista, 
                    "Reserva con menos de 2 horas de anticipación. ¿Continuar?", 
                    "Confirmación", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                    
                if (respuesta != JOptionPane.YES_OPTION) {
                    return false;
                }
            }
        }
        
        // 11. Validar horarios de check-in (14:00) y check-out (12:00)
        if (timeEntrada.isBefore(LocalTime.of(14, 0))) {
            int respuesta = JOptionPane.showConfirmDialog(vista, 
                "El check-in regular es desde las 14:00. " +
                "¿Desea solicitar early check-in?", 
                "Early Check-in", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
            if (respuesta != JOptionPane.YES_OPTION) {
                return false;
            }
        }
        
        if (timeSalida.isAfter(LocalTime.of(12, 0))) {
            int respuesta = JOptionPane.showConfirmDialog(vista, 
                "El check-out regular es hasta las 12:00. " +
                "¿Desea solicitar late check-out?", 
                "Late Check-out", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
            if (respuesta != JOptionPane.YES_OPTION) {
                return false;
            }
        }
        
        return true;
    }
    
    //==========================================================================
    // MÉTODOS DE CARGA (sin cambios)
    //==========================================================================
    
    private void cargarNombreCliente() 
    {
        String texto = vista.txtCodigoCliente.getText().trim();
        if (texto.isEmpty()) {
            vista.jlNombreCliente.setText("");
            return;
        }
        try {
            int codigo = Integer.parseInt(texto);
            if (consultas.buscarClientePorCodigo(codigo, modelo)) {
                vista.jlNombreCliente.setText(modelo.getNombreCliente());
            } else {
                vista.jlNombreCliente.setText("No encontrado");
            }
        } catch (NumberFormatException e) {
            vista.jlNombreCliente.setText("Inválido");
        }
    }
     
    private void cargarNombreHotel() 
    {
        String texto = vista.txtCodigoHotel.getText().trim();
        if (texto.isEmpty()) {
            vista.jlNombreHotel.setText("");
            return;
        }
        try {
            int codigo = Integer.parseInt(texto);
            if (consultas.buscarHotelPorCodigo(codigo, modelo)) {
                vista.jlNombreHotel.setText(modelo.getNombreHotel());
            } else {
                vista.jlNombreHotel.setText("No encontrado");
            }
        } catch (NumberFormatException e) {
            vista.jlNombreHotel.setText("Inválido");
        }
    }

private void cargarDescripcionHabitacion() 
{
    String numTxt = vista.txtNumHabitacion.getText().trim();
    String hotelTxt = vista.txtCodigoHotel.getText().trim();

    if (numTxt.isEmpty() || hotelTxt.isEmpty()) {
        vista.jlNombreHabitacion.setText("");
        // Limpiar tabla de servicios
        DefaultTableModel modelo = (DefaultTableModel) vista.jtNombreServicios.getModel();
        modelo.setRowCount(0);
        return;
    }

    try {
        int numero = Integer.parseInt(numTxt);
        int idHotel = Integer.parseInt(hotelTxt);

        if (consultas.buscarDescripcionHabitacion(idHotel, numero, modelo)) {
            vista.jlNombreHabitacion.setText(modelo.getDescripcionHabitacion());
            
            // CARGAR SERVICIOS EN LA TABLA
            DefaultTableModel modeloTabla = (DefaultTableModel) vista.jtNombreServicios.getModel();
            consultas.cargarServiciosHabitacion(modeloTabla, idHotel, numero);
            
        } else {
            vista.jlNombreHabitacion.setText("No existe");
            // Limpiar tabla de servicios
            DefaultTableModel modeloTabla = (DefaultTableModel) vista.jtNombreServicios.getModel();
            modeloTabla.setRowCount(0);
        }

    } catch (NumberFormatException e) {
        vista.jlNombreHabitacion.setText("Inválido");
        // Limpiar tabla de servicios
        DefaultTableModel modeloTabla = (DefaultTableModel) vista.jtNombreServicios.getModel();
        modeloTabla.setRowCount(0);
    }
}

private void cargarEstadosReserva() 
{
    // Limpia el combo
    vista.jcbEstadoReserva.removeAllItems();

    // Opción manual
    vista.jcbEstadoReserva.addItem("--- Ninguno ---");

    // Cargar todos los valores del enum
    for (ReservaEstado estado : ReservaEstado.values()) 
    {
        vista.jcbEstadoReserva.addItem(estado.name());
    }
}   
}

        
    
    

