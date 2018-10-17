package wso2apitest;

import org.wso2.client.api.ApiClient;
import org.wso2.client.api.ApiException;
import org.wso2.client.api.tbapi.*;
import org.wso2.client.model.tbapi.AuthPayload;
import org.wso2.client.model.tbapi.AuthToken;
import org.wso2.client.model.tbapi.TextPageDataAsset;

public class TBApiDemoApp {
	
	private final AuthControllerApi api = new AuthControllerApi();
	private final AssetControllerApi assetApi = new AssetControllerApi();
	public static void main(String[] args) throws ApiException {
		TBApiDemoApp app=new TBApiDemoApp();
		
		//1、获取缺省ApiClient
	    ApiClient apiClient=app.api.getApiClient();
	    //2、设置APIKEY
	    String accessToken = "5b0e4f6b-5117-3482-86b5-a6f8f5ec1d4f";  
	    apiClient.addDefaultHeader("Accept", "application/json");
	    apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
	    
	    //3、设置禁用SSL校验
	    apiClient.setVerifyingSsl(false);
	    //4、打开调试模式
	    apiClient.setDebugging(true);
	    
	    //5、设置API地址
	    String basePath = "https://192.168.200.24:8243/api/tb/v2";
	    apiClient.setBasePath(basePath);
	    //6、获取租户的认证token
	    AuthPayload payload = new AuthPayload();
	    payload.setUsername("tenant@thingsboard.org");
	    payload.setPassword("tenant");
	    System.out.println( payload);
	    
    	AuthToken auth=app.api.apiAuthLoginPost(payload);
    	//打印token
    	System.out.println( auth.getToken());
    	System.out.println( auth.getRefreshToken());
    	System.out.println( auth);
    
	    //7、资产查询。把上面得到的token设置到请求header的X-Authorization变量中
	    apiClient.addDefaultHeader("X-Authorization", "Bearer " + auth.getToken());
	    //app.assetApi.setApiClient(apiClient);	    
	    TextPageDataAsset result =  app.assetApi.getTenantAssetsUsingGET("3", null, null, null, null);
	    //7、输出资产查询结果
    	System.out.println( result.getData());
	}

}




