
package view;

import dao.PhaCheDAO;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.Timer;
import java.util.TimerTask;

public class PhaCheForm extends javax.swing.JFrame {
    PhaCheDAO dao = new PhaCheDAO();
    DefaultTableModel model;
    List<PhaCheDAO.MonCanLam> currentList;
    /**
     * Creates new form PhaCheForm
     */
    public PhaCheForm() {
        initComponents();
        // Cấu hình bảng
        model = (DefaultTableModel) tblPhaChe.getModel();
        
        // Load lần đầu
        loadData();
        
        // TỰ ĐỘNG CẬP NHẬT SAU MỖI 5 GIÂY (Auto Refresh)
        // Giúp pha chế không cần bấm nút F5 liên tục
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                loadData();
            }
        }, 5000, 5000); // 5000ms = 5 giây
        initEvents();
    }
    void loadData() {
        // Lưu vị trí dòng đang chọn để sau khi refresh không bị mất selection
        int selectedRow = tblPhaChe.getSelectedRow();
        
        currentList = dao.getDanhSachMonCanLam();
        model.setRowCount(0); // Xóa trắng bảng
        
        for (PhaCheDAO.MonCanLam mon : currentList) {
            model.addRow(new Object[]{
                mon.tenBan,
                mon.tenSP,
                mon.soLuong,
                mon.trangThai
            });
        }
        
        // Restore lại selection nếu có
        if (selectedRow >= 0 && selectedRow < model.getRowCount()) {
            tblPhaChe.setRowSelectionInterval(selectedRow, selectedRow);
        }
    }
    void initEvents() {
        // --- NÚT NHẬN ĐƠN (Chuyển sang "Đang làm") ---
        btnNhanDon.addActionListener(e -> {
            int row = tblPhaChe.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Chọn đơn để nhận!");
                return;
            }
            
            PhaCheDAO.MonCanLam mon = currentList.get(row);
            if (mon.trangThai.equals("Đang làm")) {
                 JOptionPane.showMessageDialog(this, "Món này đang được thực hiện!");
                 return;
            }

            dao.updateTrangThaiMon(mon.maHD, mon.maSP, "Đang làm");
            loadData(); // Load lại ngay lập tức
        });

        // --- NÚT ĐÃ XONG (Biến mất khỏi bảng) ---
        btnXong.addActionListener(e -> {
            int row = tblPhaChe.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Chọn đơn đã hoàn tất!");
                return;
            }

            PhaCheDAO.MonCanLam mon = currentList.get(row);
            
            // Hỏi xác nhận cho chắc
            if (JOptionPane.showConfirmDialog(this, "Xác nhận món " + mon.tenSP + " đã xong?") == 0) {
                dao.updateTrangThaiMon(mon.maHD, mon.maSP, "Đã xong");
                loadData(); // Món sẽ biến mất vì SQL chỉ lấy 'Chờ làm' và 'Đang làm'
            }
        });
    }
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPhaChe = new javax.swing.JTable();
        btnXong = new javax.swing.JButton();
        btnNhanDon = new javax.swing.JButton();

        jButton2.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setText("PHA CHẾ");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jScrollPane1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        tblPhaChe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "BÀN SỐ:", "TÊN MÓN:", "SỐ LƯỢNG:", "TRẠNG THÁI:"
            }
        ));
        jScrollPane1.setViewportView(tblPhaChe);

        btnXong.setText("Hoàn tất");

        btnNhanDon.setText("Nhận đơn");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNhanDon, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnXong, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXong, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(btnNhanDon, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PhaCheForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PhaCheForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PhaCheForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PhaCheForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PhaCheForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNhanDon;
    private javax.swing.JButton btnXong;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblPhaChe;
    // End of variables declaration//GEN-END:variables
}
