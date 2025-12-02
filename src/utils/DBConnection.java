/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // 1. Thông tin kết nối
    // Kết nối đến MySQL trên WampServer
    // Thêm các tham số như useSSL=false và serverTimezone=UTC để tránh lỗi/cảnh báo với MySQL 8+
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/db_coffeeshop?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Mặc định của WampServer là mật khẩu rỗng

    /**
     * Phương thức thiết lập và trả về đối tượng Connection đến database.
     * @return Connection
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Mở kết nối
            conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối Database! Vui lòng kiểm tra WampServer và driver JDBC.");
            System.err.println("Chi tiết lỗi: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }
    
    /**
     * Hàm main dùng để kiểm tra kết nối ngay lập tức.
     */
    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("Kiểm tra kết nối Database thành công!");
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Kiểm tra kết nối Database thất bại.");
        }
    }
}
