package hjb.swing.upload;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.*;

/**
 * <pre>
 *   NOTE:1.该工具只会对PDF做处理，并且保存着同一级目录 
 *   	 2. 水印较长，请使用||分割多行水印
 * </pre>
 * 
 * 
 * @author tiebin
 */
public class WatermarkAndPasswordForPdf extends JFrame implements ActionListener {
	private static final long	serialVersionUID	= 3070392568212794922L;
	private JButton				open				= new JButton("pdf加密加水印");
	private JTextField			waterMask			= new JTextField(20);
	private JTextField			password			= new JTextField(20);
	private Preferences			pref				= Preferences.userRoot().node(this.getClass().getName());

	public static void main(String[] args) {
		new WatermarkAndPasswordForPdf();
	}

	public WatermarkAndPasswordForPdf() {
		super("pdf水印加密处理工具");
		// 内容面板
		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		// 添加控件
		contentPane.add(new JLabel("NOTE:1.该工具只会对PDF做处理，并且保存着同一级目录 2. 水印较长，请使用||分割多行水印"));
		contentPane.add(new JLabel("水印说明:"));
		contentPane.add(waterMask);
		contentPane.add(new JLabel("pdf  密码:"));
		contentPane.add(password);
		contentPane.add(open);
		this.setBounds(400, 400, 600, 150);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		open.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		String textMark = waterMask.getText();
		if (textMark == null || textMark.trim().equals("")) {
			JOptionPane.showMessageDialog(this, "转换之前请输入水印");
			return;
		}
		String passwd = password.getText();
		if (passwd == null || passwd.trim().equals("")) {
			JOptionPane.showMessageDialog(this, "请输入密码");
			return;
		}

		String lastPath = pref.get("lastPath", null);
		JFileChooser jfc = new JFileChooser(lastPath);
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jfc.setSize(400, 500);
		jfc.showDialog(new JLabel(), "选择文件夹或者文件");
		File file = jfc.getSelectedFile();
		if (file != null) {
			if (file.isDirectory()) {
				for (File subFile : file.listFiles()) {
					encryptPDFFile(textMark, subFile, passwd);
				}
			} else if (file.isFile()) {
				encryptPDFFile(textMark, file, passwd);
			}
		}

		pref.put("lastPath", jfc.getCurrentDirectory().getPath());

		// 对话框xcd
		JOptionPane.showMessageDialog(null, "pdf处理成功");
	}

	private void encryptPDFFile(String textMark, File subFile, String password) {
		String fileName = subFile.getAbsolutePath();
		if (fileName.endsWith(".pdf")) {
			int pdfSuff = fileName.lastIndexOf(".pdf");
			try {
				ItextPdfUtil.addPdfTextMark(subFile.getAbsolutePath(),
						fileName.substring(0, pdfSuff) + "-" + System.currentTimeMillis() + "-加密.pdf", textMark.trim(),
						300, 200, password.trim());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
