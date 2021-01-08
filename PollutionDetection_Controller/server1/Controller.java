

import java.net.*;
import java.io.*;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.crypto.SecretKey;
import javax.swing.JTextArea;

/**
 *
 * @author Chetan
 */
public class Controller {

    public static boolean flagStop=false;
    public static void main(String args[]) {
        final ArrayList<TransactionDetails> transactionList = new ArrayList<>();
        final ArrayList<String> registeredNodesList = new ArrayList<>();
        final JTextArea transactionLog = new JTextArea(" ");
        Controller con = new Controller(transactionLog);
        //jTextArea.setText("-----------------------------------------" + "\n");
        Thread threadNodeRegister;
        threadNodeRegister = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {


                        System.out.println("Listening for Node Registration request");
                        jTextArea.append("\n" + "Listening for Node Registration request" + "\n");

                        Socket serverNodeRegistration;
                        ObjectInputStream objectInputSender;
                        ObjectOutputStream objectOutputSender;
                        ServerSocket serverSocketNodeRegistration=null;



                        //if(!serverSocketNodeRegistration.isBound())
                        serverSocketNodeRegistration = new ServerSocket(Constants.NODE_REGISTRATION_PORT);
                        //try (

                         //   )
                            {
                            //serverSocketNodeRegistration.setSoTimeout(0);
                            serverNodeRegistration = serverSocketNodeRegistration.accept();
                           // serverNodeRegistration.setSoTimeout(0);
                            //serverNodeRegistration.setKeepAlive(true);
                            objectInputSender = new ObjectInputStream(serverNodeRegistration.getInputStream());
                            objectOutputSender = new ObjectOutputStream(serverNodeRegistration.getOutputStream());
                            Object objectIP = objectInputSender.readObject();
                            String requestCode = null;
                            boolean addNode = false;
                            InetAddress ipNode = serverNodeRegistration.getInetAddress();
                            String nodeIP = ipNode.getHostName();
                            if (objectIP instanceof String) {
                                requestCode = objectIP.toString();

                                if (requestCode.compareTo(Constants.REQUEST_ADD_NODE) == 0) {
                                    int index = -1;
                                    /*Object objectIndex = objectInputSender.readObject();
                                     if(objectIndex instanceof Integer)
                                     {
                                     Integer integerIndex = (Integer)objectIndex;

                                     index = integerIndex.intValue();
                                     }
                                     if(index < 0 || index > registeredNodesList.size())
                                     {
                                     //addUniqueElement(registeredNodesList,nodeIP);
                                     if(registeredNodesList.indexOf(nodeIP)==-1)
                                     {
                                     registeredNodesList.add(nodeIP);
                                     System.out.println("Added : "+nodeIP);
                                     jTextArea.append("Added : "+nodeIP+"\n");
                                     }
                                     else
                                     {
                                     System.out.println("Already exists : "+nodeIP);
                                     jTextArea.append("Already exists : "+nodeIP+"\n");
                                     }

                                     }
                                     else
                                     {
                                     //addUniqueElement(registeredNodesList,nodeIP,index);
                                     if(registeredNodesList.indexOf(nodeIP)==-1)
                                     {
                                     registeredNodesList.add(index,nodeIP);
                                     System.out.println("Added : "+nodeIP+" at index "+index);
                                     jTextArea.append("Added : "+nodeIP+" at index "+index+"\n");
                                     }
                                     else
                                     {
                                     System.out.println("Already exists : "+nodeIP);
                                     jTextArea.append("Already exists : "+nodeIP+"\n");
                                     }
                                     }*/
                                    if (registeredNodesList.indexOf(nodeIP) == -1) {
                                        registeredNodesList.add(nodeIP);
                                        System.out.println("Added : " + nodeIP);
                                        jTextArea.append("Added : " + nodeIP + "\n");
                                    } else {
                                        System.out.println("Already exists : " + nodeIP);
                                        jTextArea.append("Already exists : " + nodeIP + "\n");
                                    }


                                    for (int i = 0; i < registeredNodesList.size()-1; i++) //System.out.println("Path :"+registeredNodesList.get(i));
                                    {
                                        jTextArea.append(registeredNodesList.get(i) + "-->");
                                    }
                                    jTextArea.append(registeredNodesList.get(registeredNodesList.size()-1));

                                } else if (requestCode.compareTo(Constants.REQUEST_REMOVE_NODE) == 0) {
                                    registeredNodesList.remove(nodeIP);
                                    System.out.println("Removed : " + nodeIP);
                                    jTextArea.append("Removed : " + nodeIP + "\n");
                                } else if (requestCode.compareTo(Constants.GET_NODE_LIST) == 0) {
                                    objectOutputSender.writeObject(registeredNodesList);
                                    System.out.println("List sent. " + registeredNodesList.size() + " Elements sent");
                                    jTextArea.append("List sent. " + registeredNodesList.size() + " Elements sent" + "\n");
                                }
                            }
                        }

                        serverNodeRegistration.close();
                        serverNodeRegistration = null;

                        objectInputSender.close();
                        objectInputSender = null;

                        objectOutputSender.close();
                        objectOutputSender = null;
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                        continue;
                    }
                }

            }
        });
        threadNodeRegister.start();

        Thread threadListen;
        threadListen = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {

                        System.out.println("Listening for Data Transfer request");
                        jTextArea.append("\n" + "Listening for Data Transfer request" + "\n");

                        Socket serverListeningIPRequest;
                        ObjectInputStream objectInputSender;
                        try (ServerSocket serverSocketListeningIPRequest = new ServerSocket(Constants.IP_SENDER_PORT)) {
                            //serverSocketListeningIPRequest.setSoTimeout(0);
                            serverListeningIPRequest = serverSocketListeningIPRequest.accept();
                            //serverListeningIPRequest.setSoTimeout(0);
                            objectInputSender = new ObjectInputStream(serverListeningIPRequest.getInputStream());
                            ObjectOutputStream objectOutputSender = new ObjectOutputStream(serverListeningIPRequest.getOutputStream());
                            Object object = objectInputSender.readObject();
                            if (object instanceof String) {
                                String requestTransactionCode = object.toString();

                                if (requestTransactionCode.compareTo(Constants.TRANSFER_REQUEST) == 0) {
                                    System.out.println("Received Transfer Request");
                                    jTextArea.append("Received Transfer Request" + "\n");
                                    object = objectInputSender.readObject();
                                    TransactionDetails tranDetail = new TransactionDetails();
                                    String receiverIP = null;
                                    if (object instanceof String) {
                                        receiverIP = object.toString();
                                        System.out.println("Ultimate receiver : " + receiverIP);
                                        jTextArea.append("Ultimate receiver : " + receiverIP + "\n");
                                    }
                                    tranDetail.receiverIP = InetAddress.getByName(receiverIP);

                                    InetAddress currentSender = serverListeningIPRequest.getInetAddress();
                                    String strCurrentSender = currentSender.getHostName();
                                    System.out.println("Original sender : " + strCurrentSender);
                                    jTextArea.append("Original sender : " + strCurrentSender + "\n");
                                    InetAddress ultimateReceiver = tranDetail.receiverIP;
                                    String intermediateIP = ultimateReceiver.getHostName();
                                    if (!currentSender.equals(ultimateReceiver)) {
                                        int ipIndex = getIndex(registeredNodesList, strCurrentSender);

                                        System.out.println("ipIndex : " + ipIndex);
                                        jTextArea.append("ipIndex : " + ipIndex + "\n");

                                        if (ipIndex == -1) {
                                            //End or error
                                            jTextArea.append("End or error" + "\n");
                                            System.out.println("End or error");
                                        } else if (ipIndex == registeredNodesList.size()) {
                                            ipIndex = 0;
                                            intermediateIP = registeredNodesList.get(ipIndex);
                                        } else {
                                            ipIndex = (ipIndex + 1) % registeredNodesList.size();
                                            intermediateIP = registeredNodesList.get(ipIndex);
                                        }
                                    }

                                    //if(isIPReachable(intermediateIP))
                                    //if(true)
                                    if (ping(intermediateIP)) {
                                        objectOutputSender.writeObject(intermediateIP);
                                        System.out.println("Sending key to sender");
                                        jTextArea.append("Sending key to sender" + "\n");

                                        int randomKey = new Random().nextInt(2147483647);/*assures random key is generated between 0 to 2147483647*/
                                        tranDetail.transcationID = randomKey;
                                        //tranDetail.receiverIP = InetAddress.getByName(receiverIP);
                                        objectOutputSender.writeObject(tranDetail.transcationID);

                                        /*generate secret key for this transaction*/
                                        tranDetail.keyForMac = GenerateKey.getSecretKey(Constants.MAC_ALGORITHM);
                                        objectOutputSender.writeObject(tranDetail.keyForMac);

                                        object = objectInputSender.readObject();/*receives mac for this transaction*/
                                        byte buffer[] = null;
                                        if (object instanceof byte[]) {
                                            buffer = (byte[]) object;
                                        }
                                        tranDetail.senderIP = serverListeningIPRequest.getInetAddress();
                                        tranDetail.MAC = buffer;
                                        //tranDetail.receiverIP = InetAddress.getByName(receiverIP);
                                        transactionList.add(tranDetail);
                                        System.out.println("Transaction created...");
                                        jTextArea.append("Transaction created..." + "\n");
                                    } else {
                                        System.out.println(receiverIP + " not reachable");
                                        jTextArea.append(receiverIP + " not reachable" + "\n");
                                        objectOutputSender.writeObject(Constants.ERROR_RANDOM_KEY);
                                    }
                                } else if (requestTransactionCode.compareTo(Constants.FORWARD_REQUEST) == 0) {
                                    Object data = objectInputSender.readObject();
                                    int transID = (int) data;

                                    int index = getIndex(transactionList, transID);

                                    objectOutputSender.writeObject(transactionList.get(index).keyForMac);
                                } else if (requestTransactionCode.compareTo(Constants.SUCCESS_MESSAGE) == 0) {
                                    Object data = objectInputSender.readObject();
                                    int transID = (int) data;
                                    int index = getIndex(transactionList, transID);

                                    System.out.println("Transaction with id " + transID + " completed successfully");
                                    jTextArea.append("Transaction with id " + transID + " completed successfully" + "\n");

                                    System.out.println("Transaction Details");
                                    jTextArea.append("Transaction Details" + "\n");

                                    System.out.println("============================================================");
                                    jTextArea.append("============================================================" + "\n");

                                    System.out.println("Original Sender : " + transactionList.get(index).senderIP);
                                    jTextArea.append("Original Sender : " + transactionList.get(index).senderIP + "\n");

                                    System.out.println("Ultimate Receiver : " + transactionList.get(index).receiverIP);
                                    jTextArea.append("Ultimate Receiver : " + transactionList.get(index).receiverIP + "\n");

                                    System.out.println("MAC : " + transactionList.get(index).keyForMac);
                                    jTextArea.append("MAC : " + transactionList.get(index).keyForMac + "\n");

                                    System.out.println("============================================================");
                                    jTextArea.append("============================================================" + "\n");
                                }
                            }
                        }

                        serverListeningIPRequest.close();
                        serverListeningIPRequest = null;

                        objectInputSender.close();
                        objectInputSender = null;
                    } catch (BindException be) {
                        JOptionPane.showMessageDialog(null, be.getMessage() + "\nServer is already running", "Exception", JOptionPane.ERROR_MESSAGE);
                        continue;
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }
        });
        threadListen.start();

        Thread threadMACVerification;
        threadMACVerification = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {

                        System.out.println("Listening for File name");
                        jTextArea.append("\n" + "Listening for File name" + "\n");
                        Socket serverMACVerification;
                        ObjectInputStream inSender;
                        ObjectOutputStream outSender;
                        try (ServerSocket serverSocketMACVerification = new ServerSocket(Constants.MAC_VERIFICATION_PORT)) {
                            serverMACVerification = serverSocketMACVerification.accept();
                            inSender = new ObjectInputStream(serverMACVerification.getInputStream());
                            outSender = new ObjectOutputStream(serverMACVerification.getOutputStream());
                            Object data = inSender.readObject();
                            int transID = (int) data;
                            int index = getIndex(transactionList, transID);
                            outSender.writeObject(transactionList.get(index).keyForMac);
                            data = inSender.readObject();
                            if (index != -1) {
                                if (data instanceof byte[]) {
                                    byte receivedMAC[] = (byte[]) data;
                                    System.out.println("Length : " + transactionList.size() + "Index ; " + index);
                                    jTextArea.append("Length : " + transactionList.size() + "Index ; " + index + "\n");

                                    if (java.security.MessageDigest.isEqual(transactionList.get(index).MAC, receivedMAC)) {
                                        System.out.println("MAC is correct");
                                        jTextArea.append("MAC is correct" + "\n");

                                        outSender.writeObject(true);
                                        InetAddress currentReceiver = serverMACVerification.getInetAddress();
                                        String strCurrentReceiver = currentReceiver.getHostName();
                                        InetAddress ultimateReceiver = transactionList.get(index).receiverIP;

                                        if (currentReceiver.getHostName().compareTo(ultimateReceiver.getHostName()) != 0) {
                                            int ipIndex = getIndex(registeredNodesList, strCurrentReceiver);
                                            if (ipIndex == -1) {
                                                //End or error
                                                System.out.println("End or error");
                                                jTextArea.append("End or error" + "\n");
                                            } else if (ipIndex == registeredNodesList.size()) {
                                                ipIndex = 0;
                                                outSender.writeObject(registeredNodesList.get(ipIndex));
                                            } else {
                                                ipIndex++;
                                                outSender.writeObject(registeredNodesList.get(ipIndex));
                                            }

                                        } else {
                                            outSender.writeObject(ultimateReceiver.toString());
                                        }
                                    } else {
                                        System.out.println("MAC is incorrect");
                                        jTextArea.append("MAC is incorrect" + "\n");


                                        int value = JOptionPane.showConfirmDialog(null, "Attacker IP:\nDo you want to remove the attacker from the network?", "Attack Detected", JOptionPane.YES_NO_OPTION);

                                        if (value == JOptionPane.YES_OPTION) {


                                        }

                                        outSender.writeObject(false);
                                    }
                                } else {
                                    System.out.println("Error: MAC is incorrect");
                                    jTextArea.append("Error: MAC is incorrect" + "\n");
                                    outSender.writeObject(false);
                                }
                            } else {
                                System.out.println("Transaction yet not ready. Please try again later");
                                jTextArea.append("Transaction yet not ready. Please try again later" + "\n");
                                outSender.writeObject(false);
                            }
                        }

                        serverMACVerification.close();
                        serverMACVerification = null;

                        inSender.close();
                        inSender = null;

                        outSender.close();
                        outSender = null;
                    } catch (BindException be) {
                        JOptionPane.showMessageDialog(null, be.getMessage() + "\nServer is already running", "Exception", JOptionPane.ERROR_MESSAGE);
                        continue;
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }
        });
        threadMACVerification.start();

        //}
    }

    private static void addUniqueElement(ArrayList<String> list, String element) {
        if (list.indexOf(element) != -1) {
            list.add(element);
        }
    }

    private static void addUniqueElement(ArrayList<String> list, String element, int index) {
        if (list.indexOf(element) != -1) {
            list.add(index, element);
        }
    }

    private static int getIndex(ArrayList<String> list, String ipAddress) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).compareTo(ipAddress) == 0) {
                return i;
            }
        }
        return -1;
    }

    private static int getIndex(ArrayList<TransactionDetails> list, int tranID) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).transcationID == tranID) {
                return i;
            }
        }
        return -1;
    }

    public static boolean ping(String host) {
        boolean isReachable = false;
        try {
            Process proc = new ProcessBuilder("ping", host).start();

            int exitValue = proc.waitFor();
            System.out.println("Exit Value:" + exitValue);
            if (exitValue == 0) {
                isReachable = true;
            }

        } catch (IOException e1) {
            System.out.println(e1.getMessage());
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return isReachable;
    }

    private static boolean isIPReachable(String ipAddress) {
        try {
            InetAddress ip = InetAddress.getByName(ipAddress);
            return ip.isReachable(3000);
        } catch (Exception e) {
            return false;
        }
    }
    public static JTextArea jTextArea;

    public Controller(JTextArea transactionLog) {
        jTextArea = transactionLog;
        //transactionLog.setText("Test");//Testing..
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

class TransactionDetails {

    public int transcationID;
    public InetAddress senderIP;
    public InetAddress receiverIP;
    public SecretKey keyForMac;
    public byte[] MAC;
}
