package goHotel.controller;

import goHotel.model.DAO.ReservaDAO;
import goHotel.model.EstadoReserva;
import goHotel.model.Reserva;
import goHotel.model.ReservaEstado;
import goHotel.view.RegistroReserva;
import goHotel.view.ReservaBuscarHabitacion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
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
    private final ReservaController controllerPrincipal;

    public ReservaRegistroController(Reserva modelo, ReservaDAO consultas, 
                                 RegistroReserva vista, String accion,
                                 String correoUsuario, int numHabitacion, 
                                 String hotelNombre, ReservaController controllerPrincipal)
    {
    this.modelo = modelo;
    this.consultas = consultas;
    this.vista = vista;
    this.accion = accion;
    this.correoUsuario = correoUsuario;
    this.numHabitacion = numHabitacion;
    this.hotelNombre = hotelNombre;
    this.controllerPrincipal= controllerPrincipal;
        
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
        vista.btnBuscar.addActionListener(this);
    }
    
    public void iniciar() 
    {
        vista.setTitle("Registro de Reserva");
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
        vista.jlAccion.setText(this.accion + " Registro");
        cargarEstadosReserva();
        actualizarLineasTablaServicios();
     
            // Ocultar líneas de la tabla al inicio
    vista.jtNombreServicios.setShowGrid(false);
    vista.jtNombreServicios.setIntercellSpacing(new java.awt.Dimension(0, 0));
    vista.jtNombreServicios.setRowHeight(25);
    vista.jtNombreServicios.setFillsViewportHeight(false);
    
    // Limpiar filas dummy de NetBeans
    DefaultTableModel modeloTabla = (DefaultTableModel) vista.jtNombreServicios.getModel();
    modeloTabla.setRowCount(0);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
    if (e.getSource() == vista.btnGuardar) 
    { 
        if (validarCamposObligatorios() && validarFechasYHoras()) 
        {
            guardarReserva();
            vista.dispose();
        }
    }
    
    if (e.getSource() == vista.btnCancelar) 
    { 
        vista.dispose();
    }
    
    if (e.getSource() == vista.btnBuscar) 
    {
        Reserva modelo = new Reserva();
        ReservaDAO dao = new ReservaDAO();
        ReservaBuscarHabitacion vista = new ReservaBuscarHabitacion();
    
        ReservaBusquedaController controller = new ReservaBusquedaController(modelo, dao, vista, correoUsuario);
        controller.iniciar();
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
    
    public void cargarNombreCliente() 
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
     
    public void cargarNombreHotel() 
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

public void cargarDescripcionHabitacion() 
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
    vista.jcbEstadoReserva.removeAllItems();
    vista.jcbEstadoReserva.addItem("--- Ninguno ---");

    for (EstadoReserva estado : EstadoReserva.values()) 
    {
        vista.jcbEstadoReserva.addItem(estado.name());
    }
}  

private boolean validarCamposObligatorios() 
{
    if (vista.txtCodigoCliente.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(vista, 
            "Debe ingresar el código del cliente", 
            "Campo Requerido", 
            JOptionPane.WARNING_MESSAGE);
        vista.txtCodigoCliente.requestFocus();
        return false;
    }
    
    if (vista.jlNombreCliente.getText().equals("No encontrado") || 
        vista.jlNombreCliente.getText().equals("Inválido")) {
        JOptionPane.showMessageDialog(vista, 
            "El cliente ingresado no existe", 
            "Cliente Inválido", 
            JOptionPane.ERROR_MESSAGE);
        vista.txtCodigoCliente.requestFocus();
        return false;
    }
    
    if (vista.txtCodigoHotel.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(vista, 
            "Debe ingresar el código del hotel", 
            "Campo Requerido", 
            JOptionPane.WARNING_MESSAGE);
        vista.txtCodigoHotel.requestFocus();
        return false;
    }
    
    if (vista.jlNombreHotel.getText().equals("No encontrado") || 
        vista.jlNombreHotel.getText().equals("Inválido")) {
        JOptionPane.showMessageDialog(vista, 
            "El hotel ingresado no existe", 
            "Hotel Inválido", 
            JOptionPane.ERROR_MESSAGE);
        vista.txtCodigoHotel.requestFocus();
        return false;
    }
    
    if (vista.txtNumHabitacion.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(vista, 
            "Debe ingresar el número de habitación", 
            "Campo Requerido", 
            JOptionPane.WARNING_MESSAGE);
        vista.txtNumHabitacion.requestFocus();
        return false;
    }
    
    if (vista.jlNombreHabitacion.getText().equals("No existe") || 
        vista.jlNombreHabitacion.getText().equals("Inválido")) {
        JOptionPane.showMessageDialog(vista, 
            "La habitación no existe en este hotel", 
            "Habitación Inválida", 
            JOptionPane.ERROR_MESSAGE);
        vista.txtNumHabitacion.requestFocus();
        return false;
    }
    
    if (vista.jcbEstadoReserva.getSelectedIndex() == 0) {
        JOptionPane.showMessageDialog(vista, 
            "Debe seleccionar un estado de reserva", 
            "Campo Requerido", 
            JOptionPane.WARNING_MESSAGE);
        vista.jcbEstadoReserva.requestFocus();
        return false;
    }
    
    return true;
}

private void guardarReserva() 
{
try {
        int idCliente = Integer.parseInt(vista.txtCodigoCliente.getText().trim());
        int idHotel = Integer.parseInt(vista.txtCodigoHotel.getText().trim());
        int numHabitacion = Integer.parseInt(vista.txtNumHabitacion.getText().trim());
        
        // Construir fecha/hora entrada
        Date dateEntrada = vista.jDateFechaEntrada.getDate();
        LocalDate fechaEntrada = dateEntrada.toInstant()
            .atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime horaEntrada = LocalTime.parse(vista.timePickerEntrada.getText().trim());
        LocalDateTime fechaHoraEntrada = LocalDateTime.of(fechaEntrada, horaEntrada);
        
        // Construir fecha/hora salida
        Date dateSalida = vista.jDateFechaSalida.getDate();
        LocalDate fechaSalida = dateSalida.toInstant()
            .atZone(ZoneId.systemDefault()).toLocalDate();
        LocalTime horaSalida = LocalTime.parse(vista.timePickerSalida.getText().trim());
        LocalDateTime fechaHoraSalida = LocalDateTime.of(fechaSalida, horaSalida);
        
        // Verificar si es edición o nuevo
        String idReservaTexto = vista.txtIdReserva.getText().trim();
        boolean esEdicion = !idReservaTexto.isEmpty();
        int idReserva = esEdicion ? Integer.parseInt(idReservaTexto) : 0;
        
        // Validar disponibilidad (excluir la reserva actual si es edición)
        if (!consultas.verificarDisponibilidadHabitacion(idHotel, numHabitacion, 
                                                          fechaHoraEntrada, fechaHoraSalida, 
                                                          esEdicion ? idReserva : 0)) 
        {
            List<String> conflictos = consultas.obtenerConflictosReserva(
                idHotel, numHabitacion, fechaHoraEntrada, fechaHoraSalida);
            
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("La habitación no está disponible en las fechas seleccionadas.\n\n");
            mensaje.append("Reservas existentes:\n");
            
            for (String conflicto : conflictos) {
                mensaje.append("• ").append(conflicto).append("\n");
            }
            
            JOptionPane.showMessageDialog(vista, 
                mensaje.toString(), 
                "Habitación No Disponible", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String estadoReserva = vista.jcbEstadoReserva.getSelectedItem().toString();
        boolean exito;
        
        if (esEdicion) {
            // ACTUALIZAR
            exito = consultas.actualizarReserva(
                idReserva, idCliente, idHotel, numHabitacion,
                fechaHoraEntrada, fechaHoraSalida, 
                estadoReserva, correoUsuario
            );
        } else {
            // INSERTAR
            exito = consultas.guardarReserva(
                idCliente, idHotel, numHabitacion,
                fechaHoraEntrada, fechaHoraSalida, 
                estadoReserva, correoUsuario
            );
        }
        
        if (exito) {
            JOptionPane.showMessageDialog(vista, 
                esEdicion ? "Reserva actualizada exitosamente" : "Reserva guardada exitosamente", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Refrescar tabla si viene de GestionReserva
            if (controllerPrincipal != null) {
                controllerPrincipal.actualizarTabla();
            }
            
            limpiarFormulario();
            vista.dispose();
        } else {
            JOptionPane.showMessageDialog(vista, 
                "No se pudo " + (esEdicion ? "actualizar" : "guardar") + " la reserva.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(vista, 
            "Error al procesar la reserva: " + ex.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
        
}

private void limpiarFormulario() 
{
    vista.txtIdReserva.setText("");
    vista.txtCodigoCliente.setText("");
    vista.txtCodigoHotel.setText("");
    vista.txtNumHabitacion.setText("");
    vista.jDateFechaEntrada.setDate(null);
    vista.jDateFechaSalida.setDate(null);
    vista.timePickerEntrada.setText("");
    vista.timePickerSalida.setText("");
    vista.jcbEstadoReserva.setSelectedIndex(0);
    vista.jlNombreCliente.setText("");
    vista.jlNombreHotel.setText("");
    vista.jlNombreHabitacion.setText("");
    
    DefaultTableModel modeloTabla = (DefaultTableModel) vista.jtNombreServicios.getModel();
    modeloTabla.setRowCount(0);
}
//Agrega este método en ReservaRegistroController:
//
//1. Agregar método para controlar las líneas
private void actualizarLineasTablaServicios() 
{
    DefaultTableModel modelo = (DefaultTableModel) vista.jtNombreServicios.getModel();

    if (modelo.getRowCount() == 0) 
    {
        // Tabla vacía → sin líneas
        vista.jtNombreServicios.setShowGrid(false);
        vista.jtNombreServicios.setIntercellSpacing(new java.awt.Dimension(0, 0));
    } else 
    {
        // Tabla con datos → mostrar líneas
        vista.jtNombreServicios.setShowGrid(true);
        vista.jtNombreServicios.setIntercellSpacing(new java.awt.Dimension(1, 1));
    }
}

public void cargarDatosEdicion(int idReserva, String estado, String fechaEntrada, String fechaSalida) 
{
    try {
        // Cargar nombres en los labels
        cargarNombreCliente();
        cargarNombreHotel();
        cargarDescripcionHabitacion();
        
        // Seleccionar estado en el combo
        for (int i = 0; i < vista.jcbEstadoReserva.getItemCount(); i++) {
            if (vista.jcbEstadoReserva.getItemAt(i).toString().equals(estado)) {
                vista.jcbEstadoReserva.setSelectedIndex(i);
                break;
            }
        }
        
        // Parsear fecha y hora de entrada (formato: 2025-12-12 14:00:00)
        if (fechaEntrada != null && !fechaEntrada.isEmpty()) {
            String[] partesEntrada = fechaEntrada.split(" ");
            if (partesEntrada.length >= 2) {
                // Fecha
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                Date dateEntrada = sdf.parse(partesEntrada[0]);
                vista.jDateFechaEntrada.setDate(dateEntrada);
                
                // Hora (quitar segundos si tiene)
                String horaEntrada = partesEntrada[1];
                if (horaEntrada.length() > 5) {
                    horaEntrada = horaEntrada.substring(0, 5); // "14:00:00" -> "14:00"
                }
                vista.timePickerEntrada.setText(horaEntrada);
            }
        }
        
        // Parsear fecha y hora de salida
        if (fechaSalida != null && !fechaSalida.isEmpty()) {
            String[] partesSalida = fechaSalida.split(" ");
            if (partesSalida.length >= 2) {
                // Fecha
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                Date dateSalida = sdf.parse(partesSalida[0]);
                vista.jDateFechaSalida.setDate(dateSalida);
                
                // Hora
                String horaSalida = partesSalida[1];
                if (horaSalida.length() > 5) {
                    horaSalida = horaSalida.substring(0, 5);
                }
                vista.timePickerSalida.setText(horaSalida);
            }
        }
        
        // Deshabilitar campo ID para edición
        vista.txtIdReserva.setEditable(false);
        
    } catch (Exception e) {
        System.err.println("Error al cargar datos de edición: " + e.getMessage());
        e.printStackTrace();
    }
}
}

        
    
    

