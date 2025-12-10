//==============================================================================
// IMPORTES
//==============================================================================
package goHotel.model;
/*****************************************************************************
 * AUTOR: GRUPO 3 / SOFIA LOAIZA, MICHELLE GUERRERO, NIXON VARGAS Y ISRAEL APUY
 * PROYECTO
 * SEMANA 14
 *****************************************************************************/
//==============================================================================  
// MODELO PAIS
//============================================================================== 
public class Pais
{
//==============================================================================
//ATRIBUTOS 
//==============================================================================

    private int idPais;
    private String codigo; // Código ISO (ej. ES, US, FR)
    private String nombre;

//==============================================================================
//CONSTRUCTOR
//==============================================================================

    public Pais() 
    {
        this.idPais = 0;
        this.codigo = "";
        this.nombre = "";
    }

    /**
     * Constructor completo.
     * @param idPais Identificador único del país.
     * @param codigo Código ISO del país.
     * @param nombre Nombre del país.
     */
    public Pais(int idPais, String codigo, String nombre) {
        this.idPais = idPais;
        this.codigo = codigo;
        this.nombre = nombre;
    }

//==============================================================================
//GETTERS Y SETTERS
//==============================================================================

    public int getIdPais() 
    {
        return idPais;
    }

    public void setIdPais(int idPais) 
    {
        this.idPais = idPais;
    }

    public String getCodigo()
    {
        return codigo;
    }

    public void setCodigo(String codigo) 
    {
        this.codigo = codigo;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre) 
    {
        this.nombre = nombre;
    }

//==============================================================================
//MÉTODOS DE OBJETO
//==============================================================================
    
    /**
     * Devuelve el nombre del país, útil para ComboBoxes.
     * @return El nombre del país.
     */
    @Override
    public String toString() {
        return nombre;
    }
}