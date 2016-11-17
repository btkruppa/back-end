package cooksys.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cooksys.cache.entity.Popular;

public interface PopularRepo extends JpaRepository<Popular, Long> {

	List<Popular> findByDayPopularGreaterThanOrderByDayPopularDesc(Date date);

}
