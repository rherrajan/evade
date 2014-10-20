package evade;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.TextBox;
import javax.microedition.midlet.MIDlet;

public class TestInternet extends MIDlet {
	
	private Display display;
	private String url = "http://www.javacourses.com/hello.txt";

	public TestInternet() {
		System.out.println(" --- TestInternet --- ");
		display = Display.getDisplay(this);
	}

	/**
	 * This will be invoked when we activate the MIDlet.
	 */
	public void startApp() {
		System.out.println(" --- startApp --- ");
		// Use the specified URL is overriden in the descriptor

			
			Downloader dl = new Downloader();
			
			dl.url = url;
			
			Thread load = new Thread(dl);
			
			load.start();
			
			try {
				load.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			TextBox t = new TextBox("hello again....", dl.result, 1024, 0);

			display.setCurrent(t);

	}

	
	private static class Downloader implements Runnable{

		String url;
		public String result;
		
		public void run() {
			
			try {
				result = downloadPage(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private String downloadPage(String url) throws IOException {
			
			StringBuffer b = new StringBuffer();
			InputStream is = null;
			HttpConnection c = null;

			System.out.println(" --- downloadPage --- ");
			
			try {
				long len = 0;
				int ch = 0;
				
				System.out.println(" --- open: " + url);
				c = (HttpConnection) Connector.open(url);
				c.setRequestProperty("User-Agent",
						"Profile/MIDP-1.0 Configuration/CLDC-1.0");
				
				System.out.println(" --- openInputStream --- ");
				
				is = c.openInputStream();

				System.out.println(" --- opened --- ");
				
				len = c.getLength();

				System.out.println(" --- len: " + len);
				
				if (len != -1) {
					// Read exactly Content-Length bytes
					for (int i = 0; i < len; i++) {
						if ((ch = is.read()) != -1) {
							b.append((char) ch);
						}
					}
				} else {
					// Read till the connection is closed.
					while ((ch = is.read()) != -1) {
						len = is.available();
						b.append((char) ch);
					}
				}

				System.out.println(" --- internet datei: " + b.toString());
				
				return b.toString();

			} finally {
				is.close();
				c.close();
			}
		}
		
	}
	

	/**
	 * Pause, discontinue....
	 */
	public void pauseApp() {
	}

	/**
	 * Destroy must cleanup everything.
	 */
	public void destroyApp(boolean unconditional) {
	}
}
