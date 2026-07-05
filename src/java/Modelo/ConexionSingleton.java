package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSingleton {

    private static Connection conexion;

    // Constructor privado para evitar instancias de la clase (Patrón Singleton)
    private ConexionSingleton() {
    }

    public static Connection getConexion() {
        try {
            // Verificamos si la conexión es nula o se ha cerrado por inactividad
            if (conexion == null || conexion.isClosed()) {
                
                // 1. Cargar el Driver de MySQL (Obligatorio en aplicaciones web)
                Class.forName("com.mysql.cj.jdbc.Driver");

                // 2. Establecer la conexión usando tu URL, usuario y contraseña de MySQL
                conexion = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/BD_Abarrotes",
                        "root",
                        "301223"
                );
                
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