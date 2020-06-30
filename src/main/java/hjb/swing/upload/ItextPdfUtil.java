package hjb.swing.upload;

import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

public class ItextPdfUtil {

	public static void addPdfTextMark(String InPdfFile, String outPdfFile, String textMark, int textWidth,
			int textHeight, String password) throws Exception {
		byte[] ownerPassword = password.getBytes();
		PdfReader reader = new PdfReader(InPdfFile, ownerPassword);
		PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(new File(outPdfFile)));
		stamp.setEncryption(null, ownerPassword,
				PdfWriter.ALLOW_SCREENREADERS | PdfWriter.ALLOW_PRINTING | PdfWriter.ALLOW_DEGRADED_PRINTING, true);
		BaseFont font = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);

		int pageSize = reader.getNumberOfPages();// 原pdf文件的总页数
		PdfContentByte contentByte = null;
		float pageWidth = 0;
		float pageHeight = 0;
		String[] markArr = textMark.split("\\|\\|");
		PdfGState gs = new PdfGState();
		gs.setFillOpacity(0.4f);
		for (int i = 1; i <= pageSize; i++) {
			contentByte = stamp.getOverContent(i);// 水印在之前文本上
			contentByte.setGState(gs);
			pageWidth = reader.getPageSize(i).getWidth();// 获取pdf文档页面宽度
			pageHeight = reader.getPageSize(i).getHeight();// 获取pdf文档页面高度
			contentByte.beginText();
			contentByte.setColorFill(BaseColor.GRAY);// 文字水印 颜色
			contentByte.setFontAndSize(font, 40);// 文字水印 字体及字号
			contentByte.setTextMatrix(textWidth, textHeight);// 文字水印 起始位置
			for (int j = 0; j < markArr.length; j++) {
				contentByte.showTextAligned(Element.ALIGN_CENTER, markArr[j], pageWidth * 0.6f - j * 10,
						pageHeight * 0.9f - j * 65, 45);
				contentByte.showTextAligned(Element.ALIGN_CENTER, markArr[j], pageWidth * 0.5f - j * 10,
						pageHeight * 0.4f - j * 65, 45);
			}
			contentByte.endText();
		}
		stamp.close();// 关闭
		reader.close();
	}

}