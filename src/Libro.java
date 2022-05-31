import java.io.Serializable;

public class Libro implements Serializable {
	private String titulo;
	private String ambito;
	private double precio;
	
	public Libro(String titulo, String ambito, double precio) {
		super();
		this.titulo = titulo;
		this.ambito = ambito;
		this.precio = precio;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getAmbito() {
		return ambito;
	}

	public double getPrecio() {
		return precio;
	}
	
	@Override
	public String toString() {
		return "Titulo=" + titulo + " Precio=" + precio;
	}
}
