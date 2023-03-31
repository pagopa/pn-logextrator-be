package it.gov.pagopa.logextractor.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.apache.commons.io.FileUtils;

import it.gov.pagopa.logextractor.dto.response.DownloadArchiveResponseDto;
import it.gov.pagopa.logextractor.util.constant.GenericConstants;
import it.gov.pagopa.logextractor.util.constant.ResponseConstants;
import it.gov.pagopa.logextractor.util.external.pnservices.NotificationDownloadFileData;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;

/**
 * Utility class to manage the server response construction
 */
public class ResponseConstructor {
	
	final static CompressionLevel compressionLevel = CompressionLevel.NO_COMPRESSION;

	private ResponseConstructor(){}
	
	/**
	 * Manages the response creation phase.
	 *
	 * @param contents the contents to write in the output file (.txt) contained in
	 *                 the output zip archive
	 * @param fileName the name of the output file contained in the output zip
	 *                 archive
	 * @param zipName  the name of the output zip archive
	 * @throws IOException in case IO errors
	 * @return {@link DownloadArchiveResponseDto} A Dto containing a byte array
	 *         representation of the output zip archive and the password to access
	 *         its files
	 */
	public static DownloadArchiveResponseDto createSimpleLogResponse(File file, String fileName, String zipName) throws IOException {
		PasswordFactory passwordFactory = new PasswordFactory();
		String password = passwordFactory.createPassword(1, 1, 1, GenericConstants.SPECIAL_CHARS, 1, 16);
		ZipFactory zipFactory = new ZipFactory();
		ZipFile zipArchive = zipFactory.createZipArchive(zipName, password);
		ZipParameters params = zipFactory.createZipParameters(true, compressionLevel, EncryptionMethod.AES);
		zipArchive = zipFactory.addFile(zipArchive, params, file);
		DownloadArchiveResponseDto serviceResponse = new DownloadArchiveResponseDto();
		serviceResponse.setPassword(password);
		serviceResponse.setZipFile(FileUtils.getFile(zipArchive.toString()));
		serviceResponse.setMessage(ResponseConstants.SUCCESS_RESPONSE_MESSAGE);
		return serviceResponse;
	}
	
	/**
	 * Manages the response creation phase.
	 *
	 * @param zipName  the name of the output zip archive
	 * @throws IOException in case IO errors
	 * @return {@link DownloadArchiveResponseDto} A Dto containing a byte array
	 *         representation of the output zip archive and the password to access
	 *         its files
	 * @throws  IOException in case an exception related with files occurs
	 */
	public static DownloadArchiveResponseDto createCsvFileResponse(List<File> csvFiles, String zipName) throws IOException {
		PasswordFactory passwordFactory = new PasswordFactory();
		String password = passwordFactory.createPassword(1, 1, 1, GenericConstants.SPECIAL_CHARS, 1, 16);
		FileUtilities utils = new FileUtilities();
		ZipFactory zipFactory = new ZipFactory();
		ZipFile zipArchive = zipFactory.createZipArchive(zipName, password);
		ZipParameters params = zipFactory.createZipParameters(true, compressionLevel, EncryptionMethod.AES);
		zipArchive = zipFactory.addFiles(zipArchive, params, csvFiles);
		utils.delete(csvFiles);
		DownloadArchiveResponseDto serviceResponse = new DownloadArchiveResponseDto();
		serviceResponse.setPassword(password);
		serviceResponse.setZipFile(FileUtils.getFile(zipArchive.toString()));
		serviceResponse.setMessage(ResponseConstants.SUCCESS_RESPONSE_MESSAGE);
		return serviceResponse;
	}
	
	/**
	 * Method that manages the notification logs response creation phase.
	 * 
	 * @param filesToAdd     list, containing every notification file to add in the
	 *                       zip archive
	 * @param filesNotDownloadable the list of files that couldn't be downloaded during the execution
	 * @param fileName       the name of file, containing the logs in the output zip
	 *                       archive
	 * @param zipName        the name of the output zip archive
	 * @return {@link DownloadArchiveResponseDto} containing a byte array
	 *         representation of the output zip archive and the password to access
	 *         its files
	 * @throws IOException in case of an IO error
	 */
	public static DownloadArchiveResponseDto createNotificationLogResponse(List<File> filesToAdd,
																		   List<NotificationDownloadFileData> filesNotDownloadable,
																		   String fileName,
																		   String zipName) throws IOException {
		PasswordFactory passwordFactory = new PasswordFactory();
		String password = passwordFactory.createPassword(1, 1, 1, GenericConstants.SPECIAL_CHARS, 1, 16);
		FileUtilities fileUtils = new FileUtilities();
		ZipFactory zipFactory = new ZipFactory();
		ZipFile zipArchive = zipFactory.createZipArchive(zipName, password);
		ZipParameters params = zipFactory.createZipParameters(true, compressionLevel , EncryptionMethod.AES);
		zipFactory.addFiles(zipArchive, params, filesToAdd);
		if(!filesNotDownloadable.isEmpty()){
			File failureSummaryFile = fileUtils.getFile(GenericConstants.ERROR_SUMMARY_FILE_NAME, GenericConstants.TXT_EXTENSION);
			JsonUtilities jsonUtilities = new JsonUtilities();
			String failsToString = jsonUtilities.toString(jsonUtilities.toJson(filesNotDownloadable));
			fileUtils.write(failureSummaryFile, failsToString);
			zipArchive = zipFactory.addFile(zipArchive, params, failureSummaryFile);
			fileUtils.delete(failureSummaryFile);
		}
		
		fileUtils.delete(filesToAdd);
		DownloadArchiveResponseDto serviceResponse = new DownloadArchiveResponseDto();
		serviceResponse.setPassword(password);
		serviceResponse.setZipFile(FileUtils.getFile(zipArchive.toString()));
		serviceResponse.setMessage(ResponseConstants.SUCCESS_RESPONSE_MESSAGE);
		return serviceResponse;
	}
}
