package hash;

//
// SEQREADER.JAVA
// Read a sequence from a file.  The file is assumed to contain a single
// sequence, possibly split across multiple lines.  Case is not preserved.
//

import java.io.*;
import java.net.*;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SeqReader {

	// Based on
	// https://stackoverflow.com/questions/6659360/how-to-solve-javax-net-ssl-sslhandshakeexception-error
	// trusting all certificate
	public static void trustCertificates() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
			}
		} };
		SSLContext sc;
		try {
			sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String readSeq(String fileName) {
		StringWriter buffer = new StringWriter();
		BufferedReader r = null;

		trustCertificates();
		parsing: {

			//
			// Create a reader for the file
			//
			URL url;
			HttpsURLConnection connection;
			try {
				url = new URL(fileName);
				// BSIEVER: Removed Https / switched to Http
				connection = (HttpsURLConnection) url.openConnection();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
				break parsing;
			} catch (Exception e) {
				e.printStackTrace();
				break parsing;
			}
			InputStream is;
			try {
				is = connection.getInputStream();
			} catch (IOException e1) {
				e1.printStackTrace();
				break parsing;
			}
			r = new BufferedReader(new InputStreamReader(is));

			// Accumulate each line of the file (minus surrounding
			// whitespace) sequentially in a string buffer. Convert
			// to lower case as we read.
			//
			try {
				boolean stop = false;

				while (!stop) {
					String nextline = r.readLine();
					if (nextline == null) // end of file
						stop = true;
					else {
						String seq = nextline.trim();
						buffer.write(seq.toLowerCase());
					}
				}
			} catch (IOException e) {
				System.out.println("IOException while reading sequence from " + fileName + "\n" + e);
				break parsing;
			}
		}
		//
		// final cleanup
		//

		if (r != null) {
			try {
				r.close();
			} catch (IOException e) {
				// error in closing stream
			}
		}

		return buffer.toString();
	}
}
