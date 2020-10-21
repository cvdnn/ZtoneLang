/*
 * Utils.java
 * 
 * Copyright 2011 sillar team, Inc. All rights reserved.
 * 
 * SILLAR PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package android.xml;

import android.text.TextUtils;
import android.util.Log;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * 
 * @author	ztone team
 * @version	1.0.0
 * @since	1.0.0	Handy	2012-6-10
 */
public final class XMLUtils {
	private static final String TAG = "DocumentHelper";
	
	private static DocumentBuilder documentBuilder;
	
	public static Document parse(File file){
		Document doc=null;
		
		if(file!=null && file.exists()){
			try {
				DocumentBuilder builder=getDocumentBuilder();
				if(builder!=null){
					builder.parse(file);
				}
			}catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
		
		return doc;
	}
	
	public static Document parse(String strUri){
		Document doc=null;
		
		if(!TextUtils.isEmpty(strUri)){
			try {
				DocumentBuilder builder=getDocumentBuilder();
				if(builder!=null){
					builder.parse(strUri);
				}
			}catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
		
		return doc;
	}
	
	public static Document parse(InputStream is){
		Document doc=null;
		
		if(is!=null){
			try {
				DocumentBuilder builder=getDocumentBuilder();
				if(builder!=null){
					builder.parse(is);
				}
			}catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
		
		return doc;
	}
	
	public static Document parse(InputSource is){
		Document doc=null;
		
		if(is!=null){
			try {
				DocumentBuilder builder=getDocumentBuilder();
				if(builder!=null){
					builder.parse(is);
				}
			}catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
		
		return doc;
	}
	
	public static DocumentBuilder getDocumentBuilder(){
		if(documentBuilder!=null){
			synchronized (XMLUtils.class) {
				if(documentBuilder!=null){
					try {
						documentBuilder= DocumentBuilderFactory.newInstance().newDocumentBuilder();
					} catch (Exception e) {
						Log.e(TAG, e.getMessage(), e);
					}
				}
			}
		}
		
		return documentBuilder;
	}
}
