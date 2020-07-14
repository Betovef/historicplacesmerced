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

public class main {


	public static void APIDownload(String key, String coordinates, String size) {
		String imgURL = "https://maps.googleapis.com/maps/api/streetview?size=" + size + "&location=" + coordinates + "&fov=80&heading=70&pitch=0&key=" + key;
		System.out.println("Getting image...");

		dynamic_scraping API = new dynamic_scraping(imgURL);
		API.getImg();


		// ##Solve file extension issue 
		//		try {
		//		URL url = new URL(imgURL); // setting up the url 
		//		HttpURLConnection http = (HttpURLConnection)url.openConnection();
		//		BufferedInputStream in = new BufferedInputStream(http.getInputStream());
		//		File out = new File(folderPath + "\\" + "experimentAPIkey.json"); 
		//		
		//		FileOutputStream fos = new FileOutputStream(out);
		//		BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
		//		bout.close();
		//		in.close();
		//		System.out.println("Download complete!");
		//		}
		//		catch(Exception e) {
		//			System.out.println("Error");
		//		}

	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner kbd = new Scanner(System.in);  
		int task; 
		String filePath, folderPath, key, coordinates, size;

		System.out.println("Welcome to the image downloader! Please select the desired task:");
		System.out.println("1. Download image URLs from csv file");
		System.out.println("2. Use google API");
		System.out.println("3. Dynamic scraping");
		task = kbd.nextInt();

		if(task == 1) {
			System.out.print("Please write the document path of your CSV file: ");
			filePath = kbd.nextLine();
			filePath = kbd.nextLine();

			System.out.print("Please write the folder path where images will be saved: ");
			folderPath = kbd.nextLine();

			System.out.println("File Path: " + filePath);
			System.out.println("Folder Path: " + folderPath);

			System.out.println("Downloading...");
			CSVdownloader images = new CSVdownloader(filePath, folderPath);

		}
		else if(task ==2) {
			System.out.print("Please enter Google API key: ");
			key = kbd.nextLine();
			key = kbd.nextLine();
			System.out.print("Enter coordinates: ");
			coordinates = kbd.nextLine();
			System.out.print("Enter picture size: ");
			size = kbd.nextLine();
			APIDownload(key, coordinates, size); // Does not support download yet ##issue with the file extension	
		}
		else if(task==3) {
			System.out.println("Please select website: \n1. redfin.com \n2. google.com/maps \n3. trulia.com");
			task = kbd.nextInt();
			if(task ==1) {
				System.out.print("Please write the document path of your CSV file: ");
				filePath = kbd.nextLine();
				filePath = kbd.nextLine();
				dynamic_scraping Web = new dynamic_scraping(filePath, task);
				Web.scrapeRedfinImg();	
			}
			else if(task == 2) {
				System.out.print("Please write the document path of your CSV file: ");
				filePath = kbd.nextLine();
				filePath = kbd.nextLine();
				System.out.print("Please write the folder path where images will be saved: ");
				folderPath = kbd.nextLine();
				dynamic_scraping Web = new dynamic_scraping(filePath, folderPath, task);
				Web.scrapeGooglemapsImg();
			}
			else if(task ==3) {
				System.out.print("Please write the document path of your CSV file: ");
				filePath = kbd.nextLine();
				filePath = kbd.nextLine();
				System.out.print("Please write the folder path where images will be saved: ");
				folderPath = kbd.nextLine();
				dynamic_scraping Web = new dynamic_scraping(filePath, folderPath, task);
				Web.scrapeTruliaImg();
			}
		}
		else {
			System.out.println("Task is not supported");
		}

	}

}
