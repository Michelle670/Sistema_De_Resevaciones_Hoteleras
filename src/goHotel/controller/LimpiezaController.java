
package goHotel.controller;
import goHotel.model.DAO.LimpiezaDAO;
import goHotel.view.GestionReporteLimpieza;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author soloa
 */
public class LimpiezaController implements ActionListener 
{
    
    private final LimpiezaDAO consultas;
    private final GestionReporteLimpieza vista;

    public LimpiezaController (LimpiezaDAO consultas, GestionReporteLimpieza vista) 
    {
        this.consultas = consultas;
        this.vista = vista;
    
        
     this.vista.btnBuscar.addActionListener(this);
     this.vista.btnLimpiar.addActionListener(this);
     this.vista.jcbNombreHoteles.addActionListener(this);
//     this.vista.jDateInicio.addPropertyChangeListener(this);
//     this.vista.jDateFin.addPropertyChangeListener(this);
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
    
    
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        
       if (e.getSource() == vista.btnLimpiar) {

            vista.jcbNombreHoteles.setSelectedIndex(0);
            vista.jDateInicio.setDate(null);
            vista.jDateFin.setDate(null);
           ((DefaultTableModel) vista.jtGestionLimpieza.getModel()).setRowCount(0);

}
       
if (e.getSource() == vista.btnBuscar) 
{
     DefaultTableModel modelo = (DefaultTableModel) vista.jtGestionLimpieza.getModel();

    String hotel = vista.jcbNombreHoteles.getSelectedItem().toString();
    Date fechaInicio = vista.jDateInicio.getDate();
    Date fechaFin    = vista.jDateFin.getDate();

    consultas.cargarReporteLimpieza(modelo, hotel, fechaInicio, fechaFin);
}
}
    }




