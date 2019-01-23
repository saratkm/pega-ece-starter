package dataProviders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import enums.DriverType;
import enums.EnvironmentType;

public class ConfigFileReader {
	private Properties properties;
	private final String propertyFilePath= "configs//Configuration.properties";
	
	public ConfigFileReader(){
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(propertyFilePath));
			properties = new Properties();
			try { properties.load(reader); }
			catch (IOException e) { e.printStackTrace(); }
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Properties file not found at path : " + propertyFilePath);
		}finally {
			try { if(reader != null) reader.close(); }
			catch (IOException ignore) {}
		}		
	}
	
	public String getDriverPath(){
		String driverPath = properties.getProperty("driverPath");
		if(driverPath!= null) return driverPath;
		else throw new RuntimeException("Driver Path not specified in the Configuration.properties file for the Key:driverPath");		
	}
	
	public long getImplicitlyWait() {		
		String implicitlyWait = properties.getProperty("implicitlyWait");
		if(implicitlyWait != null) {
			try{
				return Long.parseLong(implicitlyWait);
			}catch(NumberFormatException e) {
				throw new RuntimeException("Not able to parse value : " + implicitlyWait + " in to Long");
			}
		}
		return 30;		
	}
	
	public String getApplicationUrl() {
		String url = properties.getProperty("url");
		if(url != null) return url;
		else throw new RuntimeException("Application Url not specified in the Configuration.properties file for the Key:url");
	}
	
	
	public String getServiceUrl(String ScnString) {
		String ep = properties.getProperty("endPoint_"+ScnString);
		String baseUrl = properties.getProperty("urlBase");
		if(baseUrl != null  && ep !=null) 
			return (baseUrl+ep);
		else 
			throw new RuntimeException("End Point for Service is incorrect. Configuration.properties file for the Key:urlBase+endPoint_?");
	}
		
	
	public DriverType getBrowser() {
		String browserName = properties.getProperty("browser");
		if(browserName == null || browserName.equals("chrome")) return DriverType.CHROME;
		else if(browserName.equalsIgnoreCase("firefox")) return DriverType.FIREFOX;
		else if(browserName.equals("iexplorer")) return DriverType.INTERNETEXPLORER;
		else throw new RuntimeException("Browser Name Key value in Configuration.properties is not matched : " + browserName);
	}
	
	public EnvironmentType getEnvironment() {
		String environmentName = properties.getProperty("environment");
		if(environmentName == null || environmentName.equalsIgnoreCase("local")) return EnvironmentType.LOCAL;
		else if(environmentName.equals("remote")) return EnvironmentType.REMOTE;
		else throw new RuntimeException("Environment Type Key value in Configuration.properties is not matched : " + environmentName);
	}
	
	public Boolean getBrowserWindowSize() {
		String windowSize = properties.getProperty("windowMaximize");
		if(windowSize != null) return Boolean.valueOf(windowSize);
		return true;
	}
	
	public String getTestDataResourcePath(){
		String testDataResourcePath = properties.getProperty("testDataResourcePath");
		if(testDataResourcePath!= null) return testDataResourcePath;
		else throw new RuntimeException("Test Data Resource Path not specified in the Configuration.properties file for the Key:testDataResourcePath");		
	}

	public String getFilesPath() {
		String fp = properties.getProperty("filesPath");
		if(fp != null) return fp;
		else throw new RuntimeException("Files Path not specified in the Configuration.properties file for the Key:filesPath");
	}
	
	public String getBodyScenario(String ScnString) {
		String bs = properties.getProperty("bodyscenario_"+ScnString);
		if(bs != null) return (getFilesPath()+bs);
		else throw new RuntimeException("Body Scenario "+ScnString+" not specified in the Configuration.properties file for the Key:bodyscenario_?");
	}
	

	public String getCsvInScenario(String ScnString) {
		String bs = properties.getProperty("csvInScn_"+ScnString);
		if(bs != null) return (getFilesPath()+bs);
		else throw new RuntimeException("CSV Input for Scenario "+ScnString+" not specified in the Configuration.properties file for the Key:csvInScn_?");
	}
	
	
	public String getCsvExpectedScenario(String ScnString) {
		String bs = properties.getProperty("csvExpScn_"+ScnString);
		if(bs != null) return (getFilesPath()+bs);
		else throw new RuntimeException("CSV Expected data for Scenario "+ScnString+" not specified in the Configuration.properties file for the Key:csvExpScn_?");
	}	
	
	public String getNewJson(String ScnString) {
		// TODO Auto-generated method stub
		String bsu = properties.getProperty("bodyscenario_"+ScnString+"_Updated");
		if(bsu != null) return (getFilesPath()+bsu);
		else throw new RuntimeException("Body Scenario Updated for "+ScnString+" not specified in the Configuration.properties file for the Key:bodyscenario_?_Updated");
	}

	
	public String getUserId() {
		String userId = properties.getProperty("userId");
		if(userId != null) return userId;
		else throw new RuntimeException("User ID not specified in the Configuration.properties file for the Key:userId");
	}
	
	public String getPasswd() {
		String passwd = properties.getProperty("passwd");
		if(passwd != null) return passwd;
		else throw new RuntimeException("Password not specified in the Configuration.properties file for the Key:passwd");
	}
	
	public String getnfpath() {
		String nfpath = properties.getProperty("nfpath");
		if(nfpath != null) return nfpath;
		else throw new RuntimeException("Path not specified in the Configuration.properties file for the Key:nfpath");
	}

	public String getNewJson() {
		// TODO Auto-generated method stub
		String bsu = properties.getProperty("bodyscenario_1_Updated");
		if(bsu != null) return bsu;
		else throw new RuntimeException("bodyscenario_1_Updated updated not specified in the Configuration.properties");
	}

	public String getTmpFilesPath() {
		String fp = properties.getProperty("tmpFilesPath");
		if(fp != null) return fp;
		else throw new RuntimeException("Files Path not specified in the Configuration.properties file for the Key:filesPath");
	}
	
}
