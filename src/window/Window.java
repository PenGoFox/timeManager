package window;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.*;

import javax.swing.*;

import timeManager.Plan.Plan;
import timeManager.Plan.PlanContent;
import timeManager.Plan.PlanTime;
import timeManager.TimeManager;

public class Window extends JFrame {
	private TimeManager timeManager;
	private PlanEditWindow planEditWindow;

	public Window(String title, TimeManager timeManager) {
		this.timeManager = timeManager;
		this.planEditWindow = new PlanEditWindow("Edit plan");
		this.timeManager.readFromFile();
		ImageIcon icon = new ImageIcon("icon.png");
		Image image = icon.getImage();
		this.setIconImage(image);
		
		this.addSystemTray(image); // 系统托盘
		
		this.setTitle(title);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
		this.setMinimumSize(new Dimension(1024, 768));
		
		this.paintContent();
		
		GUITools.center(this);
		
		this.setVisible(true);

		// =============== 下面这条语句仅供测试 =================
		//this.timeManager.setPlan(new PlanTime(), new PlanContent());
		// =============== 上面这条语句仅供测试 =================

		// 启动弹窗管理器
		Runnable popupManagerRunnable = new PopupManager(this.timeManager);
		new Thread(popupManagerRunnable).start();
	}

	// 添加系统托盘
	private void addSystemTray(Image image) {
		if (SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();
			TrayIcon trayIcon = new TrayIcon(image, "TimeManager");
			trayIcon.setImageAutoSize(true);

			// 添加一个右键菜单
			PopupMenu menu = new PopupMenu();
			MenuItem exitMenuItem = new MenuItem("Exit");
			exitMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("exit");
					exit();
				}
			});
			menu.add(exitMenuItem);
			trayIcon.setPopupMenu(menu);
			try {
				tray.add(trayIcon);
			} catch (AWTException e1) {
				e1.printStackTrace();
			}

			// 左键单击显示窗口
			trayIcon.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1) {
						setVisible(true);
					}
					// 下面这段用来测试的而已
//					if (e.getButton() == MouseEvent.BUTTON2) {
//
//					}
				}
			});
		}
	}
	
	private void paintContent() {
		// 设置程序图标
		this.setIconImage(new ImageIcon("image/icon.png").getImage());

		// 容器panel
		JPanel containerPanel = new JPanel(new GridBagLayout());
		GridBagConstraints containerConstraints = new GridBagConstraints();
		// 时间panel
		JPanel timePanel = new JPanel(new GridLayout());
		timePanel.add(new JLabel("timePanel"));
		// 星期几panel
		JPanel dayPanel = new JPanel(new GridLayout(1, 7));
		//dayPanel.add(new JButton("dayPanel"));
		// 计划panel
		JPanel planPanel = new JPanel(new GridLayout(1, 7));
		//planPanel.add(new JButton("PlanPanel"));

		// 设置布局
		containerConstraints.fill = GridBagConstraints.BOTH;
		// 星期几panel
		containerConstraints.gridx = 1;
		containerConstraints.gridy = 0;
		containerConstraints.gridwidth = GridBagConstraints.REMAINDER;
		containerConstraints.weightx = 15;
		containerConstraints.weighty = 1;
		containerPanel.add(dayPanel, containerConstraints);
		// 时间panel
		containerConstraints.gridx = 0;
		containerConstraints.gridy = 1;
		containerConstraints.gridwidth = 1;
		containerConstraints.weightx = 1;
		containerConstraints.weighty = 15;
		containerPanel.add(timePanel, containerConstraints);
		// 计划panel
		containerConstraints.gridx = 1;
		containerConstraints.gridy = 1;
		containerConstraints.gridwidth = GridBagConstraints.REMAINDER;
		containerConstraints.weightx = 15;
		containerConstraints.weighty = 15;
		containerPanel.add(planPanel, containerConstraints);

		// 窗口添加容器panel
		this.add(containerPanel);

		// 添加星期信息
		String day[] = {"日", "一", "二", "三", "四", "五", "六"};
		for (int i=0; i<7; i++) {
			JLabel dayLabel = new JLabel("星期" + day[i]);
			dayLabel.setHorizontalAlignment(JLabel.CENTER);
			dayPanel.add(dayLabel);
		}

		// 7天的planPanel
		JPanel[] dayPlanPanel = new JPanel[7];
		for (int i=0; i<7; i++) {
			dayPlanPanel[i] = new JPanel();
			dayPanel.add(dayPlanPanel[i]);
		}

		

		// 因为取计划的时候已经排过序了，所以这里直接为对应的dayPlanPanel添加plan信息
//		for (int i=0; i<this.timeManager.getPlanSize(); i++) {
//			Plan plan = this.timeManager.getPlan(i);
//			int whichDay = plan.getTime().getDay();
//			//dayPlanPanel[whichDay].add();
//		}

		//这里要把上面的两个循环改一下，完成最终的计划的显示

//
//		Map<Integer, String> planMap = new HashMap<Integer, String>();
//		// 星期几
//		planMap.put(0, "");
//		planMap.put(1, "星期天");
//		planMap.put(2, "星期一");
//		planMap.put(3, "星期二");
//		planMap.put(4, "星期三");
//		planMap.put(5, "星期四");
//		planMap.put(6, "星期五");
//		planMap.put(7, "星期六");
//
//		// 时间
//		for (int i=0; i<24; i++) {
//			planMap.put((i+1)*8, String.valueOf(i));
//		}
//
//		// 计划
//		int index;
//		String plan;
//		PlanTime planTime;
//		for (int row=0; row<24; row++) {
//			for (int column=0; column<7; column++) {
//				planTime = new PlanTime(column, row, 0);
//				index = (row + 1) * 8 + column + 1;
//				plan = this.timeManager.getPlan(planTime);
//				planMap.put(index, plan);
//			}
//		}
		
		// 画组件
//		for (int i=0; i<8*25; i++) {
//			JButton button = new JButton(planMap.get(i));
//			button.setName(String.valueOf(i));
//			button.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					planPress(button.getName(), button);
//				}
//			});
//			this.add(button);
//		}

	}
	
	private void exit() {
		this.timeManager.saveToFile();
		this.dispose();
		System.exit(0);
	}
	
	private void planPress(String name, JButton button) {
		int number = Integer.valueOf(name);
		int row = number / 8 - 1;
		int column = number % 8 - 1;
		if (row < 0 || column < 0) {
			System.out.println("not plan table");
			return;
		}
		PlanTime planTime = new PlanTime(column, row, 0);
		this.planEditWindow.setPlan(this.timeManager.getPlan(planTime));
		
		this.planEditWindow.setVisible(true);
		
		// 写完计划后就获取计划
		PlanContent newPlanContent =  this.planEditWindow.getPlan();
		this.timeManager.setPlan(planTime, newPlanContent);
		button.setText(this.timeManager.getPlan(planTime).toString());
		// 保存计划
		timeManager.saveToFile();
	}
}
