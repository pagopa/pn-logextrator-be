package it.gov.pagopa.logextractor.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import it.gov.pagopa.logextractor.util.Constants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonthlyNotificationsRequestDto extends BaseRequestDto {
	
	@NotBlank
	@Pattern(regexp = Constants.INPUT_MONTH_FORMAT) 
	private String referenceMonth;
	@NotBlank
	private String ipaCode;

}
