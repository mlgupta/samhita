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
 * $Id: Theme.java,v 20040220.8 2006/03/14 05:44:57 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.theme;

/**
 *	Purpose: To generate and Store Themes In Property Bundle
 *  Info: There Will be Property Bundle named "Themes" associated with Value Default. 
 *        Each Theme will be the Property of this "Themes" Property Bundle.
 *  @author              Sudheer Pujar
 *  @version             1.0
 * 	Date of creation:   16-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */

//dms package references
import dms.beans.DbsAttributeValue;
import dms.beans.DbsException;
import dms.beans.DbsLibrarySession;
import dms.beans.DbsPrimaryUserProfile;
import dms.beans.DbsProperty;
import dms.beans.DbsPropertyBundle;
import dms.beans.DbsPropertyBundleDefinition;
import dms.beans.DbsValueDefault;
import dms.beans.DbsValueDefaultDefinition;
import dms.beans.DbsValueDefaultPropertyBundle;
import dms.beans.DbsValueDefaultPropertyBundleDefinition;
import dms.web.actionforms.theme.ThemeNewEditForm;
import dms.web.beans.utility.SearchUtil;
//Java API
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
//Logger API
import org.apache.log4j.Logger;
// DOM API
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
// SAX API
import org.xml.sax.SAXException;


public class Theme  {
  private DbsLibrarySession dbsLibrarySession=null;
  private int pageCount=1;
  private static Logger logger = Logger.getLogger("DbsLogger");
  /**
 * Purpose : Contructs a Theme Object for Given DbsLibrarysession
 * @param dbsLibrarySession - A DbsLibrarysession object 
 */
  public Theme(DbsLibrarySession dbsLibrarySession ){
    this.dbsLibrarySession=dbsLibrarySession;
  }


/**
 * Purpose : To Get the "Themes" Property Bundle.
 * @return DbsPropertyBundle Object 
 */
  private DbsPropertyBundle getThemesPropertyBundle()throws DbsException,Exception{

    DbsPropertyBundleDefinition themesPbDef=null;
    DbsPropertyBundle themesPb=null;
    DbsValueDefaultPropertyBundleDefinition themesVdPbDef=null;
    DbsValueDefaultPropertyBundle themesVdPb=null;
    DbsValueDefaultDefinition themesVdDef=null;
    DbsValueDefault themesVd=null;
    DbsAttributeValue themesAv=null;
    String pbName ="Themes";
    String defaultThemeName="DefaultTheme";
    try{
      logger.info("Entering" + this.getClass().getName() + ".getThemesPropertyBundle" );

      logger.debug("Searching For Property Bundle Named Themes");
      themesVd=(DbsValueDefault)SearchUtil.findObject(this.dbsLibrarySession,DbsValueDefault.CLASS_NAME,pbName);

      if (themesVd==null){

        logger.debug("Property Bundle of Name Themes Does Not Exist. Creating ...");

        //Creating Property Bundle Definition for Themes

        themesPbDef = new DbsPropertyBundleDefinition(dbsLibrarySession);
        themesPbDef.setAttribute(DbsPrimaryUserProfile.NAME_ATTRIBUTE, DbsAttributeValue.newAttributeValue(pbName));
        logger.debug("Property Bundle Definition for Themes Created");

        //Creating Property Bundle for Themes

        themesPb = (DbsPropertyBundle) dbsLibrarySession.createPublicObject(themesPbDef);
        logger.debug("Property Bundle for Themes Created");
        
        //Creating Value Default Property Bundle Definition for Themes

        themesVdPbDef = new DbsValueDefaultPropertyBundleDefinition(dbsLibrarySession);
        themesVdPbDef.setAttributeByUpperCaseName(DbsValueDefaultPropertyBundle.NAME_ATTRIBUTE, DbsAttributeValue.newAttributeValue(pbName));
        themesVdPbDef.setValue(themesPb);
        logger.debug("Value Default Property Bundle Definition for Themes Created");

        //Creating Value Default Property Bundle for Themes
        themesVdPb = (DbsValueDefaultPropertyBundle) dbsLibrarySession.createPublicObject(themesVdPbDef);
        logger.debug("Value Default Property Bundle for Themes Created");
        
        //Creating Value Default Definition for Themes
        themesVdDef = new DbsValueDefaultDefinition(dbsLibrarySession);
        themesVdDef.setAttributeByUpperCaseName(DbsValueDefault.NAME_ATTRIBUTE, DbsAttributeValue.newAttributeValue(pbName));
        logger.debug("Value Default Definition for Themes Created");

        //Creating Value Default for Themes
        themesVd = (DbsValueDefault) dbsLibrarySession.createSchemaObject(themesVdDef);
        themesVd.setValueDefaultPropertyBundle(themesVdPb);
        logger.debug("Value Default for Themes Created");


      }else{
        logger.debug("Property Bundle of Name Themes Already Exist.");
      }
      
      themesAv=themesVd.getPropertyValue();
      themesPb=(DbsPropertyBundle) themesAv.getPublicObject(dbsLibrarySession);

      logger.info("Exiting " + this.getClass().getName() + ".getThemesPropertyBundle()" );
    }catch(DbsException e){
      logger.error("DbsException Caught @ " + this.getClass().getName() + ".getThemesPropertyBundle()" );
      throw e;
    }catch(Exception e){
      logger.error("Exception Caught @ " + this.getClass().getName() + ".getThemesPropertyBundle()" );
      throw e;
    }
    return themesPb;

  }

  /**
   * Purpose : To Creates Value Default 
   * @param dbsLibrarySession - DbsLibrarySession object
   * @param valueDefaultName - A String object defined by Name of the Theme
   * @return A DbsValueDefault object
   */
  private DbsValueDefault createValuDefault(DbsLibrarySession dbsLibrarySession,String valueDefaultName)throws DbsException,Exception{

      DbsPropertyBundleDefinition dbsPbDef=null;
      DbsPropertyBundle dbsPb=null;
      DbsValueDefaultPropertyBundleDefinition dbsVdPbDef=null;
      DbsValueDefaultPropertyBundle dbsVdPb=null;
      DbsValueDefaultDefinition dbsVdDef=null;
      DbsValueDefault dbsVd=null;
      try{

        logger.info("Entering " + this.getClass().getName() + ".createValuDefault()" );

        //Creating Property Bundle Definition for a Theme
        dbsPbDef = new DbsPropertyBundleDefinition(dbsLibrarySession);
        dbsPbDef.setAttribute(DbsPropertyBundle.NAME_ATTRIBUTE, DbsAttributeValue.newAttributeValue(valueDefaultName));
        logger.debug("Property Bundle Definition for a Theme :" + valueDefaultName + " Created");

        //Creating Property Bundle for a Theme
        dbsPb = (DbsPropertyBundle) dbsLibrarySession.createPublicObject(dbsPbDef);
        logger.debug("Property Bundle for a Theme :" + valueDefaultName + " Created");
        
        //Creating Value Default Property Bundle Definition for a Theme
        dbsVdPbDef = new DbsValueDefaultPropertyBundleDefinition(dbsLibrarySession);
        dbsVdPbDef.setAttributeByUpperCaseName(DbsValueDefaultPropertyBundle.NAME_ATTRIBUTE, DbsAttributeValue.newAttributeValue(valueDefaultName));
        dbsVdPbDef.setValue(dbsPb);
        logger.debug("Value Defaul Property Bundle Definition for a Theme :" + valueDefaultName + " Created");

        //Creating Value Default Property Bundle for a Theme
        dbsVdPb = (DbsValueDefaultPropertyBundle) dbsLibrarySession.createPublicObject(dbsVdPbDef);
        logger.debug("Value Default Property Bundle for a Theme :" + valueDefaultName + " Created");
        
        //Creating Value Default Definition for a Theme
        dbsVdDef = new DbsValueDefaultDefinition(dbsLibrarySession);
        dbsVdDef.setAttributeByUpperCaseName(DbsValueDefault.NAME_ATTRIBUTE, DbsAttributeValue.newAttributeValue(valueDefaultName));
        logger.debug("Value Default Definition for a Theme :" + valueDefaultName + " Created");
        
        //Creating Value Default for a Theme
        dbsVd = (DbsValueDefault) dbsLibrarySession.createSchemaObject(dbsVdDef);
        dbsVd.setValueDefaultPropertyBundle(dbsVdPb);
        logger.debug("Value Default for a Theme :" + valueDefaultName + " Created");

        logger.info("Exiting " + this.getClass().getName() + ".createValuDefault()" );
      }catch(DbsException e){
        logger.error("DbsException Caught @ " + this.getClass().getName() + ".createValuDefault()" );
        throw e;        
      }catch(Exception e){
        logger.error("Exception Caught @ " + this.getClass().getName() + ".createValuDefault()" );
        throw e;        
      }
      return dbsVd;
  }



    /**
   * Purpose : To set the Theme Properties from the Form
   * @param themeNewEditForm - ThemeNewEditForm object
   * @param dbsVd - DbsValueDefault object
   * @return  True if Properties Set Successfully or False 
   */
  private boolean setThemeProperties(ThemeNewEditForm  themeNewEditForm,DbsValueDefault dbsVd)throws DbsException,Exception{
      DbsAttributeValue dbsAv=null;
      DbsPropertyBundle dbsPb=null;
      DbsValueDefault dbsThemesVd=null; 
      DbsPropertyBundle dbsThemesPb=null;
      String themeName=null;
      try{

        logger.info("Entering " + this.getClass().getName() + ".setThemeProperties()" );
        themeName=themeNewEditForm.getTxtThemeName();
        dbsAv =  dbsVd.getPropertyValue();
        dbsPb = (DbsPropertyBundle) dbsAv.getPublicObject(dbsLibrarySession);
        logger.debug("Property Bundle for the  Theme: " + themeName + " Retrived");

        dbsPb.putPropertyValue(ThemeNewEditForm.BACKGROUND,DbsAttributeValue.newAttributeValue(themeNewEditForm.getRadBackground()));
        dbsPb.putPropertyValue(ThemeNewEditForm.BACKGROUND_COLOR,DbsAttributeValue.newAttributeValue(themeNewEditForm.getHdnBackgroundColor()));
        dbsPb.putPropertyValue(ThemeNewEditForm.BACKGROUND_IMAGE,DbsAttributeValue.newAttributeValue(themeNewEditForm.getHdnBackgroundImage()));
        dbsPb.putPropertyValue(ThemeNewEditForm.BODY_TEXT,DbsAttributeValue.newAttributeValue(themeNewEditForm.getCboBodyText()));
        dbsPb.putPropertyValue(ThemeNewEditForm.BODY_TEXT_COLOR,DbsAttributeValue.newAttributeValue(themeNewEditForm.getHdnFontColorBodyText()));
        dbsPb.putPropertyValue(ThemeNewEditForm.COLOR_SCHEME,DbsAttributeValue.newAttributeValue(themeNewEditForm.getHdnColorScheme()));
        dbsPb.putPropertyValue(ThemeNewEditForm.ELEMENT_BACKGROUND_COLOR,DbsAttributeValue.newAttributeValue(themeNewEditForm.getHdnFontColorElementBg()));
        dbsPb.putPropertyValue(ThemeNewEditForm.ELEMENT_TEXT,DbsAttributeValue.newAttributeValue(themeNewEditForm.getCboElementText()));
        dbsPb.putPropertyValue(ThemeNewEditForm.ELEMENT_TEXT_COLOR,DbsAttributeValue.newAttributeValue(themeNewEditForm.getHdnFontColorElementText()));
        dbsPb.putPropertyValue(ThemeNewEditForm.HEADINGS,DbsAttributeValue.newAttributeValue(themeNewEditForm.getCboHeadings()));
        dbsPb.putPropertyValue(ThemeNewEditForm.HEADINGS_COLOR,DbsAttributeValue.newAttributeValue(themeNewEditForm.getHdnFontColorHeadings()));
        dbsPb.putPropertyValue(ThemeNewEditForm.MENU_TEXT,DbsAttributeValue.newAttributeValue(themeNewEditForm.getCboMenuText()));
        dbsPb.putPropertyValue(ThemeNewEditForm.MENU_TEXT_COLOR,DbsAttributeValue.newAttributeValue(themeNewEditForm.getHdnFontColorMenuText()));
        dbsPb.putPropertyValue(ThemeNewEditForm.STYLE,DbsAttributeValue.newAttributeValue(themeNewEditForm.getHdnStyle()));

        logger.debug("All the Properties for the Theme: " + themeName + " is Set From the Form");

        dbsThemesPb=getThemesPropertyBundle();
        logger.debug("Themes Property Bundle is  Retrived");
        
        dbsThemesPb.putPropertyValue(themeName,DbsAttributeValue.newAttributeValue(dbsPb));
        logger.debug("The Theme : " + themeName +" Property Bundle is Added to Themes Property Bundle as a Property");
        
        logger.info("Exiting " + this.getClass().getName() + ".setThemeProperties()" );
      }catch(DbsException e){
        logger.error("DbsException Caught @  " + this.getClass().getName() + ".setThemeProperties()" );
        throw e;
      }catch(Exception e){
        logger.error("Exception Caught @  " + this.getClass().getName() + ".setThemeProperties()" );
        throw e;
      }
      return true;
    }
    

  /**
   * Purpose : To Get the Theme Properties from the Property Bundle
   * @param themeName - String object
   * @return  themeNewEditForm - ThemeNewEditForm Object
   */
  public ThemeNewEditForm getThemeProperties(String themeName)throws DbsException,Exception{
    DbsAttributeValue dbsAv=null;
    DbsPropertyBundle dbsPb=null;
    DbsValueDefault dbsVd=null; 
    ThemeNewEditForm themeNewEditForm=null; 
    try{

      logger.info("Entering " + this.getClass().getName() + ".getThemeProperties()" );
        
      dbsVd = (DbsValueDefault)SearchUtil.findObject(this.dbsLibrarySession,DbsValueDefault.CLASS_NAME,themeName);
      logger.debug("Made a Call to the Function : SearchUtil.findObject() to Get ValueDefault for Theme : " + themeName + " - To Find the Theme" );
      if (dbsVd!=null){
        themeNewEditForm = new ThemeNewEditForm();      

        themeNewEditForm.setTxtThemeName(themeName);
        
        dbsAv =  dbsVd.getPropertyValue();
        dbsPb = (DbsPropertyBundle) dbsAv.getPublicObject(dbsLibrarySession);

        dbsAv= dbsPb.getPropertyValue(ThemeNewEditForm.BODY_TEXT);
        themeNewEditForm.setCboBodyText(dbsAv.getString(dbsLibrarySession));

        dbsAv= dbsPb.getPropertyValue(ThemeNewEditForm.ELEMENT_TEXT);
        themeNewEditForm.setCboElementText(dbsAv.getString(dbsLibrarySession));

        dbsAv= dbsPb.getPropertyValue(ThemeNewEditForm.HEADINGS);
        themeNewEditForm.setCboHeadings(dbsAv.getString(dbsLibrarySession));

        dbsAv= dbsPb.getPropertyValue(ThemeNewEditForm.MENU_TEXT);
        themeNewEditForm.setCboMenuText(dbsAv.getString(dbsLibrarySession));

        dbsAv= dbsPb.getPropertyValue(ThemeNewEditForm.STYLE);
        themeNewEditForm.setHdnStyle(dbsAv.getString(dbsLibrarySession));

        dbsAv= dbsPb.getPropertyValue(ThemeNewEditForm.BACKGROUND);
        themeNewEditForm.setRadBackground(dbsAv.getString(dbsLibrarySession));

        if (dbsAv.getString(dbsLibrarySession).equals(new String("color"))){
          dbsAv= dbsPb.getPropertyValue(ThemeNewEditForm.BACKGROUND_COLOR);
          themeNewEditForm.setHdnBackgroundColor(dbsAv.getString(dbsLibrarySession));
          themeNewEditForm.setHdnBackgroundImage("");
        }else{
          dbsAv= dbsPb.getPropertyValue(ThemeNewEditForm.BACKGROUND_IMAGE);
          themeNewEditForm.setHdnBackgroundImage(dbsAv.getString(dbsLibrarySession));
          themeNewEditForm.setHdnBackgroundColor("");
        }
   
        dbsAv= dbsPb.getPropertyValue(ThemeNewEditForm.COLOR_SCHEME);
        themeNewEditForm.setHdnColorScheme(dbsAv.getString(dbsLibrarySession));

        dbsAv= dbsPb.getPropertyValue(ThemeNewEditForm.HEADINGS_COLOR);
        themeNewEditForm.setHdnFontColorHeadings(dbsAv.getString(dbsLibrarySession));

        dbsAv= dbsPb.getPropertyValue(ThemeNewEditForm.BODY_TEXT_COLOR);
        themeNewEditForm.setHdnFontColorBodyText(dbsAv.getString(dbsLibrarySession));

        dbsAv= dbsPb.getPropertyValue(ThemeNewEditForm.MENU_TEXT_COLOR);
        themeNewEditForm.setHdnFontColorMenuText(dbsAv.getString(dbsLibrarySession));

        dbsAv= dbsPb.getPropertyValue(ThemeNewEditForm.ELEMENT_TEXT_COLOR);
        themeNewEditForm.setHdnFontColorElementText(dbsAv.getString(dbsLibrarySession));

        dbsAv= dbsPb.getPropertyValue(ThemeNewEditForm.ELEMENT_BACKGROUND_COLOR);
        themeNewEditForm.setHdnFontColorElementBg(dbsAv.getString(dbsLibrarySession));

        logger.debug("All the Properties for the Theme: " + themeName + " is Retrivied from the Property Bundle and Set to Form");
      }else{
        logger.debug("No Theme: " + themeName + " Exists");
      }
      logger.info("Exiting " + this.getClass().getName() + ".getThemeProperties()" );
      
    }catch(DbsException e){
      logger.error("DbsException Caught @  " + this.getClass().getName() + ".getThemeProperties()" );
      throw e;
    }catch(Exception e){
      logger.error("Exception Caught @  " + this.getClass().getName() + ".getThemeProperties()" );
      throw e;
    }
    return themeNewEditForm;
  }

      /**
   * Purpose : To Create a Theme 
   * @param themeNewEditForm - ThemeNewEditForm object
   * @return  True if Created Successfully or False if Duplicate 
   */
  public boolean createTheme(ThemeNewEditForm  themeNewEditForm )throws DbsException,Exception{
    DbsValueDefault dbsVd=null; 
    String themeName=null;
    try{
      logger.info("Entering " + this.getClass().getName() + ".createTheme()" );
      themeName=themeNewEditForm.getTxtThemeName();
      dbsVd=(DbsValueDefault)SearchUtil.findObject(this.dbsLibrarySession,DbsValueDefault.CLASS_NAME,themeName);
      logger.debug("Made a Call to the Function : SearchUtil.findObject() to Get ValueDefault for Theme : " + themeName + " - To check the Existance" );
      if (dbsVd==null){
        dbsVd = createValuDefault(dbsLibrarySession,themeName);
        logger.debug("Made a Call to the Function :" + this.getClass().getName() + ".createValuDefault()" );
        setThemeProperties(themeNewEditForm,dbsVd);
        logger.debug("Made a Call to the Function :" + this.getClass().getName() + ".setThemeProperties() To Set Properties of Theme" );
       
      }else{
        return false;
      } 
      logger.info("Exiting " + this.getClass().getName() + ".createTheme()" );
    }catch(DbsException e){
      logger.error("DbsException Caught @ " + this.getClass().getName() + ".createTheme()" );
      throw e;
    }catch(Exception e){
      logger.error("Exception Caught @ " + this.getClass().getName() + ".createTheme()" );
      throw e;
    }
    return true;
  }

    /**
   * Purpose : To Modify a Theme 
   * @param themeNewEditForm - ThemeNewEditForm object
   * @return  True if Modified Successfully or False 
   */
  public boolean editTheme(ThemeNewEditForm  themeNewEditForm )throws DbsException,Exception{
    DbsValueDefault dbsVd=null; 
    String themeName=null;
    try{
      logger.info("Entering " + this.getClass().getName() + ".editTheme()" );
      themeName=themeNewEditForm.getTxtThemeName();
      dbsVd=(DbsValueDefault)SearchUtil.findObject(this.dbsLibrarySession,DbsValueDefault.CLASS_NAME,themeName);
      logger.debug("Made a Call to the Function : SearchUtil.findObject() to Get ValueDefault for Theme : " + themeName + " - To Find the Theme" );
      if (dbsVd!=null){
        setThemeProperties(themeNewEditForm,dbsVd);
        logger.debug("Made a Call to the Function :" + this.getClass().getName() + ".setThemeProperties() To Set Properties of Theme" );
      }else{
        return false;
      }
      logger.info("Exiting " + this.getClass().getName() + ".editTheme()" );
    }catch(DbsException e){
      logger.error("DbsException Caught @ " + this.getClass().getName() + ".editTheme()" );
      throw e;
    }catch(Exception e){
      logger.error("Exception Caught @ " + this.getClass().getName() + ".editTheme()" );
      throw e;
    }
    return true;
  }

    /**
   * Purpose : To Delete a Theme 
   * @param themeName - String object
   * @return  True if Delete Successfully or False 
   */
  public boolean deleteTheme( String themeName )throws DbsException,Exception{

    DbsAttributeValue dbsAv=null;
    DbsPropertyBundle dbsPb=null;
    DbsValueDefault dbsVd=null; 
    DbsValueDefaultPropertyBundle dbsVdPb= null;
    DbsValueDefault dbsThemesVd=null; 
    DbsPropertyBundle dbsThemesPb=null;
   
    try{
      logger.info("Entering " + this.getClass().getName() + ".deleteTheme()" );
    
      dbsVd =(DbsValueDefault)SearchUtil.findObject(this.dbsLibrarySession,DbsValueDefault.CLASS_NAME,themeName);
      logger.debug("Made a Call to the Function : SearchUtil.findObject() to Get ValueDefault for Theme : " + themeName + " - To Find the Theme" );
      
      if (dbsVd!=null){

        dbsThemesPb=getThemesPropertyBundle();
        logger.debug("Propertybundle for Themes is Retrieved");
        dbsThemesPb.removePropertyValue(themeName);
        logger.debug("Property : " + themeName + " is removed from 'Themes' Property Bundle");
      
        dbsAv = dbsVd.getPropertyValue();
        dbsPb = (DbsPropertyBundle) dbsAv.getPublicObject(dbsLibrarySession);
        dbsVdPb=dbsVd.getValueDefaultPropertyBundle();
        logger.debug("Property Bundle, Value Default Property Bundle retrived for Theme : " + themeName);
        
        dbsVd.free();
        logger.debug("Value Default is Freed for Theme : " + themeName);
        dbsVdPb.free();
        logger.debug("Value Default Property Bundle is Freed for Theme : " + themeName);
        dbsPb.free();
        logger.debug("Property Bundle is Freed for Theme : " + themeName);

      }else{
        return false;
      }            
      logger.info("Exiting " + this.getClass().getName() + ".deleteTheme()" );
    }catch(DbsException e){
      logger.error("DbsException Caught @  " + this.getClass().getName() + ".deleteTheme()" );
      throw e;
    }catch(Exception e){
      logger.error("Exception Caught @  " + this.getClass().getName() + ".deleteTheme()" );
      throw e;
    }
    return true;

  }

  /**
   * Purpose : To List the Themes from the Themes Property Bundle
   * @param themeName - String object
   * @param pageNumber - int
   * @param numberOfRecords - int
   * @return  Arraylist of Themes
   */
  public ArrayList listThemes(String searchString, int pageNumber, int numberOfRecords)throws DbsException,Exception{
    DbsPropertyBundle themesDbsPb=null;
    DbsProperty[] themesdbsproperty; 
    ArrayList themes = new ArrayList();
    int numberOfThemes=0;
    String themeName=null;
    int startIndex=1;
    int endIndex=1;

    try{
      logger.info("Entering " + this.getClass().getName() + ".listThemes()" );
      if (searchString==null){
        searchString="";
      }
      searchString=searchString.trim();

      startIndex=(pageNumber*numberOfRecords) - numberOfRecords;
      endIndex=(startIndex + numberOfRecords) - 1;

      logger.debug("Start Index : " + startIndex + " and  " + "End Index : " + endIndex + " For the Page :" + pageNumber);
      
      themesDbsPb=getThemesPropertyBundle();
      if (themesDbsPb!=null){
        themesdbsproperty=themesDbsPb.getProperties();
        if (themesdbsproperty!= null){
          for(int i=0; i<themesdbsproperty.length; i++){
            themeName=themesdbsproperty[i].getName();      
            if(searchString.length()>0){
              if (themeName.indexOf(searchString)>=0){
                if (numberOfThemes>=startIndex && numberOfThemes<=endIndex){
                  themes.add(themeName);
                }
                numberOfThemes++;
              }
            }else{
              if (numberOfThemes>=startIndex && numberOfThemes<=endIndex){
                  themes.add(themeName);
                }
                numberOfThemes++;
            }
          }

          if (numberOfThemes!=0){
            this.pageCount=((numberOfThemes%numberOfRecords)==0)?(numberOfThemes/numberOfRecords):((numberOfThemes/numberOfRecords)+1);
          }else{
            this.pageCount=1;
          }

        }
      }
      logger.info("Exiting " + this.getClass().getName() + ".listThemes()" );
    }catch(DbsException e){
      logger.error("DbsException Caught @  " + this.getClass().getName() + ".listThemes()" );
      throw e;
    }catch(Exception e){
      logger.error("Exception Caught @  " + this.getClass().getName() + ".listThemes()" );
      throw e;
    }
    return themes;
  }

    /**
   * Purpose : To List the Themes from the Themes Property Bundle 
   * @return  Arraylist of Themes
   */
  public ArrayList listThemes()throws DbsException,Exception{
    DbsPropertyBundle themesDbsPb=null;
    DbsProperty[] themesdbsproperty; 
    ArrayList themes = new ArrayList();

    try{
      logger.info("Entering " + this.getClass().getName() + ".listThemes()" );

      themesDbsPb=getThemesPropertyBundle();
      if (themesDbsPb!=null){
        themesdbsproperty=themesDbsPb.getProperties();
        if (themesdbsproperty!= null){
          for(int i=0; i<themesdbsproperty.length; i++){
            themes.add(themesdbsproperty[i].getName());
          }
        }
      }
      logger.info("Exiting " + this.getClass().getName() + ".listThemes()" );
    }catch(DbsException e){
      logger.error("DbsException Caught @  " + this.getClass().getName() + ".listThemes()" );
      throw e;
    }catch(Exception e){
      logger.error("Exception Caught @  " + this.getClass().getName() + ".listThemes()" );
      throw e;
    }
    return themes;
  }


  /**
   * Purpose : To List the Themes from the Themes Property Bundle
   * @return  int - pageCount of the the Themes
   */
   public int getPageCount(){
    return pageCount;
   } 

  /**
   * Purpose : To Create the Array List of objects of StyleClass defined as Stlye Elements to generate Style Sheet 
   * @param themeNewEditForm -  ThemeNewEditForm object
   * @return  Arraylist of objects of StyleClass
   */
  public static ArrayList getStyleElementList(ThemeNewEditForm  themeForm,String locale, String physicalPath)throws Exception{
    ArrayList elements = new ArrayList();
    Hashtable styles= new Hashtable();
    String elementText=null;
    String bodyText=null;
    String headings=null;
    String menuText=null;

    ArrayList elementTextFontList=null;
    ArrayList bodyTextFontList=null;
    ArrayList headingsFontList=null;
    ArrayList menuTextFontList=null;

    try{
      logger.info("Entering getStyleElementList()" );

      elementText=themeForm.getCboElementText();
      bodyText=themeForm.getCboBodyText();
      headings=themeForm.getCboHeadings();
      menuText=themeForm.getCboMenuText();

      elementTextFontList=getFonts(locale,physicalPath);
      bodyTextFontList=getFonts(locale,physicalPath);
      headingsFontList=getFonts(locale,physicalPath);
      menuTextFontList=getFonts(locale,physicalPath);

      for(int i=0;i<elementTextFontList.size();i++){
        if(!( elementText.equals(((NameValueList)elementTextFontList.get(i)).getName()))){
          elementText=elementText+","+((NameValueList)elementTextFontList.get(i)).getName();
        }        
      }
      for(int i=0;i<bodyTextFontList.size();i++){
        if(!( bodyText.equals(((NameValueList)elementTextFontList.get(i)).getName()))){
          bodyText=bodyText+","+((NameValueList)bodyTextFontList.get(i)).getName();
        }
      }
      for(int i=0;i<headingsFontList.size();i++){
        if(!( headings.equals(((NameValueList)elementTextFontList.get(i)).getName()))){
          headings=headings+","+((NameValueList)headingsFontList.get(i)).getName();
        }
      }
      for(int i=0;i<menuTextFontList.size();i++){
        if(!( menuText.equals(((NameValueList)elementTextFontList.get(i)).getName()))){
          menuText=menuText+","+((NameValueList)menuTextFontList.get(i)).getName();
        }
      }
    
    
      //**Style
      themeForm.getHdnStyle();    //Nothing to do with this code just for info
    
      //**Color Scheme
      themeForm.getHdnColorScheme(); //Nothing to do with this code just for info
   
      //**Fonts
    
      //Headings 
      styles.put(HTMLStyles.FONT_FAMILY, headings);
      styles.put(HTMLStyles.COLOR, themeForm.getHdnFontColorHeadings());

      //TH
      elements.add(new StyleClass(HTMLTags.TH,styles));
      elements.add(new StyleClass(HTMLTags.A_ACTIVE,styles));
      elements.add(new StyleClass(HTMLTags.A_HOVER,styles));
      elements.add(new StyleClass(HTMLTags.A_LINK,styles));
      elements.add(new StyleClass(HTMLTags.A_VISITED,styles));
      elements.add(new StyleClass("dropMenuover",styles,StyleTypes.CLASS));

      styles.clear();
    
      //Body Text
      styles.put(HTMLStyles.FONT_FAMILY,bodyText);
      styles.put(HTMLStyles.COLOR, themeForm.getHdnFontColorBodyText());

      elements.add(new StyleClass(HTMLTags.TD,styles));
      elements.add(new StyleClass("tree",HTMLTags.A_ACTIVE,styles,StyleTypes.CLASS));
      elements.add(new StyleClass("tree",HTMLTags.A_HOVER,styles,StyleTypes.CLASS));
      elements.add(new StyleClass("tree",HTMLTags.A_LINK,styles,StyleTypes.CLASS));
      elements.add(new StyleClass("tree",HTMLTags.A_VISITED,styles,StyleTypes.CLASS));
      elements.add(new StyleClass("dataLink",HTMLTags.A_ACTIVE,styles,StyleTypes.CLASS));
      elements.add(new StyleClass("dataLink",HTMLTags.A_HOVER,styles,StyleTypes.CLASS));
      elements.add(new StyleClass("dataLink",HTMLTags.A_LINK,styles,StyleTypes.CLASS));
      elements.add(new StyleClass("dataLink",HTMLTags.A_VISITED,styles,StyleTypes.CLASS));
    
      styles.clear();

      //Menu Text
      styles.put(HTMLStyles.FONT_FAMILY, menuText);
      styles.put(HTMLStyles.COLOR, themeForm.getHdnFontColorMenuText());

      elements.add(new StyleClass("menu",HTMLTags.A_ACTIVE,styles,StyleTypes.CLASS));
      elements.add(new StyleClass("menu",HTMLTags.A_HOVER,styles,StyleTypes.CLASS));
      elements.add(new StyleClass("menu",HTMLTags.A_LINK,styles,StyleTypes.CLASS));
      elements.add(new StyleClass("menu",HTMLTags.A_VISITED,styles,StyleTypes.CLASS));
      elements.add(new StyleClass("tabText",styles,StyleTypes.CLASS));
      elements.add(new StyleClass("tabTextLink",HTMLTags.A_ACTIVE,styles,StyleTypes.CLASS));
      elements.add(new StyleClass("tabTextLink",HTMLTags.A_HOVER,styles,StyleTypes.CLASS));
      elements.add(new StyleClass("tabTextLink",HTMLTags.A_LINK,styles,StyleTypes.CLASS));
      elements.add(new StyleClass("tabTextLink",HTMLTags.A_VISITED,styles,StyleTypes.CLASS));
      elements.add(new StyleClass("dropMenu",styles,StyleTypes.CLASS));

      styles.clear();
    
      //Element Text
    
      styles.put(HTMLStyles.FONT_FAMILY, elementText);
      styles.put(HTMLStyles.COLOR, themeForm.getHdnFontColorElementText());

      elements.add(new StyleClass("buttons",styles,StyleTypes.CLASS));

      styles.put(HTMLStyles.BACKGROUND_COLOR, themeForm.getHdnFontColorElementBg());

      elements.add(new StyleClass("componentStyle",styles,StyleTypes.CLASS));

      styles.clear();

      //**Page Background Color or Image
      if( themeForm.getRadBackground().equals( new String("color"))){
        styles.put(HTMLStyles.BACKGROUND_COLOR, themeForm.getHdnBackgroundColor());
      }else{
        styles.put(HTMLStyles.BACKGROUND_IMAGE, themeForm.getHdnBackgroundImage());      
      }

      elements.add(new StyleClass(HTMLTags.BODY,styles));
      logger.info("Exiting  getStyleElementList()" );
    }catch(Exception e){
      logger.error("Exception Caught @  getStyleElementList()" );
      throw e;
    }
    return elements;
  }
  /**
   * Purpose : To Retrives StyleNames and Values for a Given Locale from the file "WEB-INF/theme_xmls/style_colorscheme.xml"
   * @param locale -  a string object defines the locale 
   * @param xmlFilePath -  a string object
   * @return  HashTable 
   */
  public static ArrayList getStyles(String locale, String xmlFilePath) throws DOMException,SAXException,IOException,Exception {
    ArrayList styles=null;
    Document document=null;
    DocumentBuilderFactory documentBuilderFactory = null;
    DocumentBuilder documentBuilder= null;
    Element  rootElement=null;
    NodeList localeNodeList=null;
    NodeList styleNodeList=null;
    Node styleNode=null;
    try{
      logger.info("Entering getStyles()");  
      if(locale==null){
        locale="";
      }
      if (locale.trim().length()==0 ){
        locale="en";
      }
      styles= new ArrayList();
      documentBuilderFactory = DocumentBuilderFactory.newInstance() ;
      documentBuilder= documentBuilderFactory.newDocumentBuilder();
      document = documentBuilder.parse(new File(xmlFilePath + "WEB-INF"+File.separator+"theme_xmls"+File.separator+"style_colorscheme.xml"));
      rootElement=document.getDocumentElement();
      localeNodeList=rootElement.getElementsByTagName("locale");
      for(int i=0; i<localeNodeList.getLength();i++){
        styleNodeList = localeNodeList.item(i).getChildNodes();
        if (localeNodeList.item(i).getAttributes().getNamedItem("name").getNodeValue().equals(new String(locale)) ){ 
          for(int j=0; j<styleNodeList.getLength();j++){
            styleNode=styleNodeList.item(j);   
            if(styleNode.getNodeType()==Node.ELEMENT_NODE){ 
                styles.add(new NameValueList(styleNode.getAttributes().getNamedItem("name").getNodeValue(),styleNode.getAttributes().getNamedItem("value").getNodeValue()));
            }
          }
        }
      }
    logger.info("Exiting getStyles()");  
    }catch(DOMException e){
      logger.error("DOMException Caught @  getStyles()" );
      throw e;      
    }catch(SAXException e){
      logger.error("SAXException Caught @  getStyles()" );
      throw e;      
    }catch(IOException e){
      logger.error("IOException Caught @  getStyles()" );
      throw e;      
    }catch(Exception e){
      logger.error("Exception Caught @  getStyles()" );
      throw e;      
    }    
    return styles;
  }

  /**
   * Purpose : To Retrives Color Scheme Name and Values StyleNames and Values for a Given Locale and Style from the file "WEB-INF/theme_xmls/style_colorscheme.xml"
   * @param locale -  a string object defines the locale 
   * @param style -  a string object defines the style
   * @param xmlFilePath -  a string object
   * @return  HashTable 
   */
  public static ArrayList getColorSchemes(String locale, String xmlFilePath) throws DOMException,SAXException,IOException,Exception{
    ArrayList styleWithColorSchemes=null;
    ArrayList colorSchemes=null;
    ArrayList colorSchemeProperties=null;
    Document document=null;
    DocumentBuilderFactory documentBuilderFactory = null;
    DocumentBuilder documentBuilder= null;
    Element  rootElement=null;
    NodeList localeNodeList=null;
    NodeList styleNodeList=null;
    NodeList colorSchemeNodeList=null;
    Node styleNode=null;
    Node colorSchemeNode=null;
    try{
      logger.info("Entering getColorSchemes()");  
      if(locale==null){
        locale="";
      }
      if (locale.trim().length()==0 ){
        locale="en";
      }
      colorSchemes= new ArrayList();
      styleWithColorSchemes= new ArrayList();
      colorSchemeProperties= new ArrayList();
      documentBuilderFactory = DocumentBuilderFactory.newInstance() ;
      documentBuilder= documentBuilderFactory.newDocumentBuilder();
      document = documentBuilder.parse(new File(xmlFilePath + "WEB-INF"+File.separator+"theme_xmls"+File.separator+"style_colorscheme.xml"));
      rootElement=document.getDocumentElement();
      localeNodeList=rootElement.getElementsByTagName("locale");
      for(int i=0; i<localeNodeList.getLength();i++){
        styleNodeList = localeNodeList.item(i).getChildNodes();
        if (localeNodeList.item(i).getAttributes().getNamedItem("name").getNodeValue().equals(new String(locale)) ){ 
          for(int j=0; j<styleNodeList.getLength();j++){
            styleNode=styleNodeList.item(j);   
            if(styleNode.getNodeType()==Node.ELEMENT_NODE){ 
              colorSchemeNodeList=styleNode.getChildNodes();
              colorSchemes= new ArrayList();
              for(int k=0; k<colorSchemeNodeList.getLength();k++){  
                colorSchemeNode=colorSchemeNodeList.item(k);
                if(colorSchemeNode.getNodeType()==Node.ELEMENT_NODE){
                  colorSchemeProperties= new ArrayList();
                  colorSchemeProperties.add(new NameValueList("Heading",colorSchemeNode.getAttributes().getNamedItem("heading").getNodeValue()));
                  colorSchemeProperties.add(new NameValueList("Body",colorSchemeNode.getAttributes().getNamedItem("body").getNodeValue()));
                  colorSchemeProperties.add(new NameValueList("Menu",colorSchemeNode.getAttributes().getNamedItem("menu").getNodeValue()));
                  colorSchemeProperties.add(new NameValueList("Element",colorSchemeNode.getAttributes().getNamedItem("element").getNodeValue()));
                  colorSchemes.add(new NameValueList(colorSchemeNode.getAttributes().getNamedItem("name").getNodeValue(),colorSchemeNode.getAttributes().getNamedItem("value").getNodeValue(),colorSchemeProperties));
                }
              }
              styleWithColorSchemes.add(new NameValueList(styleNodeList.item(j).getAttributes().getNamedItem("name").getNodeValue(),colorSchemes));
            }
            
          }
        }
      }
    logger.info("Exiting getColorSchemes()");    
    }catch(DOMException e){
      logger.error("DOMException Caught @  getColorSchemes()" );
      throw e;      
    }catch(SAXException e){
      logger.error("SAXException Caught @  getColorSchemes()" );
      throw e;      
    }catch(IOException e){
      logger.error("IOException Caught @  getColorSchemes()" );
      throw e;      
    }catch(Exception e){
      logger.error("Exception Caught @  getColorSchemes()" );
      throw e;
    }    
    return styleWithColorSchemes;
  }

  /**
   * Purpose : To Retrives Font Names and Values for a Given Locale from the file "WEB-INF/theme_xmls/fonts.xml"
   * @param locale -  a string object defines the locale 
   * @param xmlFilePath -  a string object
   * @return  an ArrayList Object
   */
  public static ArrayList getFonts(String locale, String xmlFilePath)throws DOMException,SAXException,IOException,Exception {
    ArrayList fonts=null;
    Document document=null;
    DocumentBuilderFactory documentBuilderFactory = null;
    DocumentBuilder documentBuilder= null;
    Element  rootElement=null;
    NodeList localeNodeList=null;
    NodeList fontNodeList=null;
    Node fontNode=null;
    try{
      logger.info("Entering getFonts()");  
      if(locale==null){
        locale="";
      }
      if (locale.trim().length()==0 ){
        locale="en";
      }
      fonts= new ArrayList();
      documentBuilderFactory = DocumentBuilderFactory.newInstance() ;
      documentBuilder= documentBuilderFactory.newDocumentBuilder();
      document = documentBuilder.parse(new File(xmlFilePath + "WEB-INF"+File.separator+"theme_xmls"+File.separator+"fonts.xml"));
      rootElement=document.getDocumentElement();
      localeNodeList=rootElement.getElementsByTagName("locale");
      
      for(int i=0; i<localeNodeList.getLength();i++){
        fontNodeList = localeNodeList.item(i).getChildNodes();
        if (localeNodeList.item(i).getAttributes().getNamedItem("name").getNodeValue().equals(new String(locale)) ){ 
          for(int j=0; j<fontNodeList.getLength();j++){
            fontNode=fontNodeList.item(j);   
            if(fontNode.getNodeType()==Node.ELEMENT_NODE){ 
                fonts.add(new NameValueList(fontNode.getAttributes().getNamedItem("name").getNodeValue(),fontNode.getAttributes().getNamedItem("value").getNodeValue()))  ; 
            }
          }
        }
      }
    logger.info("Exiting getFonts()");    
    }catch(DOMException e){
      logger.error("DOMException Caught @  getFonts()" );
      throw e;
    }catch(SAXException e){
      logger.error("SAXException Caught @  getFonts()" );
      throw e;      
    }catch(IOException e){
      logger.error("IOException Caught @  getFonts()" );
      throw e;      
    }catch(Exception e){
      logger.error("Exception Caught @  getFonts()" );
      throw e;      
    }    
    
    return fonts;
  }

  /**
   * Purpose : To Retrieve StyleTagPlaceHolder String 
   * @param themeNewEditForm - A ThemeNewEditForm object
   * @param themeName - A String Object defining name of the theme
   * @return A String Object defining style place holder
   */
  private static String getStylePlaceHolder(ThemeNewEditForm themeNewEditForm,String themeName)throws Exception,DbsException{
    String placeHolder=null;
    String style=null;
    String colorScheme=null;
    final String  PREFIX="<link href=\"themes/";
    final String  SUFFIX="\" rel=\"stylesheet\" type=\"text/css\">";
    DbsValueDefault dbsVd=null; 
    try{
      //check for Existance of Theme
      if(themeNewEditForm!=null){
        style=themeNewEditForm.getHdnStyle();
        colorScheme=themeNewEditForm.getHdnColorScheme();
      }else{
          themeName="Default";
          style="Standard" ;   
          colorScheme="Silver" ;   
      }
      style=style.toLowerCase();
      colorScheme=colorScheme.toLowerCase();
      placeHolder=PREFIX + style + "/" + style + ".css" + SUFFIX;
      placeHolder=placeHolder + "\n" + PREFIX + style + "/" + colorScheme + "/" + colorScheme + ".css" + SUFFIX;
      if (themeName!=null){
        placeHolder=placeHolder + "\n" + PREFIX + themeName.toLowerCase() + ".css" + SUFFIX;
      }
    }catch(Exception e){
      logger.error("Exception Caught @  .getPlaceHolder()" );
      throw e;
    }
    return placeHolder;      
  }

  /**
   * Purpose : To Retrieve preview Tag String 
   * @param themeNewEditForm - A ThemeNewEditForm object
   * @param themeName - A String Object defining name of the theme
   * @return A String Object defining style place holder for Preview
   */
  public static String getPreview(ThemeNewEditForm themeNewEditForm, String themeName) throws DbsException, Exception  {
    String preview=null;
    try{
      preview=getStylePlaceHolder(themeNewEditForm,themeName) + "\n";
    }catch(Exception e){
      logger.error("Exception Caught @  dms.web.beans.theme.Theme.getPreview()" );
      throw e;
    }
    return preview;
  }
  

  


  /**
   * Purpose : Public Class defined to get Name Value List Bean 
   */
  public static class NameValueList{

    private String name;
    private String value;
    private ArrayList list; 

   /**
     * Purpose : Contructs a NameValue  Object for given Name and Value
     * @param name - A String object 
     * @param value - A String object
     */
     public NameValueList(String name, String value){
      this.name=name;
      this.value=value;
    }

    /**
     * Purpose : Contructs a NameList  Object for given Name and List
     * @param name - A String object 
     * @param list - An ArrayList  object
     */
    public NameValueList(String name, ArrayList list){
      this.name=name;
      this.list=list;
    }

    /**
     * Purpose : Contructs a NameValueList  Object for given Name, Value and List
     * @param name - A String object 
     * @param value -  A String object
     * @param list - An ArrayList  object
     */
    public NameValueList(String name, String value, ArrayList list){
      this.name=name;
      this.value=value;
      this.list=list;
    }

    /**
     * Purpose : Gets the Name of the NameValue, NameList or NameValueList objects
     * @return - A String object 
     */
    public String getName(){
      return this.name;
    }

    /**
     * Purpose : Gets the value of the NameValue or NameValueList objects
     * @return - A String object 
     */
    public String getValue(){
      return this.value;
    }

    /**
     * Purpose : Gets the list of the NameList or NameValueList objects
     * @return - A String object 
     */
    public ArrayList getList(){
      return this.list;
    }

    /**
     * Purpose : Sets the Name for the NameValue, NameList or NameValueList objects
     * @param name - A String object 
     */
    public void setName(String name){
      this.name=name;
    }

    /**
     * Purpose : Sets the Value for the NameValue or NameValueList objects
     * @param value - A String object 
     */
    public void setValue(String value){
      this.value=value;
    }

    /**
     * Purpose : Sets the List for the NameList or NameValueList objects
     * @param list - A String object 
     */
    public void setList(ArrayList list){
      this.list=list;
    }

  }

  /**
   * Purpose : A class to construct  the StyleTag Place Holder 
   */
  
  public static class StyleTagPlaceHolder{
    private Theme theme=null;
    private String themeName=null;
    private ThemeNewEditForm themeNewEditForm=null;
    private DbsLibrarySession dbsLibrarySession=null;

    /**
     * Purpose : Contructs a StyleTagPlaceHolder  Object for Given DbsLibrarysession and Theme Name
     * @param dbsLibrarySession - A DbsLibrarysession object 
     * @param themeName - A String Object defining name of the theme
     */
    public StyleTagPlaceHolder(DbsLibrarySession dbsLibrarySession, String themeName)throws DbsException, Exception{
      try{
        this.themeName=themeName;
        this.theme= new Theme(dbsLibrarySession);
        this.dbsLibrarySession=dbsLibrarySession;
        this.themeNewEditForm=this.theme.getThemeProperties(this.themeName) ;     
        }catch(DbsException e){
          logger.error("DbsException Caught @  " + this.getClass().getName() + ".listThemes()" );
          throw e;
        }catch(Exception e){
          logger.error("Exception Caught @  " + this.getClass().getName() + ".listThemes()" );
          throw e;
        }
    }

    /**
     * Purpose : To Retrieve StyleTagPlaceHolder String 
     * @return A String Object defining style place holder
     */
     
    public String getStylePlaceHolder()throws DbsException, Exception {
      String placeHolder=null;
      try{
        placeHolder=Theme.getStylePlaceHolder(this.themeNewEditForm,this.themeName);
      }catch(DbsException e){
        logger.error("DbsException Caught @  " + this.getClass().getName() + ".listThemes()" );
        throw e;
      }catch(Exception e){
        logger.error("Exception Caught @  " + this.getClass().getName() + ".listThemes()" );
        throw e;
      }
      return placeHolder;      
    }

    /**
     * Purpose : To Retrieve TreeIconPath String 
     * @return A String Object defining Tree Icon Path 
     */
    
    public String getTreeIconPath(){
      String treeIconPath="";
      if(this.themeNewEditForm!=null){
        treeIconPath="themes/"+this.themeNewEditForm.getHdnStyle().toLowerCase();
      }else{
        treeIconPath="themes/standard";
      }
      return treeIconPath;      
    }
  }
}
