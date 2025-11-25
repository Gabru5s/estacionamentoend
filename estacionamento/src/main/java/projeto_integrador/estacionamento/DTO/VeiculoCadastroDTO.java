package projeto_integrador.estacionamento.DTO;

public class VeiculoCadastroDTO {

    private String categoria;
    private String placa;
    private String montadora;
    private String modelo;

    public VeiculoCadastroDTO() {
    }

    public VeiculoCadastroDTO(String categoria, String placa, String montadora, String modelo) {
        this.categoria = categoria;
        this.placa = placa;
        this.montadora = montadora;
        this.modelo = modelo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMontadora() {
        return montadora;
    }

    public void setMontadora(String montadora) {
        this.montadora = montadora;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
}
