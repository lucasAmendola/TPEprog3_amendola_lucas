package tpe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Backtracking {

    private List<Tarea> tareas;
    private List<Procesador> procesadores;
    private HashMap<String, ArrayList<Tarea>> solucionFinal;
    private HashMap<String, Integer> hashTiempo;
    private HashMap<String, ArrayList<Tarea>> hashCriticas;
    private int tareasAsignadas;

    public Backtracking(ArrayList<Tarea> tareas, ArrayList<Procesador> procesadores) {
        this.tareas = tareas;
        this.procesadores = procesadores;
        this.solucionFinal = new HashMap<String, ArrayList<Tarea>>();
        this.hashTiempo = new HashMap<String, Integer>();
        this.hashCriticas = new HashMap<String, ArrayList<Tarea>>();

        for (Procesador procesador : procesadores) {
            hashTiempo.put(procesador.getId(), 0);
            hashCriticas.put(procesador.getId(), new ArrayList<>());
        }
    }

    //FUNCION PUBLICA
    public void encontrarSolucion(int tiempoMaximo) {
        int metrica = 0;
        tareasAsignadas = 0;
        HashMap<String, ArrayList<Tarea>> solucionActual = new HashMap<String, ArrayList<Tarea>>();
        //mandar a recursivo
        this.encontrarSolucion(solucionActual,metrica,tiempoMaximo,0);

        //impre solucion
        this.imprimirSolucion(metrica);
    }

    //FUNCION PRIVADA
    private void encontrarSolucion(HashMap<String, ArrayList<Tarea>> solucionActual, int metrica,int tiempoMaximo, int indiceTareas) {
        metrica += 1;
        if (this.tareas.size() == tareasAsignadas) { // Asigné todas las tareas a algún procesador
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

                    // Si es crítica la agrego al hash auxiliar de tareas críticas
                    if (tarea.getEsCritica()) {
                        this.hashCriticas.get(procesador.getId()).add(tarea);
                    }

                    // Poda tareas críticas
                    if (this.hashCriticas.get(procesador.getId()).size() <= 2) {
                        int sumaParcial = this.hashTiempo.get(procesador.getId());
                        this.hashTiempo.put(procesador.getId(), sumaParcial + tarea.getTiempoEjecucion());

                        // Poda para procesadores no refrigerados
                        if ((!procesador.getEstaRefrigerado() && this.hashTiempo.get(procesador.getId()) < tiempoMaximo) || procesador.getEstaRefrigerado()) {
                            // ASIGNO TAREA AL PROCESADOR
                            if (!solucionActual.containsKey(procesador.getId())) {
                                // Si no existe el procesador en el hash
                                ArrayList<Tarea> newListTareas = new ArrayList<>();
                                solucionActual.put(procesador.getId(), newListTareas);
                                solucionActual.get(procesador.getId()).add(tarea);
                            } else {
                                solucionActual.get(procesador.getId()).add(tarea);
                            }
                            // Llamo a recursividad
                            tareasAsignadas++;
                            this.encontrarSolucion(solucionActual, metrica, tiempoMaximo, indiceTareas + 1);
                            // Termino recursividad
                            tareasAsignadas--;
                            this.hashTiempo.put(procesador.getId(), sumaParcial);
                            this.hashCriticas.get(procesador.getId()).remove(tarea);
                            // Saco tarea de procesador
                            solucionActual.get(procesador.getId()).remove(tarea);
                        } else {
                            this.hashTiempo.put(procesador.getId(), sumaParcial);
                        }
                    } else {
                        this.hashCriticas.get(procesador.getId()).remove(tarea);
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
            this.solucionFinal = new HashMap<>(solucionActual);
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
        System.out.println("Metrica de solucion: "+metrica);

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
    return tareasAsignadas;
  }
        
}