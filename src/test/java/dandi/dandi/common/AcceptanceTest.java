package dandi.dandi.common;

import static dandi.dandi.common.RequestURI.LOGIN_REQUEST_URI;
import static dandi.dandi.member.MemberTestFixture.OAUTH_ID;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.amazonaws.services.s3.AmazonS3;
import dandi.dandi.auth.application.dto.LoginRequest;
import dandi.dandi.auth.domain.OAuthClient;
import dandi.dandi.auth.infrastructure.token.RefreshTokenManager;
import dandi.dandi.config.AsyncTestConfig;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(AsyncTestConfig.class)
public class AcceptanceTest {

    private static final String APPLE_IDENTITY_TOKEN = "appleIdentityToken";

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @MockBean
    protected OAuthClient oAuthClient;

    @SpyBean
    protected RefreshTokenManager refreshTokenManager;

    @SpyBean
    protected AmazonS3 amazonS3;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void clearDatabase() {
        databaseCleaner.clear();
    }

    public String getToken() {
        when(oAuthClient.getOAuthMemberId(APPLE_IDENTITY_TOKEN))
                .thenReturn(OAUTH_ID);
        return HttpMethodFixture.httpPost(new LoginRequest(APPLE_IDENTITY_TOKEN), LOGIN_REQUEST_URI)
                .header(AUTHORIZATION);
    }
}
