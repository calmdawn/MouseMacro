

import java.awt.AWTException;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;

public class MouseMacro extends JFrame {

	private JPanel contentPane;
	private JTextField textField_editText;
	private JTextField textField_mouseLocate;
	private JTextField textField_mouseInfo;
	PointerInfo M_pointer = MouseInfo.getPointerInfo(); // �ʱ� ����� ���콺 ��ǥ������ //���콺 ��ǥ�� ������� ������
	private JTextField textField_addInfo;
	private JTextField textField_delayInfo;
	JFormattedTextField formattedTextField_delay;
	private JTextField textField_startInfo;
	boolean state; // ��ũ�� ���ۻ��� true�̸� ������ false�� ����
	private JButton button_allStart; // ��ũ�� ��ü����
	private JButton button_partStart;// ��ũ�� ������ ��ġ���� ����

	int btn; // ��ũ�� ��� �������� ��ư�� ������ �����忡 �Ѱ��ֱ����� 1.��ü 2.�κ�
	Robot robot = new Robot(); // Robot class�� �̿��� ���콺 ��Ʈ�� ���� ����

	// ����Ʈ�ڽ��� ���� ������

	/*
	 * String[] listText = {}; //�����Ͱ����� �������� Ȯ���ؾ��� for(int i=0; i<listText.length;
	 * i++) { m.add(i, listText[i]); }
	 */

	DefaultListModel<String> m = new DefaultListModel<String>();
	JList<String> list = new JList<String>(m);
	// delay���� ������ ���� ������

	ArrayList<Integer> listDelay = new ArrayList<>();

	// ���콺 ��ǥ�� ���������� ������

	ArrayList<Integer> listPosX = new ArrayList<>(); // ���콺 X��ǥ
	ArrayList<Integer> listPosY = new ArrayList<>(); // ���콺 Y��ǥ
	ArrayList<String> listText = new ArrayList<>(); // ������� �� �ؽ�Ʈ��

	private JTextField textField_stateStart;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MouseMacro frame = new MouseMacro();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws AWTException
	 */
	public MouseMacro() throws AWTException {
		setBackground(Color.LIGHT_GRAY);
		setTitle("��ǥ��ũ�� ver0.2");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 640);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// ����Ʈ�ڽ� ��ũ�ѵǴ� �г� �߰�

		list.setFont(new Font("�������", Font.BOLD, 12));

		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBounds(20, 20, 476, 500);
		contentPane.add(scrollPane);

		// �ڹ� ��ư ���������

		JButton button_delayChange = new JButton("������ǥ �����̸� ����"); // ��ũ�� ������ ���� ��ư
		button_delayChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedIndex() != -1) {
					int index = list.getSelectedIndex(); // ���õ� index ������
					int delayTime = 0;
					listDelay.remove(index); // Ŭ���� �������� index�� �ִ� delay�� �����Ѵ�.
					m.remove(list.getSelectedIndex()); // ���õ� index ����
					delayTime = Integer.parseInt(formattedTextField_delay.getText()) * 1000;
					listDelay.add(index, delayTime); // ������ index�� �ڸ��� ����ǥ���� �����̷� ����
					m.add(index, "������: " + formattedTextField_delay.getText() + "����" + "  ���콺�̵��� �� Ŭ��   "
							+ textField_mouseLocate.getText() + "   ����: " + textField_editText.getText()); // �����ߴ�
																											// index��
																											// textField����
																											// ������ ������
																											// set��
																											// ���ص��ǳ�?
				}
			}
		});
		button_delayChange.setFont(new Font("�������", Font.BOLD, 10));
		button_delayChange.setBackground(Color.LIGHT_GRAY);
		button_delayChange.setBounds(627, 92, 130, 23);
		contentPane.add(button_delayChange);

		JButton button_newInsert = new JButton("�����߰�"); // �����߰� ��ư
		button_newInsert.setBounds(508, 188, 71, 23);
		button_newInsert.addActionListener(new ActionListener() { // �߰� ��ư Ŭ���� �߻��ϴ� �̺�Ʈ -> �ǵڷ� �߰���
			public void actionPerformed(ActionEvent e) {
				int delayTime = 0; // delay�� �ʴ����� �ϱ�����

				m.addElement("������: " + formattedTextField_delay.getText() + "����" + "  ���콺�̵��� �� Ŭ��   "
						+ textField_mouseLocate.getText() + "   ����: " + textField_editText.getText()); // textField����
																										// ������ �����ͼ� ��������
																										// �߰�
				listPosX.add(M_pointer.getLocation().x); // x��ǥ �߰�
				listPosY.add(M_pointer.getLocation().y); // y��ǥ �߰�

				delayTime = Integer.parseInt(formattedTextField_delay.getText()) * 1000;
				listDelay.add(delayTime); // delay�� �߰�
				listText.add(textField_editText.getText()); // ����Ʈ�� ���� �߰�

			}
		});
		button_newInsert.setFont(new Font("�������", Font.BOLD, 10));
		button_newInsert.setBackground(Color.LIGHT_GRAY);
		contentPane.add(button_newInsert);

		JButton button_partInsert = new JButton("������ ��ġ�߰�"); // ������ ��ġ�߰� ��ư
		button_partInsert.setBounds(628, 188, 100, 23);
		button_partInsert.addActionListener(new ActionListener() { // �߰� ��ư Ŭ���� �߻��ϴ� �̺�Ʈ -> ���õȾ����� ������ �߰���
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedIndex() != -1) {
					int index = list.getSelectedIndex(); // ���õ� index ������
					int delayTime = 0; // delay�� �ʴ����� �ϱ�����
					m.add(index, "������: " + formattedTextField_delay.getText() + "����" + "  ���콺�̵��� �� Ŭ��   "
							+ textField_mouseLocate.getText() + "   ����: " + textField_editText.getText()); // textField����
																											// ������ �����ͼ�
																											// �������� �߰�
					listPosX.add(index, M_pointer.getLocation().x); // x��ǥ �߰�
					listPosY.add(index, M_pointer.getLocation().y); // y��ǥ �߰�

					delayTime = Integer.parseInt(formattedTextField_delay.getText()) * 1000;
					listDelay.add(index, delayTime); // delay�� �߰�
					listText.add(index, textField_editText.getText()); // ����Ʈ�� ���� �߰�
				}
			}
		});
		button_partInsert.setFont(new Font("�������", Font.BOLD, 10));
		button_partInsert.setBackground(Color.LIGHT_GRAY);
		contentPane.add(button_partInsert);

		JButton button_delete = new JButton("���� ����"); // ���� ��ư
		button_delete.setBounds(508, 251, 85, 23);
		button_delete.addActionListener(new ActionListener() { // ���� ��ư Ŭ���� �߻��ϴ� �̺�Ʈ -> ���õȾ����� ������
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedIndex() != -1) { // list.getSelectedIndex() <- ���õ� ����Ʈ �� ������
					int index = list.getSelectedIndex();
					listPosX.remove(index); // Ŭ���� �������� index�� �ִ� ���콺 X��ǥ�� �����Ѵ�.
					listPosY.remove(index); // Ŭ���� �������� index�� �ִ� ���콺 Y��ǥ�� �����Ѵ�.
					listDelay.remove(index); // Ŭ���� �������� index�� �ִ� delay�� �����Ѵ�.
					listText.remove(index); // Ŭ���� �������� index�� �ִ� ������ �����Ѵ�.
					m.remove(list.getSelectedIndex()); // Ŭ���� �������� index�� �����ͼ� list���� �����Ѵ�.

				}
			}
		});

		button_delete.setFont(new Font("�������", Font.BOLD, 12));
		button_delete.setBackground(Color.LIGHT_GRAY);
		contentPane.add(button_delete);

		JButton button_change = new JButton("���� ����"); // ���� ��ư
		button_change.setBounds(508, 310, 85, 23);
		button_change.addActionListener(new ActionListener() { // ���� ��ư Ŭ���� �߻��ϴ� �̺�Ʈ -> ���õȾ����� �����
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedIndex() != -1) {
					int index = list.getSelectedIndex(); // ���õ� index ������
					int delayTime = 0; // delay�� �ʴ����� �ϱ�����
					listPosX.remove(index); // Ŭ���� �������� index�� �ִ� ���콺 X��ǥ�� �����Ѵ�.
					listPosY.remove(index); // Ŭ���� �������� index�� �ִ� ���콺 Y��ǥ�� �����Ѵ�.
					listDelay.remove(index); // Ŭ���� �������� index�� �ִ� delay�� �����Ѵ�.
					listText.remove(index); // Ŭ���� �������� index�� �ִ� ������ �����Ѵ�.
					m.remove(list.getSelectedIndex()); // ���õ� index ����
					delayTime = Integer.parseInt(formattedTextField_delay.getText()) * 1000;
					listPosX.add(index, M_pointer.getLocation().x); // ������ index�� �ڸ��� ���� ����� ���콺X��ǥ�� ����
					listPosY.add(index, M_pointer.getLocation().y); // ������ index�� �ڸ��� ���� ����� ���콺X��ǥ�� ����
					listDelay.add(index, delayTime); // ������ index�� �ڸ��� ����ǥ���� �����̷� ����
					listText.add(index, textField_editText.getText()); // ������ index�� �ڸ��� ����ǥ���� �������� ����

					m.add(index, "������: " + formattedTextField_delay.getText() + "����" + "  ���콺�̵��� �� Ŭ��   "
							+ textField_mouseLocate.getText() + "   ����: " + textField_editText.getText()); // �����ߴ�
																											// index��
																											// textField����
																											// ������ ������
																											// set��
																											// ���ص��ǳ�?
				}
			}
		});
		button_change.setFont(new Font("�������", Font.BOLD, 12));
		button_change.setBackground(Color.LIGHT_GRAY);
		contentPane.add(button_change);

		JButton button_clear = new JButton("��ü ����"); // ��ü ���� ��ư
		button_clear.setBounds(631, 251, 97, 23);
		button_clear.addActionListener(new ActionListener() { // ��ü���� ��ư Ŭ���� �߻��ϴ� �̺�Ʈ
			public void actionPerformed(ActionEvent e) {
				listPosX.clear();
				listPosY.clear();
				listDelay.clear();
				listText.clear();
				m.clear();
			}
		});
		button_clear.setFont(new Font("�������", Font.BOLD, 12));
		button_clear.setBackground(Color.LIGHT_GRAY);
		contentPane.add(button_clear);

		button_allStart = new JButton("ó������ ����"); // ��ũ�� ��ü ���� ��ư - > ������� �����Ǿ���
		button_allStart.setBackground(Color.LIGHT_GRAY);
		button_allStart.setForeground(Color.BLACK);
		button_allStart.setFont(new Font("�������", Font.BOLD, 10));
		button_allStart.setBounds(515, 394, 100, 35);
		contentPane.add(button_allStart);

		thehandler handler = new thehandler(); // ������ �ڵ鷯 ����
		button_allStart.addActionListener(handler); // �����忡 ��ü�����ư�� �Ѱ���

		button_partStart = new JButton("<html>������ ��ũ��<br>&nbsp���ķ� ����</html>"); // ��ũ�� ������ �κк��� ���� ��ư
		button_partStart.setBounds(657, 394, 100, 35);
		button_partStart.setFont(new Font("�������", Font.BOLD, 10));
		button_partStart.setBackground(Color.LIGHT_GRAY);
		contentPane.add(button_partStart);

		button_partStart.addActionListener(handler); // �����忡 �κн����ư�� �Ѱ���

		JButton button_save = new JButton("�����ϱ�"); // ��ũ�� ���� �����ϱ�
		button_save.setBackground(Color.LIGHT_GRAY);
		button_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFrame fileFrame = new JFrame();

					JFileChooser fileChooser = new JFileChooser();
					int result = fileChooser.showSaveDialog(fileFrame); // ��â���� ���� ����ȭ���� ����ش�

					String savePath = ""; // ������ ������ ��θ� �����ϰ� �����̸��� �ȴ�

					if (result == JFileChooser.APPROVE_OPTION) { // �����ưŬ���� ����

						File file = fileChooser.getSelectedFile(); // ������ ������ ���丮�� �����´�
						savePath = file.getAbsolutePath(); // ���丮 ����� ������ �̰ž� �ٷ��̰�!!! �����̸��� ����ɼ��ְ� ���شٰ�

						int saveListCount = listDelay.size(); // ����� ��ũ�θ� �����ϴ���, ��ũ���� ������ ����

						BufferedWriter saveMacroData = new BufferedWriter(new FileWriter(new File(savePath + ".text")));

						// ������, x��ǥ, y��ǥ, ������ ����

						saveMacroData.write(String.valueOf(saveListCount)); // ������ ��ũ�� ���� ����
						saveMacroData.newLine(); // ���� ����

						for (int i = 0; i < saveListCount; i++) { // ������ ����

							saveMacroData.write(String.valueOf(listDelay.get(i))); // ������ ����
							saveMacroData.newLine(); // ���� ����
						}

						for (int i = 0; i < saveListCount; i++) { // x��ǥ ����

							saveMacroData.write(String.valueOf(listPosX.get(i))); // x��ǥ ����
							saveMacroData.newLine(); // ���� ����
						}

						for (int i = 0; i < saveListCount; i++) { // y��ǥ ����

							saveMacroData.write(String.valueOf(listPosY.get(i))); // y��ǥ ����
							saveMacroData.newLine(); // ���� ����
						}

						for (int i = 0; i < saveListCount; i++) { // ����� ����

							saveMacroData.write(listText.get(i)); // ���� ����
							saveMacroData.newLine(); // ���� ����
						}

						saveMacroData.close(); // �ݾ��ֱ�
					} else {

					}

				} catch (IOException e1) {

					e1.printStackTrace();
				}
			}
		});
		button_save.setFont(new Font("�������", Font.BOLD, 12));
		button_save.setBounds(518, 495, 97, 23);
		contentPane.add(button_save);

		JButton button_load = new JButton("�ҷ�����"); // ��ũ�� ���� �ҷ�����
		button_load.setBackground(Color.LIGHT_GRAY);
		button_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					JFrame fileFrame = new JFrame();

					JFileChooser fileChooser = new JFileChooser();
					int result = fileChooser.showSaveDialog(fileFrame); // ��â���� ���� ����ȭ���� ����ش�

					String loadPath = ""; // ������ ������ ��θ� �����ϰ� �����̸��� �ȴ�

					if (result == JFileChooser.APPROVE_OPTION) { // �����ưŬ���� ����

						File file = fileChooser.getSelectedFile(); // ������ ������ ���丮�� �����´�
						loadPath = file.getAbsolutePath(); // ���丮 ����� ������ �̰ž� �ٷ��̰�!!! �����̸��� ����ɼ��ְ� ���شٰ�
						int loadListCount = 0; // ����� ��ũ�θ� �����ϴ���, ��ũ���� ������ �����Ѱ� �ҷ����� ����

						BufferedReader loadMacroData = new BufferedReader(new FileReader(new File(loadPath)));
						String bufferMacroDataValue = "";

						listDelay.clear(); // �ʱ�ȭ�� �ϰ� �ҷ��´�.
						listPosX.clear();
						listPosY.clear();
						listText.clear();
						m.clear();

						while (bufferMacroDataValue != null) { // ������, x��ǥ, y��ǥ��, ������ ������ �����͸� �ҷ��� �����Ѵ�.
							bufferMacroDataValue = loadMacroData.readLine(); // ���۰��� ����� �� ���� �ҷ�����

							if (bufferMacroDataValue != null) {
								loadListCount = Integer.parseInt(bufferMacroDataValue); // ����� ��ũ�θ� �����ϴ���, ��ũ���� ������ ����

								for (int i = 0; i < loadListCount; i++) {
									bufferMacroDataValue = loadMacroData.readLine(); // ���۰��� ����� �� ���� �ҷ�����
									listDelay.add(Integer.parseInt(bufferMacroDataValue)); // ������ ����Ʈ�� ���ۿ� �ִ� ������ �� �ֱ�
								}
								for (int i = 0; i < loadListCount; i++) {
									bufferMacroDataValue = loadMacroData.readLine(); // ���۰��� ����� �� ���� �ҷ�����
									listPosX.add(Integer.parseInt(bufferMacroDataValue)); // x��ǥ ����Ʈ�� ���ۿ� �ִ� x��ǥ�� �ֱ�
								}
								for (int i = 0; i < loadListCount; i++) {
									bufferMacroDataValue = loadMacroData.readLine(); // ���۰��� ����� �� ���� �ҷ�����
									listPosY.add(Integer.parseInt(bufferMacroDataValue)); // y��ǥ ����Ʈ�� ���ۿ� �ִ� y��ǥ�� �ֱ�
								}
								for (int i = 0; i < loadListCount; i++) {
									bufferMacroDataValue = loadMacroData.readLine(); // ���۰��� ����� �� ���� �ҷ�����
									listText.add(bufferMacroDataValue); // ���� ����Ʈ�� ���ۿ� �ִ� �ؽ�Ʈ �ֱ�
								}

							}

						}

						loadMacroData.close(); // �ݾ��ֱ�

						for (int i = 0; i < listDelay.size(); i++) { // ����Ʈ �ڽ��� ���� �־� ǥ�����ֱ�
							m.addElement("������: " + (listDelay.get(i)) / 1000 + "����" + "  ���콺�̵��� �� Ŭ��   " + "X��ǥ:"
									+ listPosX.get(i) + " , " + "Y��ǥ:" + listPosY.get(i) + "   ����: " + listText.get(i));

						}
					} else {

					}

				} catch (FileNotFoundException e1) {

					e1.printStackTrace();
				} catch (IOException e1) {

					e1.printStackTrace();
				}

			}

		});
		button_load.setFont(new Font("�������", Font.BOLD, 12));
		button_load.setBounds(657, 495, 97, 23);
		contentPane.add(button_load);

		textField_addInfo = new JTextField("�߰����� �Է¶�");
		textField_addInfo.setDisabledTextColor(Color.BLACK);
		textField_addInfo.setEnabled(false);
		textField_addInfo.setBounds(509, 119, 70, 21);
		textField_addInfo.setFont(new Font("�������", Font.BOLD, 10));
		textField_addInfo.setEditable(false);
		textField_addInfo.setColumns(10);
		textField_addInfo.setBorder(null);
		contentPane.add(textField_addInfo);

		// �ؽ�Ʈ �ʵ�� ����

		// �߰����� �ؽ�Ʈ �ʵ�

		textField_editText = new JTextField("����");
		textField_editText.setBounds(508, 142, 200, 21);
		textField_editText.setFont(new Font("�������", Font.BOLD, 10));
		contentPane.add(textField_editText);
		textField_editText.setColumns(10);

		// ���콺 ��ǥ ǥ�� ���� �ؽ�Ʈ �ʵ�

		textField_mouseInfo = new JTextField("ShiftŰ�� ������ ���콺 ��ǥ�� �����ɴϴ�");
		textField_mouseInfo.setDisabledTextColor(Color.BLACK);
		textField_mouseInfo.setEnabled(false);
		textField_mouseInfo.setBounds(508, 19, 195, 21);
		textField_mouseInfo.setBorder(null);
		textField_mouseInfo.setEditable(false);
		textField_mouseInfo.setFont(new Font("�������", Font.BOLD, 10));
		contentPane.add(textField_mouseInfo);
		textField_mouseInfo.setColumns(10);

		// ���콺 ��ǥ ǥ�� �ؽ�Ʈ �ʵ�

		textField_mouseLocate = new JTextField("X��ǥ:" + String.valueOf(M_pointer.getLocation().x) + " , " + "Y��ǥ:"
				+ String.valueOf(M_pointer.getLocation().y));
		textField_mouseLocate.setBounds(508, 50, 157, 32);
		contentPane.add(textField_mouseLocate);
		textField_mouseLocate.setColumns(10);

		// ������ ���� �ؽ�Ʈ �ʵ�
		textField_delayInfo = new JTextField("������ �ð�(��)");
		textField_delayInfo.setDisabledTextColor(Color.BLACK);
		textField_delayInfo.setEnabled(false);
		textField_delayInfo.setEditable(false);
		textField_delayInfo.setBounds(508, 93, 70, 21);
		textField_delayInfo.setFont(new Font("�������", Font.BOLD, 10));
		textField_delayInfo.setColumns(10);
		textField_delayInfo.setBorder(null);
		contentPane.add(textField_delayInfo);

		// ������ ǥ�� �ؽ�Ʈ �ʵ�
		formattedTextField_delay = new JFormattedTextField(new NumberFormatter());
		formattedTextField_delay.setText("0"); // �ʱⰪ
		formattedTextField_delay.setFont(new Font("�������", Font.BOLD, 12));
		formattedTextField_delay.setBounds(583, 92, 32, 23);
		contentPane.add(formattedTextField_delay);

		textField_startInfo = new JTextField("��ũ�δ� 2�� ����� �����մϴ�. ��Ŭ���� ����");
		textField_startInfo.setEditable(false);
		textField_startInfo.setFont(new Font("�������", Font.BOLD, 10));
		textField_startInfo.setEnabled(false);
		textField_startInfo.setDisabledTextColor(Color.BLACK);
		textField_startInfo.setColumns(10);
		textField_startInfo.setBorder(null);
		textField_startInfo.setBounds(508, 363, 200, 21);
		contentPane.add(textField_startInfo);

		textField_stateStart = new JTextField("\uB9E4\uD06C\uB85C \uC0C1\uD0DC : \uC2E4\uD589 \uB300\uAE30");
		textField_stateStart.setHorizontalAlignment(SwingConstants.LEFT);
		textField_stateStart.setFont(new Font("�������", Font.BOLD, 12));
		textField_stateStart.setEnabled(false);
		textField_stateStart.setEditable(false);
		textField_stateStart.setDisabledTextColor(Color.BLACK);
		textField_stateStart.setColumns(10);
		textField_stateStart.setBorder(null);
		textField_stateStart.setBounds(515, 449, 200, 25);
		contentPane.add(textField_stateStart);

		// ���� ���콺 ��ǥ �������⼭ ǥ�����ֱ�
		// addKeyListener�� ���� ������ ���� �������־���Ѵ�. �װ������� �����ϱ⶧��

		KeyListener key = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SHIFT) { // ShiftŰ�� ������ ���콺 ��ǥ���� �������� ����

					// ���콺 ��ǥ�� �������� . ���� : �ֱ������� �����;��� �ٸ����������� ���� �����ǹ��� -> ���������� �����ؼ� �ذ���
					M_pointer = MouseInfo.getPointerInfo();
					String strLocate = "X��ǥ:" + String.valueOf(M_pointer.getLocation().x) + " , " + "Y��ǥ:"
							+ String.valueOf(M_pointer.getLocation().y);
					textField_mouseLocate.setText(strLocate);

				}

			}
		};
		textField_editText.addKeyListener(key); // ���콺 ��ǥ �������°� ���� �����ϱ����ؼ�
		textField_mouseLocate.addKeyListener(key);
		formattedTextField_delay.addKeyListener(key);
		list.addKeyListener(key);
		button_newInsert.addKeyListener(key);
		button_partInsert.addKeyListener(key);
		button_delete.addKeyListener(key);
		button_change.addKeyListener(key);
		button_clear.addKeyListener(key);
		button_allStart.addKeyListener(key);
		button_partStart.addKeyListener(key);
		button_delayChange.addKeyListener(key);
		button_save.addKeyListener(key);
		button_load.addKeyListener(key);

	}

	private class thehandler implements ActionListener { // thread ó���� ����

		private Looper looper;

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == button_allStart) { // ������ �̺�Ʈ�� ��ü���� ��ư�϶�
				btn = 1;
				if (looper == null) {
					looper = new Looper();
					Thread t = new Thread(looper);
					textField_stateStart.setText("��ũ�� ���� : ó������ ���� ��");
					t.start();
				} else {
					looper.stop();
					looper = null;
					textField_stateStart.setText("��ũ�� ���� : ��������");
				}
			}

			if (e.getSource() == button_partStart) { // ������ �̺�Ʈ�� ������ ��ġ���� ���� ��ư�϶�
				btn = 2;
				if (looper == null) {

					looper = new Looper();
					Thread t = new Thread(looper);
					textField_stateStart.setText("��ũ�� ���� : ������ġ���� ���� ��");
					t.start();
				} else {
					looper.stop();
					looper = null;
					textField_stateStart.setText("��ũ�� ���� : �������� ");
				}
			}

		}

	}

	private class Looper implements Runnable {

		public Looper() {
			state = true;
		}

		public void stop() {
			state = false;
		}

		@Override
		public void run() {

			if (btn == 1) { // ��ü����
				robot.delay(2000); // 2���ĺ��� ����
				int start_x = 0;
				int start_y = 0;
				int end_x = 0;
				int end_y = 0;

				for (int i = 0; i < listPosX.size(); i++) { // size()�� ����Ʈ�� ũ�⸦ ���Ѵ�
					int delay = listDelay.get(i);
					if (delay == 30000) { // ������ ���� 30000�̸� �巡�� ���¿��� Ŭ������ 30�� ǥ���
						robot.delay(2000); // 2���ĺ��� ����
						start_x = listPosX.get(i);
						start_y = listPosY.get(i);

						robot.mouseMove(start_x, start_y); // ���콺 ��ǥ������ list���� ���� ������ ������
						robot.mousePress(InputEvent.BUTTON1_MASK); // ���콺 ���ʹ�ư�� �����ִ� ���·� �����
					} else if (delay == 50000) { // ������ ���� 50000�̸� �巡�� ���¿��� Ŭ������ 50�� ǥ���
						end_x = listPosX.get(i);
						end_y = listPosY.get(i);

						for (int t = 1; t < 100; t++) {
							int mov_x = start_x + ((end_x - start_x) * t) / 100;
							int mov_y = start_y + ((end_y - start_y) * t) / 100;
							robot.mouseMove(mov_x, mov_y);
							robot.delay(20);
						}

						robot.mouseRelease(InputEvent.BUTTON1_MASK); // ���콺 ���ʹ�ư�� �������� ���� ���·� �����.
					} else {
						robot.delay(delay); // list���� ����� delay����ŭ ����Ѵ�.
						robot.mouseMove(listPosX.get(i), listPosY.get(i)); // ���콺 ��ǥ������ list���� ���� ������ ������
						robot.mousePress(InputEvent.BUTTON1_MASK); // ���콺 ���ʹ�ư�� �����ִ� ���·� �����
						robot.mouseRelease(InputEvent.BUTTON1_MASK); // ���콺 ���ʹ�ư�� �������� ���� ���·� �����.
						if (state == false) { // �ѹ���Ŭ���� stop�޼ҵ� ȣ�⿡���� false�� �Ǿ� �ݺ��� �����
							break;
						}
					}
				}
			} else if (btn == 2) { // ��������ġ���� ����
				if (list.getSelectedIndex() != -1) {
					robot.delay(2000); // 2���ĺ��� ����
					int start_x = 0;
					int start_y = 0;
					int end_x = 0;
					int end_y = 0;
					for (int i = list.getSelectedIndex(); i < listPosX.size(); i++) { // size()�� ����Ʈ�� ũ�⸦ ���Ѵ�
						int delay = listDelay.get(i);
						if (delay == 30000) { // ������ ���� 30000�̸� �巡�� ���¿��� Ŭ������ 30�� ǥ���
							robot.delay(2000); // 2���ĺ��� ����
							start_x = listPosX.get(i);
							start_y = listPosY.get(i);

							robot.mouseMove(start_x, start_y); // ���콺 ��ǥ������ list���� ���� ������ ������
							robot.mousePress(InputEvent.BUTTON1_MASK); // ���콺 ���ʹ�ư�� �����ִ� ���·� �����
						} else if (delay == 50000) { // ������ ���� 50000�̸� �巡�� ���¿��� Ŭ������ 50�� ǥ���
							end_x = listPosX.get(i);
							end_y = listPosY.get(i);

							for (int t = 1; t < 100; t++) {
								int mov_x = start_x + ((end_x - start_x) * t) / 100;
								int mov_y = start_y + ((end_y - start_y) * t) / 100;
								robot.mouseMove(mov_x, mov_y);
								robot.delay(20);
							}

							robot.mouseRelease(InputEvent.BUTTON1_MASK); // ���콺 ���ʹ�ư�� �������� ���� ���·� �����.
						} else {
							robot.delay(delay); // list���� ����� delay����ŭ ����Ѵ�.
							robot.mouseMove(listPosX.get(i), listPosY.get(i)); // ���콺 ��ǥ������ list���� ���� ������ ������
							robot.mousePress(InputEvent.BUTTON1_MASK); // ���콺 ���ʹ�ư�� �����ִ� ���·� �����
							robot.mouseRelease(InputEvent.BUTTON1_MASK); // ���콺 ���ʹ�ư�� �������� ���� ���·� �����.
							if (state == false) { // �ѹ���Ŭ���� stop�޼ҵ� ȣ�⿡���� false�� �Ǿ� �ݺ��� �����
								break;
							}
						}

					}
				}
			}

		}

	}
}
