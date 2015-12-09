
import java.sql.*;

/**
 * Consultas
 */
public class selectSQLite {
    protected String strIp, databaseName, usuario, contrasenya;

    public selectSQLite(String strIp, String databaseName, String usuario, String contrasenya){
        this.strIp = strIp;
        this. databaseName = databaseName;
        this.usuario = usuario;
        this.contrasenya = contrasenya;
        listadoPeliculas();
        listadoActores();
    }

    /**
     * Conexion a la bbdd para imprimir por pantalla
     * todo el listado de peliculas
     */
    public void listadoPeliculas(){
        Connection c = null;
        Statement stmt = null;
        try {
            //Class.forName("org.sqlite.JDBC");
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://" + strIp + ":5432/" + databaseName ,usuario,contrasenya);
            c.setAutoCommit(false);
            System.out.println("SELECT P...Conexion establecida con base de datos practicaMovieDB");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM tabla_peliculas;" );
            while ( rs.next() ) {
                int id = rs.getInt("idp");
                String  titulo = rs.getString("titulo");
                String fecha = rs.getString("fecha");

                System.out.println("ID: " + id + "\tTITULO: " + titulo + "\tFECHA: " + fecha);
            }

            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("...Consulta realizada con exito\n");
    }

    /**
     * Conexion a la bbdd para imprimir por pantalla
     * todo el listado de actores
     */
    public void listadoActores(){
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://" + strIp + ":5432/" + databaseName ,usuario,contrasenya);
            c.setAutoCommit(false);
            System.out.println("SELECT A...Conexion establecida con base de datos practicaMovieDB");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM tabla_actores;" );
            while ( rs.next() ) {
                int id = rs.getInt("ida");
                String  nombre = rs.getString("nombre");
                int id_actor = rs.getInt("id_actor");
                String personaje = rs.getString("personaje");
                int id_pelicula = rs.getInt("id_pelicula");

                System.out.println("ID: " + id + "\tNOMBRE: " + nombre + "\tID_ACTOR: " +
                        id_actor + "\tPERSONAJE: " + personaje + "\tID_PELICULA: " + id_pelicula);
            }

            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("...Consulta realizada con exito");
    }

    /**
     * En el primer mode es mostrar� un llistat numerat de les pel�l�cules. Entrant per consola el
     * n�mero identificador de la pel�l�cula, es mostrar� la informaci� (t�tol, data d'estrena i
     * personatges).
     * @param idpConsole identificador unico de pelicula
     */
    public void mode1(int idpConsole){
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://" + strIp + ":5432/" + databaseName ,usuario,contrasenya);
            c.setAutoCommit(false);
            System.out.println("\nQUERY MODE_1...Conexion establecida con base de datos practicaMovieDB");

            stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT titulo, fecha, personaje " +
                     "FROM tabla_peliculas, tabla_actores " +
                     "WHERE tabla_peliculas.idp = tabla_actores.id_pelicula "+
                     "AND tabla_peliculas.idp = " + idpConsole + ";");

            System.out.println("PERSONAJES:");
            int cont = 0;
            while (rs.next()) {//por cada row
                if(cont == 0){
                    String titulo = rs.getString("titulo");
                    String fecha = rs.getString("fecha");
                    System.out.println("TITULO: " + titulo + "\nFECHA LANZAMIENTO: " + fecha);

                    System.out.println("PERSONAJES:");
                    cont++;
                }
                String  personaje = rs.getString("personaje");
                System.out.println("\t-" + personaje);
            }

            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("...Consulta --mode1-- realizada con exito");
    }

    /**
     * En el segon mode es mostrar� un llistat numerat d'actors. Entrant per consola el n�mero
     * identificador de l'actor, �s mostrar� el llistat de pel�l�cules que ha fet (i que estan guardades
     * en la base de dades). Heu de fer que al menys d'un actor hi hagi dos pel�l�cules.
     * @param idaConsole identificador unico de actor
     */
    public void mode2(int idaConsole){
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://" + strIp + ":5432/" + databaseName ,usuario,contrasenya);
            c.setAutoCommit(false);
            System.out.println("\nQUERY MODE_2...Conexion establecida con base de datos practicaMovieDB");

            stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT titulo, nombre " +
                    "FROM tabla_peliculas, tabla_actores " +
                    "WHERE tabla_peliculas.idp = tabla_actores.id_pelicula " +
                    "AND tabla_actores.nombre='" + coincidirActor(idaConsole) + "';");

            System.out.println("PELICULAS:");

            int cont2 = 0;
            while (rs.next()) {//por cada row
                if(cont2 == 0){
                    String nombre = rs.getString("nombre");
                    System.out.println("ACTOR: " + nombre);
                    cont2++;
                }
                String pelicula = rs.getString("titulo");
                System.out.println("\t-" + pelicula);
            }

            rs.close();
            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("...Consulta --mode2-- realizada con exito");
    }

    /**
     * A partir de un identificador id de un actor,
     * devuelve el nombre del actor
     * @param idaConsole id unico de actor
     * @return nombre del actor
     */
    public String coincidirActor(int idaConsole){
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://" + strIp + ":5432/" + databaseName ,usuario,contrasenya);
            c.setAutoCommit(false);
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ida, nombre " +
                    "FROM tabla_actores " +
                    "WHERE tabla_actores.ida=" + idaConsole);

            String strActor = null;
            while (rs.next()) {//por cada row
                strActor = rs.getString("nombre");
            }

            rs.close();
            stmt.close();
            c.close();

            return strActor;

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }

        return "not match";
    }





}



/*
import java.sql.*;


public class selectSQLite {

    public static void main(String[] args) {
            Connection c = null;
            Statement stmt = null;
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:practicaMovieDB.db");
                c.setAutoCommit(false);
                System.out.println("...Conexion establecida con base de datos practicaMovieDB");

                stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery( "SELECT * FROM COMPANY;" );
                while ( rs.next() ) {
                    int id = rs.getInt("id");
                    String  name = rs.getString("name");
                    int age = rs.getInt("age");
                    String  address = rs.getString("address");
                    float salary = rs.getFloat("salary");
                    System.out.println( "ID = " + id );
                    System.out.println( "NAME = " + name );
                    System.out.println( "AGE = " + age );
                    System.out.println( "ADDRESS = " + address );
                    System.out.println( "SALARY = " + salary );
                    System.out.println();
                }
                rs.close();
                stmt.close();
                c.close();
            } catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
            }
            System.out.println("Operation done successfully");
        }

}
*/