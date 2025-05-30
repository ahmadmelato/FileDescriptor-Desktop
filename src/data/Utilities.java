package data;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Enumeration;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlgraphics.image.loader.spi.ImageLoader;
import oshi.SystemInfo;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;

public final class Utilities {

    private static final Logger LOG = Logger.getLogger(Utilities.class.getName());

    // ==============================================
    // Private static fields
    // ==============================================
    private static final String ERROR_TITLE = "فشل العملية";
    private static final String INFORMATION_TITLE = "نجاح العملية";
    private static final String QUESTION_TITLE = "سؤال";
    private static final String WARNING_TITLE = "تحذير";

    // ==============================================
    // Public static methods
    // ==============================================
    public static String getCurrentApplicationName() {
        return "";
    }

    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static void showError(Container owner, String message) {
        JOptionPane.showMessageDialog(
                ((Supplier<JDialog>) () -> {
                    final JDialog dialog = new JDialog();
                    dialog.setAlwaysOnTop(true);
                    return dialog;
                }).get(),
                message,
                String.format("%s %s", getCurrentApplicationName(), ERROR_TITLE), JOptionPane.ERROR_MESSAGE);
    }

    public static void showError(Container owner, Exception ex) {
        LOG.log(Level.SEVERE, null, ex);
        showError(owner, ex.getMessage());
    }

    public static void showError(Container owner, SQLException ex) {
        String str = ex.getErrorCode() + ":" + ex.getMessage();
        LOG.log(Level.SEVERE, null, ex);
        showError(owner, str);
    }

    public static void showInformation(Container owner, String message) {
        JOptionPane.showMessageDialog(
                ((Supplier<JDialog>) () -> {
                    final JDialog dialog = new JDialog();
                    dialog.setAlwaysOnTop(true);
                    return dialog;
                }).get(),
                message,
                String.format("%s %s", getCurrentApplicationName(), INFORMATION_TITLE), JOptionPane.INFORMATION_MESSAGE);

    }

    public static boolean showQuestion(Container owner, String message) {
        return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(owner, message, String.format("%s: %s", getCurrentApplicationName(), QUESTION_TITLE), JOptionPane.YES_NO_OPTION);
    }

    public static void showWarning(Container owner, String message) {
        JOptionPane.showMessageDialog(owner, message, getCurrentApplicationName() + ": " + WARNING_TITLE, JOptionPane.WARNING_MESSAGE);
    }

    public static String generateHardwareHash() {
        try {
            StringBuilder hardwareInfo = new StringBuilder();
            StringBuilder hardwareMac = new StringBuilder();
            String osName = System.getProperty("os.name");
            String osArch = System.getProperty("os.arch");
            String osVersion = System.getProperty("os.version");
            SystemInfo systemInfo = new SystemInfo();
            HardwareAbstractionLayer hal = systemInfo.getHardware();
            hardwareInfo.append(osName).append(osArch).append(osVersion);
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface network = networkInterfaces.nextElement();
                byte[] mac = network.getHardwareAddress();
                if (mac != null) {
                    for (byte b : mac) {
                        hardwareInfo.append(String.format("%02X", b));
                        hardwareMac.append(String.format("%02X", b));
                    }
                }
            }

            long totalMemory = hal.getMemory().getTotal();
            ComputerSystem computerSystem = hal.getComputerSystem();
            String manufacturer = computerSystem.getManufacturer();
            String model = computerSystem.getModel();
            String description = computerSystem.getFirmware().getDescription();
            hardwareInfo.append(totalMemory);
            hardwareInfo.append(manufacturer);
            hardwareInfo.append(model);
            hardwareInfo.append(description);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(hardwareInfo.toString().getBytes("UTF-8"));
            StringBuilder hashString = new StringBuilder();
            for (byte b : hashBytes) {
                hashString.append(String.format("%02x", b));
            }

            return hashString.toString();
        } catch (UnsupportedEncodingException | SocketException | NoSuchAlgorithmException ex) {
            Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    static public void initTable(JTable myTable, JScrollPane jScrollPane1) {
        myTable.setDefaultEditor(Object.class, null);
        myTable.setBackground(MyColor.White);
        myTable.setForeground(MyColor.black);
        myTable.setRowSelectionAllowed(true);
        myTable.setFont(new java.awt.Font("DIN Next LT ARABIC", Font.PLAIN, 18));
        myTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < myTable.getColumnModel().getColumnCount(); i++) {
            myTable.getColumnModel().getColumn(i).setPreferredWidth(150);
            myTable.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }

        JTableHeader header = myTable.getTableHeader();
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        header.setBackground(MyColor.black16);
        header.setForeground(MyColor.gray);
        header.setFont(new java.awt.Font("DIN Next LT ARABIC", Font.BOLD, 18));
        header.setPreferredSize(new Dimension(150, 30));

    }

    static public void initTable3(JTable myTable, JScrollPane jScrollPane1) {
        myTable.setDefaultEditor(Object.class, null);
        myTable.setBackground(MyColor.White);
        myTable.setForeground(MyColor.black);
        myTable.setRowSelectionAllowed(true);
        myTable.setFont(new java.awt.Font("DIN Next LT ARABIC", Font.PLAIN, 18));
        myTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Custom renderer for CENTER (horizontal) and TOP (vertical) alignment + multiline support
        DefaultTableCellRenderer centerTopRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);  // Center text horizontally
                setVerticalAlignment(SwingConstants.TOP);        // Align text to the top
                // Enable HTML for multiline support (use <br> for line breaks)
                if (value != null) {
                    String text = value.toString().replace("\n", "<br>"); // Convert newlines to <br>
                    setText("<html><div style='text-align:center'>" + text + "</div></html>");
                }
                return this;
            }
        };

        // Apply the custom renderer to all columns
        for (int i = 0; i < myTable.getColumnModel().getColumnCount(); i++) {
            myTable.getColumnModel().getColumn(i).setPreferredWidth(150);
            myTable.getColumnModel().getColumn(i).setCellRenderer(centerTopRenderer);
        }

        // Configure table header (centered text)
        JTableHeader header = myTable.getTableHeader();
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        header.setBackground(MyColor.black16);
        header.setForeground(MyColor.gray);
        header.setFont(new java.awt.Font("DIN Next LT ARABIC", Font.BOLD, 18));
        header.setPreferredSize(new Dimension(150, 30));

        // Auto-adjust row height for multiline text
        myTable.setRowHeight(myTable.getRowHeight() * 2); // Double the default height (adjust as needed)
    }

    static public void initTable2(JTable myTable, JScrollPane jScrollPane1) {
        myTable.setDefaultEditor(Object.class, null);
        myTable.setBackground(MyColor.White);
        myTable.setForeground(MyColor.black);
        myTable.setColumnSelectionAllowed(true);
        myTable.setRowSelectionAllowed(true);
        myTable.setFont(new java.awt.Font("DIN Next LT ARABIC", Font.PLAIN, 18));
        myTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < myTable.getColumnModel().getColumnCount(); i++) {
            myTable.getColumnModel().getColumn(i).setPreferredWidth(150);
            myTable.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
        }

        JTableHeader header = myTable.getTableHeader();
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        header.setEnabled(false);
        header.setBackground(MyColor.black16);
        header.setForeground(MyColor.gray);
        header.setFont(new java.awt.Font("DIN Next LT ARABIC", Font.BOLD, 18));
        header.setPreferredSize(new Dimension(150, 30));

    }

    public static void initJTree(JTree jTree) {
        TreeCellRenderer cr = jTree.getCellRenderer();
        if (cr instanceof DefaultTreeCellRenderer) {
            DefaultTreeCellRenderer dtcr
                    = (DefaultTreeCellRenderer) cr;

            // Set the various colors
            dtcr.setFont(new java.awt.Font("DIN Next LT Arabic", 0, 36));
//            dtcr.setBackgroundNonSelectionColor(Color.black);
//            dtcr.setBackgroundSelectionColor(Color.gray);
//            dtcr.setTextSelectionColor(Color.white);
//            dtcr.setTextNonSelectionColor(Color.green);

            // Finally, set the tree's background color 
            // jTree1.setBackground(Color.black);
        }
    }

    public static String browseToOpenPDFFile(Component parent) {

        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("pdf", "pdf");
        fc.setFileFilter(filter);
        int option = fc.showOpenDialog(parent);
        if (option == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile().getPath();
        }
        return null;
    }

    public static long getFileSize(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.length(); // returns size in bytes
        }
        return 0;
    }

    // Get file name without extension
    public static String getFileName(String filePath) {
        File file = new File(filePath);
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            return fileName.substring(0, dotIndex);
        }
        return fileName;
    }
    
    public static File getFile(String filePath) {
        File file = new File(filePath);
        return file;
    }

    // Get file extension
    public static String getFileExtension(String filePath) {
        File file = new File(filePath);
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

    public static String lastPath = null;

    public static String browseToOpenImageFile(Component parent) {

        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(System.getProperty("user.home")));
        if (lastPath != null) {
            fc.setCurrentDirectory(new File(lastPath));
        }
        FileNameExtensionFilter filter = new FileNameExtensionFilter("image", "png", "jpg");
        fc.setFileFilter(filter);
        int option = fc.showOpenDialog(parent);
        if (option == JFileChooser.APPROVE_OPTION) {
            lastPath = fc.getSelectedFile().getParentFile().getPath();
            return fc.getSelectedFile().getPath();
        }
        return "";
    }

    public static String convertToBase64(File file) {
        if (file != null) {
            FileInputStream fin = null;
            try {
                fin = new FileInputStream(file);
                byte imagebytearray[] = new byte[(int) file.length()];
                fin.read(imagebytearray);
                String imagetobase64 = Base64.getEncoder().encodeToString(imagebytearray);
                fin.close();
                return imagetobase64;
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fin.close();
                } catch (IOException ex) {
                    Logger.getLogger(ImageLoader.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return "";
    }

    public static String browseToSaveJsonFile(Component parent) {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("json", "json");
        fc.setFileFilter(filter);
        int option = fc.showSaveDialog(parent);
        if (option == JFileChooser.APPROVE_OPTION) {
            String filename = fc.getSelectedFile().getName();
            String path = fc.getSelectedFile().getParentFile().getPath();
            int len = filename.length();
            String ext = "";
            String file;
            if (len > 4) {
                ext = filename.substring(len - 4, len);
            }
            if (ext.equals(".json")) {
                file = path + FileSystems.getDefault().getSeparator() + filename;
            } else {
                file = path + FileSystems.getDefault().getSeparator() + filename + ".json";
            }
            return file;
        }
        return "";
    }

    public static boolean writeJsonFileToPath(String path, String jsonData) {
        try {
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(jsonData);
            myWriter.close();
            return true;
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }

    }

    public static void setTitleJScrollPane(JScrollPane panel, String title) {
        Border border = panel.getBorder();
        if (border instanceof TitledBorder) {
            ((TitledBorder) panel.getBorder()).setTitle(title);
            panel.repaint();
        } else {
            panel.setBorder(BorderFactory.createTitledBorder(title));
        }
    }

    public static void setTitleJScrollPane(JPanel panel, String title) {
        Border border = panel.getBorder();
        if (border instanceof TitledBorder) {
            ((TitledBorder) panel.getBorder()).setTitle(title);
            panel.repaint();
        } else {
            panel.setBorder(BorderFactory.createTitledBorder(title));
        }
    }

    public static void scaledImage(JLabel jLabel, Icon icon) {
        ImageIcon imageIcon = (ImageIcon) icon;
        Image image = imageIcon.getImage().getScaledInstance(jLabel.getWidth(), jLabel.getHeight(), Image.SCALE_SMOOTH);
        jLabel.setIcon(new ImageIcon(image));

    }

    public static boolean exportToExcel(JTable table, String filePath) {
        TableModel model = table.getModel();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        try {
            // Write table headers
            Row headerRow = sheet.createRow(0);
            for (int i = model.getColumnCount() - 1; i >= 0; i--) {
                Cell cell = headerRow.createCell(model.getColumnCount() - i - 1);
                cell.setCellValue(model.getColumnName(i));
            }

            // Write table data
            for (int i = 0; i < model.getRowCount(); i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = model.getColumnCount() - 1; j >= 0; j--) {
                    Object value = model.getValueAt(i, j);
                    Cell cell = row.createCell(model.getColumnCount() - j - 1);

                    if (value != null) {
                        if (value instanceof Number) {
                            cell.setCellValue(((Number) value).doubleValue());
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    }
                }
            }

            // Auto-size columns
            for (int i = 0; i < model.getColumnCount(); i++) {
                sheet.autoSizeColumn(i);
            }

            // Save workbook to file
            FileOutputStream fileOut = new FileOutputStream(filePath);
            workbook.write(fileOut);
            fileOut.close();

            System.out.println("Excel file exported successfully.");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String browseToSaveExcelFile(Component parent) {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel", "xlsx");
        fc.setFileFilter(filter);
        int option = fc.showSaveDialog(parent);
        if (option == JFileChooser.APPROVE_OPTION) {
            String filename = fc.getSelectedFile().getName();
            String path = fc.getSelectedFile().getParentFile().getPath();
            int len = filename.length();
            String ext = "";
            String file;
            if (len > 4) {
                ext = filename.substring(len - 4, len);
            }
            if (ext.equals(".xlsx")) {
                file = path + FileSystems.getDefault().getSeparator() + filename;
            } else {
                file = path + FileSystems.getDefault().getSeparator() + filename + ".xlsx";
            }
            return file;
        }
        return "";
    }

}
