import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDBCreator {
    private static SQLiteDBCreator instance;
    private Connection conn;

    private SQLiteDBCreator(String url) throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection(url);
        initializeDatabase();
    }

    public static synchronized SQLiteDBCreator getInstance(String url) throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new SQLiteDBCreator(url);
        }
        return instance;
    }

    public synchronized void addRecord(int date, int score, String layout) throws SQLException {
        String sql = "INSERT INTO Ranking(Date, Score, Layout) VALUES(?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, date);
            pstmt.setInt(2, score);
            pstmt.setString(3, layout);
            pstmt.executeUpdate();
        }
    }

    public synchronized void deleteRecord(int id) throws SQLException {
        String sql = "DELETE FROM Ranking WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public synchronized void updateRecord(int id, int date, int score, String layout) throws SQLException {
        String sql = "UPDATE Ranking SET Date = ?, Score = ?, Layout = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, date);
            pstmt.setInt(2, score);
            pstmt.setString(3, layout);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
        }
    }

    public synchronized void displayAllRecords() throws SQLException {
        String sql = "SELECT * FROM Ranking";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                                rs.getInt("Date") + " | " +
                                rs.getInt("Score") + " | " +
                                rs.getString("Layout"));
            }
        }
    }

    private synchronized void initializeDatabase() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS Ranking (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Date INTEGER, " +
                    "Score INTEGER, " +
                    "Layout TEXT)";
            stmt.execute(createTableSQL);
        } catch (SQLException createTableException) {
            System.out.println("Table creation error: " + createTableException.getMessage());
        }
    }


    public List<CustomRecord> getLastFiveCustomRecords() throws SQLException {
        List<CustomRecord> lastFiveRecords = new ArrayList<>();
        String sql = "SELECT * FROM Ranking ORDER BY id DESC LIMIT 5";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                CustomRecord record = new CustomRecord(
                        rs.getString("Label"),
                        rs.getString("Data1"),
                        rs.getString("Data2")
                );
                lastFiveRecords.add(record);
            }
        }
        return lastFiveRecords;
    }


}