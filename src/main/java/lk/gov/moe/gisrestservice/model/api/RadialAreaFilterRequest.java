package lk.gov.moe.gisrestservice.model.api;
public class RadialAreaFilterRequest {

	private Float centerLatitude;
	private Float centerLongitude;
	private Double radius;
	private Integer limit;
	private Boolean resultsAscending;

	public Float getCenterLatitude() {
		return centerLatitude;
	}
	public Float getCenterLongitude() {
		return centerLongitude;
	}
	public Double getRadius() {
		return radius;
	}
	public Integer getLimit() {
		return limit;
	}
	public Boolean getResultsAscending() {
		return resultsAscending;
	}
}
