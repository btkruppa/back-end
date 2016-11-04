package cooksys.projections;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import cooksys.entity.Profile;

public interface UserProjection {

	@Value("#{target.profile}")
	Profile getProfile();

	@Value("#{target.joined}")
	Date getJoined();

}
