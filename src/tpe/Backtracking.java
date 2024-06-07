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

        //data procesadores y tareas
        this.procesadores = new ArrayList<>();
        this.tareas = new ArrayList<>();

        //hashmaps auxiliares
        this.hashTiempo = new HashMap<String, Integer>();
        this.hashCriticas = new HashMap<String, ArrayList<Tarea>>();

        //estructura de solucion final
        this.solucionFinal = new HashMap<String, ArrayList<Tarea>>();

        CSVReader reader = new CSVReader();
		reader.readProcessors(pathProcesadores, procesadores);
        reader.readTasks(pathTareas, tareas, new ArrayList<>(), new ArrayList<>(), new HashMap<>());

        for (Procesador procesador : procesadores) {
            hashTiempo.put(procesador.getId(), 0);
            hashCriticas.put(procesador.getId(), new ArrayList<>());
        }
    }

    //FUNCION PUBLICA -> inicia las propiedas necesarias para hallar una solucion
    public void encontrarSolucion(int tiempoMaximo) {

        HashMap<String, ArrayList<Tarea>> solucionActual = new HashMap<String, ArrayList<Tarea>>();

        //inicializo procesadores como clave en mi hashmap de solucion actual
        for (Procesador p: procesadores) {
            ArrayList<Tarea> arrayListTareas = new ArrayList<>();
            solucionActual.put(p.getId(), arrayListTareas);
        }

        int indiceTareas = 0;
        this.metrica = 0;

        //inicia backtracking
        this.encontrarSolucion(solucionActual, tiempoMaximo, indiceTareas);

        //mostrar resultados
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
        actual y se llama recursivamente a la función para que siga con las demas tareas. Una vez encontrada
        una posible solucion, esta se compara con la considerada anteriormente y se decide
        cual es la mas optima, la cual conservaremos hasta que encontremos una mejor.>>
    */

    private void encontrarSolucion(HashMap<String, ArrayList<Tarea>> solucionActual, int tiempoMaximo, int indiceTareas) {
        metrica++;

        // Asigné todas las tareas a algún procesador
        if (indiceTareas == this.tareas.size()) {
            
            //si mi solucion final esta vacia = primera vez que encuentro una solucion
            if (this.solucionFinal.isEmpty()) {
                //asigno mi primera solucion encontrada
                this.asignarNuevaSolucion(solucionActual);
            } 
            else {
                int tiempoActual = obtenerTiempoMaximoDeEjecucion(solucionActual);
                int tiempoFinal = obtenerTiempoMaximoDeEjecucion(solucionFinal);
                if(tiempoActual < tiempoFinal){
                    //asigno nueva solucion final
                    this.asignarNuevaSolucion(solucionActual);
                }
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

                            solucionActual.get(procesador.getId()).remove(tarea);

                            // Termino recursividad y vuelvo al estado anterior
                            
                            this.hashTiempo.put(procesador.getId(), sumaParcial);

                            //si es critica, la saco del hash de criticas
                            if(tarea.getEsCritica()){
                                this.hashCriticas.get(procesador.getId()).remove(tarea);
                            }
                        
                    }   
                } 
            } 
        }
    }

    private void asignarNuevaSolucion(HashMap<String, ArrayList<Tarea>> solucionActual){
        this.solucionFinal.clear();
        //por cada procesador
        for (String idProcesador : solucionActual.keySet()) {
            String keyProcessor = idProcesador;
            ArrayList<Tarea> nuevaListaTareas = new ArrayList<Tarea>(); //new list
            int iTarea = 0; //index
            //obtengo su taskList y la recorro
            while (iTarea < solucionActual.get(idProcesador).size()) {
                //obtengo tarea
                Tarea task = solucionActual.get(idProcesador).get(iTarea);
                //guardo en lista
                nuevaListaTareas.add(task); //add task
                iTarea++;
            }
            //agreo el procesador con sus tareas a la solucion actualizada
            this.solucionFinal.put(keyProcessor, nuevaListaTareas);
        }
    }

    //obtiene el maximo tiempo de ejecucion de una solucion...
    private int obtenerTiempoMaximoDeEjecucion(HashMap<String, ArrayList<Tarea>> solucion) {
        
        //debo obtener el maximo tiempo de ejecucion
        //osea de mi solucion, el procesador que mas tarde (la suma de sus tareas es la mas larga)
        
        int tiempoMaximo = 0;
        //siempre me quedo con el tiempo mas alto
        for (ArrayList<Tarea> tareas : solucion.values()) {
            int tiempoProcesador = obtenerTiempoTotalDeEjecucion(tareas);
            if (tiempoProcesador > tiempoMaximo) {
                tiempoMaximo = tiempoProcesador;
            }
        }
    
        return tiempoMaximo;
    }

    public void imprimirSolucion(int metrica) {
        
        System.out.println("Solución Final BACKTRACKING:");
        for (Map.Entry<String, ArrayList<Tarea>> entry : this.solucionFinal.entrySet()) {
            System.out.println("Procesador: " + entry.getKey());
            for (Tarea tarea : entry.getValue()) {
                System.out.println("  Tarea: " + tarea.getNombre() + " Tiempo: " + tarea.getTiempoEjecucion());
            }
        }
        System.out.println("Metrica de solucion: " + metrica);

        int tiempoMaximoEjecucion = 0;
        for (ArrayList<Tarea> tareas : this.solucionFinal.values()) {
            int nuevoTiempo = this.obtenerTiempoTotalDeEjecucion(tareas);
            tiempoMaximoEjecucion = Math.max(tiempoMaximoEjecucion, nuevoTiempo);
        }
        System.out.println("Tiempo máximo de ejecución: " + tiempoMaximoEjecucion);
    }

    //obtiene la suma total de tiempo de una lista de tareas...
    private int obtenerTiempoTotalDeEjecucion(ArrayList<Tarea> tareas){
        int tiempoEjecucion = 0;

        for (Tarea tarea : tareas) {
             tiempoEjecucion += tarea.getTiempoEjecucion();
        }
        return tiempoEjecucion;
  }
        
}