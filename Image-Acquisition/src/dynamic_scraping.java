import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.apache.commons.io.FileUtils;


public class dynamic_scraping {
	private static String filePath, folderPath, img;
	private static String[] url = {"https://www.redfin.com/", "https://www.google.com/maps"};
	int task;

	public dynamic_scraping(String filePath) {
		this.filePath = filePath;
	}

	public dynamic_scraping(String filePath, int task) {
		this.filePath = filePath;
		this.task = task;
	}

	public dynamic_scraping(String filePath, String folderPath, int  task) {
		this.filePath = filePath;
		this.folderPath = folderPath;
		this.task = task;
	}

	public static void  getImg() {
		WebDriver driver = new ChromeDriver();
		driver.get(filePath);
	}
	public static void  saveImg(WebDriver driver, Actions act,  WebElement elem, String name) throws IOException, InterruptedException, AWTException {

		//Formating name
		String splitName[] = name.split(" ");
		String newName = "";
		for(int i = 0; i < splitName.length-1; i++) {
			if(i == splitName.length-2) {
				newName += splitName[i];
			}
			else {
				newName += splitName[i] + "_";
			}
		}
		//Setting up file screenshot 
		act.contextClick(elem).build().perform();
		act.sendKeys(Keys.CONTROL + "v").build().perform();

		Thread.sleep(3000);
		StringSelection stringSelection = new StringSelection(name);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, stringSelection);

		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_ENTER); 
		// ISSUE: Need to figure out how to name files
//		robot.keyPress(KeyEvent.VK_DOWN);
//		robot.keyPress(KeyEvent.VK_DOWN);
//		robot.keyPress(KeyEvent.VK_ENTER);
//		robot.keyPress(KeyEvent.VK_CONTROL);
//		robot.keyPress(KeyEvent.VK_V);
//		robot.keyRelease(KeyEvent.VK_V);
//		robot.keyRelease(KeyEvent.VK_CONTROL);
//		robot.keyPress(KeyEvent.VK_ENTER);  
	}
	public static void  screenshotImg(WebDriver driver, WebElement elem, String name, String date) throws IOException {

		//Formating name
		String splitName[] = name.split(" ");
		String newName = "";
		for(int i = 0; i < splitName.length-1; i++) {
			newName += splitName[i] + "_";
		}
		date = date.replaceAll("Image capture: ", "");
		date = date.replaceAll(" ", "");

		//Setting up file screenshot 
		File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		BufferedImage  fullImg = ImageIO.read(screenshot);
		Point point = elem.getLocation();

		int eleWidth = elem.getSize().getWidth();
		int eleHeight = elem.getSize().getHeight();

		BufferedImage eleScreenshot= fullImg.getSubimage(point.getX(), point.getY(),
				eleWidth, eleHeight);
		ImageIO.write(eleScreenshot, "png", screenshot);

		//Writing image in folder
		File screenshotLocation = new File(folderPath + "\\" + newName + date + ".jpg");
		FileUtils.copyFile(screenshot, screenshotLocation);
	}

	public static void scrapeTruliaImg() {
		Scanner scanIn = null;
		String InputLine = "";
		try {
			System.setProperty("webdriver.chrome.driver", "C:\\webdrivers\\chromedriver.exe"); 
			WebDriver driver = new ChromeDriver();
			Actions act = new Actions(driver);
			WebElement elem = null;

			scanIn = new Scanner(new BufferedReader(new FileReader(filePath)));
			int i = 0;

			while(scanIn.hasNextLine()) {
				InputLine = scanIn.nextLine();
				try {
					driver.get("https://www.trulia.com/");
					Thread.sleep(1000);
					elem = driver.findElement(By.id("banner-search"));
					elem.sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE)); // delete id input 
					elem.sendKeys(InputLine);// input text
					Thread.sleep(500);
					elem.sendKeys(Keys.ENTER); // search
					Thread.sleep(2000);	
					Boolean isPresent = driver.findElements(By.xpath("/html/body/div[2]/section/div[2]/div[1]/div[1]/div[1]/div[1]/h2")).size() > 0;
					if(isPresent == true) {
						System.out.println("");
						Thread.sleep(1000);
						continue;
					}
					else {
					elem = driver.findElement(By.xpath("/html/body/div[2]/section/div[2]/div[1]/div/div/div[1]/div[1]/div")); // finds image to click on
					Thread.sleep(2000);	
					act.moveToElement(elem).click().build().perform(); // clicks on image 
					elem = driver.findElement(By.tagName("img")); // identifies img 
					System.out.println(elem.getAttribute("src"));
					Thread.sleep(2000);	
					//					saveImg(driver, act, elem, InputLine); // screenshot 
					}

				} catch(Exception e) {
					System.out.println("");
				}
			}
		} catch(Exception e) {
		}
	}


	public static void scrapeGooglemapsImg() {
		Scanner scanIn = null;
		String InputLine = "", oldDate, newDate;

		try {
			System.setProperty("webdriver.chrome.driver", "C:\\webdrivers\\chromedriver.exe"); 
			WebDriver driver = new ChromeDriver();
			Actions act = new Actions(driver);
			WebElement elem = null;

			scanIn = new Scanner(new BufferedReader(new FileReader(filePath)));
			int i = 0;

			while(scanIn.hasNextLine()) {
				InputLine = scanIn.nextLine();
				newDate = " ";
				oldDate = " ";
				try {
					driver.get("https://www.google.com/maps"); // search for google maps
					elem = driver.findElement(By.id("searchboxinput")); // identifies input box
					elem.sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE)); // clears inputs
					elem.sendKeys(InputLine); // insert input
					elem.sendKeys(Keys.ENTER); // enter and search input 
					Thread.sleep(3000);	
					elem = driver.findElement(By.xpath("/html/body/jsl/div[3]/div[9]/div[8]/div/div[1]/div/div/div[1]/div[1]/button/img")); // finds image to click on
					act.moveToElement(elem).click().build().perform(); // clicks on image 
					Thread.sleep(1500);	
					elem = driver.findElement(By.xpath("/html/body/jsl/div[3]/div[9]/div[8]/div/div[3]/button")); // identifies extension arrow
					act.moveToElement(elem).click().build().perform(); // clicks on extension arrows
					//Full Google Street View

					//Capture newest picture
					elem = driver.findElement(By.xpath("/html/body/jsl/div[3]/div[9]/div[25]/div/div[1]/div/div/span[7]/span/span/span"));
					newDate = elem.getText(); // date data 
					elem = driver.findElement(By.xpath("/html/body/jsl/div[3]/div[9]/div[1]/div[3]/canvas")); // identifies canvas 
					screenshotImg(driver, elem, InputLine, newDate); // screenshot 
					Thread.sleep(2000);

					//Capture oldest picture
					elem = driver.findElement(By.xpath("/html/body/jsl/div[3]/div[9]/div[11]/div[1]/div[2]/div[4]/div/div")); // identifies time machine button
					act.moveToElement(elem).click().build().perform(); // clicks time machine button
					Thread.sleep(500);	
					elem = driver.findElement(By.xpath("/html/body/jsl/div[3]/div[9]/div[11]/div[2]/div/div/div/div[3]/img[1]")); // identifies img 
					act.moveToElement(elem).click().build().perform(); // click on new image
					Thread.sleep(500);
					elem = driver.findElement(By.xpath("/html/body/jsl/div[3]/div[9]/div[11]/div[2]/div/div/div/div[2]/button")); // close time machine 
					act.moveToElement(elem).click().build().perform(); // click on closing tab
					Thread.sleep(500);
					elem = driver.findElement(By.xpath("/html/body/jsl/div[3]/div[9]/div[25]/div/div[1]/div/div/span[7]/span/span/span"));
					oldDate = elem.getText();
					elem = driver.findElement(By.xpath("/html/body/jsl/div[3]/div[9]/div[1]/div[3]/canvas")); // identifies canvas 
					screenshotImg(driver, elem, InputLine, oldDate); // screenshot 
					//					Thread.sleep(2000);
					//Print data
					System.out.println(InputLine + ", " + newDate + ", " + oldDate);
				} catch (Exception e) {
					System.out.println(InputLine + ", " + newDate + ", " + oldDate);
				}
			}
		} catch(Exception e) {
			System.out.println("Error at method scrapeGooglemapsImg() ");
		}

	}

	public static void scrapeRedfinImg() {
		Scanner scanIn = null;
		String InputLine = "";
		try {
			System.setProperty("webdriver.chrome.driver", "C:\\webdrivers\\chromedriver.exe"); 
			WebDriver driver = new ChromeDriver();
			Actions act = new Actions(driver);
			WebElement elem = null;

			scanIn = new Scanner(new BufferedReader(new FileReader(filePath)));
			int i = 0;

			while(scanIn.hasNextLine()) {
				InputLine = scanIn.nextLine();

				try {
					driver.get("https://www.redfin.com/");
					Thread.sleep(1000);
					act.moveToElement(driver.findElement(By.xpath("/html/body/div[1]/div[6]/div[2]/div/section/div/div[1]/div/div/div/div/div/div[2]/div/div/form/div/div/input"))).click().build().perform();
					elem = driver.findElement(By.id("search-box-input"));
					elem.sendKeys(Keys.chord(Keys.CONTROL,"a", Keys.DELETE)); // delete id input 
					elem.sendKeys(InputLine);// input text
					elem.sendKeys(Keys.ENTER); // search
					driver.getCurrentUrl();
					Thread.sleep(4000);	
					elem = driver.findElement(By.xpath("/html/body/div[1]/div[8]/div[5]/section/div/div[2]/div[2]/span[1]/div/img")); //find the element of the picture
					if(elem.isDisplayed()) {
						System.out.println(elem.getAttribute("src")); //print src attributes
					}
				}
				catch(Exception e) {
					url[0] = driver.getCurrentUrl();
					//					System.out.println(url);
					Document page;
					try {
						page = Jsoup.connect(url[0]).get();
						Elements img = page.select(".streetViewImage.img-card");
						String src = "";
						System.out.println(img.attr("src").toString());
					} catch (IOException e1) {
						System.out.println("");
					}
				}
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
