
`TokenEndpoint` 整个 OAuth 流程的入口点.

```java
class TokenEndpoint{
   @RequestMapping(value = "/oauth/token", method=RequestMethod.POST)
   public ResponseEntity<OAuth2AccessToken> postAccessToken(Principal principal, @RequestParam
   Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
   
       if (!(principal instanceof Authentication)) {
           throw new InsufficientAuthenticationException(
                   "There is no client authentication. Try adding an appropriate authentication filter.");
       }
   
       String clientId = getClientId(principal);
       // 通过 clientDetailsService 取出 clientId 对应的 Client 信息
       ClientDetails authenticatedClient = getClientDetailsService().loadClientByClientId(clientId);
   
       // 获取token的请求，即 param 中的值
       TokenRequest tokenRequest = getOAuth2RequestFactory().createTokenRequest(parameters, authenticatedClient);
   
       // 验证 clientId 是否在请求中
       if (clientId != null && !clientId.equals("")) {
           // 验证客户端是否注册了
           if (!clientId.equals(tokenRequest.getClientId())) {
               // double check to make sure that the client ID in the token request is the same as that in the
               // authenticated client
               throw new InvalidClientException("Given client ID does not match authenticated client");
           }
       }
       if (authenticatedClient != null) {
           oAuth2RequestValidator.validateScope(tokenRequest, authenticatedClient);
       }
       if (!StringUtils.hasText(tokenRequest.getGrantType())) {
           throw new InvalidRequestException("Missing grant type");
       }
       if (tokenRequest.getGrantType().equals("implicit")) {
           throw new InvalidGrantException("Implicit grant type not supported from token endpoint");
       }
   
       if (isAuthCodeRequest(parameters)) {
           // The scope was requested or determined during the authorization step
           if (!tokenRequest.getScope().isEmpty()) {
               logger.debug("Clearing scope of incoming token request");
               tokenRequest.setScope(Collections.<String> emptySet());
           }
       }
   
       if (isRefreshTokenRequest(parameters)) {
           // A refresh token has its own default scopes, so we should ignore any added by the factory here.
           tokenRequest.setScope(OAuth2Utils.parseParameterList(parameters.get(OAuth2Utils.SCOPE)));
       }
   
       // 从这里开始，先获得 token 分发器(getTokenGranter) ，分别进行验证，存在一个则进行保存
       OAuth2AccessToken token = getTokenGranter().grant(tokenRequest.getGrantType(), tokenRequest);
       // 如果一个都没有，则抛出异常，不支持的授权类型(授权类型包含：password,refresh_token,authorization_code,implicit,client_credentials)
       if (token == null) {
           throw new UnsupportedGrantTypeException("Unsupported grant type: " + tokenRequest.getGrantType());
       }
   
       return getResponse(token);
   } 
}
```

`TokenGranter` 接口，包含了所有可授权的类型，以及每个授权类型对应的 `grant` 验证方式，其实现类如下所示：

![TokenGranter授权类型](https://koral-home.oss-cn-beijing.aliyuncs.com/springcloud/TokenGranter.png)

 当以上授权有一个通过之后，会调用 AbstractTokenGranter.getAccessToken() 方法，并通过 AuthorizationServerTokenServices.createAccessToken() 方法生成并持久化 Token.
 
 具体创建方法如下：
 
 ```java
class DefaultTokenServices{
    @Transactional
    	public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
    
    		OAuth2AccessToken existingAccessToken = tokenStore.getAccessToken(authentication);
    		OAuth2RefreshToken refreshToken = null;
    		if (existingAccessToken != null) {
    			if (existingAccessToken.isExpired()) {
    				if (existingAccessToken.getRefreshToken() != null) {
    					refreshToken = existingAccessToken.getRefreshToken();
    					// The token store could remove the refresh token when the
    					// access token is removed, but we want to
    					// be sure...
    					tokenStore.removeRefreshToken(refreshToken);
    				}
    				tokenStore.removeAccessToken(existingAccessToken);
    			}
    			else {
    				// Re-store the access token in case the authentication has changed
    				tokenStore.storeAccessToken(existingAccessToken, authentication);
    				return existingAccessToken;
    			}
    		}
    
    		// Only create a new refresh token if there wasn't an existing one
    		// associated with an expired access token.
    		// Clients might be holding existing refresh tokens, so we re-use it in
    		// the case that the old access token
    		// expired.
    		if (refreshToken == null) {
    			refreshToken = createRefreshToken(authentication);
    		}
    		// But the refresh token itself might need to be re-issued if it has
    		// expired.
    		else if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
    			ExpiringOAuth2RefreshToken expiring = (ExpiringOAuth2RefreshToken) refreshToken;
    			if (System.currentTimeMillis() > expiring.getExpiration().getTime()) {
    				refreshToken = createRefreshToken(authentication);
    			}
    		}
    
    		OAuth2AccessToken accessToken = createAccessToken(authentication, refreshToken);
    		tokenStore.storeAccessToken(accessToken, authentication);
    		// In case it was modified
    		refreshToken = accessToken.getRefreshToken();
    		if (refreshToken != null) {
    			tokenStore.storeRefreshToken(refreshToken, authentication);
    		}
    		return accessToken;
    
    	}
}
```