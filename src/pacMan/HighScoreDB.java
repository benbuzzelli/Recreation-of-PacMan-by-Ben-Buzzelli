package pacMan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.eclipse.core.util.RssFeed.Item;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

public class HighScoreDB {

	public static void main(String[] args){
        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
		AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard()
	            .withCredentials(credentialsProvider)
	            .withRegion("us-east-2")
	            .build();
		
		List<String> attributesToCheck = new ArrayList();
		attributesToCheck.add("score");
		attributesToCheck.add("epochTime");
		attributesToCheck.add("name");
		ScanResult scanResult = dynamoDB.scan("HighScores",attributesToCheck);
		for(Map<String,AttributeValue> maps:scanResult.getItems()){
			System.out.println(maps);
		}
		
		}
	
	//Will get epoch time and add to table
	public void addTableEntry(int score, String name){
		long epochTime = System.currentTimeMillis();
		HighScoreEntry entry = new HighScoreEntry(score, name, epochTime);
	}
	
	public List<HighScoreEntry> getTopHundred(){
		return null;
	}
	
}
