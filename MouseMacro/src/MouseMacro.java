

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
	PointerInfo M_pointer = MouseInfo.getPointerInfo(); // 초기 실행시 마우스 좌표가져옴 //마우스 좌표를 얻기위한 포인터
	private JTextField textField_addInfo;
	private JTextField textField_delayInfo;
	JFormattedTextField formattedTextField_delay;
	private JTextField textField_startInfo;
	boolean state; // 매크로 동작상태 true이면 동작중 false면 멈춤
	private JButton button_allStart; // 매크로 전체실행
	private JButton button_partStart;// 매크로 선택한 위치부터 실행

	int btn; // 매크로 어떤걸 실행할지 버튼의 종류를 스레드에 넘겨주기위해 1.전체 2.부분
	Robot robot = new Robot(); // Robot class를 이용한 마우스 컨트롤 위한 선언

	// 리스트박스에 넣을 데이터

	/*
	 * String[] listText = {}; //데이터공간이 동적인지 확인해야함 for(int i=0; i<listText.length;
	 * i++) { m.add(i, listText[i]); }
	 */

	DefaultListModel<String> m = new DefaultListModel<String>();
	JList<String> list = new JList<String>(m);
	// delay값을 가지고 있을 데이터

	ArrayList<Integer> listDelay = new ArrayList<>();

	// 마우스 좌표를 가지고있을 데이터

	ArrayList<Integer> listPosX = new ArrayList<>(); // 마우스 X좌표
	ArrayList<Integer> listPosY = new ArrayList<>(); // 마우스 Y좌표
	ArrayList<String> listText = new ArrayList<>(); // 설명란에 들어간 텍스트들

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
		setTitle("좌표매크로 ver0.2");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 640);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// 리스트박스 스크롤되는 패널 추가

		list.setFont(new Font("나눔고딕", Font.BOLD, 12));

		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBounds(20, 20, 476, 500);
		contentPane.add(scrollPane);

		// 자바 버튼 종류들모음

		JButton button_delayChange = new JButton("선택좌표 딜레이만 변경"); // 매크로 딜레이 변경 버튼
		button_delayChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedIndex() != -1) {
					int index = list.getSelectedIndex(); // 선택된 index 가져옴
					int delayTime = 0;
					listDelay.remove(index); // 클릭된 아이템의 index에 있는 delay를 삭제한다.
					m.remove(list.getSelectedIndex()); // 선택된 index 제거
					delayTime = Integer.parseInt(formattedTextField_delay.getText()) * 1000;
					listDelay.add(index, delayTime); // 삭제한 index의 자리에 현재표시한 딜레이로 변경
					m.add(index, "딜레이: " + formattedTextField_delay.getText() + "초후" + "  마우스이동한 뒤 클릭   "
							+ textField_mouseLocate.getText() + "   설명: " + textField_editText.getText()); // 선택했던
																											// index로
																											// textField에서
																											// 아이템 가져옴
																											// set은
																											// 안해도되나?
				}
			}
		});
		button_delayChange.setFont(new Font("나눔고딕", Font.BOLD, 10));
		button_delayChange.setBackground(Color.LIGHT_GRAY);
		button_delayChange.setBounds(627, 92, 130, 23);
		contentPane.add(button_delayChange);

		JButton button_newInsert = new JButton("새로추가"); // 새로추가 버튼
		button_newInsert.setBounds(508, 188, 71, 23);
		button_newInsert.addActionListener(new ActionListener() { // 추가 버튼 클릭시 발생하는 이벤트 -> 맨뒤로 추가됨
			public void actionPerformed(ActionEvent e) {
				int delayTime = 0; // delay를 초단위로 하기위해

				m.addElement("딜레이: " + formattedTextField_delay.getText() + "초후" + "  마우스이동한 뒤 클릭   "
						+ textField_mouseLocate.getText() + "   설명: " + textField_editText.getText()); // textField에서
																										// 아이템 가져와서 마지막에
																										// 추가
				listPosX.add(M_pointer.getLocation().x); // x좌표 추가
				listPosY.add(M_pointer.getLocation().y); // y좌표 추가

				delayTime = Integer.parseInt(formattedTextField_delay.getText()) * 1000;
				listDelay.add(delayTime); // delay값 추가
				listText.add(textField_editText.getText()); // 리스트에 설명 추가

			}
		});
		button_newInsert.setFont(new Font("나눔고딕", Font.BOLD, 10));
		button_newInsert.setBackground(Color.LIGHT_GRAY);
		contentPane.add(button_newInsert);

		JButton button_partInsert = new JButton("선택한 위치추가"); // 선택한 위치추가 버튼
		button_partInsert.setBounds(628, 188, 100, 23);
		button_partInsert.addActionListener(new ActionListener() { // 추가 버튼 클릭시 발생하는 이벤트 -> 선택된아이템 앞으로 추가됨
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedIndex() != -1) {
					int index = list.getSelectedIndex(); // 선택된 index 가져옴
					int delayTime = 0; // delay를 초단위로 하기위해
					m.add(index, "딜레이: " + formattedTextField_delay.getText() + "초후" + "  마우스이동한 뒤 클릭   "
							+ textField_mouseLocate.getText() + "   설명: " + textField_editText.getText()); // textField에서
																											// 아이템 가져와서
																											// 마지막에 추가
					listPosX.add(index, M_pointer.getLocation().x); // x좌표 추가
					listPosY.add(index, M_pointer.getLocation().y); // y좌표 추가

					delayTime = Integer.parseInt(formattedTextField_delay.getText()) * 1000;
					listDelay.add(index, delayTime); // delay값 추가
					listText.add(index, textField_editText.getText()); // 리스트에 설명 추가
				}
			}
		});
		button_partInsert.setFont(new Font("나눔고딕", Font.BOLD, 10));
		button_partInsert.setBackground(Color.LIGHT_GRAY);
		contentPane.add(button_partInsert);

		JButton button_delete = new JButton("선택 삭제"); // 삭제 버튼
		button_delete.setBounds(508, 251, 85, 23);
		button_delete.addActionListener(new ActionListener() { // 삭제 버튼 클릭시 발생하는 이벤트 -> 선택된아이템 삭제됨
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedIndex() != -1) { // list.getSelectedIndex() <- 선택된 리스트 값 가져옴
					int index = list.getSelectedIndex();
					listPosX.remove(index); // 클릭된 아이템의 index에 있는 마우스 X좌표를 삭제한다.
					listPosY.remove(index); // 클릭된 아이템의 index에 있는 마우스 Y좌표를 삭제한다.
					listDelay.remove(index); // 클릭된 아이템의 index에 있는 delay를 삭제한다.
					listText.remove(index); // 클릭된 아이템의 index에 있는 설명을 삭제한다.
					m.remove(list.getSelectedIndex()); // 클릭된 아이템의 index를 가져와서 list에서 삭제한다.

				}
			}
		});

		button_delete.setFont(new Font("나눔고딕", Font.BOLD, 12));
		button_delete.setBackground(Color.LIGHT_GRAY);
		contentPane.add(button_delete);

		JButton button_change = new JButton("선택 변경"); // 변경 버튼
		button_change.setBounds(508, 310, 85, 23);
		button_change.addActionListener(new ActionListener() { // 변경 버튼 클릭시 발생하는 이벤트 -> 선택된아이템 변경됨
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedIndex() != -1) {
					int index = list.getSelectedIndex(); // 선택된 index 가져옴
					int delayTime = 0; // delay를 초단위로 하기위해
					listPosX.remove(index); // 클릭된 아이템의 index에 있는 마우스 X좌표를 삭제한다.
					listPosY.remove(index); // 클릭된 아이템의 index에 있는 마우스 Y좌표를 삭제한다.
					listDelay.remove(index); // 클릭된 아이템의 index에 있는 delay를 삭제한다.
					listText.remove(index); // 클릭된 아이템의 index에 있는 설명을 삭제한다.
					m.remove(list.getSelectedIndex()); // 선택된 index 제거
					delayTime = Integer.parseInt(formattedTextField_delay.getText()) * 1000;
					listPosX.add(index, M_pointer.getLocation().x); // 삭제한 index의 자리에 현재 저장된 마우스X좌표로 변경
					listPosY.add(index, M_pointer.getLocation().y); // 삭제한 index의 자리에 현재 저장된 마우스X좌표로 변경
					listDelay.add(index, delayTime); // 삭제한 index의 자리에 현재표시한 딜레이로 변경
					listText.add(index, textField_editText.getText()); // 삭제한 index의 자리에 현재표시한 설명으로 변경

					m.add(index, "딜레이: " + formattedTextField_delay.getText() + "초후" + "  마우스이동한 뒤 클릭   "
							+ textField_mouseLocate.getText() + "   설명: " + textField_editText.getText()); // 선택했던
																											// index로
																											// textField에서
																											// 아이템 가져옴
																											// set은
																											// 안해도되나?
				}
			}
		});
		button_change.setFont(new Font("나눔고딕", Font.BOLD, 12));
		button_change.setBackground(Color.LIGHT_GRAY);
		contentPane.add(button_change);

		JButton button_clear = new JButton("전체 삭제"); // 전체 삭제 버튼
		button_clear.setBounds(631, 251, 97, 23);
		button_clear.addActionListener(new ActionListener() { // 전체삭제 버튼 클릭시 발생하는 이벤트
			public void actionPerformed(ActionEvent e) {
				listPosX.clear();
				listPosY.clear();
				listDelay.clear();
				listText.clear();
				m.clear();
			}
		});
		button_clear.setFont(new Font("나눔고딕", Font.BOLD, 12));
		button_clear.setBackground(Color.LIGHT_GRAY);
		contentPane.add(button_clear);

		button_allStart = new JButton("처음부터 실행"); // 매크로 전체 실행 버튼 - > 스레드로 구현되었음
		button_allStart.setBackground(Color.LIGHT_GRAY);
		button_allStart.setForeground(Color.BLACK);
		button_allStart.setFont(new Font("나눔고딕", Font.BOLD, 10));
		button_allStart.setBounds(515, 394, 100, 35);
		contentPane.add(button_allStart);

		thehandler handler = new thehandler(); // 스레드 핸들러 선언
		button_allStart.addActionListener(handler); // 스레드에 전체실행버튼을 넘겨줌

		button_partStart = new JButton("<html>선택한 매크로<br>&nbsp이후로 실행</html>"); // 매크로 선택한 부분부터 실행 버튼
		button_partStart.setBounds(657, 394, 100, 35);
		button_partStart.setFont(new Font("나눔고딕", Font.BOLD, 10));
		button_partStart.setBackground(Color.LIGHT_GRAY);
		contentPane.add(button_partStart);

		button_partStart.addActionListener(handler); // 스레드에 부분실행버튼을 넘겨줌

		JButton button_save = new JButton("저장하기"); // 매크로 파일 저장하기
		button_save.setBackground(Color.LIGHT_GRAY);
		button_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFrame fileFrame = new JFrame();

					JFileChooser fileChooser = new JFileChooser();
					int result = fileChooser.showSaveDialog(fileFrame); // 새창에서 파일 저장화면을 띄워준다

					String savePath = ""; // 저장한 파일의 경로를 저장하고 파일이름이 된다

					if (result == JFileChooser.APPROVE_OPTION) { // 저장버튼클릭시 동작

						File file = fileChooser.getSelectedFile(); // 선택한 파일의 디렉토리를 가져온다
						savePath = file.getAbsolutePath(); // 디렉토리 결과를 보여줌 이거야 바로이게!!! 파일이름이 변경될수있게 해준다고

						int saveListCount = listDelay.size(); // 몇번의 매크로를 실행하는지, 매크로의 개수를 저장

						BufferedWriter saveMacroData = new BufferedWriter(new FileWriter(new File(savePath + ".text")));

						// 딜레이, x좌표, y좌표, 설명을 저장

						saveMacroData.write(String.valueOf(saveListCount)); // 실행할 매크로 개수 저장
						saveMacroData.newLine(); // 한줄 띄우기

						for (int i = 0; i < saveListCount; i++) { // 딜레이 저장

							saveMacroData.write(String.valueOf(listDelay.get(i))); // 딜레이 저장
							saveMacroData.newLine(); // 한줄 띄우기
						}

						for (int i = 0; i < saveListCount; i++) { // x좌표 저장

							saveMacroData.write(String.valueOf(listPosX.get(i))); // x좌표 저장
							saveMacroData.newLine(); // 한줄 띄우기
						}

						for (int i = 0; i < saveListCount; i++) { // y좌표 저장

							saveMacroData.write(String.valueOf(listPosY.get(i))); // y좌표 저장
							saveMacroData.newLine(); // 한줄 띄우기
						}

						for (int i = 0; i < saveListCount; i++) { // 설명들 저장

							saveMacroData.write(listText.get(i)); // 설명 저장
							saveMacroData.newLine(); // 한줄 띄우기
						}

						saveMacroData.close(); // 닫아주기
					} else {

					}

				} catch (IOException e1) {

					e1.printStackTrace();
				}
			}
		});
		button_save.setFont(new Font("나눔고딕", Font.BOLD, 12));
		button_save.setBounds(518, 495, 97, 23);
		contentPane.add(button_save);

		JButton button_load = new JButton("불러오기"); // 매크로 파일 불러오기
		button_load.setBackground(Color.LIGHT_GRAY);
		button_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					JFrame fileFrame = new JFrame();

					JFileChooser fileChooser = new JFileChooser();
					int result = fileChooser.showSaveDialog(fileFrame); // 새창에서 파일 저장화면을 띄워준다

					String loadPath = ""; // 저장한 파일의 경로를 저장하고 파일이름이 된다

					if (result == JFileChooser.APPROVE_OPTION) { // 열기버튼클릭시 동작

						File file = fileChooser.getSelectedFile(); // 선택한 파일의 디렉토리를 가져온다
						loadPath = file.getAbsolutePath(); // 디렉토리 결과를 보여줌 이거야 바로이게!!! 파일이름이 변경될수있게 해준다고
						int loadListCount = 0; // 몇번의 매크로를 실행하는지, 매크로의 개수를 저장한거 불러오기 위함

						BufferedReader loadMacroData = new BufferedReader(new FileReader(new File(loadPath)));
						String bufferMacroDataValue = "";

						listDelay.clear(); // 초기화를 하고 불러온다.
						listPosX.clear();
						listPosY.clear();
						listText.clear();
						m.clear();

						while (bufferMacroDataValue != null) { // 딜레이, x좌표, y좌표값, 설명의 순으로 데이터를 불러와 저장한다.
							bufferMacroDataValue = loadMacroData.readLine(); // 버퍼값에 저장된 값 한줄 불러오기

							if (bufferMacroDataValue != null) {
								loadListCount = Integer.parseInt(bufferMacroDataValue); // 몇번의 매크로를 실행하는지, 매크로의 개수를 저장

								for (int i = 0; i < loadListCount; i++) {
									bufferMacroDataValue = loadMacroData.readLine(); // 버퍼값에 저장된 값 한줄 불러오기
									listDelay.add(Integer.parseInt(bufferMacroDataValue)); // 딜레이 리스트에 버퍼에 있는 딜레이 값 넣기
								}
								for (int i = 0; i < loadListCount; i++) {
									bufferMacroDataValue = loadMacroData.readLine(); // 버퍼값에 저장된 값 한줄 불러오기
									listPosX.add(Integer.parseInt(bufferMacroDataValue)); // x좌표 리스트에 버퍼에 있는 x좌표값 넣기
								}
								for (int i = 0; i < loadListCount; i++) {
									bufferMacroDataValue = loadMacroData.readLine(); // 버퍼값에 저장된 값 한줄 불러오기
									listPosY.add(Integer.parseInt(bufferMacroDataValue)); // y좌표 리스트에 버퍼에 있는 y좌표값 넣기
								}
								for (int i = 0; i < loadListCount; i++) {
									bufferMacroDataValue = loadMacroData.readLine(); // 버퍼값에 저장된 값 한줄 불러오기
									listText.add(bufferMacroDataValue); // 설명 리스트에 버퍼에 있는 텍스트 넣기
								}

							}

						}

						loadMacroData.close(); // 닫아주기

						for (int i = 0; i < listDelay.size(); i++) { // 리스트 박스에 값을 넣어 표시해주기
							m.addElement("딜레이: " + (listDelay.get(i)) / 1000 + "초후" + "  마우스이동한 뒤 클릭   " + "X좌표:"
									+ listPosX.get(i) + " , " + "Y좌표:" + listPosY.get(i) + "   설명: " + listText.get(i));

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
		button_load.setFont(new Font("나눔고딕", Font.BOLD, 12));
		button_load.setBounds(657, 495, 97, 23);
		contentPane.add(button_load);

		textField_addInfo = new JTextField("추가설명 입력란");
		textField_addInfo.setDisabledTextColor(Color.BLACK);
		textField_addInfo.setEnabled(false);
		textField_addInfo.setBounds(509, 119, 70, 21);
		textField_addInfo.setFont(new Font("나눔고딕", Font.BOLD, 10));
		textField_addInfo.setEditable(false);
		textField_addInfo.setColumns(10);
		textField_addInfo.setBorder(null);
		contentPane.add(textField_addInfo);

		// 텍스트 필드들 모음

		// 추가설명 텍스트 필드

		textField_editText = new JTextField("없음");
		textField_editText.setBounds(508, 142, 200, 21);
		textField_editText.setFont(new Font("나눔고딕", Font.BOLD, 10));
		contentPane.add(textField_editText);
		textField_editText.setColumns(10);

		// 마우스 좌표 표시 도움말 텍스트 필드

		textField_mouseInfo = new JTextField("Shift키를 누를시 마우스 좌표를 가져옵니다");
		textField_mouseInfo.setDisabledTextColor(Color.BLACK);
		textField_mouseInfo.setEnabled(false);
		textField_mouseInfo.setBounds(508, 19, 195, 21);
		textField_mouseInfo.setBorder(null);
		textField_mouseInfo.setEditable(false);
		textField_mouseInfo.setFont(new Font("나눔고딕", Font.BOLD, 10));
		contentPane.add(textField_mouseInfo);
		textField_mouseInfo.setColumns(10);

		// 마우스 좌표 표시 텍스트 필드

		textField_mouseLocate = new JTextField("X좌표:" + String.valueOf(M_pointer.getLocation().x) + " , " + "Y좌표:"
				+ String.valueOf(M_pointer.getLocation().y));
		textField_mouseLocate.setBounds(508, 50, 157, 32);
		contentPane.add(textField_mouseLocate);
		textField_mouseLocate.setColumns(10);

		// 딜레이 설명 텍스트 필드
		textField_delayInfo = new JTextField("딜레이 시간(초)");
		textField_delayInfo.setDisabledTextColor(Color.BLACK);
		textField_delayInfo.setEnabled(false);
		textField_delayInfo.setEditable(false);
		textField_delayInfo.setBounds(508, 93, 70, 21);
		textField_delayInfo.setFont(new Font("나눔고딕", Font.BOLD, 10));
		textField_delayInfo.setColumns(10);
		textField_delayInfo.setBorder(null);
		contentPane.add(textField_delayInfo);

		// 딜레이 표시 텍스트 필드
		formattedTextField_delay = new JFormattedTextField(new NumberFormatter());
		formattedTextField_delay.setText("0"); // 초기값
		formattedTextField_delay.setFont(new Font("나눔고딕", Font.BOLD, 12));
		formattedTextField_delay.setBounds(583, 92, 32, 23);
		contentPane.add(formattedTextField_delay);

		textField_startInfo = new JTextField("매크로는 2초 대기후 동작합니다. 재클릭시 정지");
		textField_startInfo.setEditable(false);
		textField_startInfo.setFont(new Font("나눔고딕", Font.BOLD, 10));
		textField_startInfo.setEnabled(false);
		textField_startInfo.setDisabledTextColor(Color.BLACK);
		textField_startInfo.setColumns(10);
		textField_startInfo.setBorder(null);
		textField_startInfo.setBounds(508, 363, 200, 21);
		contentPane.add(textField_startInfo);

		textField_stateStart = new JTextField("\uB9E4\uD06C\uB85C \uC0C1\uD0DC : \uC2E4\uD589 \uB300\uAE30");
		textField_stateStart.setHorizontalAlignment(SwingConstants.LEFT);
		textField_stateStart.setFont(new Font("나눔고딕", Font.BOLD, 12));
		textField_stateStart.setEnabled(false);
		textField_stateStart.setEditable(false);
		textField_stateStart.setDisabledTextColor(Color.BLACK);
		textField_stateStart.setColumns(10);
		textField_stateStart.setBorder(null);
		textField_stateStart.setBounds(515, 449, 200, 25);
		contentPane.add(textField_stateStart);

		// 현재 마우스 좌표 가져오기서 표시해주기
		// addKeyListener을 통해 범위를 직접 지정해주어야한다. 그곳에서만 동작하기때문

		KeyListener key = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SHIFT) { // Shift키를 누를시 마우스 좌표값을 가져오는 동작

					// 마우스 좌표값 가져오기 . 문제 : 주기적으로 가져와야함 다른곳에있으면 값이 고정되버림 -> 전역변수로 선언해서 해결함
					M_pointer = MouseInfo.getPointerInfo();
					String strLocate = "X좌표:" + String.valueOf(M_pointer.getLocation().x) + " , " + "Y좌표:"
							+ String.valueOf(M_pointer.getLocation().y);
					textField_mouseLocate.setText(strLocate);

				}

			}
		};
		textField_editText.addKeyListener(key); // 마우스 좌표 가져오는걸 전부 적용하기위해서
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

	private class thehandler implements ActionListener { // thread 처리를 위함

		private Looper looper;

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == button_allStart) { // 가져온 이벤트가 전체실행 버튼일때
				btn = 1;
				if (looper == null) {
					looper = new Looper();
					Thread t = new Thread(looper);
					textField_stateStart.setText("매크로 상태 : 처음부터 실행 중");
					t.start();
				} else {
					looper.stop();
					looper = null;
					textField_stateStart.setText("매크로 상태 : 실행정지");
				}
			}

			if (e.getSource() == button_partStart) { // 가져온 이벤트가 선택한 위치부터 실행 버튼일때
				btn = 2;
				if (looper == null) {

					looper = new Looper();
					Thread t = new Thread(looper);
					textField_stateStart.setText("매크로 상태 : 선택위치부터 실행 중");
					t.start();
				} else {
					looper.stop();
					looper = null;
					textField_stateStart.setText("매크로 상태 : 실행정지 ");
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

			if (btn == 1) { // 전체실행
				robot.delay(2000); // 2초후부터 실행
				int start_x = 0;
				int start_y = 0;
				int end_x = 0;
				int end_y = 0;

				for (int i = 0; i < listPosX.size(); i++) { // size()는 리스트의 크기를 구한다
					int delay = listDelay.get(i);
					if (delay == 30000) { // 딜레이 값이 30000이면 드래그 상태에서 클릭상태 30초 표기상
						robot.delay(2000); // 2초후부터 실행
						start_x = listPosX.get(i);
						start_y = listPosY.get(i);

						robot.mouseMove(start_x, start_y); // 마우스 좌표를가진 list에서 값을 가져와 움직임
						robot.mousePress(InputEvent.BUTTON1_MASK); // 마우스 왼쪽버튼이 눌려있는 상태로 만든다
					} else if (delay == 50000) { // 딜레이 값이 50000이면 드래그 상태에서 클릭해제 50초 표기상
						end_x = listPosX.get(i);
						end_y = listPosY.get(i);

						for (int t = 1; t < 100; t++) {
							int mov_x = start_x + ((end_x - start_x) * t) / 100;
							int mov_y = start_y + ((end_y - start_y) * t) / 100;
							robot.mouseMove(mov_x, mov_y);
							robot.delay(20);
						}

						robot.mouseRelease(InputEvent.BUTTON1_MASK); // 마우스 왼쪽버튼이 눌려있지 않은 상태로 만든다.
					} else {
						robot.delay(delay); // list에서 저장된 delay값만큼 대기한다.
						robot.mouseMove(listPosX.get(i), listPosY.get(i)); // 마우스 좌표를가진 list에서 값을 가져와 움직임
						robot.mousePress(InputEvent.BUTTON1_MASK); // 마우스 왼쪽버튼이 눌려있는 상태로 만든다
						robot.mouseRelease(InputEvent.BUTTON1_MASK); // 마우스 왼쪽버튼이 눌려있지 않은 상태로 만든다.
						if (state == false) { // 한번더클릭시 stop메소드 호출에의해 false가 되어 반복이 종료됨
							break;
						}
					}
				}
			} else if (btn == 2) { // 선택한위치부터 실행
				if (list.getSelectedIndex() != -1) {
					robot.delay(2000); // 2초후부터 실행
					int start_x = 0;
					int start_y = 0;
					int end_x = 0;
					int end_y = 0;
					for (int i = list.getSelectedIndex(); i < listPosX.size(); i++) { // size()는 리스트의 크기를 구한다
						int delay = listDelay.get(i);
						if (delay == 30000) { // 딜레이 값이 30000이면 드래그 상태에서 클릭상태 30초 표기상
							robot.delay(2000); // 2초후부터 실행
							start_x = listPosX.get(i);
							start_y = listPosY.get(i);

							robot.mouseMove(start_x, start_y); // 마우스 좌표를가진 list에서 값을 가져와 움직임
							robot.mousePress(InputEvent.BUTTON1_MASK); // 마우스 왼쪽버튼이 눌려있는 상태로 만든다
						} else if (delay == 50000) { // 딜레이 값이 50000이면 드래그 상태에서 클릭해제 50초 표기상
							end_x = listPosX.get(i);
							end_y = listPosY.get(i);

							for (int t = 1; t < 100; t++) {
								int mov_x = start_x + ((end_x - start_x) * t) / 100;
								int mov_y = start_y + ((end_y - start_y) * t) / 100;
								robot.mouseMove(mov_x, mov_y);
								robot.delay(20);
							}

							robot.mouseRelease(InputEvent.BUTTON1_MASK); // 마우스 왼쪽버튼이 눌려있지 않은 상태로 만든다.
						} else {
							robot.delay(delay); // list에서 저장된 delay값만큼 대기한다.
							robot.mouseMove(listPosX.get(i), listPosY.get(i)); // 마우스 좌표를가진 list에서 값을 가져와 움직임
							robot.mousePress(InputEvent.BUTTON1_MASK); // 마우스 왼쪽버튼이 눌려있는 상태로 만든다
							robot.mouseRelease(InputEvent.BUTTON1_MASK); // 마우스 왼쪽버튼이 눌려있지 않은 상태로 만든다.
							if (state == false) { // 한번더클릭시 stop메소드 호출에의해 false가 되어 반복이 종료됨
								break;
							}
						}

					}
				}
			}

		}

	}
}
