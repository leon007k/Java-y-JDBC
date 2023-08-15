package com.alura.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreaConexion {
    public Connection recuperarConexion() throws SQLException {
        // * Abrimos conexion
        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost/control_de_stock?useTimeZone=true&serverTimeZone=UTC",
                "root",
                ""
        );
        return con;
    }
}
