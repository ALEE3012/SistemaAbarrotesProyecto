package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSingleton {
    private static Connection conexion;

    private static final String URL = "jdbc:mysql://thomas.proxy.rlwy.net:26121/BD_Abarrotes";
    private static final String USER = "root";
    private static final String PASS = "vjYerGOFnXYpiBUVXovnLrVXdfkagavN"; // idealmente cámbiala después

    private ConexionSingleton() {
    }

    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexion = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("✅ Conexión Singleton exitosa a MySQL");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Error: No se encontró el Driver de MySQL (.jar)");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Error de SQL al intentar conectar a MySQL");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conexion;
    }
}