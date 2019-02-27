package calendar;

import com.sun.istack.internal.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimerTask;

import static calendar.Display.endYear;
import static calendar.Display.startYear;

public class AddTodoDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox todoType;
    private JPanel panel;
    private JPanel meetingPanel;
    private JTabbedPane optionTab;
    private JPanel datingPanel;
    private JPanel travelingPanel;
    private JTextField customTransName;
    private JPanel anniverPanel;
    private JComboBox anniType;
    private JPanel interviewingPanel;
    private JPanel coursePanel;
    private JPanel customPanel;
    private JCheckBox emergeCheck;
    private JCheckBox importantCheck;
    private JPanel remindPanel;
    private JComboBox remindTactic;
    private JComboBox earliestRemindTime;
    private JCheckBox interfaceRemind;
    private JCheckBox dialogRemind;
    private JCheckBox isRemindCheck;
    private JCheckBox setTimeCheck;
    private JRadioButton transCustomOpt;
    private JSpinner durationSpinner;
    private JSpinner repeatSpinner;
    private JPanel customStartTimePane;
    private JPanel customEndTimePane;
    private JSpinner startHour;
    private JSpinner startMin;
    private JSpinner endHour;
    private JSpinner endMin;
    private JPanel startPanel;
    private JLabel startLabel;
    private JComboBox startYearBox;
    private JComboBox startMonthBox;
    private JComboBox startDayBox;
    private JComboBox startHourBox;
    private JComboBox startMinBox;
    private JPanel endPanel;
    private JLabel endLabel;
    private JComboBox endYearBox;
    private JComboBox endMonthBox;
    private JComboBox endDayBox;
    private JComboBox endHourBox;
    private JComboBox endMinBox;
    private JRadioButton planeOpt;
    private JRadioButton trainOpt;
    private JRadioButton busOpt;
    private MeetTodo meetTodo;

    private String addStatus;
    private CalendarDate date;
    private CalendarInterval interval;
    private boolean isUsingInterval;
    private boolean isChild;
    private Todo parentTodo;
    private Todo todoNew;

    public AddTodoDialog(Display disp) {
        //this.interval = interval;
        //isUsingInterval = true;
        isChild = false;
        parentTodo = new Todo();
        init(disp);
    }

    public AddTodoDialog(Display disp, Todo parentTodo) {
        //.date = date;
        //isUsingInterval = false;
        isChild = true;
        this.parentTodo = parentTodo;
        init(disp);
    }

    public void init(Display disp) {
        setSize(700, 600);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.isUsingInterval = isUsingInterval;

        addComboItems();
        initComboBoxItems();
        SpinnerNumberModel durationModel = new SpinnerNumberModel(5, 1, 200, 1);
        durationSpinner.setModel(durationModel);
        SpinnerNumberModel repeatModel = new SpinnerNumberModel(3, 0, 6, 1);
        repeatSpinner.setModel(repeatModel);
        SpinnerNumberModel startHourModel = new SpinnerNumberModel(11, 0, 23, 1);
        startHour.setModel(startHourModel);
        SpinnerNumberModel startMinModel = new SpinnerNumberModel(30, 0, 59, 1);
        startMin.setModel(startMinModel);
        SpinnerNumberModel endHourModel = new SpinnerNumberModel(11, 0, 23, 1);
        endHour.setModel(endHourModel);
        SpinnerNumberModel endMinModel = new SpinnerNumberModel(30, 0, 59, 1);
        endMin.setModel(endMinModel);

        if (isChild) {
            optionTab.removeTabAt(5);
        }

        setTimeCheck.addActionListener(e -> {
            startPanel.setVisible(setTimeCheck.isSelected());
            endPanel.setVisible(setTimeCheck.isSelected());
            customChangeSituation();
        });

        /*isRemindCheck.addActionListener(e -> {
            remindPanel.setVisible(isRemindCheck.isSelected());
        });*/

        optionTab.addChangeListener(e -> {
            handleTabChange();
        });

        for (int i = 0; i < 4; i++) {
            JPanel transPanel = (JPanel) travelingPanel.getComponent(0);
            JRadioButton bt = (JRadioButton) transPanel.getComponent(i);
            bt.addActionListener(e -> customTransName.setEnabled(transCustomOpt.isSelected()));
        }

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK(disp);
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

    private void handleTabChange() {
        switch (optionTab.getSelectedIndex()) {
            case 0:
            case 1:
            case 2:
            case 4:
                // isRemindCheck.setVisible(true);
                //remindPanel.setVisible(isRemindCheck.isSelected());
                startPanel.setVisible(true);
                endPanel.setVisible(true);
                break;
            case 5:
                //isRemindCheck.setVisible(true);
                //remindPanel.setVisible(isRemindCheck.isSelected());
                if (isChild) {
                    customChangeSituation();
                    break;
                }
                startPanel.setVisible(false);
                endPanel.setVisible(false);
                break;
            case 6:
                customChangeSituation();
                break;
            case 3:
                //isRemindCheck.setVisible(false);
                //remindPanel.setVisible(false);
                startPanel.setVisible(false);
                endPanel.setVisible(false);
                break;
            default:
                System.out.println("待办事项类型有误");
                break;
        }
    }

    private void onOK(Display disp) {
        // add your code here;
        int isSuccess = 0;
        try {
            switch (optionTab.getSelectedIndex()) {
                case 0:
                    addStatus = "meeting";
                    handleMeetingTodo();
                    break;
                case 1:
                    addStatus = "dating";
                    handleDatingTodo();
                    break;
                case 2:
                    addStatus = "traveling";
                    handleTravelingTodo();
                    break;
                case 3:
                    addStatus = "anniversary";
                    handleAnniversaryTodo();
                    break;
                case 4:
                    addStatus = "interviewing";
                    handleInterviewTodo();
                    break;
                case 5:
                    if (isChild) {
                        addStatus = "custom";
                        handleCustomTodo();
                        break;
                    }
                    addStatus = "course";
                    handleCourseTodo();
                    break;
                case 6:
                    addStatus = "custom";
                    handleCustomTodo();
                    break;
                default:
                    System.out.println("待办事项类型有误");
                    break;
            }

            /*if (isRemindCheck.isSelected() && isRemindCheck.isVisible()) {
                GregorianCalendar earliestTime = null;
                if (todoNew.isUsingInterval()) {
                    GregorianCalendar gDate = todoNew.getInterval().getStartTime();
                    earliestTime = new GregorianCalendar(gDate.get(Calendar.YEAR), gDate.get(Calendar.MONTH), gDate.get(Calendar.DATE),
                            gDate.get(Calendar.HOUR), gDate.get(Calendar.MINUTE));
                    earliestTime.set(Calendar.AM_PM, gDate.get(Calendar.AM_PM));
                } else {
                    CalendarDate date = todoNew.getDate();
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
            }

                Remind remind = new Remind(earliestTime, mode, remindTactic.getSelectedIndex(), ahead, todoNew);
                todoNew.setReminder(remind);
                disp.getReminds().add(remind);
                remind.startRemind(disp);

            }*/

            if (isChild) {
                isSuccess = disp.getTodoArray().addChild(parentTodo, todoNew);
                //parentTodo.addChildTodo(todoNew);
                if (isSuccess == 0)
                    disp.putChildIntoList(parentTodo);
            } else {
                isSuccess = addTodo(disp, todoNew);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (isSuccess == 0)
            dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void handleMeetingTodo() throws IntervalException {
        //Todo todo=new Todo();
        /*todoNew = new MeetTodo(((JTextField) ((JPanel) (meetingPanel.getComponent(2))).getComponent(1)).getText(),
                interval, "hh", "hh");*/
        generateInterval();
        String content = getTextContent(meetingPanel, 1, 1);
        String detail = getTextContent(meetingPanel, 2, 1);
        String location = getTextContent(meetingPanel, 0, 1);

        todoNew = new MeetTodo(content, interval, location, detail, emergeCheck.isSelected(), importantCheck.isSelected());
        System.out.println(todoNew);
    }

    private void handleDatingTodo() throws IntervalException {
        generateInterval();
        String location = getTextContent(datingPanel, 0, 1);
        String people = getTextContent(datingPanel, 1, 1);
        String content = getTextContent(datingPanel, 2, 1);

        todoNew = new DateTodo(content, interval, location, people, emergeCheck.isSelected(), importantCheck.isSelected());
        System.out.println(todoNew);
    }

    private void handleTravelingTodo() throws IntervalException {
        generateInterval();
        String traffic = "";
        if (planeOpt.isSelected())
            traffic = "飞机";
        else if (trainOpt.isSelected())
            traffic = "火车";
        else if (busOpt.isSelected())
            traffic = "大巴";
        else
            traffic = getTextContent(travelingPanel, 0, 4);

        String location = getTextContent(travelingPanel, 1, 1);
        String num = getTextContent(travelingPanel, 2, 1);
        String content = getTextContent(travelingPanel, 3, 1);

        todoNew = new TripTodo(content, interval, traffic, location, num, emergeCheck.isSelected(), importantCheck.isSelected());
    }

    private void handleAnniversaryTodo() throws FormatException, IntervalException {
        CalendarDate date = new CalendarDate(getTextContent(anniverPanel, 0, 1));
        String name = getTextContent(anniverPanel, 2, 1);
        String type = (String) anniType.getSelectedItem();
        String content = getTextContent(anniverPanel, 3, 1);

        todoNew = new MemoryTodo(content, date, type, name, emergeCheck.isSelected(), importantCheck.isSelected());
    }

    private void handleInterviewTodo() throws IntervalException {
        generateInterval();
        String location = getTextContent(interviewingPanel, 0, 1);
        String company = getTextContent(interviewingPanel, 1, 1);
        String post = getTextContent(interviewingPanel, 2, 1);
        String content = getTextContent(interviewingPanel, 3, 1);

        todoNew = new ViewTodo(content, interval, location, company, post, emergeCheck.isSelected(), importantCheck.isSelected());
    }

    private void handleCourseTodo() throws FormatException, IntervalException {
        // CalendarDate date = new CalendarDate(getTextContent(coursePanel, 9, 1));
        int startHr = (int) startHour.getValue();
        int startM = (int) startMin.getValue();
        int endHr = (int) endHour.getValue();
        int endM = (int) endMin.getValue();
        String name = getTextContent(coursePanel, 2, 1);
        String content = getTextContent(coursePanel, 3, 1);
        int lastWeek = (int) durationSpinner.getValue();
        String location = getTextContent(coursePanel, 5, 1);
        String teacher = getTextContent(coursePanel, 6, 1);
        String tip = getTextContent(coursePanel, 7, 1);
        int weekNum = (int) repeatSpinner.getValue();
        CalendarDate cDate = new CalendarDate(getTextContent(coursePanel, 10, 1));

        GregorianCalendar gDate = new GregorianCalendar(cDate.getYear(), cDate.getMonth() - 1, cDate.getDay());
        while (gDate.get(Calendar.DAY_OF_WEEK) != (weekNum + 1)) {
            gDate.add(Calendar.DATE, 1);
        }
        CalendarDate date = new CalendarDate(gDate.get(Calendar.YEAR), gDate.get(Calendar.MONTH) + 1, gDate.get(Calendar.DATE));

        todoNew = new CourseTodo(content, date, startHr, startM, endHr, endM, name, location,
                lastWeek, teacher, tip, weekNum, emergeCheck.isSelected(), importantCheck.isSelected());
    }

    private void handleCustomTodo() throws IntervalException {
        String content = ((JTextArea) ((JPanel) customPanel.getComponent(1)).getComponent(1)).getText();

        if (setTimeCheck.isSelected()) {
            generateInterval();
            todoNew = new OtherTodo(content, interval, emergeCheck.isSelected(), importantCheck.isSelected());
        } else {
            todoNew = new OtherTodo(content, emergeCheck.isSelected(), importantCheck.isSelected());
        }
    }

    public void addComboItems() {
        anniType.addItem("结婚纪念日");
        anniType.addItem("生日");
        anniType.addItem("节日");
    }

    private void initComboBoxItems() {
        addItemOfBox(startYear, endYear, startYearBox, DateUtil.getToday().getYear());
        addItemOfBox(startYear, endYear, endYearBox, DateUtil.getToday().getYear());
        startYearBox.addActionListener(e -> {
            updateComboBox();
        });
        endYearBox.addActionListener(e -> {
            updateComboBox();
        });

        addItemOfBox(1, 12, startMonthBox, DateUtil.getToday().getMonth());
        addItemOfBox(1, 12, endMonthBox, DateUtil.getToday().getMonth());
        startMonthBox.addActionListener(e -> {
            updateComboBox();
        });
        endMonthBox.addActionListener(e -> {
            updateComboBox();
        });

        addItemOfBox(1, DateUtil.getNumOfDaysInMonth(DateUtil.getToday().getMonth(),
                DateUtil.getToday().getYear()), startDayBox, DateUtil.getToday().getDay());
        addItemOfBox(1, DateUtil.getNumOfDaysInMonth(DateUtil.getToday().getMonth(),
                DateUtil.getToday().getYear()), endDayBox, DateUtil.getToday().getDay());
        addItemOfBox(0, 23, startHourBox, 11);
        addItemOfBox(0, 23, endHourBox, 12);
        addItemOfBox(0, 59, startMinBox, 30);
        addItemOfBox(0, 59, endMinBox, 30);
    }

    private void addItemOfBox(int lower, int upper, JComboBox box, int selected) {
        box.removeAllItems();
        for (int i = lower; i <= upper; i++) {
            box.addItem(String.valueOf(i));
        }

        box.setSelectedItem(String.valueOf(selected));
    }

    private void updateComboBox() {
        if (startYearBox.getSelectedItem() != null && startMonthBox.getSelectedItem() != null) {
            int startYr = Integer.parseInt((String) startYearBox.getSelectedItem());
            int startMon = Integer.parseInt((String) startMonthBox.getSelectedItem());
            addItemOfBox(1, DateUtil.getNumOfDaysInMonth(startMon, startYr), startDayBox, 1);
        }

        if (endYearBox.getSelectedItem() != null && endMonthBox.getSelectedItem() != null) {
            int endYr = Integer.parseInt((String) endYearBox.getSelectedItem());
            int endMon = Integer.parseInt((String) endMonthBox.getSelectedItem());
            addItemOfBox(1, DateUtil.getNumOfDaysInMonth(endMon, endYr), endDayBox, 1);
        }
    }

    private String getTextContent(@NotNull JPanel panel, int exIndex, int inIndex) {
        return ((JTextField) ((JPanel) panel.getComponent(exIndex)).getComponent(inIndex)).getText();
    }

    private void customChangeSituation() {
        if (setTimeCheck.isSelected()) {
            //isRemindCheck.setVisible(true);
            //remindPanel.setVisible(isRemindCheck.isSelected());
            startPanel.setVisible(true);
            endPanel.setVisible(true);
        } else {
            //isRemindCheck.setVisible(false);
            //remindPanel.setVisible(false);
            startPanel.setVisible(false);
            endPanel.setVisible(false);
        }
    }

    public int addTodo(Display disp, Todo todo) {
        return disp.addTodo(todo);
    }


    private void generateInterval() throws IntervalException {
        int startYear = Integer.parseInt(startYearBox.getSelectedItem().toString());
        int startMonth = Integer.parseInt(startMonthBox.getSelectedItem().toString());
        int startDay = Integer.parseInt(startDayBox.getSelectedItem().toString());
        int startHr = Integer.parseInt(startHourBox.getSelectedItem().toString());
        int startMin = Integer.parseInt(startMinBox.getSelectedItem().toString());
        GregorianCalendar startTime = new GregorianCalendar(startYear, startMonth - 1, startDay, startHr, startMin);
        //startTime.add(Calendar.YEAR, startYear);
        //System.out.println(startTime.get(Calendar.YEAR));

        int endYear = Integer.parseInt(endYearBox.getSelectedItem().toString());
        int endMonth = Integer.parseInt(endMonthBox.getSelectedItem().toString());
        int endDay = Integer.parseInt(endDayBox.getSelectedItem().toString());
        int endHr = Integer.parseInt(endHourBox.getSelectedItem().toString());
        int endMin = Integer.parseInt(endMinBox.getSelectedItem().toString());
        GregorianCalendar endTime = new GregorianCalendar(endYear, endMonth - 1, endDay, endHr, endMin);

        interval = new CalendarInterval(startTime, endTime);
    }
}
