public class Constants
{
	/*
		*This port are selected on random basis
		*There is no specific logic behind it
	*/
	final public static int IP_SENDER_PORT = 2001;
	final public static int MAC_VERIFICATION_PORT = 2002;
	final public static int NODE_REGISTRATION_PORT = 2003;

	final public static int ERROR_RANDOM_KEY = -1;

	final public static String CONTROLLER_IP = "localhost";

	/*Request code(s) for Node Registration/ De-registration*/
	final public static String REQUEST_ADD_NODE = "JOIN_ME_TO_NODE_LIST";
	final public static String REQUEST_REMOVE_NODE = "REMOVE_ME_FROM_NODE_LIST";
	final public static String GET_NODE_LIST = "SEND_ME_NODE_LIST";

	final public static String TRANSFER_REQUEST = "TRANSFER_REQUEST";
	final public static String FORWARD_REQUEST = "FORWARD_REQUEST";
	final public static String SUCCESS_MESSAGE = "SUCCESS_MESSAGE";

	/*
		*Messages for changing status while transfering file
	*/
	final public static String INCOMING_MESSAGE = "You have one incoming message";
	final public static String STATUS_INCOMING_MESSAGE = "You have one incoming message";
	final public static String STATUS_LISTENING = "Listening for incoming message";
	final public static String STATUS_WAITING_VERIFICATION = "Waiting for verification";
	final public static String STATUS_VERIFIED_POSITIVE = "Verified. Safe to forward";
	final public static String STATUS_VERIFIED_NEGATIVE = "Corrupted. Data has been attacked";
	final public static String STATUS_MESSAGE_DOWNLOADING = "Downloading... Keep Patience";
}