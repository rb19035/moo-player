package org.tcgms.network.player;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class HttpLoggingFilterConfig
{
    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter()
    {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo( true );
        loggingFilter.setIncludeQueryString( true );
        loggingFilter.setIncludePayload( true );
        loggingFilter.setIncludeHeaders( true );
        return loggingFilter;
    }
}
