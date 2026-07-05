package Utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://thomas.proxy.rlwy.net:26121/BD_Abarrotes";
    private static final String USER = "root";
    private static final String PASS = "vjYerGOFnXYpiBUVXovnLrVXdfkagavN"; // cámbiala después de probar

    public static Connection conectar() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("✅ Conexión exitosa");
        } catch (SQLException e) {
            System.out.println("❌ Error de conexión");
            e.printStackTrace();
        }
        return con;
    }
}