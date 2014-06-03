package com.ipass.jmeterplugin.radiussampler;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.TestElementProperty;

public class RadiusConfigGUI extends AbstractConfigGui implements ActionListener
{
	private static final String ADD_COMMAND = "Add"; // $NON-NLS-1$

	private static final String DELETE_COMMAND = "Delete"; // $NON-NLS-1$



	/**
	 * 
	 */
	private static final long serialVersionUID = 8289208121020406550L;



	private JTable attributesTable;
	private RadiusAttributesManager attributeManager;

	private RadiusAttributeTableModel attributeTableModel;

	private JButton addButton;

	private JButton deleteButton;



	
	///Added for generate SessionId or not
	private javax.swing.JRadioButton yGenSessionId;
	private javax.swing.JRadioButton nGenSessionId;
	
	
	// Variables declaration - do not modify
	public javax.swing.JLabel acctPortLable;
	public javax.swing.JTextField acctPortText;
	public javax.swing.JLabel authPortLable;
	public javax.swing.JTextField authPortText;
	public javax.swing.ButtonGroup buttonGroup1;
	
	public javax.swing.ButtonGroup buttonSessionGroup;
	
	public javax.swing.JLabel passwordLable;
	public javax.swing.JPasswordField passwordText;
	public javax.swing.JRadioButton radioAcct;
	public javax.swing.JRadioButton radioAuth;
	public javax.swing.JRadioButton radioBoth;
	public javax.swing.JLabel requestTypeLable;
	public javax.swing.JLabel retryLable;
	public javax.swing.JTextField retryText;
	public javax.swing.JLabel serverIpLable;
	public javax.swing.JTextField serverTextField;
	public javax.swing.JLabel sharedSecretLable;
	public javax.swing.JTextField sharedText;
	public javax.swing.JLabel timeoutLable;
	public javax.swing.JTextField timeoutText;
	public javax.swing.JLabel usernameLable;
	public javax.swing.JTextField usernameTextField;
	// End of variables declaration


	public RadiusConfigGUI()
	{
		this(true);
	}

	public RadiusConfigGUI(boolean displayName)
	{

		init();

	}




	public void clearGui()
	{
		super.clearGui();

		attributeTableModel.clearData();
		deleteButton.setEnabled(false);


	}

	public TestElement createTestElement()
	{
		ConfigTestElement element = new ConfigTestElement();
		modifyTestElement(element);
		return element;
	}



	public String getStaticLabel()
	{
		// TODO Auto-generated method stub
		return "Radius Protocol Sampler";
	}


	public void configure(TestElement element)
	{
		super.configure(element);


		this.serverTextField.setText(element
				.getPropertyAsString(RadiusSamplerElements.SERVER_IP));

		this.authPortText.setText(element
				.getPropertyAsString(RadiusSamplerElements.AUTH_PORT));

		this.acctPortText.setText(element
				.getPropertyAsString(RadiusSamplerElements.ACCT_PORT));

		this.retryText.setText(element
				.getPropertyAsString(RadiusSamplerElements.RADIUS_RETRY));

		this.sharedText.setText(element
				.getPropertyAsString(RadiusSamplerElements.SHARED_SECRET));

		this.timeoutText.setText(element
				.getPropertyAsString(RadiusSamplerElements.SOCKET_TIMEOUT));

		this.usernameTextField.setText(element
				.getPropertyAsString(RadiusSamplerElements.USER_NAME));

		this.passwordText.setText(element
				.getPropertyAsString(RadiusSamplerElements.PASSWORD));

		this.attributeManager = (RadiusAttributesManager) element.getProperty(
				RadiusSamplerElements.RADIUS_ATTRIBUTES).getObjectValue();

		this.attributeTableModel = new RadiusAttributeTableModel(attributeManager);
		this.attributesTable.setModel(attributeTableModel);

		boolean hasRows = attributesTable.getRowCount() > 0;
		deleteButton.setEnabled(hasRows);


		String actionCommand = element
				.getPropertyAsString(RadiusSamplerElements.REQUEST_TYPE);

		Enumeration<AbstractButton> btns = this.buttonGroup1.getElements();
		while (btns.hasMoreElements())
		{
			AbstractButton abstractButton = (AbstractButton) btns.nextElement();
			if (abstractButton.getActionCommand().equals(actionCommand))
			{
				this.buttonGroup1.setSelected(abstractButton.getModel(), true);
				break;
			}

		}

	}

	public void modifyTestElement(TestElement element)
	{

		configureTestElement(element);

		element.setProperty(RadiusSamplerElements.SERVER_IP,
				this.serverTextField.getText());

		element.setProperty(RadiusSamplerElements.AUTH_PORT, this.authPortText.getText());
		element.setProperty(RadiusSamplerElements.ACCT_PORT, this.acctPortText.getText());

		element.setProperty(RadiusSamplerElements.USER_NAME, this.usernameTextField.getText());
		element.setProperty(RadiusSamplerElements.PASSWORD, new String(this.passwordText.getPassword()));
		element.setProperty(RadiusSamplerElements.RADIUS_RETRY, this.retryText.getText());
		element.setProperty(RadiusSamplerElements.SHARED_SECRET, this.sharedText.getText());
		element.setProperty(RadiusSamplerElements.SOCKET_TIMEOUT, this.timeoutText.getText());

		element.setProperty(new TestElementProperty(
				RadiusSamplerElements.RADIUS_ATTRIBUTES, this.attributeManager));



		element.setProperty(RadiusSamplerElements.REQUEST_TYPE, this.buttonGroup1
				.getSelection().getActionCommand());

		//element.setProperty(RadiusSamplerElements.REQUEST_TYPE, "auth");
		
	}

	private Component createServerPanel()

	{

		 
       
        
        JPanel pnlMain = new JPanel();
		pnlMain.setBorder(BorderFactory.createEtchedBorder());


		JPanel jp = new JPanel();
		jp.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Radius Server Configuration"));
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonSessionGroup = new javax.swing.ButtonGroup();
        
        usernameLable = new javax.swing.JLabel();
        usernameTextField = new javax.swing.JTextField();
        passwordLable = new javax.swing.JLabel();
        passwordText = new javax.swing.JPasswordField();
        radioAuth = new javax.swing.JRadioButton("Auth Request",true);
        radioAcct = new javax.swing.JRadioButton();
        radioBoth = new javax.swing.JRadioButton();
        
        //Adding session Id
        nGenSessionId = new javax.swing.JRadioButton("Unique Session Id",true);
        yGenSessionId = new javax.swing.JRadioButton();
        
        requestTypeLable = new javax.swing.JLabel();
        serverIpLable = new javax.swing.JLabel();
        sharedSecretLable = new javax.swing.JLabel();
        sharedText = new javax.swing.JTextField();
        authPortLable = new javax.swing.JLabel();
        authPortText = new javax.swing.JTextField();
        acctPortLable = new javax.swing.JLabel();
        acctPortText = new javax.swing.JTextField();
        timeoutLable = new javax.swing.JLabel();
        timeoutText = new javax.swing.JTextField();
        retryLable = new javax.swing.JLabel();
        retryText = new javax.swing.JTextField();
        serverTextField = new javax.swing.JTextField();

        usernameLable.setText("Username");


        passwordLable.setText("Password");

        this.radioAuth.setActionCommand("auth");
        this.radioAcct.setActionCommand("acct");
        this.radioBoth.setActionCommand("both");
        this.buttonGroup1.add(radioAuth);
        this.buttonGroup1.add(radioAcct);
        this.buttonGroup1.add(radioBoth);

        this.nGenSessionId.setActionCommand("gensession");
        this.yGenSessionId.setActionCommand("nosession");
        
        this.buttonSessionGroup.add(nGenSessionId);
        this.buttonSessionGroup.add(yGenSessionId);
        
        yGenSessionId.setText("True");
        nGenSessionId.setText("False");
        

        radioAuth.setText("Auth Request");



        radioAcct.setText("Acct Request");

        radioBoth.setText("Auth & Acct Request");

        requestTypeLable.setText("Request Type");

        serverIpLable.setText("Server IP");

        sharedSecretLable.setText("Shared Secret");



        authPortLable.setText("Auth Port");

        acctPortLable.setText("Acct Port");

        timeoutLable.setText("Timeout");

        retryLable.setText("Retry");

        this.serverTextField.setText("");


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(jp);
        jp.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(usernameLable)
                            .addComponent(passwordLable))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(usernameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(passwordText))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(serverIpLable)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addComponent(serverTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(authPortLable)
                        .addGap(26, 26, 26)
                        .addComponent(authPortText, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(acctPortLable)
                        .addGap(18, 18, 18)
                        .addComponent(acctPortText, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(timeoutLable)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addComponent(timeoutText, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(retryLable)
                        .addGap(34, 34, 34)
                        .addComponent(retryText, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addComponent(sharedSecretLable)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sharedText, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(127, 127, 127))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(requestTypeLable)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(radioAuth)
                        .addGap(18, 18, 18)
                        .addComponent(radioAcct)
                        .addGap(18, 18, 18)
                        .addComponent(radioBoth)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameLable)
                    .addComponent(usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(radioAuth)
                    .addComponent(radioAcct)
                    .addComponent(radioBoth)
                    .addComponent(requestTypeLable))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(timeoutText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(retryLable)
                            .addComponent(retryText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sharedSecretLable)
                            .addComponent(sharedText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(timeoutLable))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(authPortLable)
                            .addComponent(authPortText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(acctPortLable)
                            .addComponent(acctPortText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(passwordText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(passwordLable))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(serverTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(serverIpLable))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
		 pnlMain.add(jp);

		return pnlMain;

	}


	public JPanel createAttributeTablePanel()
	{
		// create the JTable that holds header per row
		attributesTable = new JTable(attributeTableModel);

		attributesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		attributesTable.setPreferredScrollableViewportSize(new Dimension(100,
				70));

		JPanel panel = new JPanel(new BorderLayout(0, 5));
		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Packet Attributes")); // $NON-NLS-1$
		panel.add(new JScrollPane(attributesTable), BorderLayout.CENTER);
		panel.add(createButtonPanel(), BorderLayout.SOUTH);

		return panel;
	}

	private JPanel createButtonPanel()
	{
		boolean tableEmpty = (attributesTable.getRowCount() == 0);

		addButton = createButton("Add", 'A', ADD_COMMAND, true); // $NON-NLS-1$
		deleteButton = createButton("Delete", 'D', DELETE_COMMAND, !tableEmpty); // $NON-NLS-1$


		JPanel buttonPanel = new JPanel();
		buttonPanel.add(addButton);
		buttonPanel.add(deleteButton);


		return buttonPanel;
	}

	private JButton createButton(String resName, char mnemonic, String command,
			boolean enabled)
	{
		JButton button = new JButton(resName);
		button.setMnemonic(mnemonic);
		button.setActionCommand(command);
		button.setEnabled(enabled);
		button.addActionListener(this);
		return button;
	}

	private void init()
	{

		attributeManager = new RadiusAttributesManager();
		attributeTableModel = new RadiusAttributeTableModel(attributeManager);

		setLayout(new BorderLayout(0, 10));
		setBorder(makeBorder());

		add(makeTitlePanel(), "North");

		JPanel mainPanel = new JPanel(new BorderLayout());

		Component serverPanel = createServerPanel();

		mainPanel.add(serverPanel, "North");
		mainPanel.add(createAttributeTablePanel(), "Center");

		add(mainPanel, "Center");
	}

	public String getLabelResource()
	{

		return getClass().getCanonicalName();
	}

	private static class RadiusAttributeTableModel extends AbstractTableModel
	{
		private static final long serialVersionUID = 240L;

		private RadiusAttributesManager manager;

		public RadiusAttributeTableModel(RadiusAttributesManager man)
		{
			manager = man;
		}

		public void clearData()
		{
			manager.clear();
			fireTableDataChanged();
		}

		public void removeRow(int row)
		{
			manager.remove(row);
		}

		public void addNewRow()
		{
			manager.add();
		}

		@Override
		public boolean isCellEditable(int row, int column)
		{
			// all table cells are editable
			return true;
		}

		@Override
		public Class<?> getColumnClass(int column)
		{
			return getValueAt(0, column).getClass();
		}

		public int getRowCount()
		{
			return manager.getAttributes().size();
		}

		/**
		 * Required by table model interface.
		 */
		public int getColumnCount()
		{
			return manager.getColumnCount();
		}

		/**
		 * Required by table model interface.
		 */
		@Override
		public String getColumnName(int column)
		{
			return manager.getColumnName(column);
		}

		/**
		 * Required by table model interface.
		 */
		public Object getValueAt(int row, int column)
		{
			RadiusAttributes head = manager.getAttribute(row);
			if (column == 0)
			{
				return head.getName();
			} else if (column == 1)
			{
				return head.getValue();
			}
			return null;
		}

		/**
		 * Required by table model interface.
		 */
		@Override
		public void setValueAt(Object value, int row, int column)
		{
			RadiusAttributes header = manager.getAttribute(row);

			if (column == 0)
			{
				header.setName((String) value);
			} else if (column == 1)
			{
				header.setValue((String) value);
			}

		}

	}

	public void actionPerformed(ActionEvent e)
	{
		String action = e.getActionCommand();

		if (action.equals(DELETE_COMMAND))
		{
			if (attributeTableModel.getRowCount() > 0)
			{
				// If a table cell is being edited, we must cancel the editing
				// before deleting the row.
				if (attributesTable.isEditing())
				{
					TableCellEditor cellEditor = attributesTable.getCellEditor(
							attributesTable.getEditingRow(),
							attributesTable.getEditingColumn());
					cellEditor.cancelCellEditing();
				}

				int rowSelected = attributesTable.getSelectedRow();

				if (rowSelected != -1)
				{
					attributeTableModel.removeRow(rowSelected);
					attributeTableModel.fireTableDataChanged();

					// Disable the DELETE and SAVE buttons if no rows remaining
					// after delete
					if (attributeTableModel.getRowCount() == 0)
					{
						deleteButton.setEnabled(false);

					}

					// Table still contains one or more rows, so highlight
					// (select) the appropriate one.
					else
					{
						int rowToSelect = rowSelected;

						if (rowSelected >= attributeTableModel.getRowCount())
						{
							rowToSelect = rowSelected - 1;
						}

						attributesTable.setRowSelectionInterval(rowToSelect,
								rowToSelect);
					}
				}
			}
		} else if (action.equals(ADD_COMMAND))
		{
			// If a table cell is being edited, we should accept the current
			// value and stop the editing before adding a new row.
			if (attributesTable.isEditing())
			{
				TableCellEditor cellEditor = attributesTable.getCellEditor(
						attributesTable.getEditingRow(),
						attributesTable.getEditingColumn());
				cellEditor.stopCellEditing();
			}

			attributeTableModel.addNewRow();
			attributeTableModel.fireTableDataChanged();

			// Enable the DELETE and SAVE buttons if they are currently
			// disabled.
			if (!deleteButton.isEnabled())
			{
				deleteButton.setEnabled(true);
			}


			// Highlight (select) the appropriate row.
			int rowToSelect = attributeTableModel.getRowCount() - 1;
			attributesTable.setRowSelectionInterval(rowToSelect, rowToSelect);
		} 


	}



}
