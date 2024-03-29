package org.osbot.jailbreak.ui;

import com.sun.tools.attach.VirtualMachineDescriptor;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class ClientSelectorView extends JPanel implements ActionListener {

	private final LauncherController controller;
	private final JButton jailbreak, refresh, startClient;
	private final JList<String> jvms;
	private final DefaultListModel<String> jvmsModel;
	private final JLabel status;

	public ClientSelectorView(LauncherController controller) {
		this.controller = controller;
		this.setLayout(new BorderLayout());
		this.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 2), "Launcher"));

		this.status = new JLabel("<html>Status: <font color='green'>Ready</font></html>");
		add(status, BorderLayout.NORTH);

		this.jvmsModel = new DefaultListModel<>();
		this.jvms = new JList<>(jvmsModel);
		this.jvms.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.jvms.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(final ListSelectionEvent e) {
				if (!jvms.isSelectionEmpty()) {
					jailbreak.setEnabled(true);
				} else {
					jailbreak.setEnabled(false);
				}
			}
		});
		refreshList();
		add(jvms, BorderLayout.CENTER);

		Box buttonBox = Box.createHorizontalBox();
		this.startClient = new JButton("Start OSBot");
		this.startClient.setActionCommand("start osbot");
		this.startClient.addActionListener(this::actionPerformed);
		buttonBox.add(startClient);
		this.jailbreak = new JButton("Jailbreak");
		this.jailbreak.addActionListener(this);
		this.jailbreak.setEnabled(false);
		this.jailbreak.setActionCommand("jailbreak");
		buttonBox.add(jailbreak);
		this.refresh = new JButton("Refresh");
		this.refresh.setActionCommand("refresh");
		this.refresh.addActionListener(this::actionPerformed);
		buttonBox.add(refresh);
		add(buttonBox, BorderLayout.SOUTH);

		setPreferredSize(new Dimension(350, 200));
	}

	private void refreshList() {
		this.jvmsModel.clear();
		HashMap<String, VirtualMachineDescriptor> jvms = controller.getJVMs();
		if (jvms.isEmpty()) {
			this.jvmsModel.addElement("No OSBot Clients found.");
			this.jvms.setEnabled(false);
			return;
		}
		for (Map.Entry<String, VirtualMachineDescriptor> entry : jvms.entrySet()) {
			this.jvmsModel.addElement(entry.getKey());
		}
		this.jvms.setEnabled(true);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		switch (e.getActionCommand()) {
			case "refresh":
				refreshList();
				break;
			case "jailbreak":
				controller.jailbreak(jvms.getSelectedValuesList());
				break;
			case "start osbot":
				controller.showOSBotLoginView();
				break;
		}

	}
}
