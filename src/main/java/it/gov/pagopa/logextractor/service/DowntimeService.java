package it.gov.pagopa.logextractor.service;

import it.gov.pagopa.logextractor.exception.LogExtractorException;
import it.gov.pagopa.logextractor.pn_logextractor_be.model.BaseResponseDto;
import it.gov.pagopa.logextractor.pn_logextractor_be.model.PnStatusUpdateEventRequestDto;

import java.util.List;

public interface DowntimeService {

    BaseResponseDto addStatusChangeEvent(List<PnStatusUpdateEventRequestDto> pnStatusUpdateEventRequestDto);

    BaseResponseDto getCurrentStatus() throws LogExtractorException;
}