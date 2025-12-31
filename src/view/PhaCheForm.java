
package view;

import dao.CaLamViecDAO;
import dao.PhaCheDAO;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.Timer;
import java.util.TimerTask;
import model.TaiKhoan;

public class PhaCheForm extends javax.swing.JFrame {
    PhaCheDAO dao = new PhaCheDAO();
    CaLamViecDAO caDAO = new CaLamViecDAO(); // Khởi tạo DAO quản lý ca
    DefaultTableModel model;
    List<PhaCheDAO.MonCanLam> currentList;
    private TaiKhoan currentUser; // Biến lưu trữ người dùng đang đăng nhập
    /**
     * Creates new form PhaCheForm
     */
    public PhaCheForm() {
        initComponents();
        setupForm();
    }
    public PhaCheForm(TaiKhoan user) {
        this.currentUser = user;
        initComponents();
        setupForm();
        if (user != null) {
            jLabel1.setText("PHA CHẾ: " + user.getTenDangNhap());
        }
    }
    private void setupForm() {
        // Cấu hình bảng
        model = (DefaultTableModel) tblPhaChe.getModel();
        
        // Load lần đầu
        loadData();
        
        // TỰ ĐỘNG CẬP NHẬT SAU MỖI 5 GIÂY
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                loadData();
            }
        }, 5000, 5000);
        
        initEvents();
        setLocationRelativeTo(null); // Canh giữa màn hình
    }
    void loadData() {
    // 1. Lưu vị trí dòng đang chọn để tránh bị nhảy chuột khi refresh
    int selectedRow = tblPhaChe.getSelectedRow();
    
    // 2. Lấy danh sách mới nhất từ DAO
    currentList = dao.getDanhSachMonCanLam();
    model.setRowCount(0); // Xóa trắng bảng cũ
    
    // 3. Đổ dữ liệu vào 5 cột
    for (PhaCheDAO.MonCanLam mon : currentList) {
        model.addRow(new Object[]{
            mon.tenBan,         // Cột 0: BÀN SỐ
            mon.tenSP,          // Cột 1: TÊN MÓN
            mon.soLuong,        // Cột 2: SỐ LƯỢNG
            mon.trangThai,      // Cột 3: TRẠNG THÁI
            "HD" + mon.maHD     // Cột 4: MÃ ĐƠN (Thêm tiền tố HD để nhìn chuyên nghiệp hơn)
        });
    }
    
    // 4. Khôi phục lại dòng đã chọn (nếu có)
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
        btnDongCa = new javax.swing.JButton();

        jButton2.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setText("PHA CHẾ");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jScrollPane1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        tblPhaChe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "BÀN SỐ", "TÊN MÓN", "SỐ LƯỢNG", "TRẠNG THÁI", "MÃ ĐƠN"
            }
        ));
        jScrollPane1.setViewportView(tblPhaChe);
        if (tblPhaChe.getColumnModel().getColumnCount() > 0) {
            tblPhaChe.getColumnModel().getColumn(0).setResizable(false);
        }

        btnXong.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnXong.setText("Hoàn tất");

        btnNhanDon.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnNhanDon.setText("Nhận đơn");

        btnDongCa.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnDongCa.setText("Đóng ca");
        btnDongCa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDongCaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDongCa))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnNhanDon, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnXong, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDongCa))
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

    private void btnDongCaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDongCaActionPerformed
        // TODO add your handling code here:
        if (currentUser == null) {
            this.dispose();
            new LoginFrom().setVisible(true);
            return;
        }

        // 1. Xác nhận đóng ca
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Xác nhận kết thúc ca làm việc và đăng xuất?", 
                "Đóng ca", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // 2. Tìm mã nhân viên và mã ca hiện tại
            int maNV = caDAO.getMaNVByMaTK(currentUser.getMaTK());
            int maCa = caDAO.getMaCaHienTai(maNV);

            if (maCa != -1) {
                // 3. Thực hiện cập nhật giờ kết thúc vào DB
                // Pha chế không cầm tiền nên Tiền mặt, Chuyển khoản, Doanh thu = 0
                if (caDAO.dongCa(maCa, 0, 0, 0)) {
                    JOptionPane.showMessageDialog(this, "Đã đóng ca thành công.");
                    this.dispose();
                    new LoginFrom().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi: Không thể cập nhật thông tin đóng ca!");
                }
            } else {
                // Nếu không tìm thấy ca đang mở thì chỉ cần thoát ra
                this.dispose();
                new LoginFrom().setVisible(true);
            }
        }
    }//GEN-LAST:event_btnDongCaActionPerformed

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
    private javax.swing.JButton btnDongCa;
    private javax.swing.JButton btnNhanDon;
    private javax.swing.JButton btnXong;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblPhaChe;
    // End of variables declaration//GEN-END:variables
}
