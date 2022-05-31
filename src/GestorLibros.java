import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class GestorLibros {
	/**
	 * Se lee el archivo con los objetos pasados por parámetros y se convierte a un HashMap
	 * con la clave siendo su título, evitando repetidos. Si los hay, se reemplaza por el más
	 * barato
	 * @param archivo El nombre del archivo
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void mostrarLibrosEficiente(String archivo) throws FileNotFoundException, IOException {
		ArrayList<Libro> libros = leerLibrosObjetos(archivo);
		
		HashMap<String, Libro> mapaLibros = new HashMap<String, Libro>();
		for (Libro libro : libros) {
			String titulo = libro.getTitulo();
			if (mapaLibros.containsKey(titulo)) {	
				if (libro.getPrecio() >= mapaLibros.get(titulo).getPrecio()) {
					continue;
				}
			}

			mapaLibros.put(titulo, libro);
		}
		
		for(String titulo : mapaLibros.keySet()) {
			Libro libro = mapaLibros.get(titulo);
			System.out.println("Titulo=" + titulo + " Tema=" + libro.getAmbito() + " Precio=" + libro.getPrecio());
		}
	}
	
	/**
	 * Mostrar los libros de un archivo de objetos
	 * @param archivo Archivo de objetos
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void mostrarLibros(String archivo) throws FileNotFoundException, IOException {
		ArrayList<Libro> libros = leerLibrosObjetos(archivo);
		for (Libro libro : libros) {
			System.out.println(libro.toString());
		}
	}
	
	/**
	 * 
	 * @param archivo El archivo de texto
	 * @param archivoObjeto El archivo con los objetos
	 * @param ambito El ámbito de los libros
	 * @param sobreescribir Sobreescribir el archivo
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void convertirDatos(String archivo, String archivoObjeto, String ambito, boolean sobreescribir) throws FileNotFoundException, IOException {
		GestorLibros.guardarLibros(GestorLibros.leerLibros(archivo), archivoObjeto, ambito, sobreescribir, false);
	}
	
	/**
	 * 
	 * @param archivo Nombre o ruta del archivo
	 * @return Si existe el archivo
	 */
	public static boolean existeArchivo(String archivo) {
		return new File(archivo).exists();
	}
	
	/**
	 * Se lee el archivo de texto pasado por parámetro con los libros y se pasa a un ArrayList de clase Libro
	 * @param archivo El archivo de texto
	 * @return ArrayList de Libro
	 * @throws FileNotFoundException
	 */
	private static ArrayList<Libro> leerLibros(String archivo) throws FileNotFoundException {
		File f = new File(archivo);
		
		Scanner sc = new Scanner(f);
		
		ArrayList<Libro> libros = new ArrayList<Libro>();
		
		while(sc.hasNextLine()) {
			String linea = sc.nextLine();
			
			String[] datos = linea.split("#");
			String titulo = datos[0], ambito = datos[1];
			double precio = Double.valueOf(datos[2]);
			
			libros.add(new Libro(titulo, ambito, precio));
		}
		
		sc.close();
		
		return libros;
	}
	
	/**
	 * Se lee un archivo con los objetos de Libro pasado por parámetro y se pasa un ArrayList de clase Libro
	 * @param archivo El archivo de objetos
	 * @return ArrayList de Libro con los libros
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static ArrayList<Libro> leerLibrosObjetos(String archivo) throws FileNotFoundException, IOException {
		ArrayList<Libro> libros = new ArrayList<Libro>();
		
		ObjectInputStream obj = new ObjectInputStream(new FileInputStream(archivo));
		
		try {
			Libro libro = null;
			while((libro = (Libro)obj.readObject()) != null) {
				libros.add(libro);
			}
		} catch(EOFException eof) {
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return libros;
	}
	
	/**
	 * Añadir un libro especificando el archivo donde guardarlo, el título, ámbito y precio del libro
	 * @param archivo El archivo de objetos
	 * @param titulo Título
	 * @param ambito Ámbito
	 * @param precio Precio
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void añadirLibro(String archivo, String titulo, String ambito, double precio) throws FileNotFoundException, IOException {
		guardarLibro(archivo, new Libro(titulo, ambito, precio));
	}
	
	/**
	 * Guardar un objeto Libro a un archivo de objetos. Se crea un ArrayList que lo contiene y se guarda
	 * @param archivo El archivo de objetos
	 * @param libro El libro en objeto
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void guardarLibro(String archivo, Libro libro) throws FileNotFoundException, IOException {
		ArrayList<Libro> libros = new ArrayList<Libro>();
		
		libros.add(libro);
		
		guardarLibros(libros, archivo, libro.getAmbito(), false, false);
	}
	
	/**
	 * Borrar un libro a partir de un ámbito o mantener los libros con ese ámbito y borrar el resto
	 * @param archivo El archivo de objetos
	 * @param ambito El ambito a eliminar
	 * @param inverso Eliminar libros coincidentes con el ambito o no coincidentes
	 * @return Número de libros borrados
	 * @throws IOException
	 */
	public static int borrarLibro(String archivo, String ambito, boolean inverso) throws IOException {
		return guardarLibros(leerLibrosObjetos(archivo), archivo, ambito, true, inverso);
	}
	
	/**
	 * Guardar un ArrayList de objetos de clase Libro a un archivo de objetos
	 * @param libros ArrayList con los libros a guardar
	 * @param archivo El nombre del archivo de objetos en el que deseamos guardarlos
	 * @param ambito Los libros coincidentes con el ambito o vacio para guardar todos
	 * @param sobreescribir Sobreescribir el archivo
	 * @param inverso Invertir la busqueda de ámbito
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static int guardarLibros(ArrayList<Libro> libros, String archivo, String ambito, boolean sobreescribir, boolean ambitoInverso) throws FileNotFoundException, IOException {
		File f = new File(archivo);
		
		ObjectOutputStream obj = null;
		
		if (f.exists() && !sobreescribir) {
			obj = new MiObjectOutputStream(new FileOutputStream(f, !sobreescribir));
		}
		else
			obj = new ObjectOutputStream(new FileOutputStream(f, !sobreescribir));

		int conteo = 0;
		for(Libro libro : libros) {
			if (!ambito.equals(libro.getAmbito()) && 
					!ambito.equals("") && !ambitoInverso) {
				conteo++;
				
				continue;
			}
			
			obj.writeObject(libro);
		}
		
		obj.close();
		
		return conteo;
	}
}
