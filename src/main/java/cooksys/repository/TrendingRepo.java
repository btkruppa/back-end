package cooksys.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cooksys.cache.entity.Trending;

public interface TrendingRepo extends JpaRepository<Trending, Long> {

	List<Trending> findByDayTrendingGreaterThanOrderByDayTrendingDesc(Date date);

}
