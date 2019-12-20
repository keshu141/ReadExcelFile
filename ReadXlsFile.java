package csvfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class ReadXlsFile {

	public static void main(String[] args) throws Exception {

		String accessKey = "AKIAIRKDJ6MFDPE7DL";
		String SecretKey = "3z+JmfR5kWvw26/ymcHVzR7WpAoaYgstI6Y4G";
		String bucketName = "dataelements123";
		String key = "cola.xls";

		AWSCredentials credentials = new BasicAWSCredentials("AKIAIRKDJ6MFDPE7DL",
				"3z+JmfR5kWvw26/ymcHVzR7WpAoaYgstI6Y4G");
		ClientConfiguration clientConfiguration = new ClientConfiguration();

		clientConfiguration.setConnectionTimeout(50000);
		clientConfiguration.setMaxConnections(500);
		clientConfiguration.setSocketTimeout(100000);
		clientConfiguration.setMaxErrorRetry(10);

		AmazonS3 s3client = new AmazonS3Client(credentials);
		List<Bucket> bu = s3client.listBuckets();
		for (Bucket bucket : bu) {
			System.out.println(bucket.getName());

			S3Object fullObject = null, objectPortion = null, headerOverrideObject = null;
			try {
				AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTH_1)
						.withCredentials(new ProfileCredentialsProvider()).build();
				fullObject = s3client.getObject(new GetObjectRequest(bucketName, key));
				System.out.println("Content-Type: " + fullObject.getObjectMetadata().getContentType());
				System.out.println("Content: ");
				displayTextInputStream(fullObject.getObjectContent());
				System.out.println(fullObject.getObjectMetadata());

			} catch (AmazonServiceException e) {
				e.printStackTrace();
			}
		}
	}

	

	private static void displayTextInputStream(S3ObjectInputStream objectContent) throws IOException , FileNotFoundException{
		FileInputStream fis=new FileInputStream(new File("C:\\Users\\PC\\OneDrive\\Desktop\\cola.xls"));  
		HSSFWorkbook wb=new HSSFWorkbook(fis);   
		//creating a Sheet object to retrieve the object  
		HSSFSheet sheet=wb.getSheetAt(0);  
		//evaluating cell type   
		FormulaEvaluator formulaEvaluator=wb.getCreationHelper().createFormulaEvaluator();  
		for(Row row: sheet)     //iteration over row using for each loop  
		{  
		for(Cell cell: row)    //iteration over cell using for each loop  
		{  
		switch(formulaEvaluator.evaluateInCell(cell).getCellType())  
		{  
		case Cell.CELL_TYPE_NUMERIC:   //field that represents numeric cell type  
		//getting the value of the cell as a number  
		System.out.print(cell.getNumericCellValue()+ "\t\t");   
		break;  
		case Cell.CELL_TYPE_STRING:    //field that represents string cell type  
		//getting the value of the cell as a string  
		System.out.print(cell.getStringCellValue()+ "\t\t");  
		break;  
		}  
		}  
		System.out.println();  
		}  
		}  
        }
      