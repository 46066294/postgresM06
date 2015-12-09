
import java.sql.*;

public class createSQLite {

    protected String strIp, databaseName, usuario, contrasenya;
    public createSQLite(String strIp, String databaseName, String usuario, String contrasenya){
        this.strIp = strIp;
        this. databaseName = databaseName;
        this.usuario = usuario;
        this.contrasenya = contrasenya;
        createTables();
    }

    /**
     * Crea las tablas de la base de datos
     *  - tabla de peliculas
     *  - tabla de actores
     */
    public void createTables(){
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://" + strIp + ":5432/" + databaseName ,usuario,contrasenya);
            System.out.println("CREATE...Conexion establecida con base de datos practicaMovieDB");

            stmt = c.createStatement();

            //tabla peliculas
            String createSqlPeliculas = "CREATE TABLE tabla_peliculas "
                    + " (idp      INT           PRIMARY KEY NOT NULL,"
                    + " titulo   CHAR(50),"
                    + " fecha    CHAR(20))";
            stmt.executeUpdate(createSqlPeliculas);

            //tabla actores
            String sqlActores = "CREATE TABLE tabla_actores "
                    + "(ida             INT          PRIMARY KEY NOT NULL,"
                    + " nombre         CHAR(50),"
                    + " id_actor       INT,"
                    + " personaje      CHAR(50),"
                    + " id_pelicula    INT)";
            stmt.executeUpdate(sqlActores);

            stmt.close();
            c.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("...Tablas creadas con exito");
    }

}
