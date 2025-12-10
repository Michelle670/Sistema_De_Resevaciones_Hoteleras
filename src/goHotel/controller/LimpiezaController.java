//==============================================================================
// IMPORTES
//==============================================================================
package goHotel.controller;
import goHotel.model.DAO.LimpiezaDAO;
import goHotel.view.GestionReporteLimpieza;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.table.DefaultTableModel;
/*****************************************************************************
 * AUTOR: GRUPO 3 / SOFIA LOAIZA, MICHELLE GUERRERO, NIXON VARGAS Y ISRAEL APUY
 * PROYECTO
 * SEMANA 14
 *****************************************************************************/
//==============================================================================
// LIMPIEZA CONTROLLER
//==============================================================================
/**
 * Controlador encargado de manejar la lógica de la pantalla
 * Reporte de Ocupación.
 */
public class LimpiezaController implements ActionListener 
{
    private final LimpiezaDAO consultas;
    private final GestionReporteLimpieza vista;
    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public LimpiezaController (LimpiezaDAO consultas, GestionReporteLimpieza vista) 
    {
     this.consultas = consultas;
     this.vista = vista;
     this.vista.btnBuscar.addActionListener(this);
     this.vista.btnLimpiar.addActionListener(this);
     this.vista.jcbNombreHoteles.addActionListener(this);
    }   
//==============================================================================  
//INICIAR
//==============================================================================      
    public void iniciar() 
    {
        vista.setTitle("Reporte de Limpieza");
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
        consultas.cargarHoteles(vista.jcbNombreHoteles);
        vista.jDateInicio.setDate(new Date());
        vista.jDateFin.setDate(new Date());
    }
    // =========================================================================
    // MANEJADOR DE EVENTOS
    // =========================================================================
    @Override
    public void actionPerformed(ActionEvent e) 
    {
       //=======================================================================
       // BTN LIMPIAR
       //=======================================================================
       if (e.getSource() == vista.btnLimpiar) 
       {
            vista.jcbNombreHoteles.setSelectedIndex(0);
            vista.jDateInicio.setDate(null);
            vista.jDateFin.setDate(null);
           ((DefaultTableModel) vista.jtGestionLimpieza.getModel()).setRowCount(0);
        }
       //=======================================================================
       // BTN BUSCAR
       //=======================================================================
        if (e.getSource() == vista.btnBuscar) 
        {
            DefaultTableModel modelo = (DefaultTableModel) vista.jtGestionLimpieza.getModel();
            String hotel = vista.jcbNombreHoteles.getSelectedItem().toString();
            Date fechaInicio = vista.jDateInicio.getDate();
            Date fechaFin    = vista.jDateFin.getDate();
            consultas.cargarReporteLimpieza(modelo, hotel, fechaInicio, fechaFin);
        }
        //======================================================================
        }
    }




