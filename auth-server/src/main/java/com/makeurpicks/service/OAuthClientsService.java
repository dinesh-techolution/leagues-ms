package com.makeurpicks.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import com.makeurpicks.dao.ClientDetailsDAO;
import com.makeurpicks.domain.OAuthClientDetails;
import com.makeurpicks.exception.OAuthclientValidationException;
import com.makeurpicks.exception.OAuthclientValidationException.OAuthClientExceptions;
import com.makeurpicks.exception.PlayerValidationException;

@Service
public class OAuthClientsService implements ClientDetailsService {
	
	//private static Logger _LOGGER=Logger.getLogger(OAuthClientsService.class);

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		OAuthClientDetails o = dao.findByClientId(clientId);
		return o;
	}

	@Autowired
	private ClientDetailsDAO dao;

	@Autowired
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public OAuthClientDetails createOAuthClientDetails(OAuthClientDetails ouathClientDetails) throws OAuthclientValidationException {
		validateClientDetails(ouathClientDetails);
		String encodedPassword = passwordEncoder.encode(ouathClientDetails.getClient_secret());
		ouathClientDetails.setClient_secret(encodedPassword);
		dao.save(ouathClientDetails);
		return ouathClientDetails;
	}

	public List<OAuthClientDetails> createOAuthClientDetailsList(List<OAuthClientDetails> ouathClientDetailsList) throws OAuthclientValidationException {
		for (OAuthClientDetails oAuthClientDetails : ouathClientDetailsList) {
			validateClientDetails(oAuthClientDetails);
			String encodedPassword = passwordEncoder.encode(oAuthClientDetails.getClient_secret());
			oAuthClientDetails.setClient_secret(encodedPassword);
		}
		dao.save(ouathClientDetailsList);
		return ouathClientDetailsList;
	}

	public void validateClientDetails(OAuthClientDetails ouathClientDetails) throws OAuthclientValidationException {
		List<OAuthClientExceptions> codes = new ArrayList<OAuthClientExceptions>();
		if (ouathClientDetails == null)
			throw new OAuthclientValidationException();

		if (ouathClientDetails.getClientId() == null || "".equals(ouathClientDetails.getClientId()))
			//codes.add(OAuthClientExceptions.CLIENT_ID_NULL);
			codes.add(OAuthClientExceptions.CLIENT_ID_NULL);

		if (ouathClientDetails.getAuthorized_grant_types() == null
				|| ouathClientDetails.getAuthorized_grant_types().size() == 0)
			codes.add(OAuthClientExceptions.AUTH_GRANT_TYPE_NULL_OR_EMPTY);

		if (ouathClientDetails.getScope() == null || ouathClientDetails.getScope().size() == 0)
			codes.add(OAuthClientExceptions.SCOPE_NULL_OR_EMPTY);

		if (ouathClientDetails.getWeb_server_redirect_uri() == null
				|| ouathClientDetails.getWeb_server_redirect_uri().size() == 0)
			codes.add(OAuthClientExceptions.REDIRECT_URI_NULL_OR_EMPTY);

		if (!codes.isEmpty()){
		//	System.out.println("Codes size is:"+codes.size());
			throw new OAuthclientValidationException(codes);
			//throw new PlayerValidationException(codes.toArray());
		}
			
	}
	
	/**
	 * This method is used to get all the client details
	 * @return
	 * @throws ClientRegistrationException
	 */
	public Iterable<OAuthClientDetails> findAllClients() throws ClientRegistrationException {
		
		
		return dao.findAll();
	}


}