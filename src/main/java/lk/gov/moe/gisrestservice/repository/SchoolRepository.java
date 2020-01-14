package lk.gov.moe.gisrestservice.repository;

import lk.gov.moe.gisrestservice.model.RadialSearchSchool;
import lk.gov.moe.gisrestservice.model.School;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolRepository extends CrudRepository<School, Long> {


	@Query(value = "SELECT * FROM schools WHERE school_zone = ?1", nativeQuery=true)
	List<School> findAllBySchoolZone(String schoolZone);

	@Query(value = "SELECT * FROM schools WHERE school_census = ?1", nativeQuery=true)
	School findSchoolById(Integer schoolId);

	@Query(value = "SELECT * FROM schools WHERE school_type = ?1", nativeQuery=true)
	List<School> findSchoolsByType(String schoolType);

	@Query(value = "SELECT * FROM schools WHERE school_type in (?1)", nativeQuery=true)
	List<School> findSchoolsByTypesIn(List<String> schoolTypes);

	@Query(value = "SELECT * FROM schools WHERE school_category in (?1)", nativeQuery=true)
	List<School> findSchoolsByCategoryIn(List<String> schoolCategories);

	@Query(value = "SELECT * FROM schools WHERE gender_composition in (?1)", nativeQuery=true)
	List<School> findSchoolsByGenderIn(List<String> schoolGender);

	@Query(value = "SELECT * FROM schools WHERE school_type in (?1) and school_category in (?2)", nativeQuery=true)
	List<School> findSchoolsByTypesInAndCategoryIn(List<String> schoolTypes, List<String> schoolCategory);

	@Query(value = "SELECT * FROM schools WHERE school_category in (?1) and gender_composition in (?2)", nativeQuery=true)
	List<School> findSchoolsByCategoryInAndGenderIn(List<String> schoolCategory, List<String> schoolGender);

	@Query(value = "SELECT * FROM schools WHERE school_type in (?1) and gender_composition in (?2)", nativeQuery=true)
	List<School> findSchoolsByTypesInAndGenderIn(List<String> schoolCategory, List<String> schoolGender);

	@Query(value = "SELECT DISTINCT school_zone FROM schools", nativeQuery=true)
	List<String> findSchoolZones();

	@Query(value = "SELECT * FROM schools WHERE school_name LIKE %?1%", nativeQuery=true)
	List<School> findSchoolsByName(String schoolString);

	@Query(value = "SELECT * FROM schools WHERE school_type in (?1) and school_category in (?2) and gender_composition in (?3)", nativeQuery=true)
	List<School> findSchoolsBySchoolTypesInAndCategoryInAndGenderIn(List<String> schoolTypes, List<String> schoolCategories, List<String> schoolGender);


}
