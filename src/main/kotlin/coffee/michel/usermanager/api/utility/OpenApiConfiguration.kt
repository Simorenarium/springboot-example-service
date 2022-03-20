package coffee.michel.usermanager.api.utility

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfiguration {

    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("User Management")
                    .description("Stores, Manages, and Authenticates Users.")
                    .version("v1.0")
                    .contact(
                        Contact()
                            .name("Jonas Michel")
                            .email("jonas@michel.coffee")
                    )
            )
    }
}
