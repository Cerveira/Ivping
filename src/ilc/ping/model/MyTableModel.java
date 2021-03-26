package ilc.ping.model;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class MyTableModel extends AbstractTableModel {
	List<Data> devices;
	private final String[] columnNames = new String[] {
			"<html><b><font size=4><font color=#013571>Host Name</font></b></html>",
			"<html><b><font size=4><font color=#013571>IP Address</font></b></html>",
			"<html><b><font size=4><font color=#013571>Location</font></b></html>" };

	@SuppressWarnings("unchecked")
	public MyTableModel(String path) throws IOException {
		List<Data> devices = new ArrayList<>();
		boolean wasSuccessful = new File(System.getenv("TEMP") + "/ivping").mkdir();
		if (wasSuccessful) {
			System.out.println("was successful.");
		}

		// Recuperando o arquivo
		File excel = new File(path);
		FileInputStream file = new FileInputStream(excel);

		@SuppressWarnings("resource")
		Workbook workbook = new XSSFWorkbook(file);

		// Setando a aba
		Sheet sheet = workbook.getSheetAt(0);

		// Setando as linhas
		List<Row> rows = (List<Row>) toList(sheet.iterator());
		rows.remove(0);

		rows.forEach(row -> {
			// Setando as celulas
			List<Cell> cells = (List<Cell>) toList(row.cellIterator());
			Data dev = new Data();
			dev.setHostname(cells.get(0).getStringCellValue());
			dev.setIP(cells.get(1).getStringCellValue());
			dev.setLocation(cells.get(2).getStringCellValue());
			devices.add(dev);
		});
		this.devices = devices;
	}

	public List<?> toList(Iterator<?> iterator) {
		return IteratorUtils.toList(iterator);
	}

	@Override
	public int getRowCount() {
		return devices.size();
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case 0:
				return devices.get(rowIndex).getHostname();
			case 1:
				return devices.get(rowIndex).getIP();
			case 2:
				return devices.get(rowIndex).getLocation();
			default:
				return null;
		}
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}
}
