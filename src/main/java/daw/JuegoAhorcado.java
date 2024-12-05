package daw;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.util.Random;

public class JuegoAhorcado {

    public static void main(String[] args) {

        String menu = """
                      ------------BIENVENIDO AL AHORCADO--------
                      Introduzca un número para elegir el modo:
                      1: MODO DOS JUGADORES
                      2: JUGAR CONTRA LA MÁQUINA
                      3: INSTRUCCIONES
                      4: EXIT
                      """;

        int opcion = 0;
        do {
            // Primero hacemos que la opción sea un String para poder pillar si
            // al meter la opcion sea null, pida la opcion de nuevo
            String opcionS = JOptionPane.showInputDialog(menu);

            if (opcionS == null) {
                JOptionPane.showInputDialog("Por favor, introduce una opcion");
                continue;
            }
            
            try {
                opcion = Integer.parseInt(opcionS); // Convierte la opcionS en 
                                                    // un int
                switch (opcion) {
                    case 1 ->
                        modoDosJugadores();
                    case 2 ->
                        modoContraMaquina();
                    case 3 ->
                        mostrarInstrucciones();
                    case 4 ->
                        JOptionPane.showMessageDialog(null, "Gracias por jugar. "
                                + "Hasta pronto!");
                    default ->
                        JOptionPane.showMessageDialog(null, "El número introducido"
                                + " no es válido.");
                }
            } catch (NumberFormatException nfe) {
                    //La excepción NumberFormatException se lanza para indicar 
                    //que la aplicación ha intentado convertir una cadena a uno 
                    //de los tipos numéricos, pero que la cadena no tiene el 
                    //formato adecuado.
                JOptionPane.showMessageDialog(null, "Por favor, introduce un "
                        + "número válido.");
            }
        } while (opcion != 4);
    }
    
    // Método para el modo dos jugadores
    public static void modoDosJugadores() {
        JOptionPane.showMessageDialog(null, "Has elegido el modo dos"
                + " jugadores");
        String jugador1 = JOptionPane.showInputDialog("Introduzca el "
                + "nombre del primer jugador");
        String jugador2 = JOptionPane.showInputDialog("Introduzca el "
                + "nombre del segundo jugador");

        String palabra;
        do {
            palabra = JOptionPane.showInputDialog(jugador1 + ", introduzca una "
                    + "palabra:");
            if (palabra == null || !contieneVocal(palabra)) {
                JOptionPane.showMessageDialog(null, "La palabra debe tener al "
                        + "menos una vocal");
                palabra = null;
            }

        } while (palabra == null);
        JOptionPane.showMessageDialog(null,jugador2+" "
                + ", es tu turno de jugar!!");
        jugarAhorcado(palabra, jugador2);
    }

    // Método para el modo contra la máquina
    public static void modoContraMaquina() {
        String[] palabras = listaPalabrasMaquina();
        // Elige una palabra de la cadena de palabras, pero coge una random
        // El next int hace que coja una de las posiciones de la cadena 
        // de palabras de la maquina
        String palabra = palabras[new Random().nextInt(palabras.length)];
        JOptionPane.showMessageDialog(null, "El robot ha elegido la palabra!");
        String jugador = JOptionPane.showInputDialog("Introduzca su nombre");
        jugarAhorcado(palabra, jugador);
    }

    // Método para jugar el ahorcado
    public static void jugarAhorcado(String palabra, String jugador) {
        
        int vidas = 7;
        // Crear lista de caracteres para la palabra oculta, las palabras 
        // seleccionadas y letras disponibles
        ArrayList<Character> palabraOculta = palabraGuiones(palabra);
        ArrayList<Character> letrasSeleccionadas = new ArrayList<>();
        ArrayList<Character> letrasDisponibles = letras();
        
        // Mostrar una vocal de ayuda
        char vocal = mostrarVocal(palabra);
        for (int i = 0; i < palabra.length(); i++) {
            if (palabra.charAt(i) == vocal) {
                palabraOculta.set(i, vocal);
            }
        }
        // Se pone la vocal de ayuda
        letrasSeleccionadas.add(vocal);
        // Removemos de la lista de letras disponibles la vocal de ayuda
        letrasDisponibles.remove((Character) vocal);

        while (vidas > 0) {
            mostrarJuego(palabraOculta, letrasSeleccionadas, letrasDisponibles,
                    vidas);
            mostrarHorca(vidas);

            String ponerLetra = JOptionPane.showInputDialog(jugador + ", introduce "
                    + "una letra:").toLowerCase(); // Pasamos a lower case por 
                                                   // si la letra es mayuscula

            // Si en la letra no se ha puesto nada o no pone una letra valida,
            // pide poner una letra valida
            if (ponerLetra == null || ponerLetra.length() != 1
                    || // Esto hace que el si el caracter no es una letra, pida
                    // también que introduzca una letra válida (La exclamacion
                    // lo niega)
                    !Character.isLetter(ponerLetra.charAt(0))) {
                JOptionPane.showMessageDialog(null, "Introduce una letra válida.");
                continue;
            }

            char letra = ponerLetra.charAt(0);
            
            if (letrasSeleccionadas.contains(letra)) {
                JOptionPane.showMessageDialog(null, "Ya seleccionaste esa letra.");
                continue;
            }
            
            // Añade la letra a la lista de palabras seleccionadas
            letrasSeleccionadas.add(letra);
            // Quita la letra de la lista de palabras disponibles
            letrasDisponibles.remove((Character) letra);

            if (palabra.contains(String.valueOf(letra))) {
                palabraOculta = ponerLetra(palabraOculta, letra, palabra);
                // Comprueba que la palabra no contiene guiones bajos, porque si
                // no contiene significa que ya ha ganado
                if (!palabraOculta.contains('_')) {
                    JOptionPane.showMessageDialog(null, "Olee :D! \n" + jugador  
                            + "\nHas adivinado la palabra!");
                    break;
                }
            } else {
                // Resta las vidas
                vidas--;
                if (vidas == 0) {
                    mostrarHorca(vidas);
                    JOptionPane.showMessageDialog(null, """
                                                        Has perdido :( !
                                                        La palabra era: """ + 
                                                        palabra);
                    
                }
            }
        }
    }

    // Mostrar el estado del juego
    public static void mostrarJuego(ArrayList<Character> palabraOculta,
            ArrayList<Character> seleccionadas, ArrayList<Character> disponibles,
            int vidas) {
        String estado = """
                        Palabra: %s
                        Letras seleccionadas: %s
                        Letras disponibles: %s
                        Vidas restantes: %d
                        """.formatted(palabraOculta, seleccionadas, disponibles,
                            vidas);
        
        JOptionPane.showMessageDialog(null, estado);
    }
    
    public static String[] listaPalabrasMaquina() {
        String[] listaPalabras = {"hola", "telefono", "dedo", "casino", "pantalla",
            "sobaco", "pokemon", "esternocleidomastoideo", "vico", "locomotora",
            "bohemio","pincelada"};
        
        return listaPalabras;
    }
    
    public static ArrayList<Character> palabraGuiones(String palabra) {
        ArrayList<Character> guiones = new ArrayList<>();
        for (int i = 0; i < palabra.length(); i++) {
            guiones.add('_');
        }
        return guiones;
    }
    
    public static ArrayList<Character> letras() {
        ArrayList<Character> letras = new ArrayList<>();
        // Bucle del abecedario
        for (char c = 'a'; c <= 'z'; c++) {          
                letras.add(c);
        }
        return letras;
    }
    
    public static ArrayList<Character> ponerLetra(ArrayList<Character> 
            palabraGuiones, char letra, String palabra) {
        for (int i = 0; i < palabra.length(); i++) {
            if (palabra.charAt(i) == letra) {
                palabraGuiones.set(i, letra);
            }
        }
        return palabraGuiones;
    }
    
    public static boolean contieneVocal(String palabra) {
        for (char c : palabra.toCharArray()) {
            // indexOf retorna el primer índice en el que se puede encontrar un  
            // elemento dado en el array
            // Esto lo que hace es recorrer la String de vocales y al encontrar
            // el primer caracter en la palabra, retorna el caracter con la letra
            // El que no sea -1 significa que si o si tiene que haber una vocal
            if ("aeiou".indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    }
    
    public static char mostrarVocal(String palabra) {
        for (char c : palabra.toCharArray()) {
            if ("aeiou".indexOf(c) != -1) {
                return c;
            }
        }
        return '_'; // No debería suceder porque siempre hay una vocal
    }
    
    public static void mostrarInstrucciones() {
        JOptionPane.showMessageDialog(null, "Has elegido mostrar las"
                + " intrucciones");
        JOptionPane.showMessageDialog(null, """
                   Adivinar una palabra en el menor numero de intentos posibles. 
                   Primero se deben marcar tantas rayas como letras tenga la 
                   palabra pensada.
                   Los jugadores tienen que ir diciendo letras para formar la palabra.
                   Se dibuja una horca y los jugadores van diciendo letras.
                   Un jugador tiene un maximo de 7 vidas.""");
    }
    
    // Método para mostrar la horca
    public static void mostrarHorca(int vidas) {
        String horca;
        switch (vidas) {
            case 7 ->
                horca = """
                            -----
                            |   |
                                |
                                |
                                |
                                |
                            -----
                            """;
            case 6 ->
                horca = """
                            -----
                            |   |
                           O  |
                                |
                                |
                                |
                            -----
                            """;
            case 5 ->
                horca = """
                            -----
                            |   |
                           O  |
                            |   |
                                |
                                |
                            -----
                            """;
            case 4 ->
                horca = """
                            -----
                            |   |
                           O  |
                           /|   |
                                |
                                |
                            -----
                            """;
            case 3 ->
                horca = """
                            -----
                            |   |
                           O  |
                           /|\\  |
                                |
                                |
                            -----
                            """;
            case 2 ->
                horca = """
                            -----
                            |   |
                           O  |
                           /|\\  |
                           /    |
                                |
                            -----
                            """;
            case 1 ->
                horca = """
                            -----
                            |   |
                           O  |
                           /|\\  |
                           / \\  |
                                |
                            -----
                            """;
            default ->
                horca = """
                        ----HAS MUERTO!!!----
                            -----
                            |   |
                           O  |
                           /|\\  |
                           / \\  |
                                |
                            -----
                            """;
        }
        JOptionPane.showMessageDialog(null, horca);
    }
}
