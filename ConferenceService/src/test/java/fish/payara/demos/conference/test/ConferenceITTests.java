package fish.payara.demos.conference.test;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Paths;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.hasSize;

@Testcontainers
public class ConferenceITTests {

    private static final MountableFile WAR = MountableFile.forHostPath(Paths.get("target/conference-service-demo.war"));

    static Network network = Network.newNetwork();

    @Container
    GenericContainer payara = new GenericContainer(DockerImageName.parse("payara/micro:5.2022.1-jdk11"))
            .withCopyFileToContainer(WAR, "/opt/payara/deployments/app.war")
            .withExposedPorts(8080)
            .waitingFor(Wait.forHttp("/application.wadl").forStatusCode(200))
            .withCommand("--deploy /opt/payara/deployments/app.war --noCluster --contextRoot /")
            .withNetwork(network)
            .dependsOn(mysql)
            .withEnv("DB_USER", mysql.getUsername())
            .withEnv("DB_PASSWORD", mysql.getPassword())
            .withEnv("DB_JDBC_URL",  "jdbc:mysql://mysql:3306/" + mysql.getDatabaseName());

    @Container
    static MySQLContainer mysql = new MySQLContainer<>("mysql:8.0")
            .withNetwork(network)
            .withNetworkAliases("mysql");

    @Test
    public void listSpeakers(){
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("http://" + payara.getHost() + ":" +payara.getMappedPort(8080) + "/speaker/all")
                .then()
                .assertThat().statusCode(200)
                .and()
                .body("$", hasSize(2));
    }
}
