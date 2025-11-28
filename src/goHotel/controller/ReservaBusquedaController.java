package goHotel.controller;
import goHotel.model.DAO.ReservaDAO;
import goHotel.model.Reserva;
import goHotel.view.ReservaBuscarHabitacion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReservaBusquedaController implements ActionListener
{
    private final Reserva modelo;
    private final ReservaDAO consultas;
    private final ReservaBuscarHabitacion vista;

    public ReservaBusquedaController(Reserva modelo, ReservaDAO consultas, ReservaBuscarHabitacion vista)
    {
        this.modelo = modelo;
        this.consultas = consultas;
        this.vista = vista;

        // ----------------------------------------------------
        // LISTENERS DE BOTONES Y COMBOS
        // ----------------------------------------------------
        this.vista.cbNombresHoteles.addActionListener(this);
        this.vista.cbServicios.addActionListener(this);
        this.vista.btnBuscar.addActionListener(this);
        this.vista.btnAgregar.addActionListener(this);
        this.vista.btnQuitar.addActionListener(this);
        this.vista.btnReservar.addActionListener(this);
        //this.vista..addActionListener(this);
        // ----------------------------------------------------
        // LISTENER PARA JSPINNER (NUMERO PERSONAS)
        // ----------------------------------------------------
        this.vista.jsNumPersonas.addChangeListener(e -> {
       
        });

        // ----------------------------------------------------
        // LISTENER PARA FECHA DE ENTRADA
        // ----------------------------------------------------
        this.vista.jdateFechaEntrada.getDateEditor().addPropertyChangeListener(event -> {
            if ("date".equals(event.getPropertyName())) {
               
            }
        });

        // ----------------------------------------------------
        // LISTENER PARA FECHA DE SALIDA
        // ----------------------------------------------------
        this.vista.jdateFechaSalida.getDateEditor().addPropertyChangeListener(event -> {
            if ("date".equals(event.getPropertyName())) {
           
            }
        });
    }

    // ----------------------------------------------------
    // MÉTODO INICIAR
    // ----------------------------------------------------
    public void iniciar()
    {
        vista.setTitle("Búsqueda de Habitación");
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
        consultas.cargarHoteles(vista.cbNombresHoteles);
        consultas.cargarServicios(vista.cbServicios);
    }

    // ----------------------------------------------------
    // MANEJO DE TODOS LOS BOTONES (ActionListener)
    // ----------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == vista.btnBuscar)
        {
            
        }

        if (e.getSource() == vista.btnAgregar)
        {
   
        }

        if (e.getSource() == vista.btnQuitar)
        {
          
        }

        if (e.getSource() == vista.btnReservar)
        {
           
        }

        if (e.getSource() == vista.cbServicios)
        {
            
        }

        if (e.getSource() == vista.cbNombresHoteles)
        {
            
        }
       // if (e.getSource() == vista.) {
            
        }
    }


