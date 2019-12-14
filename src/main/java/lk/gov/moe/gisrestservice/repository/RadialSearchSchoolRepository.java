package lk.gov.moe.gisrestservice.repository;
import lk.gov.moe.gisrestservice.model.RadialSearchSchool;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RadialSearchSchoolRepository extends CrudRepository<RadialSearchSchool, Long> {


	@Query(value = "SELECT *, (3959 * acos (cos ( radians(?1) )* cos( radians( latitude ) ) * cos( radians( longitude ) - radians(?2) ) + sin ( radians(?1) ) * sin( radians( latitude ) ))) AS distance FROM schools HAVING distance < ?3 ORDER BY distance", nativeQuery = true)
	List<RadialSearchSchool> radialSearchAll(Float centerLatitude, Float centerLongitude, Double radius);

	@Query(value = "SELECT *, (3959 * acos (cos ( radians(?1) )* cos( radians( latitude ) ) * cos( radians( longitude ) - radians(?2) ) + sin ( radians(?1) ) * sin( radians( latitude ) ))) AS distance FROM schools HAVING distance < ?3 ORDER BY distance DESC", nativeQuery = true)
	List<RadialSearchSchool> radialSearchAllDesc(Float centerLatitude, Float centerLongitude, Double radius);


	@Query(value = "SELECT *, (3959 * acos (cos ( radians(?1) )* cos( radians( latitude ) ) * cos( radians( longitude ) - radians(?2) ) + sin ( radians(?1) ) * sin( radians( latitude ) ))) AS distance FROM schools HAVING distance < ?3 ORDER BY distance LIMIT 0, ?4", nativeQuery = true)
	List<RadialSearchSchool> radialSearchLimit(Float centerLatitude, Float centerLongitude, Double radius, Integer limit);


	@Query(value = "SELECT *, (3959 * acos (cos ( radians(?1) )* cos( radians( latitude ) ) * cos( radians( longitude ) - radians(?2) ) + sin ( radians(?1) ) * sin( radians( latitude ) ))) AS distance FROM schools HAVING distance < ?3 ORDER BY distance DESC LIMIT 0, ?4", nativeQuery = true)
	List<RadialSearchSchool> radialSearchLimitDesc(Float centerLatitude, Float centerLongitude, Double radius, Integer limit);


}
