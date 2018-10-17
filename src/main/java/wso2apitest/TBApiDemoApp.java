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
		
		//1����ȡȱʡApiClient
	    ApiClient apiClient=app.api.getApiClient();
	    //2������APIKEY
	    String accessToken = "5b0e4f6b-5117-3482-86b5-a6f8f5ec1d4f";  
	    apiClient.addDefaultHeader("Accept", "application/json");
	    apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
	    
	    //3�����ý���SSLУ��
	    apiClient.setVerifyingSsl(false);
	    //4���򿪵���ģʽ
	    apiClient.setDebugging(true);
	    
	    //5������API��ַ
	    String basePath = "https://192.168.200.24:8243/api/tb/v2";
	    apiClient.setBasePath(basePath);
	    //6����ȡ�⻧����֤token
	    AuthPayload payload = new AuthPayload();
	    payload.setUsername("tenant@thingsboard.org");
	    payload.setPassword("tenant");
	    System.out.println( payload);
	    
    	AuthToken auth=app.api.apiAuthLoginPost(payload);
    	//��ӡtoken
    	System.out.println( auth.getToken());
    	System.out.println( auth.getRefreshToken());
    	System.out.println( auth);
    
	    //7���ʲ���ѯ��������õ���token���õ�����header��X-Authorization������
	    apiClient.addDefaultHeader("X-Authorization", "Bearer " + auth.getToken());
	    //app.assetApi.setApiClient(apiClient);	    
	    TextPageDataAsset result =  app.assetApi.getTenantAssetsUsingGET("3", null, null, null, null);
	    //7������ʲ���ѯ���
    	System.out.println( result.getData());
	}

}




