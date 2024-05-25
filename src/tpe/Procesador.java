package tpe;

public class Procesador {

    private String id;
    private String codigo;
    private Boolean estaRefrigerado;
    private int anioFuncionamiento;

    public Procesador(String id, String codigo, Boolean estaRefrigerado, int anioFuncionamiento) {
        this.id = id;
        this.codigo = codigo;
        this.estaRefrigerado = estaRefrigerado;
        this.anioFuncionamiento = anioFuncionamiento;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public Boolean getEstaRefrigerado() {
        return estaRefrigerado;
    }
    public void setEstaRefrigerado(Boolean estaRefrigerado) {
        this.estaRefrigerado = estaRefrigerado;
    }
    public int getAnioFuncionamiento() {
        return anioFuncionamiento;
    }
    public void setAnioFuncionamiento(int anioFuncionamiento) {
        this.anioFuncionamiento = anioFuncionamiento;
    }
}
