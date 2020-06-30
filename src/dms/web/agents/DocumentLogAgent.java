/*
 *****************************************************************************
 *                       Confidentiality Information                         *
 *                                                                           *
 * This module is the confidential and proprietary information of            *
 * DBSentry Corp.; it is not to be copied, reproduced, or transmitted in any *
 * form, by any means, in whole or in part, nor is it to be used for any     *
 * purpose other than that for which it is expressly provided without the    *
 * written permission of DBSentry Corp.                                      *
 *                                                                           *
 * Copyright (c) 2004-2005 DBSentry Corp.  All Rights Reserved.              *
 *                                                                           *
 *****************************************************************************
 * $Id: DocumentLogAgent.java,v 1.11 2006/03/03 12:46:42 suved Exp $
 *****************************************************************************
 */
package dms.web.agents;
/* dms package references */
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.web.beans.filesystem.DocLogAgentBean;
import dms.web.beans.filesystem.FolderDoc;
/* Java API */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
/* cmsdk API */
import oracle.ifs.adk.filesystem.IfsFileSystem;
import oracle.ifs.beans.ClassObject;
import oracle.ifs.beans.Document;
import oracle.ifs.beans.Family;
import oracle.ifs.beans.Format;
import oracle.ifs.beans.LibraryService;
import oracle.ifs.beans.LibrarySession;
import oracle.ifs.beans.PublicObject;
import oracle.ifs.beans.VersionDescription;
import oracle.ifs.beans.VersionSeries;
import oracle.ifs.common.AttributeValue;
import oracle.ifs.common.CleartextCredential;
import oracle.ifs.common.ConnectOptions;
import oracle.ifs.common.IfsEvent;
import oracle.ifs.common.IfsEventHandler;
import oracle.ifs.common.IfsException;
import oracle.ifs.common.ParameterTable;
import oracle.ifs.management.domain.IfsServer;
/* Struts API */
import org.apache.log4j.Logger;
/**
 *	Purpose: Activity Agent for logging events on documents
 *  @author              Maneesh Mishra 
 *  @version             1.0
 * 	Date of creation:    09-03-2005
 * 	Last Modified by :   Suved Mishra  
 * 	Last Modified Date:  03-03-2006  
 */
public class DocumentLogAgent
	extends IfsServer
	implements IfsEventHandler
{
  public static boolean isGettingUpdated=false;  
	///
	/// Member variables
	///

	/**
	 * The name of the classobject for which events will be logged.
	 */
	protected String m_ClassObjectForEventRegistration;

	/**
	 * The number of events received during the last timer period.
	 */
	protected int m_LastPeriodEventCount;

	/**
	 * The total number of events received since the agent was started.
	 */
	protected int m_TotalEventCount;

  /**
   * This is used to set the date-time format to be used during logging events.
   */
  private static String DATE_FORMAT="MM/dd/yyyy HH:mm:ss";
	///
	/// Standalone operation
	///
  public static LibrarySession loggerSession=null;
  public static String ifsServerDomain=null;
  public static String ifsServerSchemaPassword=null;
  public static String ifsServerService=null;
  public static String generalActionParamLocation=null;
  public static String docLogUser=null;
  public static String docLogPassword=null;
  public static String serviceName=null;
  public static   String schemaPassword=null;
  public static  String domain=null;
  public static  ParameterTable pt=null;
  public static LibraryService libraryService=null;
  LibrarySession session = null;
    
  /**
	 * Runs the DocumentLogAgent in standalone mode.
	 * 
	 * @param args		the command line arguments
	 *
	 * @exception Exception if the operation fails
	 */
   public static void writeToFile(String text){
   try {
        BufferedWriter out = new BufferedWriter(new FileWriter("/home/ias/logger.txt"));
        out.write(text);
        out.close();
    } catch (Exception e) {
    e.printStackTrace();
    }
   }
	public static void main(String args[])
		throws Exception
	{
     DocumentLogAgent agent = new DocumentLogAgent();   
     
    Logger logger=Logger.getLogger("DbsLogger");
     boolean errorOccured=false;
		// If something goes wrong, we want verbose exception messages.
		 try{
        
        IfsException.setVerboseMessage(true);
    
        // Construct a parameter table from the command-line arguments.
        
        pt = new ParameterTable(args,"parameterfile");   
        
        // Extract some additional parameters required for standalone operation.
        serviceName = pt.getString("IFS.SERVER.Service");
        schemaPassword = pt.getString("IFS.SERVER.SchemaPassword");
        domain = pt.getString("IFS.SERVER.Domain");
        docLogUser=pt.getString("IFS.SERVER.DocLogUser");
        docLogPassword=pt.getString("IFS.SERVER.DocLogPassword");
        
        
    
        // Start a service against which to run a DocumentLogAgent.    
       try {
          //LibraryService libraryService=  getService();     
          LibraryService libraryService=LibraryService.findService(serviceName);
          //LibraryService libraryService=LibraryService.startService(serviceName, schemaPassword, null, domain );
      
          // Construct, initialize, and start the DocumentLogAgent.
           //libraryService=getService();     
                
          
          CleartextCredential dbsCleartextCredential = new CleartextCredential(docLogUser,docLogPassword);
          String serverName="DocumentLogAgent";
          ConnectOptions options = new ConnectOptions();
          options.setApplicationName(serverName);
          agent.log(LEVEL_MEDIUM,"Trying to get logger Session");
          loggerSession=libraryService.connect(dbsCleartextCredential,options);
          if(loggerSession!=null){
            writeToFile("logger Session is not null");
          }else{
            writeToFile("logger Session is  null");
          }
          loggerSession.setAdministrationMode(true);

          
        }   
        catch (Exception e) {
          //log(LEVEL_HIGH,e.toString());
          logger.error(e.toString());
        }
        
        
        
    }catch(Exception excep)
    {
      excep.printStackTrace();
      errorOccured=true;
    }
     if(!errorOccured){
            
	    logger.info("Starting");
	    
      agent.initialize("DocumentLogAgent", serviceName, schemaPassword, pt, null, LEVEL_HIGH);    
	    logger.info("Initialized");
      agent.start();
	    logger.info("Started");
      logger.info("Trying to stop AGENT");
      agent.stop();
      logger.info("Stopped");
           
     }
	}


	///
	/// Constructor
	///
	
	/**
	 * Constructs a DocumentLogAgent.
	 *
	 * @exception IfsException if the operation fails
	 */
	public DocumentLogAgent()throws IfsException{
		super();
		clearEventCounts();    
	}

	///
	/// Agent lifecycle
	///
	
	/**
	 * Initializes this DocumentLogAgent.
	 *
	 * @exception Exception if the operation fails
	 */
	public void initialize()
		throws Exception
	{
		ParameterTable params = getParameterTable();

		// Publish our properties.
		String propname = "IFS.SERVER.AGENTS.LOGGINGAGENT.ClassObjectForEventRegistration";
		m_ClassObjectForEventRegistration = params.getString(propname, PublicObject.CLASS_NAME);
		AttributeValue av = AttributeValue.newAttributeValue(m_ClassObjectForEventRegistration);
		av.setName(propname);
		handlePropertyChangeRequest(av);
     // Extract some additional parameters required for standalone operation.
        serviceName = params.getString("IFS.SERVER.Service");
        schemaPassword = params.getString("IFS.SERVER.SchemaPassword");
        domain = params.getString("IFS.SERVER.Domain");
        docLogUser=params.getString("IFS.SERVER.DocLogUser");
        docLogPassword=params.getString("IFS.SERVER.DocLogPassword");
        
        
    
        // Start a service against which to run a DocumentLogAgent.    
       try {
          //LibraryService libraryService=  getService();     
          LibraryService libraryService=LibraryService.findService(serviceName);
          //LibraryService libraryService=LibraryService.startService(serviceName, schemaPassword, null, domain );
      
          // Construct, initialize, and start the DocumentLogAgent.
           //libraryService=getService();     
          //System.out.println(docLogUser);
          //System.out.println(docLogPassword);
          CleartextCredential dbsCleartextCredential = new CleartextCredential(docLogUser,docLogPassword);
          String serverName="DocumentLogAgent";
          ConnectOptions options = new ConnectOptions();
          options.setApplicationName(serverName);          
          loggerSession=libraryService.connect(dbsCleartextCredential,options);
          if(loggerSession!=null){
            writeToFile("logger Session is not null");
          }else{
            writeToFile("logger Session is  null");
          }
          loggerSession.setAdministrationMode(true);

          
        }   
        catch (Exception e) {
          log(LEVEL_HIGH,e.toString());
          e.printStackTrace();
        }

		// Also publish the event counts (which will be zero initially).
		publishEventCounts();
    
	}

	/**
	 * Performs pre-run activities.
	 *
	 * @exception Exception if the operation fails
	 */
	public void preRun()
		throws Exception
	{
		log(LEVEL_MEDIUM, "Start request");

		// Initialize the event counters.
		clearEventCounts();
		publishEventCounts();
    
    
		
		// Create a default session.
		connectSession();
		//System.out.println("Pre Run: "+getName());
		// Enable event listening.
		enableEventListening();

		// Start the timer.
		//startTimer();
    
    
    
    
	}

	/**
	 * Runs this DocumentLogAgent.
	 */
	public void run()
	{
        
		// While not time to stop...
		while (!stopRequested())
		{
			try
			{
				// Handle requests.
				handleRequests();

				// Process events.
				processEvents();

				// And wait for something else to do.
				waitServer();
			}
			catch (Throwable t)
			{
				log(LEVEL_LOW, t);
			}
		}
	}

	/**
	 * Performs post-run activities.
	 */
	public void postRun()
	{
		log(LEVEL_MEDIUM, "Stop request");

		try
		{
			// Stop the timer.
			stopTimer();
			
			// Disable event listening.
			disableEventListening();
      
			// Disconnect our session.
			disconnectSession();
      loggerSession.disconnect();
		}
		catch (Exception e)
		{
			log(LEVEL_LOW, e);
		}
	}


	///
	/// Suspend/resume
	///

	/**
	 * Gets whether suspend/resume is supported.
	 *
	 * @return				true
	 */
	public boolean supportsSuspendResume()
	{
		return true;
	}

	/**
	 * Handles a suspend request.
	 *
	 * @exception IfsException if operation fails
	 */
	protected void handleSuspendRequest()
		throws IfsException
	{
		log(LEVEL_MEDIUM, "Suspend request");

		// Disable event listening and the timer.
		disableEventListening();
		stopTimer();
	}
	
	/**
	 * Handles a resume request.
	 *
	 * @exception IfsException if operation fails
	 */
	protected void handleResumeRequest()
		throws IfsException
	{
		log(LEVEL_MEDIUM, "Resume request");

		// Re-enable event listening and the timer.
		enableEventListening();
		startTimer();
	}


	///
	/// Timer
	///
	
	/**
	 * Handles a timer expiration.
	 *
	 * @exception Exception if operation fails
	 */
	protected void handleTimerExpired()
		throws Exception
	{
		log(LEVEL_MEDIUM, "Timer expiration");

		// Update the published event counts.
		publishEventCounts();
	}


	///
	/// Events
	///

	/**
	 * Enables event listening.
	 *
	 * @exception IfsException if the operation fails
	 */
	public void enableEventListening()
		throws IfsException
	{
		try
		{
			session = getSession();
          

			ClassObject co = session.getClassObjectByName(
				m_ClassObjectForEventRegistration);

			session.registerClassEventHandler(co, true, this);
		}
		catch (IfsException e)
		{
			log(LEVEL_LOW, e);
			throw e;
		}
    
    
	}

	/**
	 * Disables event listening.
	 *
	 * @exception IfsException if the operation fails
	 */
	public void disableEventListening()
		throws IfsException
	{
		try
		{
			LibrarySession session = getSession();

			// Deregister for events on all instances of the classobject
			// specified in the server configuration properties.

			ClassObject co = session.getClassObjectByName(
				m_ClassObjectForEventRegistration);

			session.deregisterClassEventHandler(co, true, this);
		}
		catch (IfsException e)
		{
			log(LEVEL_LOW, e);
			throw e;
		}
	}

	/**
	 * Handles events.
	 *
	 * @param event			the event
	 *
	 * @exception IfsException if the operation fails
	 */
	public void handleEvent(IfsEvent event)
		throws IfsException
	{
		// Filter out events for that are not committed (these only
		// originate from our session).  Delegate processing of all
		// other events to the administration thread.
		
		if (event.getEventStatus() == IfsEvent.EVENTSTATUS_COMMITTED)
		{
			queueEvent(event);
		}
	}
  
  
      public static String format(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);    

    }


	/**
	 * Process an event in the administration thread.
	 *
	 * @param event			the event to process
	 *
	 * @exception IfsException if the operation fails
	 */
	public void processEvent(IfsEvent event)
		throws IfsException
	{
		// Update the counter.
    //test++;
    //System.out.println("Test="+test);
		m_LastPeriodEventCount++;

		// Log a string for the event.
		String dbsLoggerString=null;
    
    LibrarySession session =null;
    boolean docWithinFolderDeleted = false;
    Long id = null;
    boolean isDocVersioned =true;

    String userName = null; 
		try
		{
			session = getSession(); 
			id = event.getId();      
      
      if(event.getEventType() != IfsEvent.EVENTTYPE_FREE){
       
        PublicObject obj = session.getPublicObject(id);     
               
        String classname = obj.getClassObject().getName();        
        
        String name = obj.getName();
          
        if (name == null){
          dbsLoggerString += classname + " ";
        }
        else{
         dbsLoggerString += classname + " '" + name + "' ";
        }
       
      }else
         dbsLoggerString += "Doc has been deleted.";
		}catch (Exception e){
     log(LEVEL_LOW,"Exception occured in processEvent(IfsEvent event): ");
      log(LEVEL_LOW,"EventType: "+event.getEventType()+"EventSubType: "+event.getEventSubtype());
     docWithinFolderDeleted = true;
      // Consume.
		}
    String s = "Agent"+"\t";
    if(!docWithinFolderDeleted){
      if( (event.getEventType()!=IfsEvent.EVENTTYPE_FREE) ){          
        String lastModifiedDate = new String();
          
        if(event.getEventType() == IfsEvent.EVENTTYPE_CREATEINSTANCE){  
          lastModifiedDate = format(getSession().getPublicObject(event.getId()).getCreateDate(),DATE_FORMAT);
          userName = (getSession().getPublicObject(event.getId())).getCreator().getName();
          s +="Doc Created";
           
        }else{
        isDocVersioned = (getSession().getPublicObject(event.getId()).isVersioned());
        
          if(!isDocVersioned){
            lastModifiedDate = format(getSession().getPublicObject(event.getId()).getLastModifyDate(),DATE_FORMAT);
            userName = (getSession().getPublicObject(event.getId())).getLastModifier().getName();
            s +="Doc Modified";
          }else{
	    FolderDoc updateFormat=new FolderDoc(new DbsLibrarySession(getSession()));          
            try {
                if(updateFormat != null)
                  updateFormat.updateFormat((getSession().getPublicObject(event.getId())).getId());
                else
                  log(LEVEL_LOW,"updateFormat is null");
                } 
            catch (DbsException dbsEx) {
              dbsEx.printStackTrace();
              log(LEVEL_LOW,dbsEx.getMessage());
            }
            catch (Exception e) {
              e.printStackTrace();
              log(LEVEL_LOW,e.getMessage());
            }
	  }
        
          
        }
        
      }
    }
    if(!docWithinFolderDeleted){
      log(LEVEL_MEDIUM, "Event: " +dbsLoggerString);
    }
    
    if(!isDocVersioned){ 
      DocLogAgentBean  docEventLogBean = new DocLogAgentBean();
      if(userName!=null && !userName.equals("logger")){
           for( ;isGettingUpdated==true;){
                  try{
                       log(LEVEL_LOW,"Samhita updating the log, waiting for 2 secs and will then resume");
                       Thread.sleep(2000);
                        
                  }catch(Exception excep)
                  {
                    excep.printStackTrace();
                  }   
           } 
           log(LEVEL_MEDIUM, s);
           
           docEventLogBean.logEvent(loggerSession,userName,id,s);
      }
         
    }   
    
	}

	/**
	 * Clear the event counts.
	 */
	private void clearEventCounts()
	{
		m_LastPeriodEventCount = 0;
		m_TotalEventCount = 0;
	}

	/**
	 * Publish the event counts as dynamic properties.
	 *
	 * @exception Exception if the operation fails
	 */
	private void publishEventCounts()
		throws Exception
	{
		m_TotalEventCount += m_LastPeriodEventCount;

		AttributeValue av;

		av = AttributeValue.newAttributeValue(m_LastPeriodEventCount);
		av.setName("LastPeriodEventCount");
		handlePropertyChangeRequest(av);

		av = AttributeValue.newAttributeValue(m_TotalEventCount);
		av.setName("TotalEventCount");
		handlePropertyChangeRequest(av);
		
		m_LastPeriodEventCount = 0;
	}
	
 public void updateFormat(Long familyId) throws  Exception{
      IfsFileSystem dbsFileSystem = null;
      PublicObject dbsPublicObject = null;
      Document dbsDocument=null;
      VersionSeries dbsVersionSeries;
      VersionDescription[] dbsVersionDescriptions; 
      Family dbsFamily;
      
      try{
        dbsFileSystem = new IfsFileSystem(getSession());
        dbsPublicObject = dbsFileSystem.findPublicObjectById(familyId);            
        dbsFamily = dbsPublicObject.getFamily();
        if(dbsFamily != null){
          dbsVersionSeries = dbsFamily.getPrimaryVersionSeries();
          dbsVersionDescriptions = dbsVersionSeries.getVersionDescriptions();
          dbsPublicObject = dbsPublicObject.getResolvedPublicObject();
          dbsDocument = (Document)dbsPublicObject;
          Format dbsFormat = dbsDocument.getFormat();
          if(dbsFormat==null){
            PublicObject dbsPreviousPublicObject = dbsVersionDescriptions[dbsVersionDescriptions.length - 2].getPublicObject();
            Document dbsPreviousDocument = (Document)dbsPreviousPublicObject;
            Format dbsPreviousFormat = dbsPreviousDocument.getFormat();
            dbsDocument.setFormat(dbsPreviousFormat);
          }
        }
      }catch(Exception ex){
        ex.printStackTrace();
        throw ex;
      }
    }
}
