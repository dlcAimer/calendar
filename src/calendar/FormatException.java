package calendar;

import javax.swing.*;

public class FormatException extends Exception{
    @Override
    public String getMessage() {
        JOptionPane.showMessageDialog(null, "查询格式错误，令人费解");
        return "Not the right date format";
    }
}
