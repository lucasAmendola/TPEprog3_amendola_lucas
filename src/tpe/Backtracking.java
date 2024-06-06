package tpe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import tpe.utils.CSVReader;

public class Backtracking {

    private ArrayList<Tarea> tareas;
    private ArrayList<Procesador> procesadores;
    private HashMap<String, Integer> hashTiempo;
    private HashMap<String, ArrayList<Tarea>> solucionFinal;
    private HashMap<String, ArrayList<Tarea>> hashCriticas;
    private int metrica;

    public Backtracking(String pathTareas, String pathProcesadores) {
        this.procesadores = new ArrayList<>();
        this.tareas = new ArrayList<>();
        this.hashTiempo = new HashMap<String, Integer>();
        this.hashCriticas = new HashMap<String, ArrayList<Tarea>>();
        this.solucionFinal = new HashMap<String, ArrayList<Tarea>>();
        this.metrica = 0;

        CSVReader reader = new CSVReader();
		reader.readProcessors(pathProcesadores, procesadores);
        reader.readTasks(pathTareas, tareas, new ArrayList<>(), new ArrayList<>(), new HashMap<>());

        for (Procesador procesador : procesadores) {
            hashTiempo.put(procesador.getId(), 0);
            hashCriticas.put(procesador.getId(), new ArrayList<>());
        }
    }

    //FUNCION PUBLICA
    public void encontrarSolucion(int tiempoMaximo) {

        HashMap<String, ArrayList<Tarea>> solucionActual = new HashMap<String, ArrayList<Tarea>>();

        for (Procesador p: procesadores) {
            ArrayList<Tarea> arrayListTareas = new ArrayList<>();
            solucionActual.put(p.getId(), arrayListTareas);
        }
        //mandar a recursivo
        this.encontrarSolucion(solucionActual, tiempoMaximo, 0);
        this.imprimirSolucion(metrica);
    }

    /*
    * << la estrategia de backtracking utilizada en este código se basa en una búsqueda 
        exhaustiva de todas las posibles asignaciones de tareas a procesadores, 
        evaluando cada posible solución y eligiendo la mejor. En cada recursión
        son aplicadas 2 podas, la primera de ellas se encarga de comprobar que el procesador
        al que le estamos asignando tareas no sobrepase el limite de 2 tareas criticas asignadas,
        esto se comprueba a través del HashMap auxiliar "hashCriticas", en donde se van almacenando
        las tareas criticas que se le van asignando a cada procesador. Una vez superada esta poda, se
        encuentra la segunda, que consta de comprobar que aquellos procesadores que no posean refiregeración
        no sobrepasen un limite determinado de tiempo de ejecución, el cual es asignado por el usuario
        y que tambien se comprueba a través de la utilización de un HashMap auxiliar, en donde se va
        alamecenando el tiempo de ejecucion de cada procesador a medida de que se le asignan tareas. Una
        vez superadas las 2 podas, se llega a la instancia de asignacion final de la tarea al procesador 
        actual y se llama recursivamente a la función para que siga con las demas tareas.>>
    */

    private void encontrarSolucion(HashMap<String, ArrayList<Tarea>> solucionActual, int tiempoMaximo, int indiceTareas) {
        metrica++;

        // Asigné todas las tareas a algún procesador
        if (indiceTareas == this.tareas.size()) {
            
            if (this.solucionFinal.isEmpty()) {
                solucionFinal.putAll(solucionActual);
                for (String keyProcesador : this.solucionFinal.keySet()) {
                    this.solucionFinal.get(keyProcesador).addAll(solucionActual.get(keyProcesador));
                }
            } 
            else {
                this.quedarseConLaMejorSolucion(solucionActual);
            }
        } 
        else {
            
            Tarea tarea = this.tareas.get(indiceTareas);
            
            // Recorro procesadores
            for (Procesador procesador : this.procesadores) {
                
                // Poda tareas críticas
                if ((tarea.getEsCritica() && this.hashCriticas.get(procesador.getId()).size() < 2) || !tarea.getEsCritica()) {

                    int sumaParcial = this.hashTiempo.get(procesador.getId());
                    int sumaNueva = sumaParcial + tarea.getTiempoEjecucion();

                    // Poda para procesadores no refrigerados
                    if ((!procesador.getEstaRefrigerado() && sumaNueva <= tiempoMaximo) || procesador.getEstaRefrigerado()) {

                            // Si es crítica la agrego al hash auxiliar de tareas críticas
                            if (tarea.getEsCritica()) {
                                this.hashCriticas.get(procesador.getId()).add(tarea);
                            }

                            // actualizo tiempo del procesador en el hash de tiempos
                            this.hashTiempo.put(procesador.getId(), sumaNueva);

                            // ASIGNO TAREA AL PROCESADOR
                            solucionActual.get(procesador.getId()).add(tarea);
                            
                            // Llamo a recursividad
                            
                            this.encontrarSolucion(solucionActual, tiempoMaximo, indiceTareas+1);

                            // Termino recursividad y vuelvo al estado anterior
                            
                            this.hashTiempo.put(procesador.getId(), sumaParcial);

                            //si es critica, la saco del hash de criticas
                            if(tarea.getEsCritica()){
                                this.hashCriticas.get(procesador.getId()).remove(tarea);
                            }
                            
                            // Saco tarea de procesador
                            solucionActual.get(procesador.getId()).remove(tarea);
                    }   
                } 
            } 
        }
    }


    private void quedarseConLaMejorSolucion(HashMap<String, ArrayList<Tarea>> solucionActual) {
        
        int tiempoMaximoSolucionActual = obtenerTiempoMaximoDeEjecucion(solucionActual);
        int tiempoMaximoSolucionFinal = obtenerTiempoMaximoDeEjecucion(solucionFinal);

        if (tiempoMaximoSolucionFinal > tiempoMaximoSolucionActual) {
            solucionFinal.clear();
            solucionFinal.putAll(solucionActual);
            for (String keyProcesador : this.solucionFinal.keySet()) {
                this.solucionFinal.get(keyProcesador).addAll(solucionActual.get(keyProcesador));
            }
        }
    }

    private int obtenerTiempoMaximoDeEjecucion(HashMap<String, ArrayList<Tarea>> solucion) {

        int tiempoMaximo = 0;

        for (ArrayList<Tarea> tareas : solucion.values()) {
             int nuevoTiempo = 0;

             for (Tarea tarea : tareas) {
                    nuevoTiempo += tarea.getTiempoEjecucion();
             }
           
            if (tiempoMaximo < nuevoTiempo) {
                tiempoMaximo = nuevoTiempo;
            }
        }
       
        return tiempoMaximo;
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