package com.xjb.volley.toobox;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class MyTrustManager implements X509TrustManager{

	private static TrustManager[] trustManagers;  
    private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};  
  
    @Override  
    public void checkClientTrusted(  
            java.security.cert.X509Certificate[] x509Certificates, String s)  
            throws java.security.cert.CertificateException {  
        // To change body of implemented methods use File | Settings | File  
        // Templates.  
    }  
  
    @Override  
    public void checkServerTrusted(  
            java.security.cert.X509Certificate[] x509Certificates, String s)  
            throws java.security.cert.CertificateException {  
        // To change body of implemented methods use File | Settings | File  
        // Templates.  
    }  
  
    public boolean isClientTrusted(X509Certificate[] chain) {  
        return true;  
    }  
  
    public boolean isServerTrusted(X509Certificate[] chain) {  
        return true;  
    }  
  
    @Override  
    public X509Certificate[] getAcceptedIssuers() {  
        return _AcceptedIssuers;  
    }  
   /**
    * 不带证书的 允许所有
    */
    public static void allowAllSSL() {  
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				// TODO Auto-generated method stub
				return true;
			}  
  
  
        });  
  
        SSLContext context = null;  
        if (trustManagers == null) {  
            trustManagers = new TrustManager[] { new MyTrustManager() };  
        }  
  
        try {  
            context = SSLContext.getInstance("TLS");  
            context.init(null, trustManagers, new SecureRandom());  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        } catch (KeyManagementException e) {  
            e.printStackTrace();  
        }  
  
        HttpsURLConnection.setDefaultSSLSocketFactory(context  
                .getSocketFactory());  
    }
    
    /**
     * 带证书的  有待测试
     * @param in
     */
    public static void allowBksSSL(InputStream in){
    	HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				// TODO Auto-generated method stub
				return true;
			}  
  
  
        });  
  
        SSLContext context = null;  
        if (trustManagers == null) {  
            trustManagers = new TrustManager[] { new MyTrustManager() };  
        }  
    	try {
			
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
			KeyStore ks = KeyStore.getInstance("BKS");
			ks.load(in,"123456789yahA".toCharArray());
			tmf.init(ks);
			TrustManager tms[] = tmf.getTrustManagers();
			for(int i=0;i<tms.length;i++){
				if(tms[i] instanceof X509TrustManager){
					trustManagers = new TrustManager[]{(X509TrustManager) tms[i]};
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
  
        try {  
            context = SSLContext.getInstance("TLS");  
            context.init(null, trustManagers, new SecureRandom());  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        } catch (KeyManagementException e) {  
            e.printStackTrace();  
        }  
  
        HttpsURLConnection.setDefaultSSLSocketFactory(context  
                .getSocketFactory());  
    	
    	
    }

}
