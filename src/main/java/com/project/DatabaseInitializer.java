package com.project;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseInitializer {

    public static void initDatabase(String filePath) {
        Connection conn = UtilsSQLite.connect(filePath);
        
        // Crear tablas
        UtilsSQLite.queryUpdate(conn, "CREATE TABLE IF NOT EXISTS Faccio ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "nom VARCHAR(15), "
            + "resum VARCHAR(500));");
        
        UtilsSQLite.queryUpdate(conn, "CREATE TABLE IF NOT EXISTS Personatge ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "nom VARCHAR(15), "
            + "atac REAL, "
            + "defensa REAL, "
            + "idFaccio INTEGER, "
            + "FOREIGN KEY(idFaccio) REFERENCES Faccio(id));");
        
        // Insertar datos de ejemplo
        populateDatabase(conn);
        
        UtilsSQLite.disconnect(conn);
    }

    private static void populateDatabase(Connection conn) {
        UtilsSQLite.queryUpdate(conn, "INSERT INTO Faccio (nom, resum) VALUES "
            + "('Cavallers', 'Though seen as a single group, the Knights are hardly unified...'),"
            + "('Vikings', 'The Vikings are a loose coalition...'),"
            + "('Samurais', 'The Samurai are the most unified...');");

        UtilsSQLite.queryUpdate(conn, "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES "
            + "('Warden', 1, 3, 1),"
            + "('Conqueror', 2, 2, 1),"
            + "('Peacekeep', 2, 3, 1),"
            + "('Raider', 3, 3, 2),"
            + "('Warlord', 2, 2, 2),"
            + "('Berserker', 1, 1, 2),"
            + "('Kensei', 3, 2, 3),"
            + "('Shugoki', 2, 1, 3),"
            + "('Orochi', 3, 2, 3);");
    }
}
