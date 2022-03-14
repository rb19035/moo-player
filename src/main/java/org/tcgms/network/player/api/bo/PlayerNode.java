package org.tcgms.network.player.api.bo;

import org.tcgms.network.player.ValidationErrorMsgConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.InetAddress;

public record PlayerNode( @NotNull( message = ValidationErrorMsgConstants.REQUIRED_PARAMETER_CANNOT_BE_NULL ) InetAddress nodeAddress,
                          @NotBlank(message =  ValidationErrorMsgConstants.REQUIRED_PARAMETER_CANNOT_BE_NULL ) String playerNodeName )
{
}
