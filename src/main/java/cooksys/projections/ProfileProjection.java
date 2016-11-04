package cooksys.projections;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import cooksys.entity.Profile;

public interface ProfileProjection {

	@Value("#{target.profile}")
	Profile getProfil();

	@Value("#{target.joined}")
	Date getJoined();

}
