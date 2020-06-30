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
 * $Id: FolderOperation.java,v 20040220.7 2006/03/13 14:18:28 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.filesystem;
/**
 *	Purpose: Constants used for filesystem operations 
 *  @author              Jeetendra Prasad
 *  @version             1.0
 * 	Date of creation:   20-01-2004
 * 	Last Modified by :     
 * 	Last Modified Date:    
 */

public class FolderOperation  {
    public final static byte NO_OPERATION = 1;
    public final static byte MENU_CLICK = 1;
    public final static byte GOTO = 2;
    public final static byte UP_FOLDER = 3;
    public final static byte FOLDER_CLICK = 4;
    public final static byte REFRESH = 5;
    public final static byte BACK = 6;
    public final static byte FORWARD = 7;    
    public final static byte COPY = 8;
    public final static byte MOVE = 9;
    public final static byte CHECKOUT = 10;
    public final static byte CHECKIN = 11;
    public final static byte NAVIGATE = 12;
}
