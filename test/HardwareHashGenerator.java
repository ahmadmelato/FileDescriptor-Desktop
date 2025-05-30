/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ahmad
 */
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.util.Enumeration;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;

public class HardwareHashGenerator {

    public static String generateHardwareHash() throws Exception {
        StringBuilder hardwareInfo = new StringBuilder();
        StringBuilder hardwareMac = new StringBuilder();

        // جمع معلومات العتاد
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        String osVersion = System.getProperty("os.version");
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hal = systemInfo.getHardware();

        hardwareInfo.append(osName).append(osArch).append(osVersion);

        // إضافة معلومات عن واجهات الشبكة (عنوان MAC)
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

        System.err.println("Manufacturer: " + manufacturer);
        System.err.println("Model: " + model);
        System.err.println("Description: " + description);

        hardwareInfo.append(totalMemory);
        hardwareInfo.append(manufacturer);
        hardwareInfo.append(model);
        hardwareInfo.append(description);

        System.err.println(osName + "\t" + osArch + "\t" + osVersion + "\t" + hardwareMac + "\t" + totalMemory);

        // إنشاء التجزئة
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(hardwareInfo.toString().getBytes("UTF-8"));

        // تحويل البايتات إلى سلسلة نصية
        StringBuilder hashString = new StringBuilder();
        for (byte b : hashBytes) {
            hashString.append(String.format("%02x", b));
        }

        return hashString.toString();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Hardware Hash: " + generateHardwareHash());
    }
}
