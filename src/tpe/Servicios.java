package tpe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import tpe.utils.CSVReader;

/**
 * NO modificar la interfaz de esta clase ni sus métodos públicos.
 * Sólo se podrá adaptar el nombre de la clase "Tarea" según sus decisiones
 * de implementación.
 */
public class Servicios {
	CSVReader reader;
	ArrayList<Tarea> arrayTareas;
	HashMap <String, Tarea> hashAux;
	ArrayList<HashMap<Procesador, Tarea>> procesadorTarea;

	/*
     * Expresar la complejidad temporal del constructor.
     */
	public Servicios(String pathProcesadores, String pathTareas){
		this.arrayTareas = new ArrayList<>();
		this.procesadorTarea = new ArrayList<>();
		this.hashAux = new HashMap<>();
		CSVReader reader = new CSVReader();
		reader.readProcessors(pathProcesadores);
		reader.readTasks(pathTareas, hashAux);
	}
	
	/*
     * Expresar la complejidad temporal del servicio 1.
     */
	public Tarea servicio1(String ID) {
		return this.hashAux.get(ID);
	}
    
    /*
     * Expresar la complejidad temporal del servicio 2.
     */
	/*public List<Tarea> servicio2(boolean esCritica) {} */

    /*
     * Expresar la complejidad temporal del servicio 3.
     */
	/*public List<Tarea> servicio3(int prioridadInferior, int prioridadSuperior) {} */

}
