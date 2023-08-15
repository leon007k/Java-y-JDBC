package com.alura.jdbc.pruebas;

import com.alura.jdbc.CreaConexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PruebaConexion {

    public static void main(String[] args) throws SQLException {
        // * Abrimos conexion
        Connection con = new CreaConexion().recuperarConexion();

        System.out.println("Cerrando la conexión");

        con.close();
    }

}
