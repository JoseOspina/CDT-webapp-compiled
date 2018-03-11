package cdt.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cdt.dto.AppUserDto;
import cdt.dto.GetResult;
import cdt.entities.AppUser;
import cdt.services.AppUserService;

@RestController
@RequestMapping("/1")
public class UsersController extends BaseController {
	
	@Autowired
	AppUserService appUserService;
	
	@RequestMapping(path = "/user/myProfile",  method = RequestMethod.GET)
    public GetResult<AppUserDto> myProfile() {
		
		String auth0Id = SecurityContextHolder.getContext().getAuthentication().getName();
		if (auth0Id.equals("anonymousUser")) {
			return new GetResult<AppUserDto>("error", "anonymous user", null);
		}
		
		AppUser user = appUserService.getOrCreateFromAuth0Id(auth0Id);
		return appUserService.getUser(user.getId());
	}
	
	@RequestMapping(path = "/users/suggestions", method = RequestMethod.GET)
	public GetResult<List<AppUserDto>> suggestions(@RequestParam("q") String query) {
		return appUserService.searchBy(query);
	}
	
}
