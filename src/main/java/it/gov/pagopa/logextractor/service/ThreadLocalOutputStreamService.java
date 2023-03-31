package it.gov.pagopa.logextractor.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import it.gov.pagopa.logextractor.util.PasswordFactory;
import it.gov.pagopa.logextractor.util.ZipArchiverImpl;
import it.gov.pagopa.logextractor.util.constant.GenericConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.lingala.zip4j.io.outputstream.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.EncryptionMethod;

@Component
public class ThreadLocalOutputStreamService {

	@Data
	@AllArgsConstructor
	class ZipInfo{
		String password;
		ZipOutputStream zos;
		ZipArchiverImpl zip;
	}
	private static ThreadLocal<ZipInfo> local = new ThreadLocal<>();
	
	/*
	 * 		

	 */
	
	public void initialize(HttpServletResponse httpServletResponse, String attachmentName) throws IOException {
		PasswordFactory passwordFactory = new PasswordFactory();
		String password = passwordFactory.createPassword(1, 1, 1, GenericConstants.SPECIAL_CHARS, 1, 16);
		ZipArchiverImpl zip = new ZipArchiverImpl(password);
		httpServletResponse.addHeader("Access-Control-Expose-Headers", "password,content-disposition");
		httpServletResponse.addHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s.zip", attachmentName));
		httpServletResponse.addHeader("password", password);
		httpServletResponse.addHeader("Content-Type",MediaType.APPLICATION_OCTET_STREAM_VALUE);
		ZipOutputStream zos = zip.createArchiveStream(httpServletResponse.getOutputStream());
		local.set(new ZipInfo(password, zos, zip));
	}
	
	public ZipOutputStream get() {
		return local.get().getZos();
	}
	
	public void addEntry(String name) throws IOException {
		ZipParameters zipParameters = new ZipParameters();
		zipParameters.setFileNameInZip(name);
		zipParameters.setCompressionLevel(CompressionLevel.NO_COMPRESSION);
		if( local.get().password != null ) {
	        zipParameters.setEncryptFiles( true );
	        zipParameters.setEncryptionMethod( EncryptionMethod.AES );
		}

		get().putNextEntry(zipParameters);
	}
	
	public void closeEntry() throws IOException {
		get().closeEntry();
	}
	
	public String getPassword() {
		return local.get().getPassword();
	}
	public void remove() {
		local.remove();
	}
}