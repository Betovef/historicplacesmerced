import java.util.Scanner;

public class main {
	

	public static void main(String[] args) {
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
//			csvDownload(filePath, folderPath);
		}
		else if(task ==2) {
			System.out.print("Please enter Google API key: ");
			key = kbd.nextLine();
			key = kbd.nextLine();
			System.out.print("Enter coordinates: ");
			coordinates = kbd.nextLine();
			System.out.print("Enter picture size: ");
			size = kbd.nextLine();
//			APIDownload(key, coordinates, size); // Does not support download yet ##issue with the file extension	
		}
		else if(task==3) {
			System.out.print("Please write the document path of your CSV file: ");
			filePath = kbd.nextLine();
			filePath = kbd.nextLine();
//			dynamic_scraping Web = new dynamic_scraping(filePath);
//			Web.scrapeImg();
		}
		else {
			System.out.println("Task is not supported");
		}

	}

}
