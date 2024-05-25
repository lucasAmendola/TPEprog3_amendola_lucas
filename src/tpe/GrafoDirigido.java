package tpe;

import java.util.*;

public class GrafoDirigido<T> implements Grafo<T> {
    private int cantArcos;

    //hash map, contiene un vertice (clave int), y un array de sus arcos
    private HashMap<Integer, ArrayList<Arco<T>>> listVertices;
    private HashMap<Integer, String> listVerticesBFS;

    public GrafoDirigido() {
        this.cantArcos = 0;
        this.listVertices = new HashMap<Integer, ArrayList<Arco<T>>>();
        this.listVerticesBFS = new HashMap<Integer, String>();
    }
    @Override
    public void agregarVertice(int verticeId) {
        ArrayList<Arco<T>> arcos = new ArrayList<>();
        this.listVertices.put(verticeId, arcos);
    }

    @Override
    public void borrarVertice(int verticeId) {
        if(this.listVertices.containsKey(verticeId)){
            this.listVertices.remove(verticeId);
        }
    }

    @Override
    public void agregarArco(int verticeId1, int verticeId2, T etiqueta) {
        if(this.listVertices.containsKey(verticeId1) && this.listVertices.containsKey(verticeId2)){
            Arco<T> arcoNuevo = new Arco<T>(verticeId1,verticeId2,etiqueta);
            this.listVertices.get(verticeId1).add(arcoNuevo);
            this.cantArcos = this.cantArcos + 1;
        }
    }

    @Override
    public void borrarArco(int verticeId1, int verticeId2) {
        if(listVertices.containsKey(verticeId1)){
            listVertices.get(verticeId1).remove(this.obtenerArco(verticeId1,verticeId2));
            cantArcos--;
        }
    }

    @Override
    public boolean contieneVertice(int verticeId) {
        return this.listVertices.containsKey(verticeId);
    }

    @Override
    public boolean existeArco(int verticeId1, int verticeId2) {
        return obtenerArco(verticeId1, verticeId2) != null;
    }

    @Override
    public Arco<T> obtenerArco(int verticeId1, int verticeId2) {
        if(this.listVertices.containsKey(verticeId1)){
            ArrayList<Arco<T>> arcos = this.listVertices.get(verticeId1);
            Arco<T> arcoBuscado = new Arco<>(verticeId1,verticeId2,null);
            if(arcos != null && arcos.contains(arcoBuscado)){
                return arcos.get(arcos.indexOf(arcoBuscado));
            }
            return null;
        }
        return null;
    }

    @Override
    public int cantidadVertices() {
        return this.listVertices.size();
    }

    @Override
    public int cantidadArcos() {
        return this.cantArcos;
    }

    @Override
    public Iterator<Integer> obtenerVertices() {
        Iterator<Integer> vertices = this.listVertices.keySet().iterator();
        return vertices;
    }

    @Override
    public Iterator<Integer> obtenerAdyacentes(int verticeId) {
        if(this.listVertices.containsKey(verticeId)){
            ArrayList<Integer> adyacentes = new ArrayList<>();
            Iterator<Arco<T>> iteratorArcos = this.obtenerArcos(verticeId);
            while (iteratorArcos.hasNext()) {
                Arco<T> arco = iteratorArcos.next();
                adyacentes.add(arco.getVerticeDestino());
            }
            return adyacentes.iterator();
        }
        return null;
    }

    @Override
    public Iterator<Arco<T>> obtenerArcos() {
        ArrayList<Arco<T>> nueva = new ArrayList<Arco<T>>();

        for (Map.Entry<Integer, ArrayList<Arco<T>>> entrada : this.listVertices.entrySet()){

            for (Arco<T> arco : entrada.getValue()) {
                nueva.add(arco);
            }
        }

        return nueva.iterator();
    }

    @Override
    public Iterator<Arco<T>> obtenerArcos(int verticeId) {
        return this.listVertices.get(verticeId).iterator();
    }

    public ArrayList<Integer> DFS(){
        ArrayList<Integer> camino = new ArrayList<>();
        Iterator<Integer> vertices = this.listVerticesBFS.keySet().iterator();

        while (vertices.hasNext()){
               int vertice = vertices.next();
               listVerticesBFS.put(vertice, "blanco");
        }

        vertices = this.listVerticesBFS.keySet().iterator();

        while (vertices.hasNext()){
            int vertice = vertices.next();
            if(listVerticesBFS.get(vertice) == "blanco"){
                DFS(vertice, camino);
            }
        }
        return camino;
    }

    private void DFS(int vertice, ArrayList<Integer> camino) {
        this.listVerticesBFS.put(vertice, "amarillo");
        camino.add(vertice);
        Iterator<Integer> adyacentes = this.obtenerAdyacentes(vertice);

        while (adyacentes.hasNext()){
            int vAdyacente = adyacentes.next();
            if(this.listVerticesBFS.get(vAdyacente) == "blanco"){
                DFS(vAdyacente, camino);
            }
        }
        camino.remove(camino.size()-1);
    }

    /*Utilizamos BFS para encontrar el camino mas corto o mas barato entre 2 vertices*/

    public ArrayList<Integer> BFSforest(int d){
        Iterator<Integer> vertices = this.obtenerVertices();
        ArrayList<Integer> camino = new ArrayList<>();
        ArrayList<Integer> solucion = new ArrayList<>();

        while (vertices.hasNext()){
            int v = vertices.next();
            this.listVerticesBFS.put(v, "blanco");
        }

        vertices = this.obtenerVertices();

        while(vertices.hasNext()){
            int v = vertices.next();
            if (this.listVerticesBFS.get(v) == "blanco"){
                 BFSvisit(v,d,camino,solucion);
            }
        }
        return solucion;
    }

    private void BFSvisit(int v, int d, ArrayList<Integer> camino, ArrayList<Integer> solucion) {
        ArrayList<Integer>cola=new ArrayList<>();
        this.listVerticesBFS.replace(v,"amarillo");
        cola.add(v);
        Iterator<Integer>adyacentes;

        while (cola.get(0) != d || !cola.isEmpty()){
                adyacentes = this.obtenerAdyacentes(cola.get(0));
                camino.add(cola.get(0));
                cola.remove(0);
                while (adyacentes.hasNext()){
                    int ady = adyacentes.next();
                    if (this.listVerticesBFS.get(ady) == "blanco"){
                        cola.add(ady);
                        this.listVerticesBFS.replace(ady, "amarillo");
                    }
                }
        }
        solucion.addAll(camino);
    }
}