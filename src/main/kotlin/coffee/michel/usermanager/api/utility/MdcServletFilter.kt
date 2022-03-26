package coffee.michel.usermanager.api.utility

import org.slf4j.MDC
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter

/**
 * This filter will add information about the acting user to the log-messages.
 * The added information will be included in the log-pattern.
 *
 * @see logback.xml
 */
@Component
@WebFilter(filterName = "mdcFilter", urlPatterns = ["/*"])
class MdcServletFilter : Filter {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val loggedInUser = SecurityContextHolder.getContext().authentication.principal
        MDC.put("loggedIn.user", "$loggedInUser")

        runCatching { chain.doFilter(request, response) }
            .onFailure { MDC.clear() }
    }
}
