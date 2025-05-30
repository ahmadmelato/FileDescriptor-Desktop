package data;

import static data.TextFieldPermision.onlyEnglishAndSpace;
import java.awt.Color;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.plaf.metal.MetalBorders;

public class TextFieldKeyListener implements KeyListener {

    private final TextFieldPermision permision;

    public TextFieldKeyListener(TextFieldPermision permision) {
        this.permision = permision;
    }

    @Override
    public void keyTyped(KeyEvent evt) {
        Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        String c;
        char carachter;
        switch (permision) {
            case onlyArablicAndSpace:
                if (Character.isSpaceChar(evt.getKeyChar())) {
                    ((JTextField) focusOwner).setBorder(new MetalBorders.TextFieldBorder());
                    return;
                }
                c = "" + evt.getKeyChar();
                if (!c.matches("[\\p{InArabic}&&\\PN]") || evt.getKeyChar() == 'ـ') {
                    evt.consume();
                } else {
                    ((JTextField) focusOwner).setBorder(new MetalBorders.TextFieldBorder());
                }
                break;
            case onlyArablic:
                if (Character.isSpaceChar(evt.getKeyChar()) || evt.getKeyChar() == '_' || evt.getKeyChar() == '%') {
                    ((JTextField) focusOwner).setBorder(new MetalBorders.TextFieldBorder());
                    return;
                }
                c = "" + evt.getKeyChar();
                if (!c.matches("[\\p{InArabic}&&\\PN]") || evt.getKeyChar() == 'ـ') {
                    evt.consume();
                } else {
                    ((JTextField) focusOwner).setBorder(new MetalBorders.TextFieldBorder());
                }
                break;
            case onlyArablicAndNumbersAndSpace:
                if (Character.isSpaceChar(evt.getKeyChar())) {
                    ((JTextField) focusOwner).setBorder(new MetalBorders.TextFieldBorder());
                    return;
                }
                if (Character.isDigit(evt.getKeyChar())) {
                    ((JTextField) focusOwner).setBorder(new MetalBorders.TextFieldBorder());
                    return;
                }
                c = "" + evt.getKeyChar();
                if (!c.matches("[\\p{InArabic}&&\\PN]") || evt.getKeyChar() == 'ـ') {
                    evt.consume();
                } else {
                    ((JTextField) focusOwner).setBorder(new MetalBorders.TextFieldBorder());
                }
                break;
            case onlyArablicAndNumbersAndSpaceAndDash:

                if (Character.isSpaceChar(evt.getKeyChar())) {
                    ((JTextField) focusOwner).setBorder(new MetalBorders.TextFieldBorder());
                    return;
                }
                if (evt.getKeyChar() == '-') {
                    ((JTextField) focusOwner).setBorder(new MetalBorders.TextFieldBorder());
                    return;
                }
                if (Character.isDigit(evt.getKeyChar())) {
                    ((JTextField) focusOwner).setBorder(new MetalBorders.TextFieldBorder());
                    return;
                }
                c = "" + evt.getKeyChar();
                if (!c.matches("[\\p{InArabic}&&\\PN]") || evt.getKeyChar() == 'ـ') {
                    evt.consume();
                } else {
                    ((JTextField) focusOwner).setBorder(new MetalBorders.TextFieldBorder());
                }
                break;
            case onlyNumbers:
                carachter = evt.getKeyChar();
                if ((carachter < '0') || (carachter > '9')) {
                    evt.consume();
                } else {
                    ((JTextField) focusOwner).setBorder(new MetalBorders.TextFieldBorder());
                }
                break;
            case onlyDouble:
                carachter = evt.getKeyChar();
                if ((carachter < '0' || carachter > '9') && carachter != '.') {
                    evt.consume();
                } else {
                    // Check if the '.' character has already been entered
                    String text = ((JTextField) focusOwner).getText();
                    if (carachter == '.' && text.contains(".")) {
                        evt.consume();
                    } else {
                        ((JTextField) focusOwner).setBorder(new MetalBorders.TextFieldBorder());
                    }
                }
                break;
            case onlyNumbersAndMinus:
                Pattern pattern = Pattern.compile("^[\\-\\+]?\\d+(\\.\\d+)?$");
                if (evt.getKeyChar() == '-') {
                    ((JTextField) focusOwner).setBorder(new MetalBorders.TextFieldBorder());
                    return;
                }
                if ((evt.getKeyChar() < '0') || (evt.getKeyChar() > '9')) {
                    evt.consume();
                } else {
                    ((JTextField) focusOwner).setBorder(new MetalBorders.TextFieldBorder());
                }
                break;
            case onlyEnglishAndSpace:
                if (Character.isSpaceChar(evt.getKeyChar())) {
                    ((JTextField) focusOwner).setBorder(new MetalBorders.TextFieldBorder());
                    return;
                }
                c = "" + evt.getKeyChar();
                if (!c.matches("[\\p{Alnum}\\p{Punct}\\s]")) {
                    evt.consume();
                } else {
                    ((JTextField) focusOwner).setBorder(new MetalBorders.TextFieldBorder());
                }
                break;
            case onlyArablicNames:
                if (Character.isSpaceChar(evt.getKeyChar())) {
                    allowNChar((JTextField) focusOwner, 3);

                    return;
                }
                c = "" + evt.getKeyChar();
                if (!c.matches("[\\p{InArabic}&&\\PN]") || evt.getKeyChar() == 'ـ') {
                    evt.consume();
                }
                allowNChar((JTextField) focusOwner, 3);
                break;

            case all:
                allowNChar((JTextField) focusOwner, 3);
                break;

        }

    }

    @Override
    public void keyPressed(KeyEvent ke) {

    }

    @Override
    public void keyReleased(KeyEvent ke) {

    }

    // just n char
    private static void allowNChar(JTextField test, int n) {
        if (test.getText().length() < n - 1) {
            test.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
        } else {
            test.setBorder(new MetalBorders.TextFieldBorder());
        }
    }

}
