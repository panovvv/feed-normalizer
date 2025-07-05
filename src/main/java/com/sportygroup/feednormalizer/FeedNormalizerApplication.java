package com.sportygroup.feednormalizer;

import static com.sportygroup.feednormalizer.adapters.inbound.configuration.SwaggerApiTags.PROVIDER_ALPHA;
import static com.sportygroup.feednormalizer.adapters.inbound.configuration.SwaggerApiTags.PROVIDER_BETA;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
@OpenAPIDefinition(
    info =
        @Info(
            title = "Feed Normalizer API",
            version = "0.0.1",
            description =
                """
      * Parse the incoming messages from a Third Party Feed provider.
      * Convert them into a standardized internal format.
      * Send the standardized message to a message queue
      """,
            contact =
                @Contact(
                    name = "API Support",
                    email = "support@example.com",
                    url = "https://example.com/support"),
            license =
                @License(
                    name = "Apache 2.0",
                    url = "https://www.apache.org/licenses/LICENSE-2.0.html")),
    servers = {
      @Server(url = "http://localhost:8080", description = "Local development"),
      @Server(url = "https://api.example.com", description = "Production server")
    },
    tags = {
      @Tag(name = PROVIDER_ALPHA, description = "All Provider Alpha feeds"),
      @Tag(name = PROVIDER_BETA, description = "All Provider Beta feeds")
    })
public class FeedNormalizerApplication {

  public static void main(String[] args) {
    SpringApplication.run(FeedNormalizerApplication.class, args);
  }
}
