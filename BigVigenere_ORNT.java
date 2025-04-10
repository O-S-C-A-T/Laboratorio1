import java.util.*;
import java.io.*;

public class BigVigenere{
    private int[] key;
    private char[][] alphabet;
    private String clave;

    public BigVigenere(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese su clave: ");
        this.clave = sc.nextLine();
        System.out.println(" ");
        generarMatriz();
    }

    public BigVigenere(String numericKey){
        this.clave = numericKey;
        generarMatriz();
    }

    private int[] extenderClave(String message){
        int[] keyExtendida = new int[message.length()];
        int Largo = clave.length();

        for(int i=0; i<message.length(); i++){
            char c = clave.charAt(i % Largo);
            for(int j=0; j<64; j++){
                if(alphabet[0][j] == c){
                    keyExtendida[i] = j;
                    break;
                }
            }
        }
        return keyExtendida;
    }

    private void generarMatriz(){
        ArrayList<Character> alfabeto2 = new ArrayList<>();
        String alfabeto = "abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ0123456789";

        for(int i = 0; i < 64; i++){
            alfabeto2.add((alfabeto.charAt(i)));
        }

        this.alphabet = new char[64][64];

        for(int i = 0; i < 64; i++){
            for(int j = 0; j < 64; j++){
                this.alphabet[i][j] = alfabeto2.get(j);
            }
            char primero = alfabeto2.get(0);
            alfabeto2.remove(0);
            alfabeto2.add(primero);
        }
        for(int i = 0; i < 64; i++){
            for(int j = 0; j < 64; j++){
                System.out.print(this.alphabet[i][j] + " ");
            }
            System.out.println(" ");
        }
    }

    public String encrypt(String message){
        this.key = extenderClave(message);
        System.out.println(Arrays.toString(this.key));
        StringBuilder cifrado = new StringBuilder();

        for(int i=0; i<message.length(); i++){
            char c = message.charAt(i);
            int letraIndice = buscarIndice(c);

            if(letraIndice != -1){
                int claveIndice = this.key[i];
                char letraCifrado = alphabet[claveIndice][letraIndice];
                cifrado.append(letraCifrado);
            } else if (letraIndice == -1){
                cifrado.append(c);
            }
        }
        return cifrado.toString();
    }

    public String decrypt(String encryptedMessage){
        this.key = extenderClave(encryptedMessage);
        StringBuilder decryptedMessage = new StringBuilder();
        int keyIndex = 0;

        for(int i=0; i<encryptedMessage.length(); i++){
            char c = encryptedMessage.charAt(i);
            int k = key[keyIndex % key.length];

            int indiceog = -1;
            for(int j=0; j<64; j++) {
                if (alphabet[k][j] == c) {
                    indiceog = j;
                    break;
                }
            }
            if(indiceog != -1){
                c = alphabet[0][indiceog];
                decryptedMessage.append(c);
            }
            else if (indiceog == -1){
                decryptedMessage.append(c);
            }
            keyIndex++;
        }
        return decryptedMessage.toString();

    }

    private int buscarIndice(char c){
        for(int j=0; j<64; j++){
            if(this.alphabet[0][j] == c){
                return j;
            }
        }
        return -1;
    }

    public void reEncrypt(){
        System.out.println("Ingrese mensaje encriptado: ");
        Scanner sc = new Scanner(System.in);
        String encrypted = sc.nextLine();
        String decrypted = decrypt(encrypted);
        System.out.println(decrypted);
        System.out.println("Ingrese la nueva clave: ");
        clave = sc.nextLine();
        this.key = extenderClave(decrypted);
        encrypted = encrypt(decrypted);
        System.out.println(encrypted);

    }

    public char search(int position){
        int indice = 0;

        for(int i=0; i<64; i++){
            for(int j=0; j<64; j++){
                if(indice == position){
                    return alphabet[i][j];
                }
                indice++;
                }
            }
        throw new IllegalArgumentException("No hay ningun termino con ese indice");
        // si no hay termino, retorna un error.
        }


    public char optimalSearch(int position){
        if(position == -1 || position > 4096){
        }
        else{
            int fila = position / 64;
            int columna = position % 64;
            return alphabet[fila][columna];
        }
        throw new IllegalArgumentException("No hay ningun termino con ese indice");
        //si no existe el termino tira un error.
    }
}

class Main { // modificar para probar métodos
    public static void main(String[] args) {
        String mensaje = generarMensaje(10000);
        int[] tamañosClave = {10, 50, 100, 500, 1000, 5000};

        System.out.println("Tamaño Clave, Cifrado(ms), Descifrado(ms)");
        for (int i=0; i<tamañosClave.length; i++){
            int tamaño = tamañosClave[i];
            String clave = generarClaveAleatoria(tamaño);

            BigVigenere v = new BigVigenere(clave);

            long inicio = System.nanoTime();
            String cifrado = v.encrypt(mensaje);
            long fin = System.nanoTime();
            long tiempoCifrado = (fin - inicio) / 1000000;

            inicio = System.nanoTime();
            String descifrado = v.decrypt(cifrado);
            fin = System.nanoTime();
            long tiempoDescifrado = (fin - inicio) / 1000000;

            System.out.println(tamaño + "," + tiempoCifrado + "," + tiempoDescifrado);

        }

    }

    private static String generarClaveAleatoria(int largo){
        String alfabeto = "abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ0123456789";
        Random r = new Random();
        StringBuilder clave = new StringBuilder(largo);
        for(int i=0; i<largo; i++){
            clave.append(alfabeto.charAt(r.nextInt(64)));
        }
        return clave.toString();
    }

    private static String generarMensaje(int largo){
        String alfabeto = "abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ0123456789";
        Random r = new Random();
        StringBuilder mensaje = new StringBuilder();
        for(int i=0; i<largo; i++){
            mensaje.append(alfabeto.charAt(r.nextInt(64)));
        }
        return mensaje.toString();
    }
    }
