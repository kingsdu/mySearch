/* HostnameQueueAssignmentPolicy
 *
 * $Id: HostnameQueueAssignmentPolicy.java 3838 2005-09-21 23:00:47Z gojomo $
 *
 * Created on Oct 5, 2004
 *
 * Copyright (C) 2004 Internet Archive.
 *
 * This file is part of the Heritrix web crawler (crawler.archive.org).
 *
 * Heritrix is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * any later version.
 *
 * Heritrix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with Heritrix; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */ 
package org.archive.crawler.frontier;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.httpclient.URIException;
import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.framework.CrawlController;
import org.archive.net.UURI;
import org.archive.net.UURIFactory;

/**
 * QueueAssignmentPolicy based on the hostname:port evident in the given
 * CrawlURI.
 * 
 * @author gojomo
 */
public class ELFHashQueueAssignmentPolicy extends QueueAssignmentPolicy {
	private static final Logger logger = Logger
			.getLogger(ELFHashQueueAssignmentPolicy.class.getName());
	/**
	 * When neat host-based class-key fails us
	 */
	private static String DEFAULT_CLASS_KEY = "default...";

	private static final String DNS = "dns";

	public String getClassKey(CrawlController controller, CandidateURI cauri) {
		String scheme = cauri.getUURI().getScheme();
		String candidate = null;
		try {
			if (scheme.equals(DNS)){
				if (cauri.getVia() != null) {
					// Special handling for DNS: treat as being
					// of the same class as the triggering URI.
					// When a URI includes a port, this ensures 
					// the DNS lookup goes atop the host:port
					// queue that triggered it, rather than 
					// some other host queue
					UURI viaUuri = UURIFactory.getInstance(cauri.flattenVia());
					candidate = viaUuri.getAuthorityMinusUserinfo();
					// adopt scheme of triggering URI
					scheme = viaUuri.getScheme();
				} else {
					candidate= cauri.getUURI().getReferencedHost();
				}
			} else {
				String uri = cauri.getUURI().toString();
				long hash = Hflp(uri);
				candidate = Long.toString(hash % 100);
				//                candidate =  cauri.getUURI().getAuthorityMinusUserinfo();
			}

			if(candidate == null || candidate.length() == 0) {
				candidate = DEFAULT_CLASS_KEY;
			}
		} catch (URIException e) {
			logger.log(Level.INFO,
					"unable to extract class key; using default", e);
			candidate = DEFAULT_CLASS_KEY;
		}
		if (scheme != null && scheme.equals(UURIFactory.HTTPS)) {
			// If https and no port specified, add default https port to
			// distinguish https from http server without a port.
			if (!candidate.matches(".+:[0-9]+")) {
				candidate += UURIFactory.HTTPS_PORT;
			}
		}
		// Ensure classKeys are safe as filenames on NTFS
		return candidate.replace(':','#');
	}



	public long ELFHash(String str) {
		long hash = 0;
		long x = 0;
		for (int i = 0; i < str.length(); i++) {
			hash = (hash << 4) + str.charAt(i);
			if ((x = hash & 0xF0000000L) != 0) {
				hash ^= (x >> 24);
				hash &= ~x;
			}
		}
		return (hash & 0x7FFFFFFF);
	}

	
	/*
	 *  Hflp散列函数
	 */
	public static long Hflp (String str) {
		byte[] url = str.getBytes();
		long n = 0;
        byte[] bytes = intTobyte(n);
        for (int i= 0; i < url.length; i++) {
            bytes[i % 4] ^= url[i]; 
        }
        n = byteToInt(bytes);
        return n % 0x7FFFFFFF;        
    }
     
    public static byte[] intTobyte (long i) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (i & 0xFF);
        bytes[1] = (byte) ((i >> 8) & 0xFF);
        bytes[2] = (byte) ((i >> 16) & 0xFF);
        bytes[3] = (byte) ((i >> 24) & 0xFF);
        return bytes;
    }
     
    public static int byteToInt (byte[] bytes) {
        int a = bytes[0] & 0xFF;
        int b = (bytes[1] & 0xFF) << 8;
        int c = (bytes[2] & 0xFF) << 16;
        int d = (bytes[3] & 0xFF) << 24;
        return a+b+c+d;
    }

//	public static void main(String[] args) {
//		String url = "www.baidu.com";
//		byte[] u = url.getBytes();
//		Hflp(u);
//	}
//	public static void HfIp(String url)
//	{
//		int n=0;
//		String s = String.valueOf(n);
//		char[] array = s.toCharArray();
//		for (int i=0; i<url.length(); i++){
//			System.out.println(url.length());
//			array[i%4]^=url.charAt(i);
//		}
//		int a = n % 0x7FFFFFFF;
//		System.out.println(array[0]);
//		System.out.println(n);
//	}
	
//	public static int Hflp (char[] url) {
//	    int n = 0;
//	    char[] chars = intTochar(n);
//	    for (int i= 0; i < url.length; i++) {
//	        chars[i % 4] ^= url[i]; 
//	        n = charToInt(url[i]);
//	    }
////	    n = charToInt(chars);
//	    return n % 0x7FFFFFFF;	     
//	}
//	
//	public static char[] intTochar (int i) {
//	    char[] chars = new char[4];
//	    chars[0] = (char) (i & 0xFFFF);
//	    chars[1] = (char) ((i >> 16) & 0xFFFF);
//	    chars[2] = (char) ((i >> 16) & 0xFFFF);
//	    chars[3] = (char) ( i & 0xFFFF);
//	    return chars;
//	}
//	
//	public static int charToInt (char chars) {
//	    return ((chars & 0xFFFF));
//	}
	
	

}
