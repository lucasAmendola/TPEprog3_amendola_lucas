package tpe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tpe.utils.CSVReader;

public class Greedy {

    private ArrayList<Tarea> tareas;
    private ArrayList<Procesador> procesadores;
    private HashMap<String, ArrayList<Tarea>> solucionFinal;
    private HashMap<String, Integer> hashTiempo;
    private HashMap<String, ArrayList<Tarea>> hashCriticas;
    private HashMap<Tarea, Boolean> hashTareasAsignadas;
    private int metrica;

    public Greedy(String pathTareas, String pathProcesadores) {
        this.procesadores = new ArrayList<>();
        this.tareas = new ArrayList<>();
        this.metrica = 0;
        this.solucionFinal = new HashMap<String, ArrayList<Tarea>>();
        this.hashTiempo = new HashMap<String, Integer>();
        this.hashCriticas = new HashMap<String, ArrayList<Tarea>>();
        this.hashTareasAsignadas = new HashMap<Tarea, Boolean>();

        CSVReader reader = new CSVReader();
		reader.readProcessors(pathProcesadores, procesadores);
        reader.readTasks(pathTareas, tareas, new ArrayList<>(), new ArrayList<>(), new HashMap<>());

        for (Procesador procesador : procesadores) {
            hashTiempo.put(procesador.getId(), 0);
            hashCriticas.put(procesador.getId(), new ArrayList<>());
        }
    }

    public void buscarSolucion(int tiempoMaximo){
        if (encontrarSolucion(tiempoMaximo)) {
            System.out.println(solucionFinal);
            this.imprimirSolucion(metrica);
        }
        else{
            this.imprimirSolucion(tiempoMaximo);
            System.out.println("\n");
            System.out.println("Tareas que no pudieron asignarse");
            for (Map.Entry<Tarea, Boolean> entry : hashTareasAsignadas.entrySet()) {
                if (!this.hashTareasAsignadas.get(entry.getKey())) {
                    System.out.println(entry);
                }
            }
        }
    }

    private Boolean encontrarSolucion(int tiempoMaximo){

        for (Tarea tarea : tareas) {
            hashTareasAsignadas.put(tarea, false);
        }

        for (Tarea tarea : tareas) {
                metrica++;
                String idMejorProcesadorPosible = getMejorProcesadoPosible(tarea, tiempoMaximo);
                if(idMejorProcesadorPosible != null){
                    solucionFinal.computeIfAbsent(idMejorProcesadorPosible, k -> new ArrayList<>()).add(tarea);
                }
        }

        for (Boolean asignada : hashTareasAsignadas.values()) {
                if (!asignada) {
                    return false;
                }
        }
        return true;
    }

    private String getMejorProcesadoPosible(Tarea tarea, int tiempoMaximo) {
        String idMejorProcesador = null;

        for (Procesador procesador : procesadores) {

            if ((tarea.getEsCritica() && this.hashCriticas.get(procesador.getId()).size() < 2) || !tarea.getEsCritica()) {
               
                int sumaParcial = this.hashTiempo.get(procesador.getId());
                int sumaNueva = sumaParcial + tarea.getTiempoEjecucion();

                // Poda para procesadores no refrigerados
                if ((!procesador.getEstaRefrigerado() && sumaNueva <= tiempoMaximo) || procesador.getEstaRefrigerado()) {
                    if (idMejorProcesador == null || sumaNueva < this.hashTiempo.get(idMejorProcesador)) {
                        idMejorProcesador = procesador.getId();
                    }
                }
            }
        }
    
        if (idMejorProcesador != null) {
            int sumaNueva = hashTiempo.get(idMejorProcesador) + tarea.getTiempoEjecucion();
            hashTiempo.put(idMejorProcesador, sumaNueva);
            if (tarea.getEsCritica()) {
                this.hashCriticas.get(idMejorProcesador).add(tarea);
            }
            this.hashTareasAsignadas.put(tarea, true);
        }
    
        return idMejorProcesador;
}
    
    public void imprimirSolucion(int metrica) {
        
        System.out.println("Solución Final:");
        for (Map.Entry<String, ArrayList<Tarea>> entry : solucionFinal.entrySet()) {
            System.out.println("Procesador: " + entry.getKey());
            for (Tarea tarea : entry.getValue()) {
                System.out.println("  Tarea: " + tarea.getNombre() + " Tiempo: " + tarea.getTiempoEjecucion());
            }
        }
        System.out.println("Metrica de solucion: " + metrica);

        int tiempoMaximoEjecucion = 0;
        for (ArrayList<Tarea> tareas : solucionFinal.values()) {
            int nuevoTiempo = this.obtenerTiempoTotalDeEjecucion(tareas);
            tiempoMaximoEjecucion = Math.max(tiempoMaximoEjecucion, nuevoTiempo);
        }
        System.out.println("Tiempo máximo de ejecución: " + tiempoMaximoEjecucion);
    }

  private int obtenerTiempoTotalDeEjecucion(ArrayList<Tarea> tareas){
        int tiempoEjecucion = 0;

        for (Tarea tarea : tareas) {
             tiempoEjecucion += tarea.getTiempoEjecucion();
        }
        return tiempoEjecucion;
  }
}
