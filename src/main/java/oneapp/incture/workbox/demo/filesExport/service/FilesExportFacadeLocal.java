package oneapp.incture.workbox.demo.filesExport.service;

import java.io.File;

import com.sap.cloud.security.xsuaa.token.Token;

import oneapp.incture.workbox.demo.inbox.dto.InboxFilterDto;

public interface FilesExportFacadeLocal {

	String exportInboxAsFile(InboxFilterDto payload,Token token);

	File exportInboxAsFile2(InboxFilterDto payload,Token token);

	File exportInboxAsFileWithCA(InboxFilterDto payload,Token token);

	File exportInboxAsFileWithCAAsMap(InboxFilterDto payload,Token token);
	
	
	File exportInboxAsFileWithRowToColumn(InboxFilterDto payload,Token token);

}
