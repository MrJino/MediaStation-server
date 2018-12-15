package noh.jinil.boot

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Suppress("unused")
@Configuration
class MvcConfig : WebMvcConfigurer {

    @Value("\${static.scheme}")
    private val staticScheme: String? = null

    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/").setViewName("home")
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/")

        registry.addResourceHandler("/scan/**")
                .addResourceLocations("${staticScheme}/scan/")
        registry.addResourceHandler("/data/**")
                .addResourceLocations("${staticScheme}/data/")
    }
}