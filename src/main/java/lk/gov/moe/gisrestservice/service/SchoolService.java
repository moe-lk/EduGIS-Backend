package lk.gov.moe.gisrestservice.service;

import lk.gov.moe.gisrestservice.exception.BadRequestException;
import lk.gov.moe.gisrestservice.model.RadialSearchSchool;
import lk.gov.moe.gisrestservice.model.School;
import lk.gov.moe.gisrestservice.model.dto.RadialSearchSchoolListDTO;
import lk.gov.moe.gisrestservice.model.dto.SchoolListDTO;
import lk.gov.moe.gisrestservice.model.geo.GeoJSON;
import lk.gov.moe.gisrestservice.model.geo.GeoObject;
import lk.gov.moe.gisrestservice.model.geo.GeoSchool;
import lk.gov.moe.gisrestservice.repository.RadialSearchSchoolRepository;
import lk.gov.moe.gisrestservice.repository.SchoolRepository;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
@Component
public class SchoolService {

	@Autowired
	SchoolRepository repository;

	@Autowired
	RadialSearchSchoolRepository radialSearchSchoolRepository;

	/**
	 * Get all schools
	 *
	 * @return
	 */
	public List<School> getSchools() {

		return (List<School>) repository.findAll();

	}

	/**
	 * Get school by zone
	 *
	 * @param schoolZone
	 * @return
	 */
	public List<School> getSchoolsByZone(String schoolZone) {

		return repository.findAllBySchoolZone(schoolZone);

	}

	/**
	 * Get school by census id
	 *
	 * @param schoolId
	 * @return
	 */
	public School getSchoolById(Integer schoolId) {

		return repository.findSchoolById(schoolId);

	}

	/**
	 * Get schools by multiple types
	 *
	 * @param schoolTypes
	 * @return
	 */
	public List<School> getSchoolsByTypes(List<String> schoolTypes) {

		return repository.findSchoolsByTypesIn(schoolTypes);

	}

	/**
	 * List of geojson features of schools
	 *
	 * @return
	 */
	public List<GeoSchool> geoGetSchools() {

		List<School> schools = (List<School>) repository.findAll();
		List<GeoSchool> geoSchoolsList = new ArrayList<>();

		for(School school : schools) {
			geoSchoolsList.add(new GeoSchool(school));
		}

		return geoSchoolsList;

	}

	/**
	 * Check given school type(s) are valid
	 *
	 * @param types
	 * @return
	 */
	public Boolean validType(List<String> types) {

		ArrayList<String> typesArray = new ArrayList<>(Arrays.asList("1C", "1AB", "Type 2", "Type 3"));

		for(String type : types) {
			if(!typesArray.contains(type)) {
				return false;
			}

		}

		return true;
	}

	/**
	 * Check given school zone exist
	 *
	 * @param schoolZone
	 * @return
	 */
	public Boolean isAvailableSchoolZone(String schoolZone) {

		return repository.findSchoolZones().stream().anyMatch(schoolZone::equalsIgnoreCase);

	}


	public List<School> searchSchoolsByNames(String searchTerm) {

		return repository.findSchoolsByName(searchTerm);

	}

	/**
	 * Filter results based on school type, category and gender composition
	 *
	 * @param inputTypes    school type
	 * @param inputCategory school category
	 * @param inputGender   school gender composition
	 * @return
	 */
	public SchoolListDTO filterSchools(ArrayList<String> inputTypes, ArrayList<String> inputCategory, ArrayList<String> inputGender) {

		ArrayList<String> types = new ArrayList<>(Arrays.asList("1AB", "1C", "Type 2", "Type 3"));
		ArrayList<String> categories = new ArrayList<>(Arrays.asList("N", "P"));
		ArrayList<String> gender = new ArrayList<>(Arrays.asList("Mixed", "Boys", "Girls"));

		//do type validation
		if(!inputTypes.isEmpty() && Collections.disjoint(inputTypes, types)) {
			throw new BadRequestException("Invalid school type");
		}
		else if(!inputCategory.isEmpty() && Collections.disjoint(inputCategory, categories)) {
			throw new BadRequestException("Invalid school category");
		}
		else if(!inputGender.isEmpty() && Collections.disjoint(inputGender, gender)) {
			throw new BadRequestException("Invalid school gender");
		}

		SchoolListDTO schoolListDto;

		//TODO: implement
		if(inputTypes.isEmpty() && inputCategory.isEmpty() && inputGender.isEmpty()) {
			schoolListDto = new SchoolListDTO();
		}
		else if(!inputTypes.isEmpty() && inputCategory.isEmpty() && inputGender.isEmpty()) {
			schoolListDto = new SchoolListDTO(repository.findSchoolsByTypesIn(inputTypes));
		}
		else if(!inputCategory.isEmpty() && inputTypes.isEmpty() && inputGender.isEmpty()) {
			schoolListDto = new SchoolListDTO(repository.findSchoolsByCategoryIn(inputCategory));
		}
		else if(!inputGender.isEmpty() && inputTypes.isEmpty() && inputCategory.isEmpty()) {
			schoolListDto = new SchoolListDTO(repository.findSchoolsByGenderIn(inputGender));
		}
		else if(inputGender.isEmpty()) {
			schoolListDto = new SchoolListDTO(repository.findSchoolsByTypesInAndCategoryIn(inputTypes, inputCategory));
		}
		else if(inputCategory.isEmpty()) {
			schoolListDto = new SchoolListDTO(repository.findSchoolsByTypesInAndGenderIn(inputTypes, inputGender));
		}
		else if(inputTypes.isEmpty()) {
			schoolListDto = new SchoolListDTO(repository.findSchoolsByCategoryInAndGenderIn(inputCategory, inputGender));
		}
		else {
			schoolListDto = new SchoolListDTO(repository.findSchoolsBySchoolTypesInAndCategoryInAndGenderIn(inputTypes, inputCategory, inputGender));
		}

		return schoolListDto;

	}

	public ResponseEntity<GeoJSON> geoFilter(ArrayList<String> inputTypes, ArrayList<String> inputCategory, ArrayList<String> inputGender) {

		List<School> schools = filterSchools(inputTypes, inputCategory, inputGender).getSchools();
		List<GeoSchool> geoSchoolsList = new ArrayList<>();

		for(School school : schools) {
			geoSchoolsList.add(new GeoSchool(school));
		}

		List<? extends GeoObject> geoSchools = geoSchoolsList;
		return ResponseEntity.ok(new GeoJSON((List<GeoObject>) geoSchools));

	}


	/**
	 * Execute radial search and return school list and corresponsing distance from centre
	 *
	 * @param centerLatitude  radial centre latitude
	 * @param centerLongitude radial centre longitude
	 * @param radius          radius of search perimeter
	 * @param limit           return results limit
	 * @return
	 */
	public ResponseEntity<RadialSearchSchoolListDTO> radialSearch(Float centerLatitude, Float centerLongitude, Double radius,
																  @Nullable
																	  Integer limit,
																  @Nullable
																	  Boolean isResultsAscending
																 ) {

		RadialSearchSchoolListDTO schoolListDTO;

		if(limit != null)
			if(isResultsAscending == null || isResultsAscending)
				schoolListDTO = new RadialSearchSchoolListDTO(radialSearchSchoolRepository.radialSearchLimit(centerLatitude, centerLongitude, radius, limit));
			else
				schoolListDTO = new RadialSearchSchoolListDTO(radialSearchSchoolRepository.radialSearchLimitDesc(centerLatitude, centerLongitude, radius, limit));
		else {
			if(isResultsAscending == null || isResultsAscending)
				schoolListDTO = new RadialSearchSchoolListDTO(radialSearchSchoolRepository.radialSearchAll(centerLatitude, centerLongitude, radius));
			else
				schoolListDTO = new RadialSearchSchoolListDTO(radialSearchSchoolRepository.radialSearchAllDesc(centerLatitude, centerLongitude, radius));
		}


		return ResponseEntity.ok(schoolListDTO);

	}


}
