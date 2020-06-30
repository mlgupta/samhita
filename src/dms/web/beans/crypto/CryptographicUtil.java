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
 * $Id: CryptographicUtil.java,v 20040220.25 2006/03/13 14:18:45 suved Exp $
 *****************************************************************************
 */
package dms.web.beans.crypto;
/* dms package references */
import dms.beans.DbsDocument;
import dms.beans.DbsException;
/* Java API */
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Date;
import java.util.Hashtable;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
/* Logger API */
import org.apache.log4j.Logger;
/* BouncyCastle JCE API */
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.X509V3CertificateGenerator;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
/**
 *	Purpose:             Utility for performing cryptographic functions.
 *  @author              Mishra Maneesh 
 *  @version             1.0
 * 	Date of creation:    10-04-2004
 * 	Last Modified by :   Suved Mishra 
 * 	Last Modified Date:  07-03-2006 
 */
public class CryptographicUtil {
  /* member variables declaration */
  private RSAPublicKey publicKey = null; 
  private RSAPrivateCrtKey privateKey = null;
  private static final String SYMMETRIC_ALGO="Blowfish";
  private static final String ASYMMETRIC_ALGO="RSA";
  private static final String PROVIDER="BC";
  private static final String KEYSTORE_TYPE="BKS";
  private static final int BYTE_LENGTH=128;
  private static final String KEYSTORE_FILE_NAME=".keystore";
  private static final String PASSWORD_FILE_NAME=".password";
  private static final String PATH_SEPERATOR=File.separator;
  private static final String ENCRYPTED_FILE_PREFIX="encrypted_";
  private static final String DECRYPTED_FILE_PREFIX="decrypted_";
  private static final String KEYSTORE_DIR="UserKeystores";
  private static final String SYSTEM_KEYSTORE_DIR="SystemKeystore";
  private static final String KEYSTORE_NOT_PRESENT="KEYSTORE_NOT_PRESENT";
  private static final String PASSWORD_WRONG="PASSWORD_WRONG";
  private static final String SUCCESS="SUCCESS";
  private static final String FAILURE="FAILURE";      
  private static final String KEY_GENERATION_ERROR="KEY_GENERATION_ERROR";
  private static final String URL_FILE="URL_FILE";
  private Logger logger = Logger.getLogger("DbsLogger");

  /**
   * Purpose : Constructor for CryptographicUtil Class
   */
  public CryptographicUtil(){
    addProvider();
  }

  /**
   * Purpose : To add security provider 
   */
  private void addProvider(){
    java.security.Provider provider = new BouncyCastleProvider();
    int result = Security.addProvider(provider);
    if(result == -1){
      logger.info(" Provider entry already in file.\n");
    }else{
      logger.info(" Provider added at position: " + result);
    }
  }
  /**
   * Purpose : To generate public and private keys for asymmetric algorithm
   * @return : boolean true , if keys generated , else false
   */
  private boolean genKeys(){
    KeyPairGenerator keyGen = null;
    SecureRandom random = null;
    boolean keysGenerated=false;
    try{
      keyGen = KeyPairGenerator.getInstance(ASYMMETRIC_ALGO,PROVIDER);
      random = SecureRandom.getInstance("SHA1PRNG", "SUN");
      RSAKeyGenParameterSpec keySpec = new RSAKeyGenParameterSpec(1024,RSAKeyGenParameterSpec.F0);
      keyGen.initialize(keySpec, random);
      KeyPair pair = keyGen.generateKeyPair();
      publicKey = (RSAPublicKey)pair.getPublic();
      privateKey = (RSAPrivateCrtKey)pair.getPrivate();
      logger.debug("Generate Key process completed");
      keysGenerated=true;
    }catch (Exception e){
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error("No such Random/KeyPairGenerator algorithm or provider");
      logger.error(e.toString());
    }
    return keysGenerated;
  }    

  /**
   * Purpose : To save keystore generated for encryption / decryption
   * @return boolean true if generated and saved successfully , false otherwise
   * @param absolutePath
   * @param passwd
   */
  private boolean saveStore(String passwd,String absolutePath){       
    boolean keystoreSaved=false;
    ByteArrayInputStream bIn = null;
    try{
      KeyStore store = KeyStore.getInstance(KEYSTORE_TYPE,PROVIDER);
      store.load(null, null);            
      BigInteger  modulus = privateKey.getModulus();
      BigInteger  privateExponent = privateKey.getPrivateExponent();            
      Hashtable   attrs = new Hashtable();
      attrs.put(X509Principal.C, "IN");
      attrs.put(X509Principal.O, "DBSentry Solutions Pvt Ltd");
      attrs.put(X509Principal.L, "PUNE");
      attrs.put(X509Principal.ST, "MH");
      attrs.put(X509Principal.EmailAddress, "info@dbsentry.com");            
      X509V3CertificateGenerator  certGen = new X509V3CertificateGenerator();
      certGen.setSerialNumber(BigInteger.valueOf(1));
      certGen.setIssuerDN(new X509Principal(attrs));
      certGen.setNotBefore(new Date(System.currentTimeMillis() - 50000));
      certGen.setNotAfter(new Date(System.currentTimeMillis() + 50000));
      certGen.setSubjectDN(new X509Principal(attrs));
      certGen.setPublicKey(publicKey);
      certGen.setSignatureAlgorithm("MD5WithRSAEncryption");            
      java.security.cert.Certificate[]   chain = new java.security.cert.Certificate[1];           
      X509Certificate cert = certGen.generateX509Certificate(privateKey);
      cert.checkValidity(new Date());
      cert.verify(publicKey); 
      bIn = new ByteArrayInputStream(cert.getEncoded());
      CertificateFactory fact = CertificateFactory.getInstance("X.509", "BC");
      cert = (X509Certificate)fact.generateCertificate(bIn);
      chain[0] = cert;           
      store.setKeyEntry("private", privateKey, passwd.toCharArray(), chain);
      store.store(new FileOutputStream(absolutePath,false),passwd.toCharArray());
      keystoreSaved=true;
    }catch (Exception e){
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString());
    }finally{
      if( bIn != null ){
        try{
          bIn.close();
        }catch (Exception ex) {
          logger.error("Exception occurred in CryptographicUtil ...");
          logger.error(ex.toString());
        }
        bIn = null;
      }
    }
    return keystoreSaved;
  }

  /**
   * Purpose : To read the keystore generated
   * @return boolean true if read successfully , false otherwise
   * @param absolutePath
   * @param passwd
   */
  private boolean readKeyStore(String passwd,String absolutePath){
    boolean isKeystorePresent=false;
    try{           
      KeyStore store = KeyStore.getInstance("BKS","BC");            
      store.load(new FileInputStream(absolutePath),passwd.toCharArray());
      privateKey = (RSAPrivateCrtKey)store.getKey("private", passwd.toCharArray());
      java.security.cert.Certificate cert = store.getCertificateChain("private")[0];
      publicKey=(RSAPublicKey)cert.getPublicKey();
      isKeystorePresent=true;
    }catch (Exception e){
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString());
    }
    return isKeystorePresent;        
  }   

  /**
   * Purpose : To change the keystore password for a given user for encryption 
   *           and decryption purposes
   * @param 	userName
   * @param 	oldPass
   * @param 	newPass
   * @param 	path
   * @return  String indicating the result 
   */
  public String changeKeystorePass(String userName,String oldPass,String newPass,
                                   String path){
    try{
      String absolutePath=path+KEYSTORE_DIR+PATH_SEPERATOR+userName+
                          PATH_SEPERATOR+KEYSTORE_FILE_NAME;
      File keystoreFile=new File(absolutePath);
      if(!keystoreFile.exists()){
        return KEYSTORE_NOT_PRESENT;
      }
      if(readKeyStore(oldPass,absolutePath)){
        if(saveStore(newPass,absolutePath)){
          return SUCCESS;
        }else{
          return FAILURE;
        }
      }else{
        return PASSWORD_WRONG;
      }
    }catch(Exception exception){
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(exception.toString());
      return FAILURE;
    }
  }

  /**
   * Purpose : To set the keystore password for a given user for encryption 
   *           and decryption purposes
   * @param 	userName
   * @param 	newPass
   * @param 	path
   * @return  String indicating the result
   */
  public String setKeystorePass(String userName,String newPass,String path){
    try{
      String dirPath=path+KEYSTORE_DIR+PATH_SEPERATOR+userName;
      File dir=new File(dirPath);
      if(!dir.isDirectory()){
        dir.mkdirs();
      }         
      String absolutePath=path+KEYSTORE_DIR+PATH_SEPERATOR+userName+
                          PATH_SEPERATOR+KEYSTORE_FILE_NAME;
      if(genKeys()){
        if(saveStore(newPass,absolutePath)){
          return SUCCESS;
        }else{
          return FAILURE;
        }
      }else{
        return KEY_GENERATION_ERROR;
      }
    }catch(Exception exception){
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(exception.toString());
      return FAILURE;
    }
  }

  /**
   * Purpose : To set the keystore password for encryption and decryption of URL
   * @param	newPass
   * @param	path
   * @return  String indicating the result
   */
  public String setSystemKeystorePass(String newPass,String path){
    try{
      String dirPath=path+SYSTEM_KEYSTORE_DIR;
      File dir=new File(dirPath);
      if(!dir.isDirectory()){
        dir.mkdirs();
      }         
      String absolutePath=path+SYSTEM_KEYSTORE_DIR+PATH_SEPERATOR+
                          KEYSTORE_FILE_NAME;
      if(genKeys()){
        if(saveStore(newPass,absolutePath)){
          return SUCCESS;
        }else{
          return FAILURE;
        }
      }else{
        return KEY_GENERATION_ERROR;
      }
    }catch(Exception exception){
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(exception.toString());
      return FAILURE;
    }
  }

  /**
   * Purpose : To delete the keystore password for a given user for encryption 
   *           and decryption purposes
   * @param	userName
   * @param	path
   */
  public void deleteKeystore(String userName,String path){
    try{
      String dirPath=path+KEYSTORE_DIR+PATH_SEPERATOR+userName;
      File dir=new File(dirPath);
      if ((dir.exists())){
        // Deleting the files from  Folder
        File[] filesInDir=dir.listFiles();
        for(int index=0;index<filesInDir.length;index++){
          if (!(filesInDir[index].delete())){
            this.logger.error("Unable to Delete "+ filesInDir[index].getName() +
                              " File"); 
          }
        }
        // Deleting the Temp Folder
        if (!(dir.delete())){    
          this.logger.error("Unable to Delete "+ dirPath + " Directory"); 
        }else{
          this.logger.debug("Directory "+ dirPath + " Deleted");      
        }  
      }else{
        this.logger.error("Unable to Find "+dirPath+ " Directory"); 
      }
    }catch(Exception e){
      this.logger.error("Exception occurred in CryptographicUtil ...");
      this.logger.error(e.toString());       
    }
  }

  /**
   * Purpose : To get Public Key
   * @return RSAPublicKey
   */
  private RSAPublicKey getPublicKey() {
    return publicKey;
  }

  /**
   * Purpose : To get Private Key
   * @return RSAPrivateCrtKey
   */
  private RSAPrivateCrtKey getPrivateKey() {
    return privateKey;
  }

  /**
   * Purpose : To encrypt decrypt file 
   * @return String indicating result
   * @param cipher
   * @param fos
   * @param fis
   */
  private String encryptDecryptFile(InputStream fis,FileOutputStream fos,
                                    Cipher cipher){
    CipherInputStream cis = null;
    try{                      
      cis = new CipherInputStream(fis, cipher);
      byte[] b = new byte[2048];
      int i = cis.read(b);
      while (i != -1) {
        fos.write(b, 0, i);
        i = cis.read(b);
      }
    }catch(FileNotFoundException fe){
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(fe.toString());
      return "FILENOTFOUND";
    }catch(IOException ie){
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(ie.toString());
      return "IOEXCEPTION";
    }finally{
      if( cis!= null ){
        try{
          cis.close();
        }catch (Exception e) {
          logger.error("Exception occurred in CryptographicUtil ...");
          logger.error(e.toString()); 
        }
        cis = null;
      }
    }         
    return SUCCESS;       
  }

  /**
   * Purpose : To generate secret key
   * @return Key
   */
  private Key generateSecretKey(){
    Key key = null;
    try{
      SecureRandom rand = new SecureRandom();
      KeyGenerator kGen = KeyGenerator.getInstance(SYMMETRIC_ALGO);
      kGen.init(256, rand);
      key=kGen.generateKey();
      if(key!=null){
        logger.debug("key generated is not null");
      }else{
        logger.debug("key generated is null..");
      }
    }catch (NoSuchAlgorithmException e) {
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }catch(Exception ex ){
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(ex.toString()); 
    }
    return key;
  }

  /**
   * Purpose : To check existence of keystore for a given user for encryption 
   *           and decryption purposes
   * @param 	userName
   * @param 	passwd
   * @param 	path
   * @return  boolean true , if it exists else false
   */
  public boolean checkKeystore(String userName,String passwd,String path){
    String absolutePath=path+KEYSTORE_DIR+PATH_SEPERATOR+userName+PATH_SEPERATOR+
                        KEYSTORE_FILE_NAME;
    boolean isKeystorePresent=false;
    isKeystorePresent=readKeyStore(passwd,absolutePath);
    return isKeystorePresent;        
  }

  /**
   * Purpose : To generate keystore for a given user for encryption 
   *           and decryption purposes
   * @param 	userName
   * @param 	passwd
   * @param 	absolutePath
   * @return  boolean true , if it generated else false
   */
  private boolean generateKeyStore(String userName,String passwd,String absolutePath){
    boolean keystoreGenerated=false;
    boolean keysGenerated=genKeys();
    if(keysGenerated){
      keystoreGenerated = saveStore(passwd,absolutePath);
    }
    return keystoreGenerated;
  }

  /**
   * Purpose : To encrypt a given document 
   * @param 	toBeEncrypted
   * @param 	userName
   * @param 	passwd
   * @param 	path
   * @param 	createKeystore
   * @return encrypted inputStream
   */
  public InputStream encryptDoc(DbsDocument toBeEncrypted,String userName,
                     String passwd ,String path ){
    ByteArrayInputStream encryptedObject = null;
    String absolutePath=path+KEYSTORE_DIR+PATH_SEPERATOR+userName+PATH_SEPERATOR+
                        KEYSTORE_FILE_NAME;
    InputStream fis = null;
    FileInputStream encryptedStream = null;
    FileOutputStream fos = null;
    try{
      if(readKeyStore(passwd,absolutePath)==false){
        return encryptedObject;
      }
      Cipher c = Cipher.getInstance(ASYMMETRIC_ALGO,PROVIDER); 
      Key secretKey=generateSecretKey();
      c.init(Cipher.WRAP_MODE,getPublicKey());
  
      fis=toBeEncrypted.getContentStream();
      String filePathWithName=path+KEYSTORE_DIR+PATH_SEPERATOR+userName+
                              PATH_SEPERATOR+ENCRYPTED_FILE_PREFIX+
                              toBeEncrypted.getName();
      fos=new FileOutputStream(filePathWithName);
      fos.write(c.wrap(secretKey));
      Cipher encryptCipher = Cipher.getInstance(SYMMETRIC_ALGO,PROVIDER);                                 
      encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);
      String result=encryptDecryptFile(fis,fos,encryptCipher);
      //fis.close();
      //fos.close(); 
      if(result.equals(SUCCESS)){
        File file = new File(filePathWithName);
        encryptedStream=new FileInputStream(file);
        int availableBytes=encryptedStream.available();
        byte fileBytes[]=new byte[availableBytes];
        encryptedStream.read(fileBytes,0,availableBytes);
        encryptedObject=new ByteArrayInputStream(fileBytes);
        //encryptedStream.close();
        //encryptedStream = null;
        if(file.exists()){
          file.delete();
        }
      }
    }catch (NoSuchAlgorithmException e) {
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }catch (NoSuchProviderException e) {
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }catch (NoSuchPaddingException e) {
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }catch (InvalidKeyException e) {
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }catch (IllegalStateException e) {
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }catch(DbsException dbsExcep){
      logger.error("Exception occurred in CryptographicUtil ...");
     logger.error(dbsExcep.toString());   
    }catch (Exception e){
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }finally{
      /* close all the open streams */
      if( fis != null ){
        try{
          fis.close();
        }catch( Exception ex ){
          logger.error("Exception occurred in CryptographicUtil ...");
          logger.error(ex.toString()); 
        }
        fis = null;
      }
      if( fos != null ){
        try{
          fos.close();
        }catch( Exception ex ){
          logger.error("Exception occurred in CryptographicUtil ...");
          logger.error(ex.toString()); 
        }
        fos = null;
      }
      if( encryptedStream != null ){
        try{
          encryptedStream.close();
        }catch( Exception ex ){
          logger.error("Exception occurred in CryptographicUtil ...");
          logger.error(ex.toString()); 
        }
        encryptedStream = null;
      }
    }
    return encryptedObject;
  }
    //----------------Added by Suved--------------//
  /**
   * Purpose: added to account for encrypting userId,userpassword,docId in URL
   * @param path
   * @param passwd
   * @param userName
   * @param iStream
   * @return encrypted inputStream
   */
  public InputStream encryptString(InputStream iStream,String userName,
          String passwd ,String path ){
    ByteArrayInputStream encryptedObject=null;
    String absolutePath=path+SYSTEM_KEYSTORE_DIR+PATH_SEPERATOR+
                        KEYSTORE_FILE_NAME;
    InputStream fis = null;
    FileInputStream encryptedStream = null;
    FileOutputStream fos = null;
    try {
      if(readKeyStore(passwd,absolutePath)==false){
        return encryptedObject;
      }
      Cipher c = Cipher.getInstance(ASYMMETRIC_ALGO,PROVIDER); 
      Key secretKey=generateSecretKey();
      c.init(Cipher.WRAP_MODE,getPublicKey());
      
      fis=iStream;
      //logger.debug("fis :"+fis.toString());
      String filePathWithName=path+SYSTEM_KEYSTORE_DIR+PATH_SEPERATOR+
                              ENCRYPTED_FILE_PREFIX+URL_FILE;
      fos=new FileOutputStream(filePathWithName);
      //logger.debug("First   "+secretKey.getEncoded());
      fos.write(c.wrap(secretKey));
      Cipher encryptCipher = Cipher.getInstance(SYMMETRIC_ALGO,PROVIDER);                                 
      encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);
      String result=encryptDecryptFile(fis,fos,encryptCipher);
      //fis.close();
      //fos.close(); 
      if(result.equals(SUCCESS)){
        File file = new File(filePathWithName);
        encryptedStream=new FileInputStream(file);
        int availableBytes=encryptedStream.available();
        byte fileBytes[]=new byte[availableBytes];
        encryptedStream.read(fileBytes,0,availableBytes);
        encryptedObject=new ByteArrayInputStream(fileBytes);
        //encryptedStream.close();
        //encryptedStream = null;
        if(file.exists()){
          file.delete();
        }
      }
    }catch (NoSuchAlgorithmException e) {
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }catch (NoSuchProviderException e) {
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }catch (NoSuchPaddingException e) {
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }catch (InvalidKeyException e) {
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }catch (IllegalStateException e) {
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }catch (Exception e){
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }finally{
      /* close all the open streams */
      if( fis != null ){
        try{
          fis.close();
        }catch( Exception ex ){
          logger.error("Exception occurred in CryptographicUtil ...");
          logger.error(ex.toString()); 
        }
        fis = null;
      }
      if( fos != null ){
        try{
          fos.close();
        }catch( Exception ex ){
          logger.error("Exception occurred in CryptographicUtil ...");
          logger.error(ex.toString()); 
        }
        fos = null;
      }
      if( encryptedStream != null ){
        try{
          encryptedStream.close();
        }catch( Exception ex ){
          logger.error("Exception occurred in CryptographicUtil ...");
          logger.error(ex.toString()); 
        }
        encryptedStream = null;
      }
    }
    return encryptedObject;
  }


 /**
  * Purpose: added to account for decrypting userId,userpassword,docId in URL
  * @param path
  * @param passwd
  * @param iStream
  * @return decrypted inputStream
  */
  public InputStream decryptString(InputStream iStream,String passwd ,
                     String path ){
    String absolutePath=path+SYSTEM_KEYSTORE_DIR+PATH_SEPERATOR+
                        KEYSTORE_FILE_NAME;
    ByteArrayInputStream decryptedObject=null;
    InputStream fis=null;
    FileInputStream decryptedStream = null;
    FileOutputStream fos=null;
    try{
      if(readKeyStore(passwd,absolutePath)==false){
        return decryptedObject;
      }
      Cipher c = Cipher.getInstance(ASYMMETRIC_ALGO,PROVIDER); 
      Key secretKey=generateSecretKey();
      c.init(Cipher.UNWRAP_MODE,getPrivateKey());        
      fis=iStream;
      int available=fis.available();
      //logger.debug("fis.available in DecryptString(): "+available);
      String filePathWithName=path+SYSTEM_KEYSTORE_DIR+PATH_SEPERATOR+
                              DECRYPTED_FILE_PREFIX+URL_FILE;
      fos=new FileOutputStream(filePathWithName);
      byte[] wrappedKey=new byte[BYTE_LENGTH];
      fis.read(wrappedKey,0,wrappedKey.length);
      Key unWrappedKey=c.unwrap(wrappedKey,SYMMETRIC_ALGO,Cipher.SECRET_KEY); 
      //logger.debug("Two   "+unWrappedKey.getEncoded());
      Cipher decryptCipher = Cipher.getInstance(SYMMETRIC_ALGO,PROVIDER);                                 
      decryptCipher.init(Cipher.DECRYPT_MODE, unWrappedKey);
      String result=encryptDecryptFile(fis,fos,decryptCipher);
      //fis.close();
      //fos.close(); 
      if(result.equals(SUCCESS)){
        File file = new File(filePathWithName);
        decryptedStream=new FileInputStream(file);
        int availableBytes=decryptedStream.available();
        byte fileBytes[]=new byte[availableBytes];
        decryptedStream.read(fileBytes,0,availableBytes);
        decryptedObject=new ByteArrayInputStream(fileBytes); 
        if(file.exists()){
          file.delete();
        }
        //decryptedStream.close();
      }  
    }catch (NoSuchAlgorithmException e) {
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }catch (NoSuchProviderException e) {
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }catch (NoSuchPaddingException e) {
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }catch (InvalidKeyException e) {
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }catch (IllegalStateException e) {
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }catch (Exception e){
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }finally{
      /* close all the open streams */
      if( fis != null ){
        try{
          fis.close();
        }catch( Exception ex ){
          logger.error("Exception occurred in CryptographicUtil ...");
          logger.error(ex.toString()); 
        }
        fis = null;
      }
      if( fos != null ){
        try{
          fos.close();
        }catch( Exception ex ){
          logger.error("Exception occurred in CryptographicUtil ...");
          logger.error(ex.toString()); 
        }
        fos = null;
      }
      if( decryptedStream != null ){
        try{
          decryptedStream.close();
        }catch( Exception ex ){
          logger.error("Exception occurred in CryptographicUtil ...");
          logger.error(ex.toString()); 
        }
        decryptedStream = null;
      }
    }
    if(decryptedObject!=null){
      logger.debug("decryptedObject: "+"is not null");
    }else{
      logger.debug("decryptedObject: "+"is null");
    }
    return decryptedObject;
  }
    //----------------Added by Suved-----------------//
  /**
   * Purpose: added to decrypt a document that is already encrypted 
   * @param 	toBeDecrypted
   * @param 	userName
   * @param 	passwd
   * @param 	path
   * @return  decrypted inputStream
   */
  public InputStream decryptDoc(DbsDocument toBeDecrypted,String userName,
                     String passwd ,String path ){
    String absolutePath=path+KEYSTORE_DIR+PATH_SEPERATOR+userName+PATH_SEPERATOR+
                        KEYSTORE_FILE_NAME;
    ByteArrayInputStream decryptedObject=null;
    InputStream fis=null;
    FileInputStream decryptedStream = null;
    FileOutputStream fos=null;
    try{
      if(readKeyStore(passwd,absolutePath)==false){
        return decryptedObject;
      }
      Cipher c = Cipher.getInstance(ASYMMETRIC_ALGO,PROVIDER); 
      Key secretKey=generateSecretKey();
      c.init(Cipher.UNWRAP_MODE,getPrivateKey());        
      fis=toBeDecrypted.getContentStream();
      String filePathWithName=path+KEYSTORE_DIR+PATH_SEPERATOR+userName+
                              PATH_SEPERATOR+DECRYPTED_FILE_PREFIX+
                              toBeDecrypted.getName();
      fos=new FileOutputStream(filePathWithName);
      byte[] wrappedKey=new byte[BYTE_LENGTH];
      fis.read(wrappedKey,0,wrappedKey.length);
      Key unWrappedKey=c.unwrap(wrappedKey,SYMMETRIC_ALGO,Cipher.SECRET_KEY);            
      Cipher decryptCipher = Cipher.getInstance(SYMMETRIC_ALGO,PROVIDER);                                 
      decryptCipher.init(Cipher.DECRYPT_MODE, unWrappedKey);
      String result=encryptDecryptFile(fis,fos,decryptCipher);
      //fis.close();
      //fos.close(); 
      if(result.equals(SUCCESS)){
        File file = new File(filePathWithName);
        decryptedStream=new FileInputStream(file);
        int availableBytes=decryptedStream.available();
        byte fileBytes[]=new byte[availableBytes];
        decryptedStream.read(fileBytes,0,availableBytes);
        decryptedObject=new ByteArrayInputStream(fileBytes); 
        if(file.exists()){
          file.delete();
        }
        //decryptedStream.close();
      }  
    }catch (NoSuchAlgorithmException e) {
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }catch (NoSuchProviderException e) {
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }catch (NoSuchPaddingException e) {
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }catch (InvalidKeyException e) {
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }catch (IllegalStateException e) {
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }catch(DbsException dbsExcep){
      logger.error("Exception occurred in CryptographicUtil ...");
     logger.error(dbsExcep.toString());   
    }catch (Exception e){
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString()); 
    }finally{
      /* close all the open streams */
      if( fis != null ){
        try{
          fis.close();
        }catch( Exception ex ){
          logger.error("Exception occurred in CryptographicUtil ...");
          logger.error(ex.toString()); 
        }
        fis = null;
      }
      if( fos != null ){
        try{
          fos.close();
        }catch( Exception ex ){
          logger.error("Exception occurred in CryptographicUtil ...");
          logger.error(ex.toString()); 
        }
        fos = null;
      }
      if( decryptedStream != null ){
        try{
          decryptedStream.close();
        }catch( Exception ex ){
          logger.error("Exception occurred in CryptographicUtil ...");
          logger.error(ex.toString()); 
        }
        decryptedStream = null;
      }
    }     
    return decryptedObject;
  }

  /**
   * Purpose: added to obtain passwordfile path 
   * @param : contextPath
   * @return  String passwordfile path
   **/
  public String getPwdFilePath(String contextPath){
    String pwdFilePath=new String();
    pwdFilePath=contextPath+SYSTEM_KEYSTORE_DIR+PATH_SEPERATOR+PASSWORD_FILE_NAME;
    return pwdFilePath;
  }

  /**
   * Purpose: added to check the existence of .keyStore as well as .password 
   *          in SystemKeystore 
   * @param : contextPath 
   * @return  boolean true if .keyStore as well as .password exist else false 
   **/
  public boolean getSystemKeyStoreFile(String contextPath){         
    boolean keyStoreExists=false;
    try{
      String filePath=contextPath+SYSTEM_KEYSTORE_DIR;
      File keyStoreDir=new File(filePath);
      if(keyStoreDir.isDirectory()){
      
      }else if(keyStoreDir.isFile()){
        keyStoreDir.delete();
        keyStoreDir.mkdirs();
      }else{
        keyStoreDir.mkdirs();
      }
      filePath=contextPath+SYSTEM_KEYSTORE_DIR+PATH_SEPERATOR+KEYSTORE_FILE_NAME;
      String pwdFilePath=new String();
      pwdFilePath=contextPath+SYSTEM_KEYSTORE_DIR+PATH_SEPERATOR+
                  PASSWORD_FILE_NAME;
      File keyStore=new File(filePath);
      File pwdFile=new File(pwdFilePath);
      if(keyStore.exists() && pwdFile.exists() ){
        keyStoreExists=true;
      }         
    }catch(Exception exception){
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(exception.toString());            
    }
    logger.debug("keyStoreExists: "+keyStoreExists);
    return keyStoreExists;
  }

   /**
    * Purpose: added to obtain .keyStore path in SystemKeystore 
    * @param : contextPath
    * @return  String keystore file path
    **/
  public String getSystemKeyStorePath(String contextPath){
    String path=new String("");
    try{
      path=contextPath+SYSTEM_KEYSTORE_DIR+PATH_SEPERATOR+KEYSTORE_FILE_NAME;
    }catch(Exception e){
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(e.toString());            
    }
    return path;
  }

  /**
   * Purpose: added to obtain  cryptoPassword for URL Encrytion/Decryption
   * @param : passwordFilePath
   * @return  String password file path
   **/
  public String getCryptoPassword(String pwdFilePath){
    String cryptoPassword=new String();
    FileReader fr = null;
    BufferedReader br = null;
    try{
      fr=new FileReader(pwdFilePath);
      br=new BufferedReader(fr);
      StringBuffer sb=new StringBuffer();
      while((cryptoPassword=br.readLine())!=null){
        //logger.debug(cryptoPassword);
        sb.append(cryptoPassword);
      }
      //logger.debug("sb: "+sb.toString());
      cryptoPassword=sb.toString();
      //logger.debug("cryptoPassword: "+cryptoPassword);
      //fr.close();
    }catch(Exception ex){        
      logger.error("Exception occurred in CryptographicUtil ...");
      logger.error(ex.toString());
    }finally{
      /* close all the open streams */
      if( fr != null ){
        try{
          fr.close();
        }catch( Exception ex ){
          logger.error("Exception occurred in CryptographicUtil ...");
          logger.error(ex.toString()); 
        }
        fr = null;
      }
      if( br != null ){
        try{
          br.close();
        }catch( Exception ex ){
          logger.error("Exception occurred in CryptographicUtil ...");
          logger.error(ex.toString()); 
        }
        br = null;
      }
    }
    return cryptoPassword;
  }

}
