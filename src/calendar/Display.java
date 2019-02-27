package calendar;/*
* You need to implement Calendar GUI here!
* show the calendar of month of today.
* jump to last/next month's calendar
* jump to last/next year's calendar
*
* jump to one specific day's calendar
* */


import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Timer;

public class Display {
    private final int UB=48;
    private final int LB=7;
    public static final int startYear = 1800;
    public static final int endYear = 2300;

    private JComboBox<String> yearBox;
    private JComboBox<String> monthBox;
    private JButton checkBt;
    private JButton todayBt;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JPanel rootPanel;
    private JPanel midPanel;
    private JTextField dateText;
    private JButton searchBt;
    private JPanel todoPanel;
    private JPanel startPanel;
    private JPanel endPanel;
    private JLabel startLabel;
    private JComboBox startYearBox;
    private JComboBox startMonthBox;
    private JComboBox startDayBox;
    private JComboBox startHourBox;
    private JComboBox startMinBox;
    private JLabel endLabel;
    private JComboBox endYearBox;
    private JComboBox endMonthBox;
    private JComboBox endDayBox;
    private JComboBox endHourBox;
    private JComboBox endMinBox;
    private JButton todoSearchBt;
    private JPanel handlePanel;
    private JComboBox<Todo> todoList;
    private JButton deleteTodoBt;
    private JButton addBt;
    private JLabel contentLabel;
    private JLabel dateLabel;
    private JRadioButton meetingOption;
    private JRadioButton datingOption;
    private JRadioButton otherOption;
    private JTextField people;
    private JTextField issue;
    private JTextField place;
    private JButton childAddBt;
    private JButton completeBt;
    private JComboBox childTodoListBox;
    private JButton childDelButton;
    private JButton childCompleteButton;
    private JLabel levelLabel;
    private JLabel modeLabel;
    private JPanel alertPane;
    private JLabel childContentLabel;
    private JLabel childLevelLabel;
    private JLabel childTimeLabel;
    private JLabel childModeLabel;
    private JLabel infoLabel;
    public JButton addRemindBt;
    public JButton cancelRemindBt;
    public JButton childAddRemindBt;
    public JButton childCancelRemindBt;
    JFrame frame;

    private TodoList todoArray;
    private CalendarDate todoDate;
    private CalendarInterval interval;
    private CalendarDate dateViewing;
    private String addStatus;
    public Todo todoReminding;


    private ArrayList<Remind> reminds;
    //private java.util.Timer timer;

    public Display() throws IntervalException {
        init();
    }

    /**
     * Init the UI Windows here. For example, the frame, some panels and buttons.
     */
    private void init() throws IntervalException {
        reminds = new ArrayList<>();
        frame = new JFrame ("Calendar");
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            //UIManager.put("swing.boldMetal", Boolean.FALSE);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1800, 700);
        Dimension todoSize=new Dimension();
        todoSize.setSize(600,40);
        contentLabel.setPreferredSize(todoSize);
        dateLabel.setPreferredSize(todoSize);
        //frame.pack();
        //frame.setVisible(true);
        //todoArray=new TodoList();
        todoDate=DateUtil.getToday();
        addStatus = "meeting";


        //todoArray = new TodoList();
        //todoArray.loadTodoList();
        try {
            loadTodoList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent windowEvent) {

            }

            @Override
            public void windowClosing(WindowEvent windowEvent) {
                try {
                    saveTodoList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void windowClosed(WindowEvent windowEvent) {

            }

            @Override
            public void windowIconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeiconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowActivated(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeactivated(WindowEvent windowEvent) {

            }
        });


        interval=new CalendarInterval();

        //Component c=midPanel.getComponent(14);
        initComboBoxItems();
        try {
            //generateInterval();
            putTodoIntoList(todoDate);
            if (todoList.getSelectedItem() != null)
                refreshTodo((Todo) todoList.getSelectedItem());
        } catch (IntervalException e1) {
            System.out.println(e1.getMessage());
        }

        paintDays(new CalendarDate(DateUtil.getToday().getYear(), DateUtil.getToday().getMonth(), 1));

        checkBt.addActionListener(event->{
            int year=Integer.parseInt((String)yearBox.getSelectedItem());
            int month=Integer.parseInt((String)monthBox.getSelectedItem());
            try {
                paintDays(new CalendarDate(year, month, 1));
            } catch (IntervalException e) {
                e.printStackTrace();
            }
        });

        todayBt.addActionListener((e -> {
            todoDate=DateUtil.getToday();
            try {
                paintDays(new CalendarDate(DateUtil.getToday().getYear(), DateUtil.getToday().getMonth(), 1));
            } catch (IntervalException e1) {
                e1.printStackTrace();
            }
            yearBox.setSelectedItem(String.valueOf(DateUtil.getToday().getYear()));
            monthBox.setSelectedItem(String.valueOf(DateUtil.getToday().getMonth()));
        }));

        searchBt.addActionListener(e -> {
            try {
                CalendarDate date=new CalendarDate(dateText.getText());
                if(DateUtil.isValid(date)) {
                    todoDate=date;
                    putTodoIntoList(todoDate);
                    paintDays(date);
                }else{
                    System.out.println("Input exception");
                    JOptionPane.showMessageDialog(null,"你的输入，我看不懂");
                }
            } catch (FormatException e1) {
                System.out.println(e1.getMessage());
            } catch (IntervalException e1) {
                e1.printStackTrace();
            }
        });

        todoSearchBt.addActionListener(e->{
            try {
                generateInterval();
                putTodoIntoList(interval);
            } catch (IntervalException e1) {
                System.out.println(e1.getMessage());
            }

        });

        addBt.addActionListener(e->{
            /*if(dateOption.isSelected()){
                Todo todo=constructTodo(todoDate);
                todoArray.add(todo);
                if(todoDate.equals(DateUtil.getToday()) && getDateButton(todoDate)!=null){
                    getDateButton(todoDate).setForeground(Color.MAGENTA);
                }else if(getDateButton(todoDate)!=null){
                    getDateButton(todoDate).setForeground(Color.ORANGE);
                }
                putTodoIntoList(todoDate);
            }else{
                Todo todo= null;
                try {
                    generateInterval();
                    todo = constructTodo(interval);
                    todoArray.add(todo);
                    paintDays(dateViewing);
                    putTodoIntoList(interval);
                } catch (IntervalException e1) {
                    System.out.println(e1.getMessage());
                }
            }*/
            try {
                //generateInterval();
                AddTodoDialog dialog = new AddTodoDialog(this);
                paintDays(dateViewing);
            } catch (IntervalException e1) {
                System.out.println(e1.getMessage());
            }

        });

        deleteTodoBt.addActionListener(e->{
            int confirm = JOptionPane.showConfirmDialog(null, "确定要删除该待办事项吗？");
            try {
                if (todoList.getSelectedItem() != null && confirm == JOptionPane.OK_OPTION) {
                    Todo todo = (Todo) todoList.getSelectedItem();
                    todoArray.delete(todo);
                    if (todo.getReminder() != null) {
                        todo.getReminder().cancelRemind();
                        reminds.remove(todo.getReminder());
                        if (todo == todoReminding) {
                            infoLabel.setText("");
                            todoReminding = null;
                        }
                    }
                    todoList.removeItem(todo);
                    childTodoListBox.removeAllItems();
                    paintDays(dateViewing);
                    if (todo == todoReminding || todo.getChildTodo().contains(todoReminding)) {
                        infoLabel.setText("");
                        todoReminding = null;
                    }
                }
            } catch (Exception e1) {
                System.out.println(e1.getMessage());
            }

        });

        completeBt.addActionListener(e -> {
            if (todoList.getSelectedItem() != null) {
                Todo todo = (Todo) todoList.getSelectedItem();
                if (todo.getMode() == Constants.processing) {
                    int confirm = JOptionPane.showConfirmDialog(null, "确定要完成该待办事项吗？");
                    if (confirm == JOptionPane.OK_OPTION) {
                        try {
                            todo.stateMaintenance(true);
                            if (todo instanceof OtherTodo) {
                                ((OtherTodo) todo).setCompleteTime((GregorianCalendar) GregorianCalendar.getInstance());
                            }
                        } catch (IntervalException e1) {
                            e1.printStackTrace();
                        }
                        refreshTodo(todo);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "待办事项状态不是进行中，无法完成。");
                }
            }
        });

        addRemindBt.addActionListener(e -> {
            if (todoList.getSelectedItem() != null) {
                AddRemindDialog dialog = new AddRemindDialog((Todo) todoList.getSelectedItem(), this);
                //addRemindBt.setEnabled(false);
                //cancelRemindBt.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(null, "请为提醒指定一个待办事项。");
            }
        });

        cancelRemindBt.addActionListener(e -> {
            if (todoList.getSelectedItem() != null) {
                int confirm = JOptionPane.showConfirmDialog(null, "确定要取消该待办事项提醒吗？");
                Todo todo = (Todo) todoList.getSelectedItem();
                if (todo.getReminder() != null && confirm == JOptionPane.OK_OPTION) {
                    todo.getReminder().cancelRemind();
                    reminds.remove(todo.getReminder());
                    todo.setReminder(null);
                    if (todo == todoReminding) {
                        infoLabel.setText("");
                        todoReminding = null;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "请为待办事项设置一个提醒。");
                }
                addRemindBt.setEnabled(true);
                cancelRemindBt.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(null, "请为提醒指定一个待办事项。");
            }
        });

        childAddBt.addActionListener(e -> {
            if (todoList.getSelectedItem() != null && ((Todo) todoList.getSelectedItem()).getMode() != 2) {
                Todo todo = (Todo) todoList.getSelectedItem();
                AddTodoDialog dialog = new AddTodoDialog(this, todo);
                try {
                    paintDays(dateViewing);
                    todoList.setSelectedItem(todo);
                } catch (IntervalException e1) {
                    e1.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "请为它指认一位未完成的父事项，靴靴！");
            }
        });

        childDelButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "确定要删除该子待办事项提醒吗？");
            if (childTodoListBox.getSelectedItem() != null && confirm == JOptionPane.OK_OPTION) {
                Todo todo = (Todo) todoList.getSelectedItem();
                Todo childTodo = (Todo) childTodoListBox.getSelectedItem();
                todo.deleteChildTodo(childTodo);
                if (childTodo.getReminder() != null) {
                    childTodo.getReminder().cancelRemind();
                    reminds.remove(childTodo.getReminder());
            }
                refreshChildTodo((Todo) childTodoListBox.getSelectedItem());
                childTodoListBox.removeItem(childTodo);
                if (childTodo == todoReminding) {
                    infoLabel.setText("");
                    todoReminding = null;
                }
                try {
                    todo.stateMaintenance(false);
                    refreshTodo(todo);
                } catch (IntervalException e1) {
                    e1.printStackTrace();
                }
            } else {
                childContentLabel.setText("");
                childLevelLabel.setText("");
                childTimeLabel.setText("");
                childModeLabel.setText("");
            }
        });

        childCompleteButton.addActionListener(e -> {
            if (childTodoListBox.getSelectedItem() != null) {
                Todo todo = (Todo) childTodoListBox.getSelectedItem();
                Todo parent = (Todo) todoList.getSelectedItem();
                if (todo.getMode() == Constants.processing) {
                    int confirm = JOptionPane.showConfirmDialog(null, "确定要完成该待办事项吗？");
                    if (confirm == JOptionPane.OK_OPTION) {
                        try {
                            todo.stateMaintenance(true);
                            parent.stateMaintenance(true);
                            if (todo instanceof OtherTodo) {
                                ((OtherTodo) todo).setCompleteTime((GregorianCalendar) GregorianCalendar.getInstance());
                            }
                        } catch (IntervalException e1) {
                            e1.printStackTrace();
                        }
                        refreshChildTodo(todo);
                        refreshTodo(parent);
                    }
                }
            }
        });

        childAddRemindBt.addActionListener(e -> {
            if (childTodoListBox.getSelectedItem() != null) {
                AddRemindDialog dialog = new AddRemindDialog((Todo) childTodoListBox.getSelectedItem(), this);
                //childAddRemindBt.setEnabled(false);
                //childCancelRemindBt.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(null, "请为提醒指定一个待办事项。");
            }
        });

        childCancelRemindBt.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "确定要取消该子待办事项提醒吗？");
            if (childTodoListBox.getSelectedItem() != null && confirm == JOptionPane.OK_OPTION) {
                Todo todo = (Todo) childTodoListBox.getSelectedItem();
                if (todo.getReminder() != null) {
                    todo.getReminder().cancelRemind();
                    reminds.remove(todo.getReminder());
                    todo.setReminder(null);
                    if (todo == todoReminding) {
                        infoLabel.setText("");
                        todoReminding = null;
                    }
                }
                childAddRemindBt.setEnabled(true);
                childCancelRemindBt.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(null, "请为提醒指定一个待办事项。");
            }
        });

        todoList.addActionListener(e->{
            if(todoList.getSelectedItem()!=null) {
                Todo todo = (Todo) todoList.getSelectedItem();
                refreshTodo(todo);
                putChildIntoList(todo);
                if (todo instanceof OtherTodo && ((OtherTodo) todo).getPersistance()) {
                    addRemindBt.setEnabled(false);
                    cancelRemindBt.setEnabled(false);
                } else if (todo.getReminder() == null) {
                    addRemindBt.setEnabled(true);
                    cancelRemindBt.setEnabled(false);
                } else {
                    addRemindBt.setEnabled(false);
                    cancelRemindBt.setEnabled(true);
                }
            }else{
                contentLabel.setText("");
                dateLabel.setText("");
                modeLabel.setText("");
                levelLabel.setText("");
                childContentLabel.setText("");
                childLevelLabel.setText("");
                childTimeLabel.setText("");
                childModeLabel.setText("");
            }
        });

        childTodoListBox.addActionListener(e -> {
            if (childTodoListBox.getSelectedItem() != null) {
                Todo todo = (Todo) childTodoListBox.getSelectedItem();
                refreshChildTodo(todo);

                if (todo instanceof OtherTodo && ((OtherTodo) todo).getPersistance()) {
                    childAddRemindBt.setEnabled(false);
                    childCancelRemindBt.setEnabled(false);
                } else if (todo.getReminder() == null) {
                    childAddRemindBt.setEnabled(true);
                    childCancelRemindBt.setEnabled(false);
                } else {
                    childAddRemindBt.setEnabled(false);
                    childCancelRemindBt.setEnabled(true);
                }
            } else {
                childContentLabel.setText("");
                childLevelLabel.setText("");
                childTimeLabel.setText("");
                childModeLabel.setText("");
            }
        });

        frame.setVisible(true);
    }

    /**
     * paint the days of whole current month on the frame with the given CalendarDate
     * @param date a valid CalendarDate param.
     */
    private boolean paintDays(CalendarDate date) throws IntervalException {
        dateViewing=date;

        if(!DateUtil.isValid(date)){
            return false;
        }

        ArrayList<CalendarDate> dateList=(ArrayList<CalendarDate>)DateUtil.getDaysInMonth(date);
        for(int i=LB; i<=UB; i++){
            JButton bt=(JButton)midPanel.getComponent(i);
            bt.setText(" ");
            bt.setBorderPainted(false);
            //bt.setForeground(Color.BLACK);
            Dimension size=new Dimension();
            size.setSize(80,40);
            bt.setPreferredSize(size);
            for(ActionListener e: bt.getActionListeners()){
                bt.removeActionListener(e);
            }
            //bt.setEnabled(true);
        }

        for(CalendarDate monthDay: dateList){
            JButton bt = getDateButton (monthDay);
            bt.setText ("<html><div style='text-align:center'>" + String.valueOf (monthDay.getDay ()) + "<br><span style='color:red'>" + monthDay.getFestival () + "</span><span style='font-size:5px'>" + (monthDay.getIsHoliday () ? "休" : (monthDay.getIsWorkday () ? "班" : "")) + "</span></div></html>");
            bt.addActionListener (e -> {
                for (int i = LB; i <= UB; i++) {
                    JButton b = (JButton) midPanel.getComponent (i);
                    //b.setEnabled(true);
                    b.setBorderPainted (false);
                }
                todoDate=monthDay;
                try {
                    putTodoIntoList (todoDate);
                } catch (IntervalException e1) {
                    e1.printStackTrace ();
                }

                //bt.setEnabled(false);
                bt.setBorderPainted (true);
            });
            if (monthDay.equals (DateUtil.getToday ()) && !todoArray.search (monthDay).isEmpty ()) {
                bt.setForeground (Color.MAGENTA);
            } else if (!todoArray.search (monthDay).isEmpty ()) {
                bt.setForeground (Color.ORANGE);
            } else if (monthDay.equals (DateUtil.getToday ())) {
                bt.setForeground (Color.BLUE);
            } else {
                bt.setForeground (Color.BLACK);
            }
        }

        if(getDateButton(todoDate)!=null)
            getDateButton(todoDate).setBorderPainted(true);
        //getDateButton(todoDate).setEnabled(false);

        yearBox.setSelectedItem(String.valueOf(date.getYear()));
        monthBox.setSelectedItem(String.valueOf(date.getMonth()));

        putTodoIntoList(todoDate);
        if (todoList.getSelectedItem() != null) {
            Todo todo = (Todo) todoList.getSelectedItem();
            refreshTodo(todo);
            putChildIntoList(todo);
        } else {
            childTodoListBox.removeAllItems();
        }

        return true;
    }

    public ArrayList<Remind> getReminds() {
        return reminds;
    }

    public void paneAlert(String info) {
        infoLabel.setText(info);
    }

    private void putTodoIntoList(CalendarDate date) throws IntervalException {
        todoList.removeAllItems();
        ArrayList<Todo> result=todoArray.search(date);
        if(result!=null) {
            result.forEach(todo -> {
                if (!todo.getHasParent()) {
                    try {
                        if (!(todo instanceof OtherTodo))
                            todo.stateMaintenance(false);
                    } catch (IntervalException e) {
                        e.printStackTrace();
                    }
                    todoList.addItem(todo);
                }
            });
        }

        if (todoList.getSelectedItem() != null) {
            Todo todo = (Todo) todoList.getSelectedItem();
            refreshTodo(todo);
            putChildIntoList(todo);

            if (todo instanceof OtherTodo && ((OtherTodo) todo).getPersistance()) {
                addRemindBt.setEnabled(false);
                cancelRemindBt.setEnabled(false);
            } else if (todo.getReminder() == null) {
                addRemindBt.setEnabled(true);
                cancelRemindBt.setEnabled(false);
            } else {
                addRemindBt.setEnabled(false);
                cancelRemindBt.setEnabled(true);
            }
        } else {
            childTodoListBox.removeAllItems();
        }
    }

    private void putTodoIntoList(CalendarInterval inter) throws IntervalException {
        todoList.removeAllItems();
        if(inter!=null) {
            ArrayList<Todo> result = todoArray.search(inter);
            if (result != null) {
                result.forEach(todo -> {
                    if (!todo.getHasParent()) {
                        try {
                            if (!(todo instanceof OtherTodo))
                                todo.stateMaintenance(false);
                        } catch (IntervalException e) {
                            e.printStackTrace();
                        }
                        todoList.addItem(todo);
                    }
                });
            }
        }

        int year=inter.getStartTime().get(Calendar.YEAR);
        int month=inter.getStartTime().get(Calendar.MONTH);
        int day=inter.getStartTime().get(Calendar.DATE);
        todoDate=dateViewing=new CalendarDate(year, month+1, day);
        //paintDays(dateViewing);
        if (todoList.getSelectedItem() != null) {
            Todo todo = (Todo) todoList.getSelectedItem();
            refreshTodo(todo);
            putChildIntoList(todo);

            if (todo instanceof OtherTodo && ((OtherTodo) todo).getPersistance()) {
                addRemindBt.setEnabled(false);
                cancelRemindBt.setEnabled(false);
            } else if (todo.getReminder() == null) {
                addRemindBt.setEnabled(true);
                cancelRemindBt.setEnabled(false);
            } else {
                addRemindBt.setEnabled(false);
                cancelRemindBt.setEnabled(true);
            }
        } else {
            childTodoListBox.removeAllItems();
        }
    }

    private void refreshTodo(Todo todo) {
        contentLabel.setText(todo.toString());
        String urgency = todo.getUrgency() ? "紧急" : "不紧急";
        String important = todo.getImportance() ? "重要" : "不重要";
        levelLabel.setText(urgency + " " + important);

        String mode;
        switch (todo.getMode()) {
            case Constants.notStart:
                mode = "未开始";
                break;
            case Constants.processing:
                mode = "进行中";
                break;
            case Constants.completed:
                mode = "已完成";
                break;
            case Constants.expired:
                mode = "过期";
                break;
            default:
                mode = "未知";
                JOptionPane.showMessageDialog(null, "出现了关于状态的问题");

        }
        modeLabel.setText(mode);

        if (!(todo instanceof OtherTodo) && todo.isUsingInterval()) {
            String startDisplay = DateUtil.calendarToString(todo.getInterval().getStartTime());
            String endDisplay = DateUtil.calendarToString(todo.getInterval().getEndTime());
            dateLabel.setText(startDisplay + " ~ " + endDisplay);
        } else if (!(todo instanceof OtherTodo) && !todo.isUsingInterval() && !(todo instanceof CourseTodo)) {
            dateLabel.setText(todo.getDate().toString());
        } else if (todo instanceof CourseTodo) {
            CourseTodo cTodo = (CourseTodo) todo;
            dateLabel.setText(cTodo.getStartHour() + ":" + cTodo.getStartMinute() + "~" + cTodo.getEndHour() + ":" + cTodo.getEndMinute());
        } else if (todo instanceof OtherTodo && ((OtherTodo) todo).getPersistance()) {
            dateLabel.setText("");
        }
    }

    private void refreshChildTodo(Todo todo) {
        childContentLabel.setText(todo.toString());
        String urgency = todo.getUrgency() ? "紧急" : "不紧急";
        String important = todo.getImportance() ? "重要" : "不重要";
        childLevelLabel.setText(urgency + " " + important);

        String mode;
        switch (todo.getMode()) {
            case Constants.notStart:
                mode = "未开始";
                break;
            case Constants.processing:
                mode = "进行中";
                break;
            case Constants.completed:
                mode = "已完成";
                break;
            case Constants.expired:
                mode = "过期";
                break;
            default:
                mode = "未知";
                JOptionPane.showMessageDialog(null, "出现了关于状态的问题");

        }
        childModeLabel.setText(mode);

        if (todo.isUsingInterval()) {
            String startDisplay = DateUtil.calendarToString(todo.getInterval().getStartTime());
            String endDisplay = DateUtil.calendarToString(todo.getInterval().getEndTime());
            childTimeLabel.setText(startDisplay + " ~ " + endDisplay);
        } else {
            childTimeLabel.setText(todo.getDate().toString());
        }

    }

    public void putChildIntoList(Todo parent) {
        try {
            generateInterval();
        } catch (IntervalException e) {
            e.printStackTrace();
        }
        childTodoListBox.removeAllItems();
        parent.getChildTodo().forEach(child -> {
            try {
                if (!(child instanceof OtherTodo))
                    child.stateMaintenance(false);
            } catch (IntervalException e) {
                e.printStackTrace();
            }
            childTodoListBox.addItem(child);
        });

        if (childTodoListBox.getSelectedItem() != null) {
            Todo todo = (Todo) childTodoListBox.getSelectedItem();
            refreshChildTodo(todo);

            if (todo instanceof OtherTodo && ((OtherTodo) todo).getPersistance()) {
                childAddRemindBt.setEnabled(false);
                childCancelRemindBt.setEnabled(false);
            } else if (todo.getReminder() == null) {
                childAddRemindBt.setEnabled(true);
                childCancelRemindBt.setEnabled(false);
            } else {
                childAddRemindBt.setEnabled(false);
                childCancelRemindBt.setEnabled(true);
            }
        } else {
            childContentLabel.setText("");
            childLevelLabel.setText("");
            childTimeLabel.setText("");
            childModeLabel.setText("");
        }
    }

    private void changeStatusOfTodoDatePanel(boolean status){
        for (Component component:
                startPanel.getComponents()) {
            component.setEnabled(status);
        }

        for (Component component:
                endPanel.getComponents()) {
            component.setEnabled(status);
        }

        todoSearchBt.setEnabled(status);
    }

    private void generateInterval() throws IntervalException {
        int startYear=Integer.parseInt(startYearBox.getSelectedItem().toString());
        int startMonth=Integer.parseInt(startMonthBox.getSelectedItem().toString());
        int startDay=Integer.parseInt(startDayBox.getSelectedItem().toString());
        int startHr=Integer.parseInt(startHourBox.getSelectedItem().toString());
        int startMin=Integer.parseInt(startMinBox.getSelectedItem().toString());
        GregorianCalendar startTime=new GregorianCalendar(startYear, startMonth-1, startDay, startHr, startMin);
        //startTime.add(Calendar.YEAR, startYear);
        //System.out.println(startTime.get(Calendar.YEAR));

        int endYear=Integer.parseInt(endYearBox.getSelectedItem().toString());
        int endMonth=Integer.parseInt(endMonthBox.getSelectedItem().toString());
        int endDay=Integer.parseInt(endDayBox.getSelectedItem().toString());
        int endHr=Integer.parseInt(endHourBox.getSelectedItem().toString());
        int endMin=Integer.parseInt(endMinBox.getSelectedItem().toString());
        GregorianCalendar endTime=new GregorianCalendar(endYear, endMonth-1, endDay, endHr, endMin);

        interval=new CalendarInterval(startTime, endTime);
    }

    private void initComboBoxItems(){
        addItemOfBox(startYear, endYear, yearBox, DateUtil.getToday().getYear());
        addItemOfBox(startYear, endYear, startYearBox, DateUtil.getToday().getYear());
        addItemOfBox(startYear, endYear, endYearBox, DateUtil.getToday().getYear());
        startYearBox.addActionListener(e->{
            updateComboBox();
        });
        endYearBox.addActionListener(e->{
            updateComboBox();
        });

        addItemOfBox(1, 12, monthBox, DateUtil.getToday().getMonth());
        addItemOfBox(1, 12, startMonthBox, DateUtil.getToday().getMonth());
        addItemOfBox(1, 12, endMonthBox, DateUtil.getToday().getMonth());
        startMonthBox.addActionListener(e->{
            updateComboBox();
        });
        endMonthBox.addActionListener(e->{
            updateComboBox();
        });

        addItemOfBox(1, DateUtil.getNumOfDaysInMonth(DateUtil.getToday().getMonth(), DateUtil.getToday ().getYear ()), startDayBox, DateUtil.getToday ().getDay ());
        addItemOfBox(1, DateUtil.getNumOfDaysInMonth(DateUtil.getToday().getMonth(),
                DateUtil.getToday().getYear()), endDayBox, DateUtil.getToday().getDay());
        addItemOfBox(0, 23, startHourBox, 11);
        addItemOfBox(0, 23, endHourBox, 12);
        addItemOfBox(0, 59, startMinBox, 30);
        addItemOfBox(0, 59, endMinBox, 30);
    }

    private void addItemOfBox(int lower, int upper, JComboBox box, int selected){
        box.removeAllItems();
        for(int i=lower; i<=upper; i++){
            box.addItem(String.valueOf(i));
        }

        box.setSelectedItem(String.valueOf(selected));
    }

    private void updateComboBox(){
        if(startYearBox.getSelectedItem()!=null && startMonthBox.getSelectedItem()!=null) {
            int startYr = Integer.parseInt((String)startYearBox.getSelectedItem());
            int startMon = Integer.parseInt((String)startMonthBox.getSelectedItem());
            addItemOfBox(1, DateUtil.getNumOfDaysInMonth(startMon,startYr), startDayBox, 1);
        }

        if(endYearBox.getSelectedItem()!=null && endMonthBox.getSelectedItem()!=null) {
            int endYr = Integer.parseInt((String) endYearBox.getSelectedItem());
            int endMon = Integer.parseInt((String) endMonthBox.getSelectedItem());
            addItemOfBox(1, DateUtil.getNumOfDaysInMonth(endMon,endYr), endDayBox, 1);
        }
    }

    private JButton getDateButton(CalendarDate monthDay){
        if(monthDay.getYear()==dateViewing.getYear() && monthDay.getMonth()==dateViewing.getMonth())
            return (JButton)midPanel.getComponent(monthDay.getWeek()*7+monthDay.getDayOfWeek()%7);
        return null;
    }

    public int addTodo(Todo todo) {
        int i = -1;
        /*if (dateOption.isSelected()) {
            try {
                todoArray.add(todo);
                if (todoDate.equals(DateUtil.getToday()) && getDateButton(todoDate) != null) {
                    getDateButton(todoDate).setForeground(Color.MAGENTA);
                } else if (getDateButton(todoDate) != null) {
                    getDateButton(todoDate).setForeground(Color.ORANGE);
                }
                putTodoIntoList(todoDate);
            } catch (IntervalException e) {
                e.printStackTrace();
            }
        } else {*/
        try {
            //generateInterval();
            i = todoArray.add(todo);
            paintDays (dateViewing);
            putTodoIntoList (todoDate);
        } catch (IntervalException e1) {
            System.out.println (e1.getMessage ());
        }
        // }
        return i;
    }

    public TodoList getTodoArray() {
        return todoArray;
    }

    public void setTodoArray(TodoList todoArray) {
        this.todoArray = todoArray;
    }

    public void setVi() {
        frame.setVisible (false);
    }

    public void saveTodoList() throws IOException {
        ArrayList remindnTodoList = new ArrayList();
        remindnTodoList.add(todoArray);
        remindnTodoList.add(reminds);

        FileOutputStream out = new FileOutputStream("todos.txt");
        ObjectOutputStream obj = new ObjectOutputStream(out);
        obj.writeObject(remindnTodoList);
        obj.close();
    }

    public void loadTodoList() throws IOException, ClassNotFoundException {
        FileInputStream in = new FileInputStream("todos.txt");
        ObjectInputStream obj = new ObjectInputStream(in);
        ArrayList remindnTodoList = (ArrayList) obj.readObject();
        todoArray = (TodoList) remindnTodoList.get(0);
        reminds = (ArrayList<Remind>) remindnTodoList.get(1);
        reminds.forEach(remind -> remind.startRemind(this));

        obj.close();
    }

}
