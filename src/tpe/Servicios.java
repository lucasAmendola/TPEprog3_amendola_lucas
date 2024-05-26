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
		reader.readTasks(pathTareas, arrayTareas, hashAux);
	}
	
	/*
     * Complejidad Temporal: O(1).
     */
	public Tarea servicio1(String ID) {
		return this.hashAux.get(ID);
	}
    
    /*
     * Complejidad Temporal: O(n) en el peor de los casos, ya que podría iterar sobre todas las tareas.
     */

	/*Permitir que el usuario decida si quiere ver todas las tareas críticas o no críticas y generar
	el listado apropiado resultante. */

	public List<Tarea> servicio2(boolean esCritica) {
		List<Tarea> tareasCumple = new ArrayList<>();
		servicio2(tareasCumple, esCritica);
		return tareasCumple;
	} 

	private void servicio2(List<Tarea> tareasCumple, Boolean esCritica){
		for(int i = 0; i<this.arrayTareas.size(); i++){
			if(this.arrayTareas.get(i).getEsCritica() ){
				Tarea tCopia = new Tarea(this.arrayTareas.get(i));
				tareasCumple.add(tCopia);
			}
		}
	}

    /*
     * Complejidad Temporal: O(n) en el peor de los casos, ya que podría iterar sobre todas las tareas.
     */

	 /*Obtener todas las tareas entre 2 niveles de prioridad indicados */
	public List<Tarea> servicio3(int prioridadInferior, int prioridadSuperior) {
		List<Tarea> tareasCumple = new ArrayList<>();
		servicio3(tareasCumple, prioridadInferior, prioridadSuperior);
		return tareasCumple;
	}

	private void servicio3(List<Tarea> tareasCumple, int prioridadInferior, int prioridadSuperior) {
		for(Tarea tarea: hashAux.values()){
			Integer nivelPrioridad = tarea.getNivelPrioridad();
			if((nivelPrioridad >= prioridadInferior) && (nivelPrioridad <= prioridadSuperior)){
				tareasCumple.add(tarea);
			}
		}
	} 

	public void procesarDatosServicio(List<Tarea> tareasResultados){
		System.out.println("\nTareas:");
		for(Tarea t : tareasResultados){
			System.out.println(t);
		}
	}
}
