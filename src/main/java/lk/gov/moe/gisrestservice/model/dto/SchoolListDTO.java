package lk.gov.moe.gisrestservice.model.dto;
import lk.gov.moe.gisrestservice.model.School;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class SchoolListDTO implements Serializable {

	private List<School> schools;
	private Integer total;
	private String message;

	public SchoolListDTO() {
		this.schools = new ArrayList<>();
		this.total = 0;
	}

	public SchoolListDTO(List<School> schools) {
		this.schools = schools;
		this.total = schools.size();
	}

	public List<School> getSchools() {
		return schools;
	}
	public void setSchools(List<School> schools) {
		this.schools = schools;
	}

	public Integer getTotal() {
		return total;
	}
}
