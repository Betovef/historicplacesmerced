import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CSVdownloader {

	public CSVdownloader(String filePath, String folderPath) throws FileNotFoundException {
		Scanner scanIn = null;
		scanIn = new Scanner(new BufferedReader(new FileReader(filePath)));
		int i = 0;
		String fileName = null, imgURL = null, row = null;
		double fileSize;

		
		while(scanIn.hasNextLine()) {
			try {		
			row = scanIn.nextLine(); //reading every line in the csv file
			String[] data = row.split(","); //split the names of the files and the url
			if (data.length == 2 && (data[1] != "" || data[0] != "")) {		
			fileName = data[0]; // save names and url into variables 
			imgURL = data[1];
			
			URL url = new URL(imgURL); // setting up the url 
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			fileSize = (double)http.getContentLengthLong();
			BufferedInputStream in = new BufferedInputStream(http.getInputStream());
			
			File out = new File(folderPath + "\\" + fileName + ".jpg"); //writing the names and the path o
			FileOutputStream fos = new FileOutputStream(out);
			BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
			
			byte[] buffer = new byte[1024]; //writing code to visualize progress (per url)
			double downloaded = 0.00;
			int read = 0;
			double percentDownlaoded = 0.00;
			while((read = in.read(buffer, 0, 1024)) >= 0) {
				bout.write(buffer, 0, read);
				downloaded += read;
				percentDownlaoded = (downloaded*100)/fileSize;
				String percent = String.format("%4f",  percentDownlaoded);
				System.out.println("Downloaded " + percent + "% of a file.");
			}
			bout.close();
			in.close();
			System.out.println("Download " + i + " complete!");
			}
			else {
				continue;
			}
			}
			
			catch(Exception e) {
				System.out.println("Error on iteration " + i);
			}
			i++;
		}
		
	}
		
	}
