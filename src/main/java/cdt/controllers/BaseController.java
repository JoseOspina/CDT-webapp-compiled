package cdt.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import cdt.entities.AppUser;
import cdt.services.AppUserService;
import cdt.services.OrganizationService;

public class BaseController {
	
	@Autowired
	protected AppUserService appUserService;
	
	@Autowired
	protected OrganizationService organizationService;
	
	
	protected UUID getLoggedUserId() {
		AppUser loggedUser = getLoggedUser();
		if (loggedUser == null) {
			return null;
		} else {
			return loggedUser.getId();
		}
	}
	
	protected AppUser getLoggedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return appUserService.getFromAuth0Id(auth.getName());
	}
} 