package coffeeshopmanagement;

import utils.DBConnection; // Import file kết nối DB
import view.ManagerForm;   // Import form Quản lý
import java.sql.Connection;

/**
 *
 * @author lehoa
 */
public class CoffeeShopManagement {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // --- BƯỚC 1: KIỂM TRA KẾT NỐI DATABASE ---
        System.out.println("Dang kiem tra ket noi CSDL...");
        Connection conn = DBConnection.getConnection();
        
        if (conn != null) {
            System.out.println("✅ Ket noi thanh cong!");
            // Đóng kết nối kiểm tra lại cho gọn
            try { conn.close(); } catch(Exception e){}
        } else {
            System.err.println("❌ Ket noi that bai! Ban da bat XAMPP chua?");
            return; // Dừng chương trình nếu không kết nối được
        }
        
        // --- BƯỚC 2: MỞ GIAO DIỆN QUẢN LÝ (MANAGER FORM) ---
        System.out.println("Dang khoi chay ManagerForm...");
        
        /* Set the Nimbus look and feel (Làm đẹp giao diện nếu có) */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagerForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        // Chạy form
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Mở form Quản lý lên
                new ManagerForm().setVisible(true);
            }
        });
    }
    
}