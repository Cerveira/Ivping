package ilc.ping.view;

import ilc.ping.interfaces.DialogoFiltro;
import ilc.ping.interfaces.Pinger;
import ilc.ping.model.MyTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class RootFrame extends JFrame {
    private static final String ECO_N = "@ping -n 10 ";
    private static final String ECO_T = "@ping -t ";
    private static final String pathPlanilha1 = System.getenv("USERPROFILE") + "/ivping/listdev.xlsx";
    private static final String pathPlanilha2 = System.getenv("USERPROFILE") + "/ivping/list_all.xlsx";
    private JPopupMenu popupMenu;
    private TableRowSorter<MyTableModel> sorter1;
    private TableRowSorter<MyTableModel> sorter2;
    private JPanel mainPanel;
    private JTabbedPane hostsTabbedPane;
    private JButton buttonPing;
    private JCheckBox checkBoxT;
    private JButton buttonHttp;
    private JTextField txtFilter;
    private JButton buttonTrash;
    private JButton buttonMais;
    private JButton buttonHome;
    private JTable radioTable;
    private JTable todosTable;
    private JScrollPane todosScroll;
    private JScrollPane radioScroll;

    public RootFrame(String title) throws IOException {
        super(title);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 656);
        setMinimumSize(new Dimension(1000, 500)); // novo
        setLocationRelativeTo(null);
        setContentPane(mainPanel);

        inicializaFrame();
    } // end of constructor

    private void inicializaFrame() throws IOException {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/ivping_48.png")));
        MyTableModel tmodelRadio = new MyTableModel(pathPlanilha1);
        MyTableModel tmodelTodos = new MyTableModel(pathPlanilha2);
        radioTable.setModel(tmodelRadio);
        todosTable.setModel(tmodelTodos);

        sorter1 = new TableRowSorter<>(tmodelRadio);
        sorter2 = new TableRowSorter<>(tmodelTodos);
        radioTable.setRowSorter(sorter1);
        todosTable.setRowSorter(sorter2);

        // Possibilita chamar o método Ping ao pressionar a tecla Enter
        addBindings(radioTable);
        addBindings(todosTable);

        radioTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) metodoping();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    // get row that pointer is over
                    int row = radioTable.rowAtPoint(e.getPoint());

                    // if pointer is over a selected row, show popup
                    if (radioTable.isRowSelected(row)) {
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });

        todosTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    metodoping();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = todosTable.rowAtPoint(e.getPoint());
                    if (todosTable.isRowSelected(row)) {
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });

        popupMenu = new JPopupMenu();
        JMenuItem itemCopiar = new JMenuItem();
        JMenuItem itemPingPopup = new JMenuItem();

        itemCopiar.setText("Copiar IP");
        itemCopiar.addActionListener(this::itemCopiarActionPerformed);
        popupMenu.add(itemCopiar);
        itemPingPopup.setText("Ping (Ctrl+G)");
        itemPingPopup.addActionListener(e -> metodoping());
        popupMenu.add(itemPingPopup);

        radioScroll.setBorder(new EmptyBorder(0, 0, 0, 0));
        todosScroll.setBorder(new EmptyBorder(0, 0, 0, 0));

        // barra de menus
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu menuFile = new JMenu();
        menuFile.setText("Arquivo");
        menuFile.setMnemonic('A');
        menuBar.add(menuFile);
        JMenuItem itemQuit = new JMenuItem();
        itemQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        itemQuit.addActionListener(e -> System.exit(0));
        itemQuit.setText("Sair");
        itemQuit.setMnemonic('r');
        menuFile.add(itemQuit);

        JMenu menuTools = new JMenu();
        menuTools.setText("Ferramentas");
        menuTools.setMnemonic('F');
        menuBar.add(menuTools);
        JMenuItem itemPing = new JMenuItem("Ping");

        itemPing.addActionListener(e -> metodoping());

        itemPing.setMnemonic('P');
        itemPing.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK));
        menuTools.add(itemPing);

        JMenuItem mntmBrowser = new JMenuItem("Acessar via Browser");
        mntmBrowser.addActionListener(e -> http());
        mntmBrowser.setMnemonic('B');
        mntmBrowser.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK));
        menuTools.add(mntmBrowser);

        JMenu menuHelp = new JMenu();
        menuHelp.setText("Ajuda");
        menuHelp.setMnemonic('u');
        menuBar.add(menuHelp);

        JMenuItem itemAbout = new JMenuItem();
        itemAbout.addActionListener(e -> JOptionPane.showMessageDialog(null,
                "<html><b><font color=#013571>Ivping 1.0</font></b></html>\nDesenvolvido por: Ivaldo Cerveira",
                "Sobre o Ivping", JOptionPane.PLAIN_MESSAGE));
        itemAbout.setText("Sobre o Ivping");
        itemAbout.setMnemonic('S');
        menuHelp.add(itemAbout);
        // fim da barra de menus

        buttonPing.addActionListener(e -> metodoping());
        buttonHttp.addActionListener(e -> http());
        buttonMais.addActionListener(e -> {
            int tab = hostsTabbedPane.getSelectedIndex();
            try {
                txtFilter.setText("");
                DialogoFiltro dlg = new DialogoFiltro(null);
                dlg.setVisible(true);
                String aux1 = dlg.getTf1();
                String aux2 = dlg.getTf2();
                RowFilter<MyTableModel, Object> filtroFinal = null;
                if (aux1 != null && aux2 != null) {
                    List<RowFilter<MyTableModel, Object>> filters = new ArrayList<>(
                            2);
                    filters.add(RowFilter.regexFilter("(?i)" + aux1));
                    filters.add(RowFilter.regexFilter("(?i)" + aux2));
                    filtroFinal = RowFilter.orFilter(filters);
                }

                if (tab == 0)
                    sorter1.setRowFilter(filtroFinal);
                else if (tab == 1)
                    sorter2.setRowFilter(filtroFinal);
            } catch (IndexOutOfBoundsException ex) {
                ex.printStackTrace();
            }
        });

        // Whenever txtFilter changes, invoke filTabs
        txtFilter.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) { filTabs(); }

            @Override
            public void insertUpdate(DocumentEvent e) { filTabs(); }

            @Override
            public void removeUpdate(DocumentEvent e) { filTabs(); }

            private void filTabs() {
                int tab = hostsTabbedPane.getSelectedIndex();
                RowFilter<MyTableModel, Object> rFilter;

                try {
                    rFilter = RowFilter.regexFilter("(?i)" + txtFilter.getText());
                } catch (java.util.regex.PatternSyntaxException e) {
                    return;
                }

                try {
                    if (tab == 0) {
                        sorter1.setRowFilter(rFilter);
                    } else if (tab == 1) {
                        sorter2.setRowFilter(rFilter);
                    }
                } catch (IndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonHome.addActionListener(e -> {
            int tab = hostsTabbedPane.getSelectedIndex();
            String aux1 = "";
            String aux2 = "";

            // retorna a condicao inicial caso o filtro "Mais" tenha sido aplicado
            // anteriormente
            List<RowFilter<MyTableModel, Object>> filters = new ArrayList<>(2);
            filters.add(RowFilter.regexFilter("(?i)" + aux1));
            filters.add(RowFilter.regexFilter("(?i)" + aux2));
            RowFilter<MyTableModel, Object> filtroFinal = RowFilter.orFilter(filters);

            if (tab == 0) {
                sorter1.setRowFilter(filtroFinal);
                radioTable.changeSelection(0, 0, false, false);
                radioTable.requestFocusInWindow();

            } else if (tab == 1) {
                sorter2.setRowFilter(filtroFinal);
                todosTable.changeSelection(0, 0, false, false);
                todosTable.requestFocusInWindow();
            }
            txtFilter.setText("");
            if (RootFrame.this.getExtendedState() != 6)
                setLocationRelativeTo(null);
        });

        buttonHome.setMnemonic(KeyEvent.VK_HOME);

        TableColumn column;
        for (int i = 0; i < 3; i++) {
            column = radioTable.getColumnModel().getColumn(i);
            if (i == 2) {
                column.setPreferredWidth(500); // third column is bigger
            } else {
                column.setPreferredWidth(60);
            }
        }

        TableColumn column2;
        for (int i = 0; i < 3; i++) {
            column2 = todosTable.getColumnModel().getColumn(i);
            if (i == 2) {
                column2.setPreferredWidth(500); // third column is bigger
            } else {
                column2.setPreferredWidth(60);
            }
        }

        radioTable.changeSelection(0, 0, false, false);
        todosTable.changeSelection(0, 0, false, false);

        buttonTrash.addActionListener(e -> {
            txtFilter.setText("");
            txtFilter.requestFocusInWindow();
        });

        final JTextField ftf = txtFilter;
        javax.swing.SwingUtilities.invokeLater(ftf::requestFocusInWindow);
    } // end of inicializaFrame

    private void metodoping() {
        try {
            String eco = (checkBoxT.isSelected() ? ECO_T : ECO_N);
            Pinger.ping(radioTable, todosTable, hostsTabbedPane, eco);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void http() {
        int tab = hostsTabbedPane.getSelectedIndex();
        try {
            if (tab == 0) {
                int linhaSelecionada = radioTable.getSelectedRow();
                String hostname = (String) radioTable.getValueAt(linhaSelecionada, 0);
                String ip = (String) radioTable.getValueAt(linhaSelecionada, 1);
                String location = (String) radioTable.getValueAt(linhaSelecionada, 2);

                String comando = "cmd /c start http://" + ip;

                if ((location.contains("(CERAGON)") && !hostname.contains("BE01") && !hostname.contains("BE02")
                        && !hostname.contains("BE03")) || location.contains("(LEDR)") || location.contains("(NEC")
                        || location.contains("(ASGA)") || location.contains("(DIGITEL)")
                        || location.contains("(APRISA)") || location.contains("(MIKROTIK)")
                        || location.contains("(TP-LINK)")) {
                    try {
                        Runtime.getRuntime().exec(comando);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else if (((hostname.contains("BE01") || hostname.contains("BE02") || hostname.contains("BE03"))
                        && location.contains("(CERAGON)")) || (hostname.equals("ANRITSU")
                        && location.contains("ANRITSU")))
                {
                    try {
                        Runtime.getRuntime().exec("mstsc /v " + ip);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    // called when the user presses the Enter key
    private void addBindings(JTable table) {
        table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        table.getActionMap().put("Enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                metodoping();
            }
        });
    }

    private void itemCopiarActionPerformed(java.awt.event.ActionEvent e) {
        int tab = hostsTabbedPane.getSelectedIndex();
        String ip = "";
        if (tab == 0) {
            ip = (String) radioTable.getValueAt(radioTable.getSelectedRow(), 1);
        } else if (tab == 1) {
            ip = (String) todosTable.getValueAt(todosTable.getSelectedRow(), 1);
        }
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(ip);
        clipboard.setContents(selection, null);
    }
}
