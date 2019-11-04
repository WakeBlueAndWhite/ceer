package life.gutong.ceer.provider;

import com.alibaba.fastjson.JSON;
import life.gutong.ceer.dto.AccessTokenDTO;
import life.gutong.ceer.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.provider
 * @ClassName: GithubProvider
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/3 20:04
 */
@Component
public class GithubProvider {
    /**
     *
     * @param accessTokenDTO
     * @return 根据accessTokenDTO返回token值
     */
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType= MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accessTokenDTO));
            Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token?" +
                            "client_id=645a2f60bd9b2345d890&client_secret=fe91e30cca72aa7780774aa51f83aee59f675abf" +
                            "&code="+accessTokenDTO.getCode()+"&redirect_uri=http://localhost:9797/callback&state=1")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String string =  response.body().string();
                String token = string.split("&")[0].split("=")[1];
                System.out.println(string);
                System.out.println(token);
                return token;
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
    }

    /**
     *
     * @param accessToken
     * @return 根据token返回具体的用户
     */
    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string =  response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
