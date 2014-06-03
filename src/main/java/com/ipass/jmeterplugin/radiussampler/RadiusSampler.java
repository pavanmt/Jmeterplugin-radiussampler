package com.ipass.jmeterplugin.radiussampler;

import java.util.Random;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.TestElementProperty;
import org.tinyradius.attribute.RadiusAttribute;
import org.tinyradius.packet.AccessRequest;
import org.tinyradius.packet.AccountingRequest;
import org.tinyradius.packet.RadiusPacket;
import org.tinyradius.util.RadiusClient;
import org.tinyradius.util.RadiusUtil;

import com.ipass.radius.aaasampler.Attribute;
import com.ipass.radius.aaasampler.IpassDictionary;

public class RadiusSampler extends AbstractSampler
{

	private static Random random = new Random();

	public static void main(String[] args) {
		String str=frameSessionId("pavan@ipass.com");
		System.out.println(str);
	}

	private static String genSessionId(int min,int max){
		String val="";
		for(int i=0;i<8;i++){
			int s=random.nextInt((max - min) + 1) + min;
			char c = (char)s;
			val+=c;

		}

		return val;
	}


	private static String frameSessionId(String username){
		String str=genSessionId(97,120);
		return "0U"+str+"/"+username;
	}

	public SampleResult sample(Entry arg0)
	{

		String userName = getUserName();
		String password = getPassword();
		String serverIp = getServerIp();
		String sharedSecret = getSharedSecret();
		int authPort = getAuthPort();
		int acctPort = getAcctPort();

		int retryCount = getRetryCount();
		int timeout = getSocketTimeout();

		SampleResult res = new SampleResult();
		res.setSampleLabel(getName());
		if(authPort !=0 && acctPort !=0 ){
			res.setSamplerData("Host: " + getServerIp() + " Auth Port: " + getAuthPort() + " Acct Port: "+getAcctPort());
		}



		CollectionProperty collectionProperty=getAttributesManager().getAttributes();

		AddAttributes add = new AddAttributes();

		if((userName==null || userName.length()<=0 ) && collectionProperty!=null){
			userName = add.getRequiredAttribute(collectionProperty,"user-name");
		}

		if((password==null || password.length()<=0) && collectionProperty!=null){
			password = add.getRequiredAttribute(collectionProperty,"user-password");
		}		

		res.sampleStart();

		//collectionProperty = add.removeAttributes(collectionProperty);

		if ( (userName!=null && userName.length()>0 && password!=null && password.length()>0 ) && (serverIp != null) && (serverIp.length() > 0) && (authPort!=0 || acctPort !=0) && (sharedSecret!=null && sharedSecret.length()>0))
		{

			if(System.getenv("GEN_SES_ID")!=null && System.getenv("GEN_SES_ID").toLowerCase().equals("true"))
				userName = frameSessionId(userName);

			try
			{
				RadiusClient rcClient = null;
				AccessRequest accessReq = null;
				AccountingRequest acctReq = null;
				RadiusPacket authRadiusPacket = null;
				RadiusPacket acctStartRadiusPacket = null;
				RadiusPacket acctStopRadiusPacket = null;
				boolean reqAuthNAcct = false;
				boolean authRAcct = true;


				AddAttributes addAttributes = new AddAttributes();

				String reqType = getRequestType();

				if(reqType.equalsIgnoreCase("both")){
					reqAuthNAcct=true;
					//Auth Records
					rcClient = new RadiusClient(serverIp,sharedSecret);
					accessReq = new AccessRequest(userName, password);
					rcClient.setAuthPort(authPort);
					if(timeout>0)
						rcClient.setSocketTimeout(timeout);

					if(retryCount>0)
						rcClient.setRetryCount(retryCount);


					if(collectionProperty!=null)
						accessReq=addAttributes.addAuthRadiusAttribute(accessReq, collectionProperty);

					authRadiusPacket=rcClient.authenticate(accessReq);


					//Start Records
					rcClient = new RadiusClient(serverIp,sharedSecret);
					acctReq = new AccountingRequest(userName, AccountingRequest.ACCT_STATUS_TYPE_START);
					rcClient.setAcctPort(acctPort);
					if(timeout>0)
						rcClient.setSocketTimeout(timeout);

					if(retryCount>0)


						if(collectionProperty!=null)
							acctReq=addAttributes.addAcctRadiusAttribute(acctReq, collectionProperty);
					acctStartRadiusPacket=rcClient.account(acctReq);

					//Stop records
					rcClient = new RadiusClient(serverIp,sharedSecret);
					acctReq = new AccountingRequest(userName, AccountingRequest.ACCT_STATUS_TYPE_STOP);
					rcClient.setAcctPort(acctPort);
					if(timeout>0)
						rcClient.setSocketTimeout(timeout);

					if(retryCount>0)
						rcClient.setRetryCount(retryCount);
					/*if(acctRadAttr!=null)
						accessReq.addAttribute(getAttributes());*/
					if(collectionProperty!=null)
						acctReq=addAttributes.addAcctRadiusAttribute(acctReq, collectionProperty);
					acctStopRadiusPacket=rcClient.account(acctReq);


				}else if(reqType.equalsIgnoreCase("auth")){
					//Auth Records
					rcClient = new RadiusClient(serverIp,sharedSecret);
					accessReq = new AccessRequest(userName, password);
					rcClient.setAuthPort(authPort);
					if(collectionProperty!=null)
						accessReq=addAttributes.addAuthRadiusAttribute(accessReq, collectionProperty);
					/*RadiusAttribute radAttr = getAttributes();
					if(radAttr!=null)
						accessReq.addAttribute(getAttributes());*/
					if(timeout>0)
						rcClient.setSocketTimeout(timeout);

					if(retryCount>0)
						rcClient.setRetryCount(retryCount);
					authRadiusPacket=rcClient.authenticate(accessReq);
				}else if(reqType.equalsIgnoreCase("acct")){
					authRAcct=false;
					rcClient = new RadiusClient(serverIp,sharedSecret);
					acctReq = new AccountingRequest(userName, AccountingRequest.ACCT_STATUS_TYPE_START);
					rcClient.setAcctPort(acctPort);
					if(collectionProperty!=null)
						acctReq=addAttributes.addAcctRadiusAttribute(acctReq, collectionProperty);
					/*RadiusAttribute acctRadAttr = getAttributes();
					if(acctRadAttr!=null)
						accessReq.addAttribute(getAttributes());*/
					if(timeout>0)
						rcClient.setSocketTimeout(timeout);

					if(retryCount>0)
						rcClient.setRetryCount(retryCount);
					acctStartRadiusPacket=rcClient.account(acctReq);

					//Stop records
					rcClient = new RadiusClient(serverIp,sharedSecret);
					acctReq = new AccountingRequest(userName, AccountingRequest.ACCT_STATUS_TYPE_STOP);
					rcClient.setAcctPort(acctPort);
					/*if(acctRadAttr!=null)
						accessReq.addAttribute(getAttributes());*/
					if(timeout>0)
						rcClient.setSocketTimeout(timeout);

					if(retryCount>0)
						rcClient.setRetryCount(retryCount);
					if(collectionProperty!=null)
						acctReq=addAttributes.addAcctRadiusAttribute(acctReq, collectionProperty);
					acctStopRadiusPacket=rcClient.account(acctReq);
				}else{
					throw new IllegalArgumentException("Radius packet type is only auth,acct or both. Invalid request Type"+reqType);
				}


				if(reqAuthNAcct){

					if(authRadiusPacket!= null && acctStartRadiusPacket!=null && acctStopRadiusPacket!=null){
						res.setSuccessful(true);
						res.setResponseData(authRadiusPacket.getPacketTypeName().getBytes());
						res.setDataType("text");
						res.setResponseCodeOK();
						res.setResponseMessage(authRadiusPacket.getPacketTypeName());
					}
					else {
						res.setSuccessful(false);
						if(authRadiusPacket==null)
							res.setResponseMessage("Server Dropped the auth request ");
						if(acctStartRadiusPacket==null)
							res.setResponseMessage("Server Dropped the Start request ");
						if(acctStopRadiusPacket==null)
							res.setResponseMessage("Server Dropped the Stop request ");
						res.setResponseCode("500");
					}


				}else{

					if(authRAcct){

						if(authRadiusPacket!=null){
							res.setSuccessful(true);
							res.setResponseData(authRadiusPacket.getPacketTypeName().getBytes());
							res.setDataType("text");
							res.setResponseCodeOK();
							res.setResponseMessage(authRadiusPacket.getPacketTypeName());
						}else{
							res.setSuccessful(false);
							if(authRadiusPacket==null)
								res.setResponseMessage("Server Dropped the auth request ");
							res.setResponseCode("500");
						}

					}else{

						if(acctStartRadiusPacket!=null && acctStopRadiusPacket!=null){
							res.setSuccessful(true);
							res.setResponseData(acctStartRadiusPacket.getPacketTypeName().getBytes());
							res.setDataType("text");
							res.setResponseCodeOK();
							res.setResponseMessage(acctStartRadiusPacket.getPacketTypeName());
						}else{
							res.setSuccessful(false);
							if(acctStartRadiusPacket==null)
								res.setResponseMessage("Server Dropped the start request ");
							if(acctStopRadiusPacket==null)
								res.setResponseMessage("Server Dropped the stop request ");
							res.setResponseCode("500");
						}

					}

				}

			}
			catch (Throwable excep)
			{
				excep.printStackTrace();
				res.setSuccessful(false);
				res.setResponseMessage(excep.getMessage());
				res.setResponseCode("500");
			}
			finally {

				res.sampleEnd();

			}
		}else{
			throw new NullPointerException("Some of the parameters like serverIp, port, shared secret cannot be null");
		}
		return res;
	}



	public void setAttributesManager(RadiusAttributesManager value)
	{
		setProperty(new TestElementProperty(RadiusSamplerElements.RADIUS_ATTRIBUTES, value));
	}

	public RadiusAttributesManager getAttributesManager()
	{
		return (RadiusAttributesManager)getProperty(RadiusSamplerElements.RADIUS_ATTRIBUTES).getObjectValue();
	}

	public void setServerIp(String serverIp)
	{
		setProperty(RadiusSamplerElements.SERVER_IP, serverIp);
	}

	public String getServerIp()
	{
		return getPropertyAsString(RadiusSamplerElements.SERVER_IP);
	}



	public void setSharedSecret(String sharedSecret)
	{
		setProperty(RadiusSamplerElements.SHARED_SECRET, sharedSecret);
	}

	public String getSharedSecret()
	{
		return getPropertyAsString(RadiusSamplerElements.SHARED_SECRET);
	}

	public int getRetryCount(){
		return getPropertyAsInt(RadiusSamplerElements.RADIUS_RETRY);
	}

	public void setRetryCount(int retryCount){
		setProperty(RadiusSamplerElements.RADIUS_RETRY,retryCount);
	}

	public int getSocketTimeout(){
		return getPropertyAsInt(RadiusSamplerElements.SOCKET_TIMEOUT);
	}

	public void setSocketTimeout(int socketTimeout){
		setProperty(RadiusSamplerElements.SOCKET_TIMEOUT,socketTimeout);
	}

	public void setAuthPort(int authPort){
		setProperty(RadiusSamplerElements.AUTH_PORT,authPort);
	}

	public void setAcctPort(int acctPort){
		setProperty(RadiusSamplerElements.ACCT_PORT,acctPort);
	}

	public int getAuthPort(){
		return getPropertyAsInt(RadiusSamplerElements.AUTH_PORT);
	}

	public int getAcctPort()
	{
		return getPropertyAsInt(RadiusSamplerElements.ACCT_PORT);
	}

	public void setRequestType(String requestType)
	{
		setProperty(RadiusSamplerElements.REQUEST_TYPE, requestType);
	}

	public String getRequestType()
	{
		return getPropertyAsString(RadiusSamplerElements.REQUEST_TYPE);
	}

	public void setUserName(String userName)
	{
		setProperty(RadiusSamplerElements.USER_NAME, userName);
	}

	public String getUserName()
	{
		return getPropertyAsString(RadiusSamplerElements.USER_NAME);
	}
	public void setPassword(String password)
	{
		setProperty(RadiusSamplerElements.PASSWORD, password);
	}

	public String getPassword()
	{
		return getPropertyAsString(RadiusSamplerElements.PASSWORD);
	}

}