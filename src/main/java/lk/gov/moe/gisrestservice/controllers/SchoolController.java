package lk.gov.moe.gisrestservice.controllers;

import io.swagger.annotations.*;
import lk.gov.moe.gisrestservice.model.School;
import lk.gov.moe.gisrestservice.model.api.ErrorDetails;
import lk.gov.moe.gisrestservice.model.api.FilterRequest;
import lk.gov.moe.gisrestservice.model.api.RadialAreaFilterRequest;
import lk.gov.moe.gisrestservice.model.dto.RadialSearchSchoolListDTO;
import lk.gov.moe.gisrestservice.model.geo.GeoJSON;
import lk.gov.moe.gisrestservice.model.geo.GeoObject;
import lk.gov.moe.gisrestservice.exception.BadRequestException;
import lk.gov.moe.gisrestservice.exception.NotFoundException;
import lk.gov.moe.gisrestservice.model.dto.SchoolListDTO;
import lk.gov.moe.gisrestservice.service.SchoolService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Payload;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/schools")
@Api(description = "School endpoints", produces = "application/json")
public class SchoolController {

	@Autowired
	SchoolService schoolService;

//	@RequestMapping(method = RequestMethod.GET)
//	@ApiOperation(
//		value = "All schools in the database",
//		response = SchoolListDTO.class
//	)
//	public List<School> getAllSchools() {
//
//		SchoolListDTO schoolListDto = new SchoolListDTO(schoolService.getSchools());
//		return schoolService.getSchools();
//	}

	@RequestMapping(value="/test", method = RequestMethod.GET)
	@ApiOperation(
		value = "Test endpoint",
		response = String.class
	)
	public String getTest() {

		return "endpoint working1123";
	}

	@RequestMapping(value="/search", method = RequestMethod.GET)
	public ResponseEntity<SchoolListDTO> searchSchools(@RequestParam String name) {

		SchoolListDTO schoolListDto = new SchoolListDTO(schoolService.searchSchoolsByNames(name));

		return ResponseEntity.ok(schoolListDto);

	}

	@RequestMapping(value="/census/{schoolId}", method = RequestMethod.GET)
	@ApiOperation(
		value = "Returns school by census id", notes = "/census/{schoolId}",
		response = School.class
	)
	@ApiResponses(value = {
		@ApiResponse(code = 404, message = "School census id not available") }
	)
	public School getSchoolById(@PathVariable Integer schoolId) {

		School retrievedSchool = schoolService.getSchoolById(schoolId);
		if (retrievedSchool == null) {
			throw new NotFoundException("School census id not available");
		}

		return schoolService.getSchoolById(schoolId);
	}


	@RequestMapping(value="/zone/{schoolZone}", method = RequestMethod.GET)
	@ApiOperation(
		value = "Returns schools by zone", notes = "/zone/{schoolZone}",
		response = SchoolListDTO.class
	)
	@ApiResponses(value = {
		@ApiResponse(code = 404, message = "Provided school zone not available") }
	)
	public ResponseEntity<SchoolListDTO> getSchoolsByZone(@PathVariable
							String schoolZone) {

		//is school zone available
		if (!schoolService.isAvailableSchoolZone(schoolZone)) {

			throw new NotFoundException("Provided school zone not available");

		}

		SchoolListDTO schoolListDto = new SchoolListDTO(schoolService.getSchoolsByZone(schoolZone));
		return ResponseEntity.ok(schoolListDto);

	}


	@RequestMapping(value="/type", method = RequestMethod.GET)
	@ApiOperation(
		value = "Returns schools by multiple types", notes = "/type?schoolTypes=1C,1AB",
		response = SchoolListDTO.class
	)
	@ApiResponses(value = {
		@ApiResponse(code = 400, message = "Invalid School Type") }
	)
	public ResponseEntity<SchoolListDTO> getSchoolsByType(@RequestParam List<String> schoolTypes) {

		//validate types
		if (!schoolService.validType(schoolTypes)) {

			throw new BadRequestException("Invalid school type");

		}
		SchoolListDTO schoolListDto = new SchoolListDTO(schoolService.getSchoolsByTypes(schoolTypes));
		return ResponseEntity.ok(schoolListDto);
	}

	@RequestMapping(value="/geo", method = RequestMethod.GET)
	@ApiOperation(
		value = "GeoJSON layer for all schools", notes = "returns all schools",
		response = GeoJSON.class

		)
	public ResponseEntity<GeoJSON> geoGetAllSchools() {

		List<? extends GeoObject> geoSchoolsList = schoolService.geoGetSchools();
		return ResponseEntity.ok(new GeoJSON((List<GeoObject>) geoSchoolsList));
	}

//	@RequestMapping(value="/filter", method = RequestMethod.POST)
//	@ApiOperation(
//		value = "Returns schools by type, category and gender composition", notes = "returns filtered schools",
//		response = SchoolListDTO.class
//	)
//	@ApiResponses(value = {
//		@ApiResponse(code = 200, message = ""),
//		@ApiResponse(code = 400, message = "Invalid school type/category/gender, Missing field", response = ErrorDetails.class)
//		}
//	)
//	public ResponseEntity<SchoolListDTO> filterSchools(@RequestBody
//														   FilterRequest payload) {
//
//		try {
//
//			ArrayList<String> typeList = payload.getType();
//			ArrayList<String> categoryList = payload.getCategory();
//			ArrayList<String> genderList = payload.getGender();
//
//			return schoolService.filterSchools(typeList, categoryList, genderList);
//		} catch(Exception e) {
//			throw new BadRequestException("Missing field");
//		}
//
//
//	}

	@RequestMapping(value="/filter/geo", method = RequestMethod.POST)
	@ApiOperation(
		value = "Returns GeoJSON format schools by type, category and gender composition", notes = "returns filtered schools",
		response = GeoJSON.class
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = ""),
		@ApiResponse(code = 400, message = "Invalid school type/category/gender, Missing field", response = ErrorDetails.class)
	}
	)
	public ResponseEntity<GeoJSON> geofilterSchools(@RequestBody
															FilterRequest payload) {

		try {

			ArrayList<String> typeList = payload.getType();
			ArrayList<String> categoryList = payload.getCategory();
			ArrayList<String> genderList = payload.getGender();

			return schoolService.geoFilter(typeList, categoryList, genderList);
		} catch(Exception e) {
			throw new BadRequestException("Missing field");
		}
	}

	@RequestMapping(value="/search/radial", method = RequestMethod.POST)
	@ApiOperation(
		value = "Return schools and corresponding distances(to center) within radial area", notes = "returns filtered schools",
		response = SchoolListDTO.class
	)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "", response = RadialSearchSchoolListDTO.class),
		@ApiResponse(code = 400, message = "Missing field(s)", response = ErrorDetails.class)
	}
	)
	public ResponseEntity<RadialSearchSchoolListDTO> radialSearch(@RequestBody RadialAreaFilterRequest payload) {

		Float centerLat = null;
		Float centerLon = null;
		Double radius = null;
		Integer limit = null;
		Boolean resultsAscending = null;

		try {

			centerLat = payload.getCenterLatitude();
			centerLon = payload.getCenterLongitude();
			radius = payload.getRadius();
			limit = payload.getLimit();
			resultsAscending = payload.getResultsAscending();

		} catch(Exception e) {

			e.printStackTrace();
			System.out.println(e.getMessage());

			throw new BadRequestException("Missing field(s)");
		}

		return schoolService.radialSearch(centerLat, centerLon, radius, limit, resultsAscending);

	}


}
