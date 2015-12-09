import java.util.Scanner;
import java.util.regex.Pattern;

/**Ejecucion de todo el programa
 *
 * Menu con dos opciones:
 *  1. MONTAR BASE DE DATOS
 *  2. SOLO CONSULTAS
 */
public class EjecutableBBDD {

    public static void main(String[] args) {
        try{
            boolean salidaPrograma = false;//controla el bucle de salida del programa

            Scanner input = new Scanner(System.in);
            Scanner ip = new Scanner(System.in);
            Scanner database = new Scanner(System.in);
            Scanner user = new Scanner(System.in);
            Scanner pass = new Scanner(System.in);

            System.out.println("--CONEXION A BASE DE DATOS POSTGRES--\n");
            //c = DriverManager.getConnection("jdbc:postgresql://192.168.1.111:5432/marc","marc","marc");

            System.out.println("Introduzca IP de la maquina a conectarse:");
            String strIp = ip.nextLine();
            if(!validate(strIp)){
                System.out.println("...formato de IP incorrecto");
                input.close();
                ip.close();
                database.close();
                user.close();
                pass.close();

                System.exit(0);
            }
            System.out.println("Introduzca el nombre de la base de datos:");
            String databaseName = database.nextLine();
            System.out.println("Usuario:");
            String usuario = user.nextLine();
            System.out.println("Contrasenya;");
            String contrasenya = pass.nextLine();

            while(!salidaPrograma){
                System.out.println("\n1. MONTAR BASE DE DATOS");
                System.out.println("2. SOLO CONSULTAS");
                int opcio = input.nextInt();

                if(opcio == 1){

                    createSQLite creaTablasBD = new createSQLite(strIp, databaseName, usuario,contrasenya);
                    themovieDBproject accesoApi = new themovieDBproject(strIp, databaseName, usuario, contrasenya);
                }
                else if(opcio == 2){
                    selectSQLite select = new selectSQLite(strIp, databaseName, usuario, contrasenya);

                    System.out.println("\nMODE1:: Llistat numerat de les pel-licules.\nEntrant per consola el" +
                            " numero identificador de la pel-licula,\nes mostrara la informacio (titol, data d-estrena i" +
                            " personatges).\nEntra numero entre 0 i 10:");
                    int m1 = input.nextInt();

                    select.mode1(m1);

                    System.out.println("\nMODE2::  llistat numerat d-actors.\nEntrant per consola el numero" +
                            "identificador de l'actor,\nes mostrara el llistat de pel-licules que ha fet (i que estan guardades\n" +
                            "en la base de dades). EL ACTOR QUE SALE EN DOS PELICULAS ES BILL MURRAY! (ID = 0)\nEntra numero:");
                    int m2 = input.nextInt();

                    select.mode2(m2);

                    Scanner salida = new Scanner(System.in);
                    System.out.println("\n...SALIR DEL PROGRAMA? (s/n)");
                    String sal = salida.nextLine();
                    sal.toLowerCase();
                    if(sal.contains("s")) salidaPrograma = true;
                }
                else{
                    System.out.println("...opcion incorrecta");
                }
            }


            input.close();
            ip.close();
            database.close();
            user.close();
            pass.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public static boolean validate(final String ip) {
        return PATTERN.matcher(ip).matches();
    }

}
