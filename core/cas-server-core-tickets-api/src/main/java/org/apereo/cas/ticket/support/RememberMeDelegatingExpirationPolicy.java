package org.apereo.cas.ticket.support;

import org.apereo.cas.authentication.RememberMeCredential;
import org.apereo.cas.ticket.ExpirationPolicy;
import org.apereo.cas.ticket.TicketState;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/**
 * Delegates to different expiration policies depending on whether remember me
 * is true or not.
 *
 * @author Scott Battaglia
 * @since 3.2.1
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@Slf4j
public class RememberMeDelegatingExpirationPolicy extends BaseDelegatingExpirationPolicy {


    private static final long serialVersionUID = -2735975347698196127L;

    /**
     * Instantiates a new Remember me delegating expiration policy.
     *
     * @param policy the policy
     */
    @JsonCreator
    public RememberMeDelegatingExpirationPolicy(@JsonProperty("policy") final ExpirationPolicy policy) {
        super(policy);
    }

    @Override
    protected String getExpirationPolicyNameFor(final TicketState ticketState) {
        val attrs = ticketState.getAuthentication().getAttributes();
        val b = (Boolean) attrs.get(RememberMeCredential.AUTHENTICATION_ATTRIBUTE_REMEMBER_ME);

        if (b == null || b.equals(Boolean.FALSE)) {
            LOGGER.debug("Ticket is not associated with a remember-me authentication.");
            return PolicyTypes.DEFAULT.name();
        }
        return PolicyTypes.REMEMBER_ME.name();
    }

    /**
     * Policy types.
     */
    public enum PolicyTypes {
        /**
         * Remember me policy type.
         */
        REMEMBER_ME,
        /**
         * Default policy type.
         */
        DEFAULT
    }
}
