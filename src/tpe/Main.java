package tpe;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        //----------------------Inicio Prueba de servicios de la parte 1--------------------//

        String pathTareas = "src\\tpe\\datasets\\Tareas.csv";
        String pathProcesadores = "src\\tpe\\datasets\\Procesadores.csv";

       Servicios s1 = new Servicios(pathProcesadores, pathTareas);

        String t1 = "T1";
        String t2 = "T2";
        String t3 = "T3";
        String t4 = "T4";
        
        System.out.println("\n");
        System.out.println("COMIENZO EJECUCION PARTE 1");

        System.out.println("Ejecucion servicio 1:");
        System.out.println(s1.servicio1(t1));
        System.out.println(s1.servicio1(t2));
        System.out.println(s1.servicio1(t3));
        System.out.println(s1.servicio1(t4));


        //Devuelve aquellas tareas que sean o no criticas segun el usuario lo desee
        List<Tarea> tareasServicio2 = s1.servicio2(true);

        //Devuelve tareas entre 2 parametros de prioridad impuestos por el usuario
        List<Tarea> tareasServicio3 = s1.servicio3(30, 59);

        System.out.println("\n");
        System.out.println("Ejecucion servicio 2:");

        //imprime la solucion del servicio 2
        s1.procesarDatosServicio(tareasServicio2);

        System.out.println("\n");

        //imprime la solucion del servicio 3
        System.out.println("Ejecucion servicio 3:");
        s1.procesarDatosServicio(tareasServicio3);

        System.out.println("\n");
        System.out.println("COMIENZO EJECUCION PARTE 2");

        //----------------------Fin de prueba de servicios de la parte 1--------------------//

        /*.................................................................................. */

        //----------------------Inicio prueba de servicios de la parte 2--------------------//

        //------------------------Solucion Greedy------------------------//
        Greedy greedy = new Greedy(pathTareas, pathProcesadores);
        greedy.buscarSolucion(100);

        System.out.println("\n");

         //------------------------Solucion Backtracking------------------------//
        Backtracking backtracking = new Backtracking(pathTareas, pathProcesadores);
        backtracking.encontrarSolucion(100);

        //----------------------Fin de prueba de servicios de la parte 2--------------------// 
        System.out.println("\n");
        System.out.println("FIN EJECUCION PARTE 2");
    }
}