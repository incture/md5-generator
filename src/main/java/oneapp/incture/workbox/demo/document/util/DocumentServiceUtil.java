package oneapp.incture.workbox.demo.document.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisNameConstraintViolationException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.sap.ecm.api.EcmService;
import com.sap.ecm.api.RepositoryOptions;
import com.sap.ecm.api.RepositoryOptions.Visibility;

import oneapp.incture.workbox.demo.document.dto.AttachmentRequestDto;
import oneapp.incture.workbox.demo.sharepointFileUpload.SharepointUploadFile;


@Component
public class DocumentServiceUtil {

	@Autowired
	private SharepointUploadFile sharepointUploadFile;

	private DocumentServiceUtil() {
//		throw new IllegalAccessError("DocumentServiceUtil class");
	}

	/**
	 * @param encodedFile
	 * @param fileName
	 * @param fileType
	 * @return object id of newly created document, returns null if upload fails
	 */
	@SuppressWarnings("unused")
	public static String uploadToDocumentService(byte[] encodedFile, String fileName, String fileType) {

		String secretKey = "Incture@123";
		Session openCmisSession = null;
		String uniqueName = "WorkboxDevRepository";
		List<Ace> addAcl = null;
		Folder root = null;
		String objectId = null;
		try {
			// long start=System.currentTimeMillis();
			openCmisSession = connectToEcmService(secretKey, uniqueName);
			// System.err.println("[WBP-Dev]DocumentServiceUtil.uploadToDocumentService()
			// Connection time : "+(System.currentTimeMillis()-start));
			System.err.println("[WBP-Dev]Connection done");
			// start=System.currentTimeMillis();
			root = openCmisSession.getRootFolder();
			// System.err.println("[WBP-Dev]DocumentServiceUtil.uploadToDocumentService()
			// to get root folder time : "+(System.currentTimeMillis()-start));
			System.err.println("[WBP-Dev]got root Folder");
			System.err.println("[WBP-Dev]Creating Folder");
			// start=System.currentTimeMillis();
			creatFolder(root);
			// System.err.println("[WBP-Dev]DocumentServiceUtil.uploadToDocumentService()
			// to create folder time : "+(System.currentTimeMillis()-start));
			System.err.println("[WBP-Dev]creating document");
			Map<String, Object> properties = new HashMap<>();
			properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
			fileName += Long.toString(System.currentTimeMillis());
			properties.put(PropertyIds.NAME, fileName);
			// start=System.currentTimeMillis();
			objectId = createDocument(openCmisSession, root, properties, encodedFile, fileName, fileType);
			// System.err.println("[WBP-Dev]DocumentServiceUtil.uploadToDocumentService()
			// to upload doc time : "+(System.currentTimeMillis()-start));
			// start=System.currentTimeMillis();
			// getRootChild(addAcl, root);
			// System.err.println("[WBP-Dev]DocumentServiceUtil.uploadToDocumentService()
			// to get child object time : "+(System.currentTimeMillis()-start));
			return objectId;
		} catch (Exception e) {
			System.err.println(
					"DocumentServiceUtil.uploadToDocumentService() error  : name " + fileName + " type " + fileType);

		}

		return objectId;
	}

	// byte array content
	public static String uploadToDocumentService(String encodedFile, String fileName, String fileType) {

		System.err.println(
				"DocumentServiceUtil.uploadToDocumentService() attachment : name " + fileName + " type " + fileType);

		String secretKey = "Incture@123";
		Session openCmisSession = null;
		String uniqueName = "WorkboxDevRepository";
		List<Ace> addAcl = null;
		Folder root = null;
		String objectId = null;
		try {
			openCmisSession = connectToEcmService(secretKey, uniqueName);
			System.err.println("[WBP-Dev]Connection done");

			root = openCmisSession.getRootFolder();
			System.err.println("[WBP-Dev]got root Folder");
			System.err.println("[WBP-Dev]Creating Folder");

			creatFolder(root);
			System.err.println("[WBP-Dev]creating document");

			Map<String, Object> properties = new HashMap<>();
			properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
			fileName += Long.toString(System.currentTimeMillis());
			properties.put(PropertyIds.NAME, fileName);

			objectId = createDocument(openCmisSession, root, properties, encodedFile, fileName, fileType);

			getRootChild(addAcl, root);

			return objectId;

		} catch (Exception e) {
			System.err.println(
					"DocumentServiceUtil.uploadToDocumentService() error  : name " + fileName + " type " + fileType);

		}

		return objectId;
	}

	private static Session connectToEcmService(String secretKey, String uniqueName) throws NamingException {

		Session openCmisSession = null;
		EcmService ecmSvc = null;
		InitialContext ctx = null;
		String lookupName = null;

		try {
			ctx = new InitialContext();
			lookupName = "java:comp/env/EcmService";
			ecmSvc = (EcmService) ctx.lookup(lookupName);
			// connect to my repository
			openCmisSession = ecmSvc.connect(uniqueName, secretKey);

		} catch (CmisObjectNotFoundException e) {
			// repository does not exist, so try to create it
			System.err.println(
					"[WBP-Dev]DocumentServiceUtil.connectToEcmService():CmisObjectNotFoundException error : " + e);
			RepositoryOptions options = new RepositoryOptions();
			options.setUniqueName(uniqueName);
			options.setRepositoryKey(secretKey);
			options.setVisibility(Visibility.PROTECTED);
			if (ecmSvc != null) {
				ecmSvc.createRepository(options);

				// should be created now, so connect to it
				openCmisSession = ecmSvc.connect(uniqueName, secretKey);
			}
		} catch (Exception exception) {
			System.err.println("[WBP-Dev]DocumentServiceUtil.connectToEcmService() error : " + exception);
		}

		return openCmisSession;
	}

	private static void creatFolder(Folder root) {
		try {

			System.err.println("[WBP-Dev]Creating folder");
			// create a new folder
			Map<String, String> newFolderProps = new HashMap<>();
			newFolderProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
			newFolderProps.put(PropertyIds.NAME, "SPDocuments");
			root.createFolder(newFolderProps);
			// root.deleteTree(true, UnfileObject.DELETE, true);

			System.err.println("[WBP-Dev]Folder creation done");
		} catch (CmisNameConstraintViolationException e) {
			System.err.println("[WBP-Dev]Folder already exists");
		} catch (Exception e) {
			System.err.println("[WBP-Dev]Error while creating folder");

		}
	}

	@SuppressWarnings("unused")
	private static void getRootChild(List<Ace> addAcl, Folder root) {
		try {
			ItemIterable<CmisObject> children = root.getChildren();
			System.err.println("[WBP-Dev]no of root children: " + children.getTotalNumItems());
			for (CmisObject o : children) {
				if (o instanceof Folder) {
					// System.err.println("[WBP-Dev]content " + o.getCreatedBy());
					// System.err.println("[WBP-Dev]Folder recieved: " + o.getName());
				} else {
					Document doc = (Document) o;
					// System.err.println("[WBP-Dev]Document created by : " +
					// doc.getCreatedBy() + " Object id : " + doc.getId());

				}
			}
		} catch (Exception e) {
			System.err.println("[WBP-Dev]Error in listing content");

		}
	}

	private static String createDocument(Session openCmisSession, Folder root, Map<String, Object> properties,
			String encodedFile, String fileName, String fileType) {
		String objectId = null;
		try {

			// byte[] fileContent =
			// Base64.getEncoder().encode(encodedFile.getBytes());
			byte[] fileContent = Base64.getDecoder().decode(encodedFile);
			// byte[] helloContent = "Hello World!".getBytes("UTF-8");
			InputStream stream = new ByteArrayInputStream(fileContent);
			ContentStream contentStream = openCmisSession.getObjectFactory().createContentStream(fileName,
					fileContent.length, fileType + ";base64", stream);
			Document newDoc = root.createDocument(properties, contentStream, VersioningState.NONE);
			System.err.println("[WBP-Dev]Document creation done with id: " + newDoc.getId());
			objectId = newDoc.getId();

			return objectId;
		} catch (Exception e) {
			System.err.println("[WBP-Dev]error while file creation");

		}
		return objectId;
	}

	private static String createDocument(Session openCmisSession, Folder root, Map<String, Object> properties,
			byte[] encodedFile, String fileName, String fileType) {
		String objectId = null;
		try {

			// byte[] fileContent =
			// Base64.getEncoder().encode(encodedFile.getBytes());
			byte[] fileContent = encodedFile;// Base64.getDecoder().decode(encodedFile);
			// byte[] helloContent = "Hello World!".getBytes("UTF-8");
			InputStream stream = new ByteArrayInputStream(fileContent);
			ContentStream contentStream = openCmisSession.getObjectFactory().createContentStream(fileName,
					fileContent.length, fileType + ";base64", stream);
			Document newDoc = root.createDocument(properties, contentStream, VersioningState.NONE);
			System.err.println("[WBP-Dev]Document creation done with id: " + newDoc.getId());
			objectId = newDoc.getId();

			return objectId;
		} catch (Exception e) {
			System.err.println("[WBP-Dev]error while file creation");

		}
		return objectId;
	}

	@SuppressWarnings("unused")
	public AttachmentRequestDto getOriginalDocument(String objectId) {

		System.err.println("[WBP-Dev]DocumentServiceUtil.getOriginalDocument() object  Id  : " + objectId);
		InitialContext ctx = null;
		Session openCmisSession = null;
		Folder root = null;
		Document document = null;
		byte[] originalDoc = null;
		String lookupName = "java:comp/env/" + "EcmService";
		System.err.println("[WBP-Dev]Lookup created");
		AttachmentRequestDto dto = new AttachmentRequestDto();
		try {

			String base64Content = sharepointUploadFile.getFileUsingSharepoint(objectId);
			dto.setEncodedFileContent(base64Content);
			dto.setFileName(objectId);
			String fileExtension  = objectId.split(".")[1];
			String fileType = "application/" + fileExtension;
			if(fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("jpeg") || fileExtension.equalsIgnoreCase("png")) {
				fileType = "image/" + fileExtension;
			}
			dto.setFileType(fileType);
//			dto.setFileType(document.getContentStreamMimeType());

//			ctx = new InitialContext();
//			openCmisSession = connectToEcmService("Incture@123", "WorkboxDevRepository");
//			System.err.println("[WBP-Dev]Connection done");
//			root = openCmisSession.getRootFolder();
//			document = (Document) openCmisSession.getObject(objectId);
//			// ObjectId id = objectId;
//
//			if (null != document) {
//				Property<String> p = document.getProperty(PropertyIds.NAME);
////				System.err.println("[WBP-Dev]DocumentServiceUtil.getOriginalDocument() : Document Name : " + p.getValue());
////				System.err.println("[WBP-Dev]DocumentServiceUtil.getOriginalDocument() : " + p);
////				System.err.println("[WBP-Dev]DocumentServiceUtil.getOriginalDocument() : Document getContentStreamMimeType : "+ document.getContentStreamMimeType());
////				System.err.println("[WBP-Dev]DocumentServiceUtil.getOriginalDocument() : Document getContentStreamFileName : "+ document.getContentStreamFileName());
////				System.err.println("[WBP-Dev]DocumentServiceUtil.getOriginalDocument() : Document getName : " + document.getName());
////				System.err.println("[WBP-Dev]DocumentServiceUtil.getOriginalDocument() : Document getType : " + document.getType());
////				System.err.println("[WBP-Dev]DocumentServiceUtil.getOriginalDocument() : Document getProperties : "+ document.getProperties());
////				System.err.println("[WBP-Dev]DocumentServiceUtil.getOriginalDocument() : " + p);
//
//				InputStream stream = document.getContentStream().getStream();
//				// System.err.println("[WBP-Dev]DocumentServiceUtil.getOriginalDocument()
//				// : content " + stream.);
//				originalDoc = IOUtils.toByteArray(stream);
//
//				byte[] encoded=Base64.getEncoder().encode(originalDoc);
//				System.err.println("[WBP-Dev]DocumentServiceUtil.getOriginalDocument() String data :"+new String(encoded));
//				//System.err.println("[WBP-Dev]DocumentServiceUtil.getOriginalDocument() String data : "+new String(encoded, StandardCharsets.UTF_8));
//				
//			    dto.setEncodedFileContent(new String(encoded));
			dto.setFileName(document.getContentStreamFileName());
			dto.setFileType(document.getContentStreamMimeType());

			return dto;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println("[WBP-Dev]Repo connection failed");
		}
		return dto;
	}

}