package com.alura.jdbc.pruebas;

import com.alura.jdbc.factory.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PruebaDelete {

    public static void main(String[] args) throws SQLException {
        Connection con = new ConnectionFactory().recuperarConexion();

        Statement statement = con.createStatement();

        statement.execute("DELETE FROM PRODUCTO WHERE ID = 10");

        int updateCount = statement.getUpdateCount();

        System.out.println(String.format("%d registros eliminados", updateCount));

        con.close();
    }

}
