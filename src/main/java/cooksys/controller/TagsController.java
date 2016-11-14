package cooksys.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cooksys.entity.Tag;
import cooksys.entity.Tweet;
import cooksys.service.TagService;

@RestController
@CrossOrigin
@RequestMapping("tags")
public class TagsController {

	private TagService tagService;

	public TagsController(TagService service) {
		this.tagService = service;
	}

	@GetMapping
	public List<Tag> getTags() {
		return tagService.getTags();
	}

	@GetMapping("/{label}")
	public List<Tweet> getTweetsByTag(@PathVariable String label) {
		return tagService.getTweetsByTag('#' + label);
	}
}
