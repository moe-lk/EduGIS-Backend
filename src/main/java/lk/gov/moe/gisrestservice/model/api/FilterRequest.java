package lk.gov.moe.gisrestservice.model.api;
import java.util.ArrayList;
public class FilterRequest {
	private ArrayList<String> type;
	private ArrayList<String> category;
	private ArrayList<String> gender;

	public ArrayList<String> getType() {
		return type;
	}
	public ArrayList<String> getCategory() {
		return category;
	}
	public ArrayList<String> getGender() {
		return gender;
	}
}
