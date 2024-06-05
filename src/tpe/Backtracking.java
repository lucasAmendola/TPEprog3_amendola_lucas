package tpe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import tpe.utils.CSVReader;

public class Backtracking {

    private ArrayList<Tarea> tareas;
    private ArrayList<Procesador> procesadores;
    private HashMap<String, ArrayList<Tarea>> solucionFinal;
    private HashMap<String, Integer> hashTiempo;
    private HashMap<String, ArrayList<Tarea>> hashCriticas;
    private int tareasAsignadas;
    private int metrica;

    public Backtracking(String pathTareas, String pathProcesadores) {
        this.procesadores = new ArrayList<>();
        this.tareas = new ArrayList<>();
        this.solucionFinal = new HashMap<String, ArrayList<Tarea>>();
        this.hashTiempo = new HashMap<String, Integer>();
        this.hashCriticas = new HashMap<String, ArrayList<Tarea>>();
        this.metrica = 0;
        this.tareasAsignadas = 0;

        CSVReader reader = new CSVReader();
		reader.readProcessors(pathProcesadores, procesadores);
        reader.readTasks(pathTareas, tareas, new ArrayList<>(), new ArrayList<>(), new HashMap<>());

        for (Procesador procesador : procesadores) {
            hashTiempo.put(procesador.getId(), 0);
            hashCriticas.put(procesador.getId(), new ArrayList<>());
        }
    }

    public void imprimirPrueba(HashMap<String, ArrayList<Tarea>> solucionAct) {
        for (String p : solucionAct.keySet()) {
                System.out.println("Procesador: ");
                System.out.println(p);
                ArrayList <Tarea> arrayListTareas = solucionAct.get(p);
                Iterator <Tarea> itTareas = arrayListTareas.iterator();
                while (itTareas.hasNext()) {
                        Tarea t = itTareas.next();
                        System.out.println("tareas del procesador: " + p);
                        System.out.println("Tarea : " + t.getId());
                }
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
        this.imprimirPrueba(solucionFinal);
        this.imprimirSolucion(this.metrica);
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
       
        if (this.tareas.size() == tareasAsignadas) {

             // Asigné todas las tareas a algún procesador
            if (this.solucionFinal.isEmpty()) {
                this.solucionFinal.putAll(solucionActual);
            } else {
                this.quedarseConLaMejorSolucion(solucionActual);
            }
            
        } else {
            if (indiceTareas < this.tareas.size()) {
                
                Tarea tarea = this.tareas.get(indiceTareas);
                int j = 0;
                // Recorro procesadores
                while (j < this.procesadores.size()) {
                    
                    Procesador procesador = this.procesadores.get(j);

                    // Poda tareas críticas
                    if ((tarea.getEsCritica() && this.hashCriticas.get(procesador.getId()).size() < 2) || !tarea.getEsCritica()) {

                        int sumaParcial = this.hashTiempo.get(procesador.getId());
                        int sumaNueva = sumaParcial + tarea.getTiempoEjecucion();

                        // Poda para procesadores no refrigerados
                        if ((!procesador.getEstaRefrigerado() && sumaNueva < tiempoMaximo) || procesador.getEstaRefrigerado()) {

                                // Si es crítica la agrego al hash auxiliar de tareas críticas
                                if (tarea.getEsCritica()) {
                                    this.hashCriticas.get(procesador.getId()).add(tarea);
                                }

                                // actualizo tiempo del procesador en el hash de tiempos
                                this.hashTiempo.put(procesador.getId(), sumaNueva);

                                // ASIGNO TAREA AL PROCESADOR
                                solucionActual.get(procesador.getId()).add(tarea);
                                
                                // Llamo a recursividad
                                tareasAsignadas++;
                                this.encontrarSolucion(solucionActual, tiempoMaximo, tareasAsignadas);

                                // Termino recursividad y vuelvo al estado anterior
                                tareasAsignadas--;
                                this.hashTiempo.put(procesador.getId(), sumaParcial);

                                //si es critica, la saco del hash de criticas
                                if(tarea.getEsCritica()){
                                    this.hashCriticas.get(procesador.getId()).remove(tarea);
                                }
                                
                                // Saco tarea de procesador
                                solucionActual.get(procesador.getId()).remove(tarea);
                        }   
                    } 
                    j++;
                } 
            }
        }
    }


    private void quedarseConLaMejorSolucion(HashMap<String, ArrayList<Tarea>> solucionActual) {

        int tiempoMaximoSolucionActual = obtenerTiempoMaximo(solucionActual);
        int tiempoMaximoSolucionFinal = obtenerTiempoMaximo(solucionFinal);

        if (tiempoMaximoSolucionFinal > tiempoMaximoSolucionActual) {
                this.solucionFinal.clear();
                this.solucionFinal.putAll(solucionActual);  
        }
    }

    private int obtenerTiempoMaximo(HashMap<String, ArrayList<Tarea>> solucion) {

        int tiempoMaximo = 0;
        for (ArrayList<Tarea> tareas : solucion.values()) {
            int nuevoTiempo = tareas.stream().mapToInt(Tarea::getTiempoEjecucion).sum();
            if (tiempoMaximo < nuevoTiempo) {
                tiempoMaximo = nuevoTiempo;
            }
        }
        return tiempoMaximo;
    }

    public void imprimirSolucion(int metrica) {
        System.out.println("Metrica de solucion: "+ metrica);

        Integer tiempoMaximoEjecucion = 0;

        for (ArrayList<Tarea> tareas : this.solucionFinal.values()) {

            Integer nuevoTiempo = this.getTiempoMaximoEjecucion(tareas);

            if(tiempoMaximoEjecucion < nuevoTiempo) {
                tiempoMaximoEjecucion = nuevoTiempo;
            }

        }

        System.out.println("tiempo máximo de ejecución: " + tiempoMaximoEjecucion);


        for (String key : this.solucionFinal.keySet()) {
            //procesador
            // System.out.println("Procesador con el codigo: " + this.procesadores.get().getCodigo());
            this.solucionFinal.get(key);
            //tareas
            for (Tarea tarea : this.solucionFinal.get(key)) {
                 System.out.println("Tarea: "+tarea.getNombre());
            }
        }
    }

  public int getTiempoMaximoEjecucion(ArrayList<Tarea> tareas){
        int tiempoEjecucion = 0;

        for (Tarea tarea : tareas) {
             tiempoEjecucion += tarea.getTiempoEjecucion();
        }
        return tiempoEjecucion;
  }
        
}