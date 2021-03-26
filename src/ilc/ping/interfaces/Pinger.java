package ilc.ping.interfaces;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@SuppressWarnings("SpellCheckingInspection")
public abstract class Pinger {

	public static void ping(JTable tableRadio, JTable tableTodos, JTabbedPane tabbedPane, String eco)
			throws IOException {

		int tab = tabbedPane.getSelectedIndex();
		String hostname;
		String ip_address;
		int numeroLinha;
		JTable table = null;

		if (tab == 0) {
			table = tableRadio;
		} else if (tab == 1) {
			table = tableTodos;
		}

		assert table != null;
		int[] lin = table.getSelectedRows();
		for (int i : lin) {
			hostname = (String) table.getValueAt(i, 0);
			ip_address = (String) table.getValueAt(i, 1);
			numeroLinha = i;
			String env_temp = System.getenv("TEMP");
			FileWriter bat = new FileWriter(env_temp + "/ivping/ping" + numeroLinha + ".bat");
			try (BufferedWriter bf = new BufferedWriter(bat)) {
				bf.write("@echo off");
				bf.newLine();
				bf.write("@cls");
				bf.newLine();
				bf.write("@color 17");
				bf.newLine();
				bf.write("@title Ping  " + hostname + "  [" + ip_address + "]");
				bf.newLine();
				bf.write(eco + ip_address);
				bf.newLine();
				bf.write("@pause");
			}
			String comando = env_temp + "/ivping/ping" + numeroLinha + ".bat";
			Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + comando);
		}
	}
}
