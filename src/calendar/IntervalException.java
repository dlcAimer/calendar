package calendar;

import javax.swing.*;

public class IntervalException extends Exception {
    @Override
    public String getMessage() {
        JOptionPane.showMessageDialog(null, "区间时序有问题，怀疑是穿越行径");
        return "Interval Exception";
    }
}
