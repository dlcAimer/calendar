package calendar;

import javax.swing.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddRemindDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel remindPanel;
    private JComboBox earliestRemindTime;
    private JCheckBox interfaceRemind;
    private JCheckBox dialogRemind;
    private JComboBox remindTactic;

    private Todo todo;
    private Display disp;

    public AddRemindDialog(Todo todo, Display disp) {
        this.todo = todo;
        this.disp = disp;

        setSize(400, 400);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        earliestRemindTime.addItem("提前1小时");
        earliestRemindTime.addItem("提前5小时");
        earliestRemindTime.addItem("提前1天");
        earliestRemindTime.addItem("提前1周");
        remindTactic.addItem("每5分钟提醒一次");
        remindTactic.addItem("每1小时提醒一次");
        remindTactic.addItem("每天提醒一次");


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        setVisible(true);
    }

    private void onOK() {
        GregorianCalendar earliestTime = null;
        if (todo.isUsingInterval()) {
            GregorianCalendar gDate = todo.getInterval().getStartTime();
            earliestTime = new GregorianCalendar(gDate.get(Calendar.YEAR), gDate.get(Calendar.MONTH), gDate.get(Calendar.DATE),
                    gDate.get(Calendar.HOUR), gDate.get(Calendar.MINUTE));
            earliestTime.set(Calendar.AM_PM, gDate.get(Calendar.AM_PM));
        } else {
            CalendarDate date = todo.getDate();
            earliestTime = new GregorianCalendar(date.getYear(), date.getMonth() - 1, date.getDay());
        }

        int ahead = 0;
        switch ((String) earliestRemindTime.getSelectedItem()) {
            case "提前1小时":
                earliestTime.add(Calendar.HOUR, -1);
                ahead = Constants.hrAhead;
                break;
            case "提前5小时":
                earliestTime.add(Calendar.HOUR, -5);
                ahead = Constants.hr5Ahead;
                break;
            case "提前1天":
                earliestTime.add(Calendar.DATE, -1);
                ahead = Constants.dayAhead;
                break;
            case "提前1周":
                earliestTime.add(Calendar.DATE, -7);
                ahead = Constants.weekAhead;
                break;
            default:
                JOptionPane.showMessageDialog(null, "未选择最早提醒时间");
        }

        int mode = interfaceRemind.isSelected() ?
                (dialogRemind.isSelected() ? 2 : 0) :
                (dialogRemind.isSelected() ? 1 : 3);

            /*long millis=0;
            switch (remindTactic.getSelectedIndex()){
                case Constants.remindEvery5Minutes:
                    millis=5*60*1000;
                    break;
                case Constants.remindEvery1Hour:
                    millis=60*60*1000;
                    break;
                case Constants.remindEvery1Day:
                    millis=24*60*60*1000;
                    break;
            }*/

        Remind remind = new Remind(earliestTime, mode, remindTactic.getSelectedIndex(), ahead, todo);
        todo.setReminder(remind);
        disp.getReminds().add(remind);
        remind.startRemind(disp);

        disp.addRemindBt.setEnabled(false);
        disp.cancelRemindBt.setEnabled(true);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
