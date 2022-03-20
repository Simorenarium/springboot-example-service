package coffee.michel.usermanager.api.utility

import org.slf4j.MDC
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import kotlin.runCatching

/**
 * This filter will add information about the acting user to the log-messages.
 */
// TODO check if this is necessary
@Component
@WebFilter(filterName = "mdcFilter", urlPatterns = ["/*"])
class MdcServletFilter : Filter {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        // TODO take username or id
        // TODO add bean interceptor for trace logs
        MDC.put("loggedIn.user", "...")

        runCatching { chain.doFilter(request, response) }
            .onFailure { MDC.clear() }
    }
}
