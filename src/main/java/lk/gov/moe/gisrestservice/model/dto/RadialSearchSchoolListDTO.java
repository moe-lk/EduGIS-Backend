package lk.gov.moe.gisrestservice.model.dto;
import lk.gov.moe.gisrestservice.model.RadialSearchSchool;

import java.io.Serializable;
import java.util.List;
public class RadialSearchSchoolListDTO implements Serializable {

	private List<RadialSearchSchool> schools;
	private Integer total;
	private String message;

	public RadialSearchSchoolListDTO() {
	}
	public RadialSearchSchoolListDTO(List<RadialSearchSchool> schools) {
		this.schools = schools;
		this.total = schools.size();
	}

	public List<RadialSearchSchool> getSchools() {
		return schools;
	}
	public void setSchools(List<RadialSearchSchool> schools) {
		this.schools = schools;
	}
	public Integer getTotal() {
		return total;
	}

}
