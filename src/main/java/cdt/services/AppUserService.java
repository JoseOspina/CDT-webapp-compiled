package cdt.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.mgmt.users.User;

import cdt.dto.AppUserDto;
import cdt.dto.GetResult;
import cdt.dto.PostResult;
import cdt.entities.AppUser;
import cdt.entities.Organization;

@Service
public class AppUserService extends BaseService {
	
	@Autowired
	private ManagementAPI mgmt;

	@Transactional
	public AppUser getOrCreateFromAuth0Id(String auth0Id) {
		
		AppUser appUser = appUserRepository.findByAuth0Id(auth0Id);
		
		if(appUser == null) {
			/* the first time a user is requested, it 
			 * is added to the local DB */
			appUser = addUserToLocalDB(auth0Id);
		}
    	
    	return appUser;
	}
	
	@Transactional
	public AppUser getFromAuth0Id(String auth0Id) {
    	return appUserRepository.findByAuth0Id(auth0Id);
	}
	
	@Transactional
	public AppUser getFromId(UUID id) {
		return appUserRepository.findById(id);
	}
	
	@Transactional
	public GetResult<AppUserDto> getUser(UUID id) {
		AppUserDto userDto = getFromId(id).toDto();
		
		List<Organization> orgs = organizationRepository.findByAdmins_IdOrderByCreationDateDesc(id);
		
		for (Organization org : orgs) {
			userDto.getOrganizations().add(org.toDto());
		}
		
		return new GetResult<AppUserDto>("success", "user profile retrieved", userDto);
	}
	
	@Transactional
	public GetResult<List<AppUserDto>> searchBy(String q) {
		List<AppUser> appUsers = appUserRepository.findTop10ByNicknameLikeIgnoreCase("%"+q+"%");
		
		List<AppUserDto> appUserDtos = new ArrayList<AppUserDto>();
		
		for(AppUser appUser : appUsers) {
			appUserDtos.add(appUser.toDto());
		}
		
		return new GetResult<List<AppUserDto>>("succes", "initiatives returned", appUserDtos);
	}
	
	@Transactional
	private AppUser addUserToLocalDB(String auth0Id) {
		/* retrieve from Auth0 */
		AppUser appUser = null;
		User auth0User = null;
		
		if (auth0Id.equals("anonymousUser")) {
			return null;
		}
		
		try {
			auth0User = mgmt.users().get(auth0Id, null).execute();
			
			/* check if this email is already registered. */
			appUser = appUserRepository.findByEmail(auth0User.getEmail());
			
			if (appUser == null) {
				// if (auth0User.isEmailVerified()) {
				 if (true) {
					/* create a new user if not */
					appUser = new AppUser();
					
					appUser.getAuth0Ids().add(auth0User.getId());
					appUser.setEmail(auth0User.getEmail());
					appUser.setNickname(auth0User.getNickname());
					appUser.setEmailNotificationsEnabled(true);
				} 
			} else {
				/* just add the auth0id to the existing user */
				appUser.getAuth0Ids().add(auth0Id); 
			}
			
			appUser = appUserRepository.save(appUser);
			
		} catch (APIException exception) {
		    System.out.println(exception.getMessage());
		} catch (Auth0Exception exception) {
			System.out.println(exception.getMessage());
		}
		
		return appUser;
	} 
	
	@Transactional
	public PostResult disableEmailNotifications(UUID userId) {
		AppUser user = appUserRepository.findById(userId);
		user.setEmailNotificationsEnabled(false);
		return new PostResult("success", "email notifications disabled", "");
	}

}
