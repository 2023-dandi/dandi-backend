package dandi.dandi.common;

import static dandi.dandi.common.HttpMethodFixture.httpPostWithAuthorization;
import static dandi.dandi.common.RequestURI.LOGIN_REQUEST_URI;
import static dandi.dandi.common.RequestURI.POST_REGISTER_REQUEST_URI;
import static dandi.dandi.member.MemberTestFixture.OAUTH_ID;
import static dandi.dandi.post.PostFixture.ADDITIONAL_OUTFIT_FEELING_INDICES;
import static dandi.dandi.post.PostFixture.MAX_TEMPERATURE;
import static dandi.dandi.post.PostFixture.MIN_TEMPERATURE;
import static dandi.dandi.post.PostFixture.OUTFIT_FEELING_INDEX;
import static dandi.dandi.post.PostFixture.POST_IMAGE_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import dandi.dandi.auth.adapter.in.web.dto.LoginRequest;
import dandi.dandi.auth.adapter.out.jwt.RefreshTokenManagerAdapter;
import dandi.dandi.auth.application.port.in.TokenResponse;
import dandi.dandi.auth.application.port.out.oauth.OAuthClientPort;
import dandi.dandi.config.AsyncTestConfig;
import dandi.dandi.post.adapter.in.web.dto.OutfitFeelingRequest;
import dandi.dandi.post.adapter.in.web.dto.PostRegisterRequest;
import dandi.dandi.post.adapter.in.web.dto.TemperatureRequest;
import dandi.dandi.post.application.port.in.PostRegisterResponse;
import io.restassured.RestAssured;
import java.time.Clock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,
        properties = "cloud.aws.cloud-front.uri=https://www.cloud-front.com/")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(AsyncTestConfig.class)
public class AcceptanceTest {

    private static final String APPLE_IDENTITY_TOKEN = "appleIdentityToken";
    private static final String APPLE_IDENTITY_TOKEN2 = "appleIdentityToken2";
    protected static final String PUSH_NOTIFICATION_TOKEN = "pushNotificationToken";
    private static final String OAUTH_ID2 = "oAuthId2";

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @MockBean
    protected OAuthClientPort oAuthClientPort;

    @SpyBean
    protected RefreshTokenManagerAdapter refreshTokenManager;

    @SpyBean
    protected AmazonS3 amazonS3;

    @SpyBean
    protected Clock clock;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void clearDatabase() {
        databaseCleaner.truncate();
        databaseCleaner.initializeAutoIncrement();
    }

    public String getToken() {
        when(oAuthClientPort.getOAuthMemberId(APPLE_IDENTITY_TOKEN))
                .thenReturn(OAUTH_ID);
        return HttpMethodFixture.httpPost(
                        new LoginRequest(APPLE_IDENTITY_TOKEN, PUSH_NOTIFICATION_TOKEN), LOGIN_REQUEST_URI)
                .jsonPath()
                .getObject(".", TokenResponse.class)
                .getAccessToken();
    }

    public String getAnotherMemberToken() {
        when(oAuthClientPort.getOAuthMemberId(APPLE_IDENTITY_TOKEN2))
                .thenReturn(OAUTH_ID2);
        return HttpMethodFixture.httpPost(
                        new LoginRequest(APPLE_IDENTITY_TOKEN2, PUSH_NOTIFICATION_TOKEN), LOGIN_REQUEST_URI)
                .jsonPath()
                .getObject(".", TokenResponse.class)
                .getAccessToken();
    }

    protected Long registerPost(String token) {
        PostRegisterRequest postRegisterRequest = new PostRegisterRequest(POST_IMAGE_URL,
                new TemperatureRequest(MIN_TEMPERATURE, MAX_TEMPERATURE),
                new OutfitFeelingRequest(OUTFIT_FEELING_INDEX, ADDITIONAL_OUTFIT_FEELING_INDICES));
        return httpPostWithAuthorization(POST_REGISTER_REQUEST_URI, postRegisterRequest, token)
                .jsonPath()
                .getObject(".", PostRegisterResponse.class)
                .getPostId();
    }

    protected void mockAmazonS3Exception() {
        Mockito.doThrow(AmazonClientException.class)
                .when(amazonS3)
                .putObject(any(), any(), any(), any());
    }
}
