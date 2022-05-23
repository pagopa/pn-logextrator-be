package it.gov.pagopa.logextractor.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DomainResponseDto {

	private String type;
	private int code;
}
