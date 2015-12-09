
import java.sql.*;

public class insertSQLite {

    /**
     * Procedimiento que inserta los datos requeridos en la
     * tabla_peliculas de la base de datos.
     * @param id identificador unico de una pelicula
     * @param strIp
     * @param databaseName
     * @param titulo nombre de la pelicula
     * @param fecha publicacion de la pelicula
     */
    public static void insertDataPeliculas(int id, String titulo, String fecha, String strIp, String databaseName, String usuario, String contrasenya){
        Connection c = null;
        PreparedStatement prpStmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://" + strIp + ":5432/" + databaseName ,usuario,contrasenya);
            c.setAutoCommit(false);
            System.out.println("INSERT Pelicula...Conexion establecida con base de datos practicaMovieDB");

            //stmt = c.createStatement();


            titulo = comillas(titulo);
            String insertSqlPeliculas = "INSERT INTO tabla_peliculas (idp, titulo, fecha) "
                    //+"VALUES (" + id + ",'" + titulo + "','" + fecha + "');";
                    +"VALUES(?,?,?)";
            //insertSqlPeliculas = comillas(insertSqlPeliculas);
            prpStmt = c.prepareStatement(insertSqlPeliculas);
            prpStmt.setInt(1, id);
            prpStmt.setString(2,titulo);
            prpStmt.setString(3, fecha);

            //stmt.executeUpdate(insertSqlPeliculas);
            //prpStmt.executeUpdate(insertSqlPeliculas);
            prpStmt.executeUpdate();

            //stmt.close();
            prpStmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("...Datos actualizados");
    }

    /**
     * Procedimiento que inserta los datos requeridos en la
     * tabla_actores de la base de datos.
     * @param id identificador unico del actor
     * @param nombre nombre del actor
     * @param id_actor identificador del actor que forma parte de json
     * @param personaje nombre del personaje
     * @param idPelicula identificador de la pelicula
     * @param strIp
     * @param databaseName
     * @param usuario
     * @param contrasenya
     */
    public static void insertDataActores(int id, String nombre, long id_actor, String personaje, int idPelicula, String strIp, String databaseName, String usuario, String contrasenya){
        Connection c = null;
        PreparedStatement prpStmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://" + strIp + ":5432/" + databaseName ,usuario,contrasenya);
            c.setAutoCommit(false);
            System.out.println("INSERT Actor...Conexion establecida con base de datos practicaMovieDB");

            //stmt = c.createStatement();

            nombre = comillas(nombre);
            personaje = comillas(personaje);
            String insertSqlActores = "INSERT INTO tabla_actores (ida,nombre,id_actor,personaje,id_Pelicula) "
                    //+"VALUES (" + id + ",'" + nombre + "'," + id_actor + ",'" + personaje + "'," + idPelicula + ");";
                    +"VALUES (?,?,?,?,?)";

            prpStmt = c.prepareStatement(insertSqlActores);
            prpStmt.setInt(1, id);
            prpStmt.setString(2, nombre);
            prpStmt.setLong(3, id_actor);
            prpStmt.setString(4, personaje);
            prpStmt.setInt(5, idPelicula);///////////////////////////////////
            //prpStmt.executeUpdate(insertSqlActores);
            prpStmt.executeUpdate();

            prpStmt.close();
            c.commit();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("...Datos actualizados\n");
    }

    /**
     * Funcion que cambia las comillas simples por espacios
     * en los campos char de la bbdd, par que no hayan conflictos
     * @param insert string para analizar
     * @return string sin comillas simples
     */
    public static String comillas(String insert){
        if(insert.contains("\'")){
            insert = insert.replace("\'", " ");
        }
        return insert;
    }

}
