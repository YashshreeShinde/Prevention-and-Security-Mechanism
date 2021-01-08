/*importing required packages*/
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent.*;
import java.awt.event.*;
import java.applet.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

class SenderFrame extends JFrame implements ActionListener
{
	int width=700,height=400;

	public JMenu file = new JMenu("File");
	public JMenu help = new JMenu("Help");
	public JMenu tool = new JMenu("Tools");
	public JMenu view = new JMenu("View");
	public JMenu look = new JMenu("Look and Feel");

	static public JTextField txtInFile,txtOutFile;
	private GridBagLayout gbl;
	private GridBagConstraints gbc;
	static String spath,dpath;
	static byte c=0;

	JProgressBar jprgbar;

	JMenuItem btnSource,btnDest,btnStart1,btnStop1;

	String btnSource_name="",btnDest_name="",btnStart_name="",btnReload_name="";

	private JPanel panel;

	//file chooser
	public static JFileChooser  sourcefile= new JFileChooser("./");
	public static JButton btnStart,btnStop,btnBrowseIn,btnReload;
	public static JLabel lblInFile,lblOutFile;

	public JCheckBox shutdown = new JCheckBox("Shutdown when done",false);

	public JComboBox cmbSelectDest;

	//AboutDataSec about=null;
	//Contact contact=null;
	//FrameTest frame=null;

	public SenderFrame()
	{
		spath=dpath=null;

		setTitle("Pollution Detection");
		btnSource_name = "Source File";
		btnStart_name = "Send File";
		btnDest_name = "Choose Receiver";
		btnReload_name = "Reload List";
		designFrame();
		//actionPerformed(ActionEvent e);
	}


	public void designFrame()
	{

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JMenuBar mb = new JMenuBar();													//menu bar

		Icon play = new ImageIcon("images/play.jpg");
		Icon stop = new ImageIcon("images/stop.jpg");
		Icon refresh = new ImageIcon("images/reload.png");

		makeButton("Metal","javax.swing.plaf.metal.MetalLookAndFeel");
		makeButton("Motif","com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		makeButton("Windows","com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

		/*File Menu*/
		JMenu btnOpen = new JMenu("Open");
		file.add(btnOpen);file.addSeparator();
		btnSource = new JMenuItem(btnSource_name);
		btnOpen.add(btnSource);
		JMenuItem btnExit = new JMenuItem("Exit",'E');
		file.add(btnExit);
		mb.add(file);

		/*View Menu*/
		view.add(look);
		final JCheckBoxMenuItem top =new JCheckBoxMenuItem ("Always on top",false);
		view.add(top);
		mb.add(view);


		/*Tools Menu*/
		btnStart1 = new JMenuItem("   "+btnStart_name);
		tool.add(btnStart1);
		btnStart1.setIcon(play);
		tool.addSeparator();
		btnStop1 = new JMenuItem("   Stop");
		btnStop1.setIcon(stop);
		tool.add(btnStop1);
		mb.add(tool);

		/*Help Menu*/
		JMenuItem btnHelp = new JMenuItem("Help",'H');
		help.add(btnHelp);
		mb.add(help);

		/*Loading bar*/
		jprgbar = new JProgressBar();
		jprgbar.setOrientation(JProgressBar.HORIZONTAL);
		jprgbar.setBackground(new Color(212,223,255));
		jprgbar.setFont(new java.awt.Font("Algerian",Font.BOLD,15 ));
		jprgbar.setForeground(Color.black);
		jprgbar.setMaximumSize(new Dimension(32767, 20));
		jprgbar.setMinimumSize(new Dimension(10, 20));
		jprgbar.setOpaque(true);
		jprgbar.setToolTipText("Please wait");
		jprgbar.setIndeterminate(true);
		//jprgbar.setBounds(new Rectangle(134, 84, 150, 27));
		jprgbar.setString("Sending file... Please Wait...");
		jprgbar.setValue(0);
		jprgbar.setStringPainted(true);

		/*Adding Mnemonic*/
		file.setMnemonic('F');
		btnOpen.setMnemonic('O');
		view.setMnemonic('V');
		tool.setMnemonic('l');
		help.setMnemonic('H');
		look.setMnemonic('K');


		/*Menu Bar add*/
		setJMenuBar(mb);

		c=1;

		lblInFile= new JLabel(btnSource_name);
		lblOutFile= new JLabel(btnDest_name);

		txtInFile= new JTextField(20);
		txtInFile.setBorder(BorderFactory.createLoweredBevelBorder());
		txtInFile.setToolTipText("Enter the path for source file");

		btnStart= new JButton("   "+btnStart_name);
		btnStart.setToolTipText(btnStart_name);
		btnStop= new JButton("   Stop");
		btnStop.setToolTipText("Stop");
		btnBrowseIn= new JButton("Browse...");
		btnBrowseIn.setToolTipText("Browse");
		btnReload= new JButton("   "+btnReload_name);
		btnReload.setToolTipText(btnReload_name);

		//String[] petStrings = { "192.168.2.1", "192.168.2.2", "192.168.2.3", "192.168.2.3", "192.168.2.5" };
		//cmbSelectDest = new JComboBox(petStrings);

		cmbSelectDest = new JComboBox();
		loadIPAddress(cmbSelectDest);

		btnStart.setIcon(play);
		btnStop.setIcon(stop);
		btnReload.setIcon(refresh);


		btnStart.addActionListener(this);
		btnStart1.addActionListener(this);
		btnStop.addActionListener(this);
		btnStop1.addActionListener(this);
		btnBrowseIn.addActionListener(this);
		btnSource.addActionListener(this);
		btnReload.addActionListener(this);

		//shutdown.addItemListener(this);

		btnHelp.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				try
				{
					//Runtime run= Runtime.getRuntime();
					File f = new File("help.html");
					f.setReadOnly();
					Runtime.getRuntime().exec("explorer help.html");
				}
				catch(Exception e)
				{
					JOptionPane.showMessageDialog(null,e,"Exception",JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnExit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
					closeFrame();
			}
		});

		top.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(top.getState())
					setAlwaysOnTop(true);
				else
					setAlwaysOnTop(false);
			}
		});

		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(WindowEvent winEvt)
			{
				// Perhaps ask user if they want to really exit.
				closeFrame();
			}
		});

		panel= new JPanel();
		//panel.setBackground(Color.yellow);
		panel.setForeground(Color.black);
		gbl= new GridBagLayout();
		gbc= new GridBagConstraints();
		panel.setLayout(gbl);

		gbc.weighty= 100;
		gbc.weightx=0;

		gbc.gridx= 0;	gbc.gridy= 2;	gbl.setConstraints(lblInFile, gbc);
		panel.add(lblInFile);

		gbc.gridx= 2;	gbl.setConstraints(txtInFile, gbc);
		panel.add(txtInFile);

		gbc.gridx= 4;	gbl.setConstraints(btnBrowseIn, gbc);
		panel.add(btnBrowseIn);

		gbc.gridx= 0;	gbc.gridy= 3;	gbl.setConstraints(lblOutFile, gbc);
		panel.add(lblOutFile);

		gbc.gridx= 2;	gbl.setConstraints(cmbSelectDest, gbc);
		panel.add(cmbSelectDest);

		gbc.gridx= 3;	gbl.setConstraints(btnReload, gbc);
		panel.add(btnReload);

		gbc.gridx= 2;	gbc.gridy= 5;	gbc.fill= 0;	gbc.gridwidth= 1;
		gbl.setConstraints(btnStart, gbc);
		panel.add(btnStart);

		gbc.gridx= 3;	gbl.setConstraints(btnStop, gbc);
		panel.add(btnStop);

		jprgbar.enable(true);
		gbc.gridx= 2;	gbc.gridy= 6;	gbc.fill= 0;	gbc.gridwidth= 1;
		gbl.setConstraints(jprgbar, gbc);
		panel.add(jprgbar);
		//jprgbar.setVisible(false);

		gbc.gridx= 0;	gbc.gridy= 7;	gbc.fill= 0;	gbc.gridwidth= 1;
		gbl.setConstraints(shutdown, gbc);


		//panel.add(btnStart);

		//gbc.fill= 0; gbc.gridwidth= 1;

		panel.add(shutdown);

		getContentPane().add(panel);
		setSize(width, height);
		setResizable(false);
		Dimension d= Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((d.width- width)/2, (d.height- height)/2);
		setIconImage(Toolkit.getDefaultToolkit().getImage("images/stop1.jpg"));
		setVisible(true);

	}

	public void closeFrame()
	{
		int option = JOptionPane.showConfirmDialog(this,"Are you sure want to exit?",
			"Confirm Exit",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
		if(option ==  JOptionPane.YES_OPTION)
		{
			updateControllerList(false);
			System.exit(0);
		}
		else
		{}
	}

	//overriding abstract class method
	public void actionPerformed(ActionEvent e)
	{
		Object o=e.getSource();
		if(o == btnBrowseIn || o==btnSource)
		{
			try
			{
				AudioClip ac = Applet.newAudioClip(new File("Sounds\\Browse File.wav").toURL());
				ac.play();
			}
			catch(Exception excep){}

			/*Getting source file*/
			if(sourcefile.showOpenDialog(this)== sourcefile.APPROVE_OPTION)
			{
				try
				{
					spath= sourcefile.getSelectedFile().getPath();
					txtInFile.setText(spath);
					File f=new File(spath);
					if(!f.exists())
					{
						try
						{
							AudioClip ac = Applet.newAudioClip(new File("Sounds\\File does not exist.wav").toURL());
							ac.play();
						}
						catch(Exception ace){}
						JOptionPane.showMessageDialog(null,"File "+spath+" does not exists","Error!!!",JOptionPane.ERROR_MESSAGE);
						return;
					}
					txtInFile.setText(spath);
				}
				catch(Exception ee)
				{
					JOptionPane.showMessageDialog(null,e,"Exception!!!",JOptionPane.ERROR_MESSAGE);
				}
			}

		}

		/*Process to be done on start button*/
		if(o == btnStart || o == btnStart1)
		{
			try
			{
				if(spath == null)
				{
					JOptionPane.showMessageDialog(null, "Please enter the filename", "Error!!!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(cmbSelectDest.getSelectedItem()==null)
				{
					JOptionPane.showMessageDialog(null, "Please select the receiver name", "Error!!!", JOptionPane.ERROR_MESSAGE);
					return;
				}

				btnStart.setEnabled(false);
				jprgbar.setVisible(true);
				//JOptionPane.showMessageDialog(null, "Oops! Functionality not yet implemented!", "ERROR", JOptionPane.INFORMATION_MESSAGE);
				InetAddress inetAdd = InetAddress.getByName(cmbSelectDest.getSelectedItem().toString());
				System.out.println ("IP Address is : " + inetAdd.getHostAddress());

				btnStart.setEnabled(true);
				jprgbar.setVisible(false);

				int status = Client.sendFile(spath,inetAdd.getHostAddress());

				if(status==0)
				{
					JOptionPane.showMessageDialog(this, "Hurray! File sent successfully!", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
					txtInFile.setText("");
				}
				else if(status==1)
				{
					JOptionPane.showMessageDialog(this, "Oops! Receiver rejected the file!", "REJECTION", JOptionPane.INFORMATION_MESSAGE);
				}
				else
					JOptionPane.showMessageDialog(this, "Oops! Error occured while sending file!", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
			catch(Exception e1)
			{
				JOptionPane.showMessageDialog(null, "PLZ ENTER FILE NAME", "ERROR", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}

		/*Process to be done on refresh button*/
		if(o == btnReload)
		{
			loadIPAddress(cmbSelectDest);
		}

		/*Process to be done on stop button*/
		if(o ==btnStop || o == btnStop1)
		{
		    closeFrame();
   		}
	}



	void makeButton(String name, final String plafName)
	{
			 // add button to panel

			  JMenuItem mi = new JMenuItem(name);
			  look.add(mi);

			   // set button action

			   mi.addActionListener(new
			   ActionListener()
			   {
			        public void actionPerformed(ActionEvent event)
			        {
			            // button action: switch to the new look and feel
			            try
			             {
			                UIManager.setLookAndFeel(plafName);
			                SwingUtilities.updateComponentTreeUI
			                (SenderFrame.this);
			              }
			              catch(Exception e) { e.printStackTrace(); }
			          }
			    });
	  }

	  public void loadIPAddress(JComboBox jcmbBox)
	  {
		  try
		  {
			  jcmbBox.removeAllItems();
			  Socket socket = new Socket(Constants.CONTROLLER_IP, Constants.NODE_REGISTRATION_PORT);

			  /* A timeout of zero is interpreted as an infinite timeout.*/
			  //socket.setSoTimeout(300000);
			  /* make sure the connection is actually alive.*/
			  //socket.setKeepAlive(true);

			  ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			  ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			  oos.writeObject(Constants.GET_NODE_LIST);

			  Object object = ois.readObject();
			  if(object instanceof ArrayList)
			  {
				  ArrayList<String> nodes = (ArrayList<String>)object;
				  for(int i=0;i<nodes.size();i++)
				  {
					  InetAddress nodeInet = InetAddress.getByName(nodes.get(i));
					  jcmbBox.addItem(nodeInet.getHostName());
				  }
			  }
			  oos.close();
			  ois.close();
			  socket.close();
		  }
		  catch (Exception inte)
		  {
			  inte.printStackTrace();
		  }
	  }

	  public void updateControllerList(boolean addMe)
	  {
		  try
		  {
			  Socket socket = new Socket(Constants.CONTROLLER_IP, Constants.NODE_REGISTRATION_PORT);

			  /* A timeout of zero is interpreted as an infinite timeout.*/
			  //socket.setSoTimeout(50000);
			  /* make sure the connection is actually alive.*/
			 // socket.setKeepAlive(true);

			  ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			  if(addMe)
			  {
				  //Ask controller to add this IP Address
				  oos.writeObject(Constants.REQUEST_ADD_NODE);
			  }
			  else
			  {
				  //Ask controller to remove this IP Address
				  oos.writeObject(Constants.REQUEST_REMOVE_NODE);
			  }

			  InetAddress localhost = InetAddress.getLocalHost();
			  byte[] ip = localhost.getAddress();
			  oos.writeObject(new Integer(""+ip[3]));

			  oos.close();
			  socket.close();
		  }
		  catch (SocketException se)
		  {
			  JOptionPane.showMessageDialog(null, se.getMessage() + "\nMake sure server is on", "Error!!!", JOptionPane.ERROR_MESSAGE);
		  }
		  catch(Exception e)
		  {
			  e.printStackTrace();
		  }
	  }

	  public static void main(String args[])
	  {
		  SenderFrame sf = new SenderFrame();
		  sf.updateControllerList(true);
	  }
}
