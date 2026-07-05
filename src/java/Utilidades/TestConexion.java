package Utilidades;

import java.sql.Connection;

public class TestConexion {

    public static void main(String[] args) {

        Connection cn = Conexion.conectar();

        if (cn != null) {
            System.out.println("✅ Conexión establecida correctamente.");
        } else {
            System.out.println("❌ No se pudo conectar a la base de datos.");
        }
    }
}