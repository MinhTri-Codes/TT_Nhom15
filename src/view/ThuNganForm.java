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
    java.util.List<model.ChiTietHoaDon> currentList = new java.util.ArrayList<>();
    int maBanDangChon = -1;
    double tienGiamGiaTamTinh = 0;
    DefaultTableModel modelOrder;
    private TaiKhoan currentUser;
private CaLamViecDAO caDAO = new CaLamViecDAO();
    /**
     * Creates new form ThuNganForm
     */
    public ThuNganForm() {
        initComponents();
        initCustom();
    }

    public ThuNganForm(TaiKhoan user) {
       this.currentUser = user; // Lưu user lại để dùng khi đóng ca
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
        btnXoaMon.addActionListener(e -> {
            int row = tblOrder.getSelectedRow();
            if (row == -1) {
                javax.swing.JOptionPane.showMessageDialog(this, "Chọn món cần xóa đi ní!");
                return;
            }
            
            // Lấy món từ danh sách dựa vào dòng đang chọn
            model.ChiTietHoaDon mon = currentList.get(row);
            int maHD = hdDAO.getMaHoaDonCho(maBanDangChon);
            
            if (javax.swing.JOptionPane.showConfirmDialog(this, "Chắc chắn xóa món " + mon.getTenSP() + "?") == 0) {
                hdDAO.xoaMon(maHD, mon.getMaSP());
                loadOrderCuaBan(maBanDangChon); // Load lại bảng
            }
        });

        // 2. Nút SỬA SỐ LƯỢNG
        btnSuaSL.addActionListener(e -> {
            int row = tblOrder.getSelectedRow();
            if (row == -1) {
                javax.swing.JOptionPane.showMessageDialog(this, "Chọn món cần sửa số lượng!");
                return;
            }
            
            model.ChiTietHoaDon mon = currentList.get(row);
            int maHD = hdDAO.getMaHoaDonCho(maBanDangChon);
            
            // Hiện hộp thoại nhập số
            String input = javax.swing.JOptionPane.showInputDialog(this, "Nhập số lượng mới cho " + mon.getTenSP() + ":", mon.getSoLuong());
            if (input != null && !input.isEmpty()) {
                try {
                    int slMoi = Integer.parseInt(input);
                    hdDAO.updateSoLuong(maHD, mon.getMaSP(), slMoi);
                    loadOrderCuaBan(maBanDangChon);
                } catch (NumberFormatException ex) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Nhập số dùm cái!");
                }
            }
        });

        // 3. Nút HỦY ĐƠN (Xóa sạch bàn làm lại)
        btnHuyDon.addActionListener(e -> {
            if (maBanDangChon == -1) return;
            int maHD = hdDAO.getMaHoaDonCho(maBanDangChon);
            if (maHD == -1) return;

            if (javax.swing.JOptionPane.showConfirmDialog(this, "BẠN CÓ CHẮC MUỐN HỦY HẾT ĐƠN BÀN NÀY KHÔNG?\nHành động này không thể hoàn tác!", "Cảnh báo hủy đơn", javax.swing.JOptionPane.YES_NO_OPTION) == javax.swing.JOptionPane.YES_OPTION) {
                hdDAO.huyDon(maHD); // Xóa sạch trong DB
                banDAO.updateTrangThai(maBanDangChon, "Trống"); // Trả bàn về màu xanh
                
                loadDanhSachBan(); // Vẽ lại nút bàn
                loadOrderCuaBan(maBanDangChon); // Xóa trắng bảng
                javax.swing.JOptionPane.showMessageDialog(this, "Đã hủy đơn thành công!");
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

        tienGiamGiaTamTinh = 0; // Reset giảm giá khi chọn bàn mới
        // ---------------------
        
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
            lblTongTien.setText("Tổng tiền: 0 VND");
            currentList.clear(); // Xóa danh sách tạm
            return;
        }
        
        // QUAN TRỌNG: Lưu danh sách lấy từ DB vào biến toàn cục
        currentList = hdDAO.getChiTietHoaDon(maHD);
        
        double tongTien = 0;
        for (model.ChiTietHoaDon ct : currentList) {
            modelOrder.addRow(new Object[]{
                ct.getTenSP(),
                ct.getSoLuong(),
                String.format("%,.0f", ct.getDonGia()),
                String.format("%,.0f", ct.getThanhTien())
            });
            tongTien += ct.getThanhTien();
        }
        lblTongTien.setText("Tổng tiền: " + String.format("%,.0f VND", tongTien));
        double khachCanTra = tongTien - tienGiamGiaTamTinh;
        if (tienGiamGiaTamTinh > 0) {
            lblTongTien.setText("Tổng tiền: " + String.format("%,.0f VNĐ", khachCanTra));
        } else {
            lblTongTien.setText("Tổng tiền: " + String.format("%,.0f VNĐ", tongTien));
        }
    }

    void xuLyThanhToan() {
    // 1. Kiểm tra bàn và hóa đơn chờ
    if (maBanDangChon == -1) return;
    int maHD = hdDAO.getMaHoaDonCho(maBanDangChon);
    if (maHD == -1) return;

    // 2. Tính toán tổng tiền thực tế từ danh sách món ăn
    double tongTienGoc = 0;
    for (model.ChiTietHoaDon mon : currentList) {
        tongTienGoc += mon.getThanhTien();
    }
    
    if (tongTienGoc == 0) {
        JOptionPane.showMessageDialog(this, "Bàn này chưa có món ăn!");
        return;
    }

    double khachCanTra = tongTienGoc - tienGiamGiaTamTinh;
    
    // 3. Hiển thị hộp thoại lựa chọn phương thức thanh toán
    Object[] options = {"Tiền mặt", "Chuyển khoản", "Hủy"};
    String msg = "Tổng gốc: " + String.format("%,.0f", tongTienGoc) + 
                 "\nĐã giảm: -" + String.format("%,.0f", tienGiamGiaTamTinh) +
                 "\n--------------------------" +
                 "\nKHÁCH CẦN TRẢ: " + String.format("%,.0f VNĐ", khachCanTra);

    int choice = JOptionPane.showOptionDialog(this, msg, "Xác nhận thanh toán",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, options, options[0]);

    String phuongThuc = "";
    if (choice == 0) phuongThuc = "Tiền mặt";
    else if (choice == 1) phuongThuc = "Chuyển khoản";
    else return; // Hủy thanh toán

    try {
        // 4. Lấy thông tin Ca làm việc và Nhân viên hiện tại
        int maNV = caDAO.getMaNVByMaTK(currentUser.getMaTK());
        int maCa = caDAO.getMaCaHienTai(maNV);

        if (maCa == -1) {
            JOptionPane.showMessageDialog(this, "Lỗi: Không tìm thấy ca làm việc đang mở!");
            return;
        }

        // 5. Gọi DAO thực hiện lưu hóa đơn
        if (hdDAO.thanhToan(maHD, khachCanTra, phuongThuc, tienGiamGiaTamTinh, maNV, maCa)) {
            
            // 6. Cập nhật trạng thái bàn và reset giao diện
            banDAO.updateTrangThai(maBanDangChon, "Trống");
            tienGiamGiaTamTinh = 0; 
            
            loadDanhSachBan();
            loadOrderCuaBan(maBanDangChon);
            
            JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
            
            // Reset thông tin bàn đang chọn
            maBanDangChon = -1;
            jLabel1.setText("Chưa chọn bàn");
            lblTongTien.setText("Tổng: 0 VNĐ");
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật hóa đơn!");
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + e.getMessage());
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
        btnXoaMon = new javax.swing.JButton();
        btnSuaSL = new javax.swing.JButton();
        btnHuyDon = new javax.swing.JButton();
        btnGiamGia = new javax.swing.JButton();
        btnDongCa = new javax.swing.JButton();

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

        btnXoaMon.setText("Xóa Món");

        btnSuaSL.setText("Sửa số lượng");

        btnHuyDon.setText("Hủy Đơn");
        btnHuyDon.setToolTipText("");

        btnGiamGia.setText("Giảm giá");
        btnGiamGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGiamGiaActionPerformed(evt);
            }
        });

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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1075, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnSuaSL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnXoaMon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTongTien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnThanhToan, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                            .addComponent(btnHuyDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnGiamGia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnDongCa)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(btnDongCa))
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnXoaMon, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSuaSL, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnHuyDon, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnThanhToan)
                            .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        xuLyThanhToan();
    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void btnGiamGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGiamGiaActionPerformed
                                           
        if (maBanDangChon == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn để áp dụng giảm giá");
            return;
        }
        
        // 1. Tính tổng tiền gốc hiện tại
        double tongTienGoc = 0;
        for (model.ChiTietHoaDon mon : currentList) {
            tongTienGoc += mon.getThanhTien();
        }
        
        if (tongTienGoc == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Bàn hiện tại chưa có món");
            return;
        }

        // 2. Nhập số tiền muốn giảm
        String input = javax.swing.JOptionPane.showInputDialog(this, 
            "Tổng gốc: " + String.format("%,.0f VNĐ", tongTienGoc) + 
            "\nNhập số tiền giảm (VNĐ):", String.format("%.0f", tienGiamGiaTamTinh));
            
        if (input != null && !input.isEmpty()) {
            try {
                double giamMoi = Double.parseDouble(input);
                
                // Kiểm tra không cho giảm quá lố
                if (giamMoi > tongTienGoc) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Không nhập quá tiền gốc");
                    return;
                }
                
                // Lưu vào biến toàn cục
                tienGiamGiaTamTinh = giamMoi;
                
                // 3. Cập nhật lại cái nhãn Tổng tiền cho đẹp
                double khachCanTra = tongTienGoc - tienGiamGiaTamTinh;
                lblTongTien.setText("Tổng tiền: " + String.format("%,.0f VNĐ", khachCanTra));
            } catch (NumberFormatException e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ");
            }
        }
    }//GEN-LAST:event_btnGiamGiaActionPerformed

    private void btnDongCaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDongCaActionPerformed
        // TODO add your handling code here:
        if (currentUser == null) return;

    // 1. Tìm MaNV và MaCa đang mở
    int maNV = caDAO.getMaNVByMaTK(currentUser.getMaTK());
    int maCa = caDAO.getMaCaHienTai(maNV);

    if (maCa == -1) {
        JOptionPane.showMessageDialog(this, "Không tìm thấy ca làm việc đang mở!");
        return;
    }

    // 2. Xác nhận đóng ca
    int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn kết thúc ca làm việc?", "Xác nhận đóng ca", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) return;

    // 3. Tính toán tiền mặt, chuyển khoản và doanh thu
    double tienMat = caDAO.getTongTienTheoPhuongThuc(maCa, "Tiền mặt");
    double tienCK = caDAO.getTongTienTheoPhuongThuc(maCa, "Chuyển khoản");
    double doanhThu = tienMat + tienCK;

    // 4. Lưu vào Database
    if (caDAO.dongCa(maCa, tienMat, tienCK, doanhThu)) {
        String thongBao = String.format("Kết ca thành công!\n"
                + "--------------------------\n"
                + "Tiền mặt: %,.0f VNĐ\n"
                + "Chuyển khoản: %,.0f VNĐ\n"
                + "Tổng doanh thu: %,.0f VNĐ", tienMat, tienCK, doanhThu);
        
        JOptionPane.showMessageDialog(this, thongBao, "Báo cáo kết ca", JOptionPane.INFORMATION_MESSAGE);
        
        // 5. Đóng form và quay lại màn hình đăng nhập
        this.dispose();
        new LoginFrom().setVisible(true);
    } else {
        JOptionPane.showMessageDialog(this, "Lỗi khi đóng ca!");
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
    private javax.swing.JButton btnDongCa;
    private javax.swing.JButton btnGiamGia;
    private javax.swing.JButton btnHuyDon;
    private javax.swing.JButton btnSuaSL;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnXoaMon;
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
