package it.gov.pagopa.logextractor.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import it.gov.pagopa.logextractor.util.constant.GenericConstants;
import it.gov.pagopa.logextractor.util.constant.ResponseConstants;
import it.gov.pagopa.logextractor.dto.response.DownloadArchiveResponseDto;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.apache.commons.io.FileUtils;

/**
 * Utility class to manage the server response construction
 */
public class ResponseConstructor {

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
	public static DownloadArchiveResponseDto createSimpleLogResponse(List<String> contents, String fileName, String zipName) throws IOException {
		PasswordFactory passwordFactory = new PasswordFactory();
		String password = passwordFactory.createPassword(1, 1, 1, GenericConstants.SPECIAL_CHARS, 1, 16);
		FileUtilities utils = new FileUtilities();
		File file = utils.getFile(fileName, GenericConstants.TXT_EXTENSION);
		utils.write(file, contents);
		ZipFactory zipFactory = new ZipFactory();
		ZipFile zipArchive = zipFactory.createZipArchive(zipName, password);
		ZipParameters params = zipFactory.createZipParameters(true, CompressionLevel.HIGHER, EncryptionMethod.AES);
		zipArchive = zipFactory.addFile(zipArchive, params, file);
		byte[] zipfile = zipFactory.toByteArray(zipArchive);
		utils.deleteFile(file);
		utils.deleteFile(FileUtils.getFile(zipArchive.toString()));
		DownloadArchiveResponseDto serviceResponse = new DownloadArchiveResponseDto();
		serviceResponse.setPassword(password);
		serviceResponse.setZip(zipfile);
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
		ZipParameters params = zipFactory.createZipParameters(true, CompressionLevel.HIGHER, EncryptionMethod.AES);
		zipArchive = zipFactory.addFiles(zipArchive, params, csvFiles);
		byte[] zipfile = zipFactory.toByteArray(zipArchive);
		utils.deleteFiles(csvFiles);
		utils.deleteFile(FileUtils.getFile(zipArchive.toString()));
		DownloadArchiveResponseDto serviceResponse = new DownloadArchiveResponseDto();
		serviceResponse.setPassword(password);
		serviceResponse.setZip(zipfile);
		serviceResponse.setMessage(ResponseConstants.SUCCESS_RESPONSE_MESSAGE);
		return serviceResponse;
	}
	
	/**
	 * Method that manages the notification logs response creation phase.
	 * 
	 * @param openSearchLogs the contents from OpenSearch to write in the output
	 *                       file (.txt), contained in the output zip archive
	 * @param filesToAdd     list, containing every notification file to add in the
	 *                       zip archive
	 * @param fileName       the name of file, containing the logs in the output zip
	 *                       archive
	 * @param zipName        the name of the output zip archive
	 * @return {@link DownloadArchiveResponseDto} containing a byte array
	 *         representation of the output zip archive and the password to access
	 *         its files
	 * @throws IOException in case of an IO error
	 */
	public static DownloadArchiveResponseDto createNotificationLogResponse(List<String> openSearchLogs, List<File> filesToAdd, String fileName, String zipName) throws IOException {
		PasswordFactory passwordFactory = new PasswordFactory();
		String password = passwordFactory.createPassword(1, 1, 1, GenericConstants.SPECIAL_CHARS, 1, 16);
		FileUtilities utils = new FileUtilities();
		ZipFactory zipFactory = new ZipFactory();
		ZipFile zipArchive = zipFactory.createZipArchive(zipName, password);
		ZipParameters params = zipFactory.createZipParameters(true, CompressionLevel.HIGHER, EncryptionMethod.AES);
		zipFactory.addFiles(zipArchive, params, filesToAdd);
		if(!openSearchLogs.isEmpty()){
			File logFile = utils.getFile(fileName, GenericConstants.TXT_EXTENSION);
			utils.write(logFile, openSearchLogs);
			zipArchive = zipFactory.addFile(zipArchive, params, logFile);
			utils.deleteFile(logFile);
		}
		byte[] zipfile = zipFactory.toByteArray(zipArchive);
		utils.deleteFiles(filesToAdd);
		utils.deleteFile(FileUtils.getFile(zipArchive.toString()));
		DownloadArchiveResponseDto serviceResponse = new DownloadArchiveResponseDto();
		serviceResponse.setPassword(password);
		serviceResponse.setZip(zipfile);
		serviceResponse.setMessage(ResponseConstants.SUCCESS_RESPONSE_MESSAGE);
		return serviceResponse;
	}
}
