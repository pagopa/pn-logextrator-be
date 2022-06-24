package it.gov.pagopa.logextractor.dto.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetBasicDataResponseDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String data;
}
