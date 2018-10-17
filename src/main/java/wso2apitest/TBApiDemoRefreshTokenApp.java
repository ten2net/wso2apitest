package wso2apitest;

import java.util.logging.Logger;

import org.wso2.client.api.ApiClient;
import org.wso2.client.api.ApiException;
import org.wso2.client.api.tbapi.*;
import org.wso2.client.model.tbapi.AuthPayload;
import org.wso2.client.model.tbapi.AuthToken;
import org.wso2.client.model.tbapi.Payload;
import org.wso2.client.model.tbapi.TextPageDataAsset;

public class TBApiDemoRefreshTokenApp {
	private final Logger logger = Logger.getLogger(TBApiDemoRefreshTokenApp.class.getName());
	
	private final AuthControllerApi authApi = new AuthControllerApi();
	private final AssetControllerApi assetApi = new AssetControllerApi();
	
	public static void main(String[] args) throws ApiException {
		TBApiDemoRefreshTokenApp app=new TBApiDemoRefreshTokenApp();
		//1、获取缺省ApiClient
	    ApiClient apiClient=app.authApi.getApiClient();
	    //2、设置APIKEY
	    String apikey = "5b0e4f6b-5117-3482-86b5-a6f8f5ec1d4f"; 
	    String basePath = "https://192.168.200.24:8243/api/tb/v3";
	    String tbUsername = "tenant@thingsboard.org";
	    String tbPassword = "tenant";	    
	    apiClient.addDefaultHeader("Accept", "application/json");
	    apiClient.addDefaultHeader("Authorization", "Bearer " + apikey);
	    
	    //3、设置禁用SSL校验
	    apiClient.setVerifyingSsl(false);
	    //4、打开调试模式
	    apiClient.setDebugging(false);
	    
	    //5、设置API地址
	    apiClient.setBasePath(basePath);
	    //6、获取租户的认证token
	    AuthPayload payload = new AuthPayload();
	    payload.setUsername(tbUsername);
	    payload.setPassword(tbPassword);
	    app.logger.info( payload.toString());
	    
    	AuthToken auth=app.authApi.apiAuthLoginPost(payload);
    	//打印token
//    	app.logger.info( auth.getToken());
//    	app.logger.info( auth.getRefreshToken());
//    	app.logger.info( auth.toString());
    
    	//7、把上面得到的token设置到请求header的X-Authorization变量中
    	apiClient.addDefaultHeader("X-Authorization", "Bearer " + auth.getToken());
	    //8、资产查询
	    String idOffset = null;
	    while(true){
	    	try{
			    TextPageDataAsset result =  app.assetApi.getTenantAssetsUsingGET("3", null, null, idOffset, null);
			    if (result.getHasNext())
			    	idOffset = result.getNextPageLink().getIdOffset();
			    else
			    	idOffset = null;
			    //7、输出资产查询结果
			    app.logger.info(result.getData().toString());
	    	}catch(ApiException e){
	    		app.logger.warning(e.getResponseBody());
	    		if(e.getResponseBody().contains("\"errorCode\":11")){  //{"status":401,"message":"Token has expired","errorCode":11}
	    			Payload refreshTokenPayload = new Payload();
	    			refreshTokenPayload.setRefreshToken(auth.getRefreshToken());
	    			try{
	    				auth = app.authApi.apiAuthTokenPost(refreshTokenPayload);
	    			}catch(ApiException ee){
	    				ee.printStackTrace();	    				
	    			}
	    			apiClient.addDefaultHeader("X-Authorization", "Bearer " + auth.getToken());
	    		}
	    	}
	    	try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}

}




