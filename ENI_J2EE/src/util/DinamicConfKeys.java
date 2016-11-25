package util;

import com.sap.aii.mapping.api.DynamicConfigurationKey;

public interface DinamicConfKeys 
{
	public static final DynamicConfigurationKey KEY_FILENAME = DynamicConfigurationKey.create("http://sap.com/xi/XI/System/File","FileName" );
	public static final DynamicConfigurationKey KEY_CONVERSATION_ID = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System", "ConversationId" );  
	public static final DynamicConfigurationKey KEY_INTERFACE = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System", "Interface" );  
	public static final DynamicConfigurationKey KEY_INTERFACE_NAMESPACE = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System", "InterfaceNamespace" );  
	public static final DynamicConfigurationKey KEY_MESSAGE_ID = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System", "MessageId" );  
	public static final DynamicConfigurationKey KEY_PROCESSING_MODE = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System", "ProcessingMode" );  
	public static final DynamicConfigurationKey KEY_QUALITY_OF_SERVICE = DynamicConfigurationKey.create("http://sap.com/xi/XI/System", "QualityOfService" );  
	public static final DynamicConfigurationKey KEY_QUEUE_ID = DynamicConfigurationKey.create("http://sap.com/xi/XI/System", "QueueId" );  
	public static final DynamicConfigurationKey KEY_RECEIVER_SERVICE = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System", "ReceiverService"	);  
	public static final DynamicConfigurationKey KEY_REF_TO_MESSAGE_ID = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System", "RefToMessageId" );  
	public static final DynamicConfigurationKey KEY_SENDER_SERVICE = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System", "SenderService" );  
	public static final DynamicConfigurationKey KEY_TIME_SENT = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System", "TimeSent" ); 
	public static final DynamicConfigurationKey KEY_DIRECTORY = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System/File", "Directory"	);  
	public static final DynamicConfigurationKey KEY_FILE_ENCODING = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System/File", "FileEncoding"	);  
	public static final DynamicConfigurationKey KEY_FILE_TYPE = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System/File", "FileType" );  
	public static final DynamicConfigurationKey KEY_FILE_SIZE = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System/File", "SourceFileSize" );  
	public static final DynamicConfigurationKey KEY_FTP_HOST = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System/File", "SourceFTPHost" );  
	public static final DynamicConfigurationKey KEY_SOURCE_FILE_TIME_STAMP = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System/File", "SourceFileTimestamp");  
	public static final DynamicConfigurationKey KEY_HTTP_DEST = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System/HTTP", "HTTPDest");  
	public static final DynamicConfigurationKey KEY_TARGET_URL_HTTP = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System/HTTP", "TargetURL");  
	public static final DynamicConfigurationKey KEY_S_HEADER_CC = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System/Mail", "SHeaderCC");  
	public static final DynamicConfigurationKey KEY_S_HEADER_FROM = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System/Mail", "SHeaderFROM");  
	public static final DynamicConfigurationKey KEY_S_HEADER_SUBJECT = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System/Mail", "SHeaderSUBJECT"	);  
	public static final DynamicConfigurationKey KEY_RFC_DESTINATION = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System/RFC", "RfcDestination" );  
	public static final DynamicConfigurationKey KEY_REMOTE_HOST = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System/SOAP" ,"SRemoteHost"	);  
	public static final DynamicConfigurationKey KEY_REMOTE_USER = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System/SOAP", "SRemoteUser" );  
	public static final DynamicConfigurationKey KEY_AUTH_KEY = DynamicConfigurationKey.create( "http://sap.com/xi/XI/System/SOAP", "TAuthKey");  
}
