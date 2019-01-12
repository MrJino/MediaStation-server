package noh.jinil.boot.config

import noh.jinil.boot.auth.firebase.FirebaseRequestFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.schema.ModelRef
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.*

@Configuration
@EnableSwagger2
class SwaggerConfig {
    @Bean
    @Suppress("unused")
    fun api(): Docket {
        val params = ParameterBuilder()
                .name(FirebaseRequestFilter.HEADER_NAME)
                .description(FirebaseRequestFilter.HEADER_EXPLAIN)
                .modelRef(ModelRef("string"))
                .parameterType("header")
                .required(true)
                .build()

        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("noh.jinil.boot.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(Arrays.asList(params))
    }
}