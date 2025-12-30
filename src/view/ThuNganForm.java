/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;
import dao.*;
import model.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
/**
 *
 * @author lengu
 */
public class ThuNganForm extends javax.swing.JFrame {
    BanDAO banDAO = new BanDAO();
    SanPhamDAO spDAO = new SanPhamDAO();
    HoaDonDAO hdDAO = new HoaDonDAO();

    int maBanDangChon = -1;
    DefaultTableModel modelOrder;
    /**
     * Creates new form ThuNganForm
     */
    public ThuNganForm() {
        initComponents();
        initCustom();
    }

    public ThuNganForm(TaiKhoan user) {
        initComponents();
        initCustom();
        if (user != null) {
            this.setTitle("Thu Ngân: " + user.getTenDangNhap());
        }
    }

    private void initCustom() {
        modelOrder = (DefaultTableModel) tblOrder.getModel();
        this.setExtendedState(MAXIMIZED_BOTH); // Toàn màn hình

        // 1. Cấu hình ScrollPane chỉ cuộn dọc
        setupScrollPane(jScrollPane2);
        setupScrollPane(jScrollPane3);

        // 2. TỰ ĐỘNG CĂN CHỈNH KHI THAY ĐỔI KÍCH THƯỚC MÀN HÌNH
        // Đây là phần quan trọng nhất để không bị vượt quá màn hình
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                loadDanhSachBan();
                loadThucDon();
            }
        });

        // 3. Định dạng bảng và label
        tblOrder.setRowHeight(35);
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 22));
    }

    private void setupScrollPane(JScrollPane scrollPane) {
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(25); // Cuộn mượt
        scrollPane.setBorder(null);
    }

    // --- 1. LOAD BÀN (Tự tính số cột theo chiều rộng) ---
    void loadDanhSachBan() {
        pnlListBan.removeAll();
        
        // Tính toán số cột: Chiều rộng panel / (độ rộng thẻ + khoảng cách)
        int width = jScrollPane2.getWidth() - 20; 
        int columns = Math.max(1, width / 95); // Mỗi thẻ bàn rộng ~85px + 10px gap
        
        pnlListBan.setLayout(new java.awt.GridLayout(0, columns, 10, 10));
        
        List<Ban> listBan = banDAO.getAllBan();
        for (Ban b : listBan) {
            JButton btn = createTableButton(b);
            pnlListBan.add(btn);
        }
        pnlListBan.revalidate();
        pnlListBan.repaint();
    }

    private JButton createTableButton(Ban b) {
        JButton btn = new JButton("<html><center><b>" + b.getTenBan() + "</b><br><small>" + b.getTrangThai() + "</small></center></html>");
        btn.setPreferredSize(new Dimension(85, 85));
        btn.setFocusPainted(false);
        if (b.getTrangThai().equalsIgnoreCase("Có Khách")) {
            btn.setBackground(new Color(255, 102, 102));
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(new Color(240, 240, 240));
        }
        btn.addActionListener(e -> {
            maBanDangChon = b.getMaBan();
            jLabel1.setText("Đang chọn: " + b.getTenBan());
            loadOrderCuaBan(maBanDangChon);
        });
        return btn;
    }

    // --- 2. LOAD MÓN ĂN (Tự tính số cột để không tràn màn hình) ---
    void loadThucDon() {
        pnlListMon.removeAll();
        
        // Tính toán số cột tự động
        int width = jScrollPane3.getWidth() - 30;
        int columns = Math.max(1, width / 160); // Mỗi thẻ món rộng ~150px + 10px gap
        
        pnlListMon.setLayout(new java.awt.GridLayout(0, columns, 12, 12));

        List<SanPham> listSP = spDAO.getAllSanPham();
        for (SanPham sp : listSP) {
            JButton btn = createProductButton(sp);
            pnlListMon.add(btn);
        }
        pnlListMon.revalidate();
        pnlListMon.repaint();
    }

    private JButton createProductButton(SanPham sp) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout(0, 5));
        btn.setPreferredSize(new Dimension(150, 190));
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        // Ảnh món ăn
        JLabel lblImg = new JLabel();
        lblImg.setHorizontalAlignment(JLabel.CENTER);
        if (sp.getImgUrl() != null) {
            File f = new File(sp.getImgUrl());
            if (!f.exists()) f = new File(System.getProperty("user.dir") + File.separator + sp.getImgUrl());
            if (f.exists()) {
                lblImg.setIcon(new ImageIcon(new ImageIcon(f.getAbsolutePath()).getImage().getScaledInstance(130, 100, Image.SCALE_SMOOTH)));
            }
        }

        // Thông tin món
        String info = "<html><center>" + sp.getTenSP() + "<br><b style='color:red;'>" + String.format("%,.0f", sp.getDonGia()) + "đ</b></center></html>";
        JLabel lblInfo = new JLabel(info);
        lblInfo.setHorizontalAlignment(JLabel.CENTER);

        btn.add(lblImg, BorderLayout.CENTER);
        btn.add(lblInfo, BorderLayout.SOUTH);
        btn.addActionListener(e -> xuLyGoiMon(sp));
        return btn;
    }

    // Các hàm xử lý DB giữ nguyên...
    void xuLyGoiMon(SanPham sp) {
        if (maBanDangChon == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn!");
            return;
        }
        int maHD = hdDAO.getMaHoaDonCho(maBanDangChon);
        if (maHD == -1) {
            maHD = hdDAO.taoHoaDon(maBanDangChon);
            banDAO.updateTrangThai(maBanDangChon, "Có Khách");
            loadDanhSachBan();
        }
        hdDAO.themMon(maHD, sp.getMaSP(), 1);
        loadOrderCuaBan(maBanDangChon);
    }

    void loadOrderCuaBan(int maBan) {
        modelOrder.setRowCount(0);
        int maHD = hdDAO.getMaHoaDonCho(maBan);
        if (maHD == -1) {
            lblTongTien.setText("Tổng: 0 VND");
            return;
        }
        List<ChiTietHoaDon> list = hdDAO.getChiTietHoaDon(maHD);
        double tong = 0;
        for (ChiTietHoaDon ct : list) {
            modelOrder.addRow(new Object[]{ct.getTenSP(), ct.getSoLuong(), String.format("%,.0f", ct.getDonGia()), String.format("%,.0f", ct.getThanhTien())});
            tong += ct.getThanhTien();
        }
        lblTongTien.setText("Tổng: " + String.format("%,.0f VND", tong));
    }

    void xuLyThanhToan() {
        if (maBanDangChon == -1) return;
        int maHD = hdDAO.getMaHoaDonCho(maBanDangChon);
        if (maHD != -1 && JOptionPane.showConfirmDialog(this, "Thanh toán?") == JOptionPane.YES_OPTION) {
            hdDAO.thanhToan(maHD, 0);
            banDAO.updateTrangThai(maBanDangChon, "Trống");
            loadDanhSachBan();
            modelOrder.setRowCount(0);
            lblTongTien.setText("Tổng: 0 VND");
            maBanDangChon = -1;
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        pnlListBan = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        pnlListMon = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblOrder = new javax.swing.JTable();
        lblTongTien = new java.awt.Label();
        btnThanhToan = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlListBan.setLayout(new java.awt.GridLayout(1, 0));
        jScrollPane2.setViewportView(pnlListBan);

        pnlListMon.setLayout(new java.awt.GridLayout(1, 0));
        jScrollPane3.setViewportView(pnlListMon);

        tblOrder.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tblOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Tên Món", "Số Lượng", "Đơn Giá", "Thành Tiền"
            }
        ));
        tblOrder.setRowHeight(30);
        tblOrder.setShowGrid(false);
        jScrollPane4.setViewportView(tblOrder);

        lblTongTien.setText("Tổng: 0 VND");

        btnThanhToan.setText("Thanh toán");
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setText("Chưa chọn bàn");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1075, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 264, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTongTien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(15, 15, 15)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnThanhToan)
                .addGap(0, 29, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnThanhToanActionPerformed

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
            java.util.logging.Logger.getLogger(ThuNganForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ThuNganForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ThuNganForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ThuNganForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ThuNganForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private java.awt.Label lblTongTien;
    private javax.swing.JPanel pnlListBan;
    private javax.swing.JPanel pnlListMon;
    private javax.swing.JTable tblOrder;
    // End of variables declaration//GEN-END:variables
}
