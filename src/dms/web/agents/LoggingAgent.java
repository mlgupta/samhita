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
 * $Id: LoggingAgent.java,v 1.3 2006/03/17 13:01:40 suved Exp $
 *****************************************************************************
 */
package dms.web.agents;
/* Java API */
import java.text.SimpleDateFormat;
import java.util.Date;
/* Oracle API */
import oracle.ifs.beans.ClassObject;
import oracle.ifs.beans.LibraryService;
import oracle.ifs.beans.LibrarySession;
import oracle.ifs.beans.PublicObject;
import oracle.ifs.common.AttributeValue;
import oracle.ifs.common.IfsEvent;
import oracle.ifs.common.IfsEventHandler;
import oracle.ifs.common.IfsException;
import oracle.ifs.common.ParameterTable;
import oracle.ifs.management.domain.IfsServer;

/**
 * LoggingAgent illustrates how to write an agent by subclassing IfsServer.
 * <p>
 * The agent logs events on instances of a classobject specified by a server
 * configuration parameter, and publishes event counts as dynamic properties.
 * <p>
 * It demonstrates all of the common aspects of agents:
 * <ul>
 * <li> Registering for and acting on events.
 * <li> Using a timer for timed activities.
 * <li> Reacting to server configuration parameters.
 * <li> Publishing dynamic server properties.
 * <li> Overriding IfsServer methods.
 * </ul>
 */
public class LoggingAgent
	extends IfsServer
	implements IfsEventHandler
{
  static long test=1;
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
	
	/**
	 * Runs the LoggingAgent in standalone mode.
	 * 
	 * @param args		the command line arguments
	 *
	 * @exception Exception if the operation fails
	 */
	public static void startAgent()
		throws Exception
	{
		// If something goes wrong, we want verbose exception messages.
		IfsException.setVerboseMessage(true);

		// Construct a parameter table from the command-line arguments.
		ParameterTable pt = new ParameterTable();

    pt.put("IFS.SERVER.Domain","ifs://user11.dbsentry.com:1521:cmsdk10g.user11.dbsentry.com:CMSDK10G");
    pt.put("IFS.SERVER.SchemaPassword","cmsdk10g");
    pt.put("IFS.SERVER.Service","IfsDefault");
    pt.put("IFS.SERVER.AGENTS.LOGGINGAGENT.ClassObjectForEventRegistration","DOCUMENT");
    pt.put("IFS.SERVER.SESSION.User","system");
    pt.put("IFS.SERVER.TIMER.ActivationPeriod","60s");
    pt.put("IFS.SERVER.TIMER.InitialDelay","10s");
		// Extract some additional parameters required for standalone operation.
		String serviceName = pt.getString("IFS.SERVER.Service");
		String schemaPassword = pt.getString("IFS.SERVER.SchemaPassword");
		String domain = pt.getString("IFS.SERVER.Domain");

		// Start a service against which to run a LoggingAgent.
		LibraryService.startService(serviceName, schemaPassword, null, domain );

		// Construct, initialize, and start the LoggingAgent.
		LoggingAgent agent = new LoggingAgent();

		agent.initialize("LoggingAgent", serviceName, schemaPassword, pt, null, LEVEL_HIGH);

		agent.start();
	}


	///
	/// Constructor
	///
	
	/**
	 * Constructs a LoggingAgent.
	 *
	 * @exception IfsException if the operation fails
	 */
	public LoggingAgent()
		throws IfsException
	{
		super();
		clearEventCounts();
	}



	///
	/// Agent lifecycle
	///
	
	/**
	 * Initializes this LoggingAgent.
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
		
		// Enable event listening.
		enableEventListening();

		// Start the timer.
		//startTimer();
	}

	/**
	 * Runs this LoggingAgent.
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
			LibrarySession session = getSession();

			// Register for events on all instances of the classobject
			// specified in the server configuration properties.

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
		String s = "Agent:"+"\t";
    
    LibrarySession session =null;
    boolean docWithinFolderDeleted = false;
    Long id = null;
    

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
        
              
          s += classname + " ";
        }
        else{
          
          
          s += classname + " '" + name + "' ";
        }
        
      }else
          s += "Document has been deleted.";
		}catch (Exception e){
      System.err.println("Exception occured during processEvent(IfsEvent event): ");
      System.err.println("EventType: "+event.getEventType()+"EventSubType: "+event.getEventSubtype());
      docWithinFolderDeleted = true;
      // Consume.
		}
    if(!docWithinFolderDeleted){
      if( (event.getEventType()!=IfsEvent.EVENTTYPE_FREE) ){          
        String lastModifiedDate = new String();
          
        if(event.getEventType() == IfsEvent.EVENTTYPE_CREATEINSTANCE){  
          lastModifiedDate = format(getSession().getPublicObject(event.getId()).getCreateDate(),DATE_FORMAT);
          userName = (getSession().getPublicObject(event.getId())).getCreator().getName();
          s +="Doc Created By:"+userName+"\t"+lastModifiedDate+"\t"+"Event:"+event.toString();
           
        }else{
          lastModifiedDate = format(getSession().getPublicObject(event.getId()).getLastModifyDate(),DATE_FORMAT);
          userName = (getSession().getPublicObject(event.getId())).getLastModifier().getName();
          s +="Last Modified By:"+userName+"\t"+lastModifiedDate+"\t"+"Event:"+event.toString();
          
        }
        
      }
    }
    if(!docWithinFolderDeleted){
      log(LEVEL_MEDIUM, "Event: " + s);
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
}
