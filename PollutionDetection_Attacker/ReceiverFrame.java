/*importing required packages*/

import java.security.MessageDigest;
import javax.crypto.SecretKey;
import java.net.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent.*;
import java.awt.event.*;
import java.applet.*;

class ReceiverFrame extends JFrame implements ActionListener, Runnable {

    public static final int PORT = 3333;
    public static final int BUFFER_SIZE = 100;
    int transactionID;
    public String nextNodeIP=null;

    int width = 700, height = 400;
    Thread thread;
    String filename = null;
    byte[] receivedMessageDigest;
    String fileExtension;
    public JMenu file = new JMenu("File");
    public JMenu help = new JMenu("Help");
    public JMenu tool = new JMenu("Tools");
    public JMenu view = new JMenu("View");
    public JMenu look = new JMenu("Look and Feel");
    static public JTextField txtInFile, txtOutFile;
    private GridBagLayout gbl;
    private GridBagConstraints gbc;
    static String spath, dpath;
    static byte c = 0;
    JMenuItem btnSource, btnDest, btnVerify1, btnStop1, btnForward1;
    String btnSource_name = "", btnDest_name = "", btnStart_name = "", btnStop_name = "", btnForward_name = "", btnDownload_name;
    private JPanel panel;
    //file chooser
    public static JFileChooser sourcefile = new JFileChooser("./");
    public static JFileChooser destfile = new JFileChooser("./");
    public static JButton btnVerify, btnStop, btnBrowseIn, btnBrowseOut;
    public static JButton btnForward, btnDownload;
    public static JLabel lblInFile, lblOutFile;
    public static JProgressBar jprgbar;
    ;

	public JCheckBox shutdown = new JCheckBox("Shutdown when done", false);

    //FrameTest frame=null;
    public ReceiverFrame() {

        spath = dpath = null;

        setTitle("Receiver");
        btnSource_name = "Source File";
        btnDest_name = "Save to";
        btnDownload_name = "Download";
        btnStart_name = "Verify";
        btnForward_name = "Forward";
        btnStop_name = "Close";
        designFrame();
        //actionPerformed(ActionEvent e);

    }

    public void designFrame() {

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE );
        JMenuBar mb = new JMenuBar();													//menu bar

        //Icon play = new ImageIcon("images/play.jpg");
        //Icon stop = new ImageIcon("images/stop.jpg");

        makeButton("Metal", "javax.swing.plaf.metal.MetalLookAndFeel");
        makeButton("Motif", "com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        makeButton("Windows", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

        /*
         * File Menu
         */
        JMenu btnOpen = new JMenu("Open");
        file.add(btnOpen);
        file.addSeparator();
        btnSource = new JMenuItem(btnSource_name);
        btnOpen.add(btnSource);
        btnOpen.addSeparator();
        btnDest = new JMenuItem(btnDest_name);
        btnOpen.add(btnDest);
        JMenuItem btnExit = new JMenuItem("Exit", 'E');
        file.add(btnExit);
        mb.add(file);

        /*
         * View Menu
         */
        view.add(look);
        final JCheckBoxMenuItem top = new JCheckBoxMenuItem("Always on top", false);
        view.add(top);
        mb.add(view);


        /*
         * Tools Menu
         */
        btnVerify1 = new JMenuItem(btnStart_name);
        tool.add(btnVerify1);
        //btnVerify1.setIcon(play);
        tool.addSeparator();
        btnForward1 = new JMenuItem(btnForward_name);
        tool.add(btnForward1);
        tool.addSeparator();
        btnStop1 = new JMenuItem(btnStop_name);
        //btnStop1.setIcon(stop);
        tool.add(btnStop1);

        mb.add(tool);

        /*
         * Help Menu
         */
        /*
         * JMenuItem btnAbout = new JMenuItem("About Data Security...",'A');
         */
        JMenuItem btnHelp = new JMenuItem("Help", 'H');
        /*
         * JMenuItem btnContact = new JMenuItem("Contact us...",'C');
         */
        help.add(btnHelp);
        /*
         * help.addSeparator(); help.add(btnContact); help.addSeparator();
         * help.add(btnAbout);
         */
        mb.add(help);

        /**
         * * Status for receiving or sending file **
         */
        jprgbar = new JProgressBar();
        jprgbar.setOrientation(JProgressBar.HORIZONTAL);
        jprgbar.setBackground(new Color(212, 223, 255));
        jprgbar.setFont(new java.awt.Font("Algerian", Font.BOLD, 15));
        jprgbar.setForeground(Color.black);
        jprgbar.setMaximumSize(new Dimension(32767, 20));
        jprgbar.setMinimumSize(new Dimension(10, 20));
        jprgbar.setOpaque(true);
        jprgbar.setToolTipText("Status");
        jprgbar.setIndeterminate(true);
        //jprgbar.setBounds(new Rectangle(134, 84, 150, 27));
        jprgbar.setString(Constants.STATUS_LISTENING);
        jprgbar.setValue(0);
        jprgbar.setStringPainted(true);

        /*
         * Adding Mnemonic
         */
        file.setMnemonic('F');
        btnOpen.setMnemonic('O');
        view.setMnemonic('V');
        //btnTheme.setMnemonic('T');
        tool.setMnemonic('l');
        help.setMnemonic('H');
        look.setMnemonic('K');


        /*
         * Menu Bar add
         */
        setJMenuBar(mb);

        c = 1;

        lblInFile = new JLabel(btnSource_name);
        lblOutFile = new JLabel(btnDest_name);

        txtInFile = new JTextField(20);
        txtInFile.setBorder(BorderFactory.createLoweredBevelBorder());
        txtInFile.setToolTipText("Enter the path for source file");
        txtOutFile = new JTextField(20);
        txtOutFile.setBorder(BorderFactory.createLoweredBevelBorder());
        txtOutFile.setToolTipText("Enter the path for output file");

        btnDownload = new JButton(btnDownload_name);
        btnVerify = new JButton(btnStart_name);
        btnVerify.setToolTipText(btnStart_name);
        btnForward = new JButton(btnForward_name);
        btnStop = new JButton(btnStop_name);
        btnStop.setToolTipText(btnStop_name);
        btnBrowseIn = new JButton("Browse...");
        btnBrowseIn.setToolTipText("Browse");
        btnBrowseOut = new JButton("Browse...");
        btnBrowseOut.setToolTipText("Browse");

        //btnStart.setIcon(play);
        //btnStop.setIcon(stop);



        btnDownload.addActionListener(this);
        btnVerify.addActionListener(this);
        btnForward.addActionListener(this);
        btnVerify1.addActionListener(this);
        btnStop.addActionListener(this);
        btnStop1.addActionListener(this);
        btnBrowseIn.addActionListener(this);
        btnSource.addActionListener(this);
        btnBrowseOut.addActionListener(this);
        btnDest.addActionListener(this);

        //shutdown.addItemListener(this);

        /*
         * btnAbout.addActionListener(new ActionListener() { public void
         * actionPerformed(ActionEvent event) { if(about==null) about= new
         * AboutDataSec(ReceiverFrame.this,"About Data Security",true);
         * about.setVisible(true); } });
         */

        btnHelp.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                try {
                    //Runtime run= Runtime.getRuntime();
                    File f = new File("help.html");
                    f.setReadOnly();
                    Runtime.getRuntime().exec("explorer help.html");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e, "Exception", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
				closeFrame();
            }
        });

        this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent winEvt) {
				// Perhaps ask user if they want to really exit.
				closeFrame();
			}
		});

        top.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (top.getState()) {
                    setAlwaysOnTop(true);
                } else {
                    setAlwaysOnTop(false);
                }
            }
        });

        /*
         * btnContact.addActionListener(new ActionListener() { public void
         * actionPerformed(ActionEvent e) { if(contact==null) contact=new
         * Contact(ReceiverFrame.this,"Contact us...",true);
         * contact.setVisible(true); } });
         */

        panel = new JPanel();
        //panel.setBackground(Color.yellow);
        panel.setForeground(Color.black);
        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();
        panel.setLayout(gbl);

        gbc.weighty = 100;
        gbc.weightx = 0;

        /*
         * gbc.gridx= 2;	gbc.gridy= 6;	gbc.fill= 0;	gbc.gridwidth= 1;
         * gbl.setConstraints(jprgbar, gbc); panel.add(jprgbar);
         */

        jprgbar.enable(true);
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbl.setConstraints(jprgbar, gbc);
        panel.add(jprgbar);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = 0;
        gbc.gridwidth = 1;
        /*gbl.setConstraints(btnDownload, gbc);
        panel.add(btnDownload);

        gbc.gridx = 1;*/
        gbl.setConstraints(btnVerify, gbc);
        panel.add(btnVerify);

        gbc.gridx = 2;
        gbl.setConstraints(btnForward, gbc);
        panel.add(btnForward);

        gbc.gridx = 4;
        gbl.setConstraints(btnStop, gbc);
        panel.add(btnStop);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbl.setConstraints(lblOutFile, gbc);
        panel.add(lblOutFile);

        gbc.gridx = 2;
        gbl.setConstraints(txtOutFile, gbc);
        panel.add(txtOutFile);

        gbc.gridx = 4;
        gbl.setConstraints(btnBrowseOut, gbc);
        panel.add(btnBrowseOut);


        /*
         * gbc.gridx= 0;	gbc.gridy= 6;	gbl.setConstraints(lblInFile, gbc);
         * panel.add(lblInFile);
         *
         * gbc.gridx= 2;	gbl.setConstraints(txtInFile, gbc);
         * panel.add(txtInFile);
         *
         * gbc.gridx= 4;	gbl.setConstraints(btnBrowseIn, gbc);
         * panel.add(btnBrowseIn);/
         */


        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = 0;
        gbc.gridwidth = 1;
        gbl.setConstraints(shutdown, gbc);
        //panel.add(btnStart);

        //gbc.fill= 0; gbc.gridwidth= 1;

        panel.add(shutdown);

        /**
         * ******** Currently button will be disabled ********
         */
        //btnDownload.setEnabled(false);
        btnVerify.setEnabled(false);
        btnForward.setEnabled(false);
        /*
         *
         */

        getContentPane().add(panel);
        setSize(width, height);
        setResizable(false);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((d.width - width) / 2, (d.height - height) / 2);
        setIconImage(Toolkit.getDefaultToolkit().getImage("images/stop1.jpg"));
        setVisible(true);

        thread = new Thread(this, "Listen");
        thread.start();

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
		{

		}
	}

    //overriding abstract class method
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == btnBrowseIn || o == btnSource) {
            try {
                AudioClip ac = Applet.newAudioClip(new File("Sounds\\Browse File.wav").toURL());
                ac.play();
            } catch (Exception excep) {
            }

            if (sourcefile.showOpenDialog(this) == sourcefile.APPROVE_OPTION) {
                try {
                    spath = sourcefile.getSelectedFile().getPath();
                    txtInFile.setText(spath);
                    File f = new File(spath);
                    if (!f.exists()) {
                        try {
                            AudioClip ac = Applet.newAudioClip(new File("Sounds\\File does not exist.wav").toURL());
                            ac.play();
                        } catch (Exception ace) {
                        }
                        JOptionPane.showMessageDialog(null, "File " + spath + " does not exists", "Error!!!", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    txtInFile.setText(spath);
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(null, e, "Exception!!!", JOptionPane.ERROR_MESSAGE);
                }
            }

        }

        /*
         * Getting destination file
         */
        if (o == btnBrowseOut || o == btnDest) {
            try {
                AudioClip ac = Applet.newAudioClip(new File("Sounds\\Browse File.wav").toURL());
                ac.play();
            } catch (Exception excep) {
            }
            //destfile.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Files", fileExtension);
    		destfile.setFileFilter(filter);
            if (destfile.showSaveDialog(this) == destfile.APPROVE_OPTION) {
                dpath = destfile.getSelectedFile().getPath();
                if(!dpath.toLowerCase().endsWith("."+fileExtension))
				{
				    dpath = dpath + "."+fileExtension;
				}
                File f = new File(dpath);
                if (f.exists()) {
                    int value = JOptionPane.showConfirmDialog(null, "File " + dpath + " already exist\nDo you want to replace it?", "Confirm File Replace", JOptionPane.YES_NO_OPTION);

                    if (value == JOptionPane.YES_OPTION) {
                        txtOutFile.setText(dpath);
                    } else if (value == JOptionPane.NO_OPTION) {
                        return;
                    }
                }

                txtOutFile.setText(dpath);
            }
        }

        /*
         * Process to be done on start button
         */
        if (o == btnVerify || o == btnVerify1) {
            try {
                /*
                 * if(spath == null || dpath == null) {
                 * JOptionPane.showMessageDialog(null, "Please enter the
                 * filename", "Error!!!", JOptionPane.ERROR_MESSAGE); return; }
                 */
                 Socket controllerMACVerification = new Socket(Constants.CONTROLLER_IP, Constants.MAC_VERIFICATION_PORT);

                 /* A timeout of zero is interpreted as an infinite timeout.*/
				// controllerMACVerification.setSoTimeout(0);
				 /* make sure the connection is actually alive.*/
				// controllerMACVerification.setKeepAlive(true);

				 ObjectOutputStream outMACVerfication = new ObjectOutputStream(controllerMACVerification.getOutputStream());
                 outMACVerfication.writeObject(transactionID);

				 ObjectInputStream resultMACVerification = new ObjectInputStream(controllerMACVerification.getInputStream());
				 /*Receive secret key for this transaction*/
				 Object object = resultMACVerification.readObject();
				 SecretKey keyForMACCalculation=null;
				 if(object instanceof SecretKey)
				 {
				 	keyForMACCalculation = (SecretKey)object;
				 }

                 //outMACVerfication.writeObject(MessageDigestImplementation.getMessageDigest(filename));
                 outMACVerfication.writeObject(MACGeneration.getMAC(filename,keyForMACCalculation));

                 Object status = resultMACVerification.readObject();
                 System.out.println("Status : "+status);
                 boolean isValid=false;
                 if(status instanceof Boolean)
                 {
					 Boolean boolObject = (Boolean)status;
					 isValid = boolObject.booleanValue();
				 }

                if (isValid) {
                    JOptionPane.showMessageDialog(this, "Congrats! You got file safely", "Success", JOptionPane.INFORMATION_MESSAGE);
                    btnForward.setEnabled(true);//enables forward button
                    System.out.println("Congrats! You got file safely");
                    ReceiverFrame.jprgbar.setString(Constants.STATUS_VERIFIED_POSITIVE);
                    Object nextIP = resultMACVerification.readObject();
                    if(nextIP instanceof String)
                    {
						String temp = (String)nextIP;
						//InetAddress tempInet = InetAddress.getByName(temp);
						//nextNodeIP = tempInet.getHostName();
						temp = temp.split("/")[0];
						nextNodeIP = InetAddress.getByName(temp).getHostName();
						//String hostname = InetAddress.getByName(temp).toString().split("/")[1];
						System.out.println("Now file will be sent to "+ nextNodeIP);
						System.out.println("I am "+InetAddress.getLocalHost().getHostName());

						if(nextNodeIP.compareTo(InetAddress.getLocalHost().getHostName())==0)
						{
							btnForward.setText("Finish");
							nextNodeIP = null;
						}
						else
						{
							btnForward.setText(btnForward_name);
						}

					}

                } else {
                    JOptionPane.showMessageDialog(this, "Alert! Some one has attacked the data", "Alert", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Alert! Some one has attacked the data");
                    ReceiverFrame.jprgbar.setString(Constants.STATUS_VERIFIED_NEGATIVE);
                }

                controllerMACVerification.close();
                outMACVerfication.close();
                resultMACVerification.close();

                controllerMACVerification = null;
                outMACVerfication = null;
                resultMACVerification = null;
                status = null;


            } catch (Exception e1)
            {
                JOptionPane.showMessageDialog(this, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                updateControllerList(false);
                System.exit(0);
            }
        }

        /*
		* Process to be done on Forward button
		*/
		if (o == btnForward || o == btnForward1)
		{
            try
            {
				if(nextNodeIP != null)
				{
					System.out.println("Next node ip : "+nextNodeIP);
					Client.sendFile(filename,nextNodeIP,transactionID);
				}
				else
				{
					Client.sendSuccess(transactionID);
				}
			}
			catch(Exception eFwd)
			{
				JOptionPane.showMessageDialog(this, eFwd.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}

        /*
         * Process to be done on stop button
         */
        if (o == btnStop || o == btnStop1)
        {
			closeFrame();
        }
    }

    void makeButton(String name, final String plafName) {
        // add button to panel

        JMenuItem mi = new JMenuItem(name);
        look.add(mi);

        // set button action

        mi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                // button action: switch to the new look and feel
                try {
                    UIManager.setLookAndFeel(plafName);
                    SwingUtilities.updateComponentTreeUI(ReceiverFrame.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);

            /* A timeout of zero is interpreted as an infinite timeout.*/
			serverSocket.setSoTimeout(300000);

            while (true) {
                Socket s = serverSocket.accept();

                saveFile(s);
            }
        } catch (Exception e) {
        }
    }

    private String getExtension(String filename)
    {
		StringTokenizer st = new StringTokenizer(filename,".");
		String ext="";
		while(st.hasMoreTokens())
		{
			ext = st.nextToken();
		}
		return ext;
	}

    private void saveFile(Socket socket) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        FileOutputStream fos = null;
        byte[] buffer = new byte[BUFFER_SIZE];

        // 0. Read transaction ID
        Object o = ois.readObject();
        transactionID = (int)o;

        // 1. Read file name.
        o = ois.readObject();

        if (o instanceof String) {
            //String option[] = new String[]{"Download","Close"};
            if (ReceiverFrame.jprgbar == null) {
                System.out.println("Progress bar is null");
            } else {
                ReceiverFrame.jprgbar.setString(Constants.STATUS_INCOMING_MESSAGE);
                //ReceiverFrame.btnDownload.setEnabled(true);
            }
            /*
             * JOptionPane.showOptionDialog(null, Constants.INCOMING_MESSAGE,
             * "Message", JOptionPane.OK_CANCEL_OPTION,
             * JOptionPane.INFORMATION_MESSAGE, null, option, // this is the
             * array "default");
             */

            filename = o.toString();//here will be filename of file to be received
            String options[] = new String[]{"Download", "Reject"};
            btnVerify.setEnabled(false);
            btnForward.setEnabled(false);
            txtOutFile.setText("");
            ReceiverFrame.jprgbar.setString(Constants.STATUS_INCOMING_MESSAGE);
            int option = JOptionPane.showOptionDialog(this, "You have one incoming message \nWhat do you want to do?", "Message", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (option == JOptionPane.OK_OPTION)
            {
                oos.writeObject("SEND_FILE");
                //txtOutFile.setText(filename);
                // 2. Read file to the end.
                Integer bytesRead = 0;
                fileExtension = getExtension(filename);

				btnBrowseOut.doClick();
				filename = txtOutFile.getText();
				ReceiverFrame.jprgbar.setString(Constants.STATUS_MESSAGE_DOWNLOADING);
				fos = new FileOutputStream(filename);
                do {


                    o = ois.readObject();

                    if (!(o instanceof Integer)) {
                        throwException("Something is wrong");
                    }

                    bytesRead = (Integer) o;

                    o = ois.readObject();

                    if (!(o instanceof byte[])) {
                        throwException("Something is wrong");
                    }

                    buffer = (byte[]) o;

                    // 3. Write data to output file.
                    fos.write(buffer, 0, bytesRead);
                } while (bytesRead == BUFFER_SIZE);

                /*
                 * Read digest
                 */
                o = ois.readObject();
                if (!(o instanceof Integer)) {
                    throwException("Something is wrong");
                }
                bytesRead = (Integer) o;
                o = ois.readObject();
                if (!(o instanceof byte[])) {
                    throwException("Something is wrong");
                }
                buffer = (byte[]) o;

                btnVerify.setEnabled(true);
                receivedMessageDigest = buffer;

                System.out.println("Digest : " + buffer);
                /*
                 * End reading digest
                 */

            } else {
                oos.writeObject("REQUEST_REJECTED");
            }

            //if user selects download then receiver will reply to drawframe as either poitive or negative
            //if positive then read point no 2 code
            //if negative then it will read point no 4

        } else {
            throwException("Something is wrong");
        }




        jprgbar.setString(Constants.STATUS_WAITING_VERIFICATION);



        /*
         * if(MessageDigest.isEqual(buffer,MessageDigestImplementation.getMessageDigest(filename)))
         * { System.out.println("Congrats! You got file safely"); } else {
         * System.out.println("Alert! Some one has attacked the data"); }
         */
        //4. close all sockets {
        fos.close();

        ois.close();

        oos.close();
        //}
    }

    public void updateControllerList(boolean addMe)
    {
		 try
		 {
			 Socket socket = new Socket(Constants.CONTROLLER_IP, Constants.NODE_REGISTRATION_PORT);

			 /* A timeout of zero is interpreted as an infinite timeout.*/
			 //socket.setSoTimeout(0);
			 /* make sure the connection is actually alive.*/
			 //socket.setKeepAlive(true);

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

//			 InetAddress localhost = InetAddress.getLocalHost();
//			 byte[] ip = localhost.getAddress();
//			 oos.writeObject(new Integer(""+ip[3]));

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

    public static void throwException(String message) throws Exception {
        throw new Exception(message);
    }

    public static void main(String[] args) {
        ReceiverFrame rf = new ReceiverFrame();
        rf.updateControllerList(true);
    }
}
