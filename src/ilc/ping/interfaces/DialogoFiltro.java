package ilc.ping.interfaces;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

@SuppressWarnings("SpellCheckingInspection")
public class DialogoFiltro extends JDialog {

	private final JTextField tfCima;
	private final JTextField tfBaixo;
	private String tf1 = "";
	private String tf2 = "";

	public String getTf1() {
		return tf1;
	}

	public String getTf2() {
		return tf2;
	}

	public DialogoFiltro(Frame owner) {
		super(owner, true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("Mostrar linhas que:");
		setBounds(100, 100, 380, 204);
		BorderLayout borderLayout = new BorderLayout();
		getContentPane().setLayout(borderLayout);
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(12, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JLabel lbl1 = new JLabel("Cont\u00E9m:");
		lbl1.setHorizontalAlignment(SwingConstants.RIGHT);

		tfCima = new JTextField();
		tfCima.setColumns(10);

		JLabel lbl_or = new JLabel("ou");
		lbl_or.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lbl2 = new JLabel("Cont\u00E9m:");
		lbl2.setHorizontalAlignment(SwingConstants.RIGHT);

		tfBaixo = new JTextField();
		tfBaixo.setColumns(10);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup().addContainerGap().addGroup(gl_contentPanel
						.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
								.addComponent(lbl_or, GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE).addGap(301))
						.addGroup(gl_contentPanel.createSequentialGroup()
								.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
										.addComponent(lbl2, GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
										.addComponent(lbl1, GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
										.addComponent(tfCima)
										.addComponent(tfBaixo, GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE))
								.addContainerGap(23, Short.MAX_VALUE)))));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPanel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(tfCima, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lbl1))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(lbl_or, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE).addComponent(lbl2).addComponent(
						tfBaixo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(48)));
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new EmptyBorder(0, 0, 0, 15));
			FlowLayout fl_buttonPane = new FlowLayout(FlowLayout.CENTER);
			fl_buttonPane.setVgap(14);
			buttonPane.setLayout(fl_buttonPane);
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(e -> {
					tf1 = tfCima.getText();
					tf2 = tfBaixo.getText();
					if (!tf1.isEmpty() && !tf2.isEmpty()) {
						setVisible(false);
						dispose();
					}
					tfCima.requestFocus();
				});
				okButton.setPreferredSize(new Dimension(78, 28));
				okButton.setMnemonic('O');
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton btnLimpar = new JButton("Limpar");
				btnLimpar.addActionListener(e -> {
					tfCima.setText("");
					tfBaixo.setText("");
					tfCima.requestFocus();
				});
				btnLimpar.setPreferredSize(new Dimension(78, 28));
				btnLimpar.setMnemonic('L');
				buttonPane.add(btnLimpar);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.addActionListener(e -> {
					setVisible(false);
					dispose();
				});
				cancelButton.setMnemonic('C');
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		setLocationRelativeTo(null);

		// Close the dialog when Esc is pressed
		String cancelName = "cancel";
		InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
		ActionMap actionMap = getRootPane().getActionMap();
		actionMap.put(cancelName, new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
	}
}
