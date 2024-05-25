package tpe;

public class Tarea {

    private String id;
    private String nombre;
    private int tiempoEjecucion;
    private Boolean esCritica;
    private int nivelPrioridad;

    public Tarea(String id, String nombre, int tiempoEjecucion, Boolean esCritica, int nivelPrioridad) {
        this.id = id;
        this.nombre = nombre;
        this.tiempoEjecucion = tiempoEjecucion;
        this.esCritica = esCritica;
        this.nivelPrioridad = nivelPrioridad;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getTiempoEjecucion() {
        return tiempoEjecucion;
    }
    public void setTiempoEjecucion(int tiempoEjecucion) {
        this.tiempoEjecucion = tiempoEjecucion;
    }
    public Boolean getEsCritica() {
        return esCritica;
    }
    public void setEsCritica(Boolean esCritica) {
        this.esCritica = esCritica;
    }
    public int getNivelPrioridad() {
        return nivelPrioridad;
    }
    public void setNivelPrioridad(int nivelPrioridad) {
        this.nivelPrioridad = nivelPrioridad;
    }

    

}
