package com.project;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PR210Honor {
    public static void main(String[] args) throws SQLException {
        String basePath = System.getProperty("user.dir") + "/data/";
        String filePath = basePath + "for_honor.db";
        
        File dbFile = new File(filePath);
        if (!dbFile.exists()) {
            DatabaseInitializer.initDatabase(filePath);
        }
        
        Connection conn = UtilsSQLite.connect(filePath);
        
        // Menú de opciones
        Scanner scanner = new Scanner(System.in);
        int option;
        do {
            System.out.println("Seleccione una opción:");
            System.out.println("1. Mostrar una tabla");
            System.out.println("2. Mostrar personajes por facción");
            System.out.println("3. Mostrar el mejor atacante por facción");
            System.out.println("4. Mostrar el mejor defensor por facción");
            System.out.println("5. Salir");
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    showTable(conn);
                    break;
                case 2:
                    showCharactersByFaction(conn);
                    break;
                case 3:
                    showBestAttackerByFaction(conn);
                    break;
                case 4:
                    showBestDefenderByFaction(conn);
                    break;
                case 5:
                    System.out.println("Saliendo del programa.");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (option != 5);
        
        UtilsSQLite.disconnect(conn);
        scanner.close();
    }

    private static void showTable(Connection conn) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("¿Qué tabla quieres mostrar? (1 para Faccio, 2 para Personatge): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consumir nueva línea

        String tableName;
        if (choice == 1) {
            tableName = "Faccio";
        } else if (choice == 2) {
            tableName = "Personatge";
        } else {
            System.out.println("Selección no válida.");
            return;
        }

        String query = "SELECT * FROM " + tableName;
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Mostrar nombres de columnas
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + "\t");
            }
            System.out.println();

            // Mostrar filas de la tabla
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar la tabla: " + e.getMessage());
        }
    }

    private static void showCharactersByFaction(Connection conn) {
        String query = "SELECT Personatge.nom AS Nombre, Faccio.nom AS Faccion " +
                       "FROM Personatge INNER JOIN Faccio ON Personatge.idFaccio = Faccio.id " +
                       "ORDER BY Faccio.nom, Personatge.nom";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("Personajes agrupados por facción:");
            while (rs.next()) {
                String personaje = rs.getString("Nombre");
                String faccion = rs.getString("Faccion");
                System.out.println("Facción: " + faccion + " | Personaje: " + personaje);
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar personajes por facción: " + e.getMessage());
        }
    }
    

    private static void showBestAttackerByFaction(Connection conn) {
        String query = "SELECT Faccio.nom AS Faccion, Personatge.nom AS MejorAtacante, MAX(Personatge.atac) AS Atac " +
                       "FROM Personatge INNER JOIN Faccio ON Personatge.idFaccio = Faccio.id " +
                       "GROUP BY Faccio.nom";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("Mejor atacante por facción:");
            while (rs.next()) {
                String faccion = rs.getString("Faccion");
                String mejorAtacante = rs.getString("MejorAtacante");
                int ataque = rs.getInt("Atac");
                System.out.println("Facción: " + faccion + " | Mejor Atacante: " + mejorAtacante + " | Ataque: " + ataque);
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar el mejor atacante por facción: " + e.getMessage());
        }
    }
    

    private static void showBestDefenderByFaction(Connection conn) {
        String query = "SELECT Faccio.nom AS Faccion, Personatge.nom AS MejorDefensor, MAX(Personatge.defensa) AS Defensa " +
                       "FROM Personatge INNER JOIN Faccio ON Personatge.idFaccio = Faccio.id " +
                       "GROUP BY Faccio.nom";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("Mejor defensor por facción:");
            while (rs.next()) {
                String faccion = rs.getString("Faccion");
                String mejorDefensor = rs.getString("MejorDefensor");
                int defensa = rs.getInt("Defensa");
                System.out.println("Facción: " + faccion + " | Mejor Defensor: " + mejorDefensor + " | Defensa: " + defensa);
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar el mejor defensor por facción: " + e.getMessage());
        }
    }
    
}
