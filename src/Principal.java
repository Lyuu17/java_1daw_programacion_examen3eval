import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Principal {

	private static String archivoDatos = "datos.txt", archivoObjetos = "librosPsicologia.obj";
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		boolean bucle = true;
		do {
			System.out.println("");
			
			System.out.println("Archivos de trabajo: ");
			System.out.println(" Datos: " + archivoDatos + ", objetos: " + archivoObjetos);
			System.out.println("0 - Cambiar archivos");
			System.out.println("1 - Convertir Datos");
			System.out.println("2 - Añadir libro");
			System.out.println("3 - Borrar libro");
			System.out.println("4 - Mostrar libros");
			System.out.println("5 - Mostrar libros (EFICIENTE)");
			System.out.println("6 - Salir");
			
			System.out.print(" > ");
			
			String opcion = sc.nextLine();
			
			switch(Integer.valueOf(opcion)) {
			default:
			case 0:
				cambiarArchivos(sc);
				
				break;
			case 1:
				convertirDatos(sc);
				
				break;
			case 2:
				añadirLibro(sc);
				
				break;
			case 3:
				borrarLibro(sc);
				
				break;
			case 4:
				mostrarLibros(sc);
				
				break;
			case 5:
				mostrarLibrosEficiente(sc);
				
				break;
			case 6:
				bucle = false;
				
				break;
			}
		} while(bucle);
		
		sc.close();
	}
	
	public static void cambiarArchivos(Scanner sc) {
		System.out.print("Introduzca el archivo con los datos [\"datos.txt\"]: ");
		
		archivoDatos = sc.nextLine();
		
		System.out.print("Introduzca el archivo para los objetos [\"librosPsicologia.obj\"]: ");

		archivoObjetos = sc.nextLine();
	}
	
	public static void convertirDatos(Scanner sc) {
		try {
			System.out.print("Introduzca el ámbito [Defecto: \"psychology\"]: ");
			
			String ambito = sc.nextLine();
			ambito = ambito.equals("") ? "psychology" : ambito;
			
			boolean sobreescribir = false;
			if (GestorLibros.existeArchivo(archivoObjetos)) {
				System.out.print("¿Sobreescribir archivo actual? (S/N) [Defecto: S]: ");
				
				String respuesta = sc.nextLine();
				sobreescribir = respuesta.toLowerCase().equals("n") ? false : true;
			}
			
			GestorLibros.convertirDatos(archivoDatos, archivoObjetos, ambito, sobreescribir);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void añadirLibro(Scanner sc) {
		boolean seguir = true;
		while(seguir) {
			System.out.print("Título: ");
			
			String titulo = sc.nextLine();
			
			System.out.print("Ámbito: ");
			
			String ambito = sc.nextLine();
			
			System.out.print("Precio: ");
			
			String precio = sc.nextLine();
			
			try {
				GestorLibros.añadirLibro(archivoObjetos, titulo, ambito, Double.valueOf(precio));
			} catch (NumberFormatException e) {
				System.out.println("ERROR: No se ha introducido un precio correcto.");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.print("¿Añadir más libros? (S/N) [Defecto: N]");
			
			String respuesta = sc.nextLine();
			seguir = respuesta.toLowerCase().equals("n") ? true : false;
		}
		
		mostrarLibros(sc);
	}
	
	public static void borrarLibro(Scanner sc) {
		System.out.print("Ámbito [Defecto: \"psychology\"]: ");
		
		String ambito = sc.nextLine();
		ambito = ambito.equals("") ? "psychology" : ambito;
		
		System.out.print("¿Mantener solo los libros con el ámbito introducido [S] o eliminarlos [N]? (S/N) [Defecto: S]: ");
		
		String respuesta = sc.nextLine();
		boolean invertir = respuesta.toLowerCase().equals("n") ? true : false;
		
		try {
			int borrados = GestorLibros.borrarLibro(archivoObjetos, ambito, invertir);
			
			if (borrados > 0) {
				System.out.println("Se han borrado " + borrados + " libros");
			}
			else
				throw new PsicologiaException("Todos los libros almacenados en el fichero son del ambito " + ambito);
			
			System.out.println("El contenido del fichero tras borrar los libros que no son de " + ambito + " es: ");
			GestorLibros.mostrarLibros(archivoObjetos);
		} catch (NumberFormatException e) {
			System.out.println("ERROR: No se ha introducido un precio correcto.");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PsicologiaException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void mostrarLibros(Scanner sc) {
		try {
			System.out.println("Los libros de psicología son: ");
			
			GestorLibros.mostrarLibros(archivoObjetos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void mostrarLibrosEficiente(Scanner sc) {
		try {
			System.out.println("El contenido de la colección es: ");
			
			GestorLibros.mostrarLibrosEficiente(archivoObjetos);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
